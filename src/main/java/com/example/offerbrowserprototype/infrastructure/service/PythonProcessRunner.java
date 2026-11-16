package com.example.offerbrowserprototype.infrastructure.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Runs Python scripts with an argument list (no shell), a hard timeout and
 * size-capped stdout/stderr drained on separate threads so a chatty script can
 * never block on a full pipe.
 */
@Component
public class PythonProcessRunner {

    private static final Logger logger = LoggerFactory.getLogger(PythonProcessRunner.class);
    static final int MAX_STDOUT_BYTES = 5 * 1024 * 1024;
    static final int MAX_STDERR_BYTES = 64 * 1024;

    private final String pythonPath;

    public PythonProcessRunner(@Value("${python.path}") String pythonPath) {
        this.pythonPath = pythonPath;
    }

    public record Result(int exitCode, String stdout, String stderrTail, boolean timedOut) {

        public boolean succeeded() {
            return !timedOut && exitCode == 0;
        }

        /** Last non-blank stdout line — scripts print their JSON there. */
        public String lastStdoutLine() {
            String[] lines = stdout.strip().split("\\R");
            return lines.length == 0 ? "" : lines[lines.length - 1];
        }
    }

    public Result run(String scriptPath, List<String> args, Duration timeout) {
        List<String> command = new ArrayList<>();
        command.add(pythonPath);
        command.add(scriptPath);
        command.addAll(args);

        ProcessBuilder pb = new ProcessBuilder(command);
        pb.environment().put("PYTHONIOENCODING", "utf-8");
        pb.environment().put("PYTHONUTF8", "1");

        Process process;
        try {
            process = pb.start();
        } catch (IOException e) {
            throw new UncheckedIOException("Nie można uruchomić Pythona (" + pythonPath + "): " + e.getMessage(), e);
        }
        logger.info("Started python process pid={} script={}", process.pid(), scriptPath);

        CappedReader out = new CappedReader(process.getInputStream(), MAX_STDOUT_BYTES, false);
        CappedReader err = new CappedReader(process.getErrorStream(), MAX_STDERR_BYTES, true);
        Thread outThread = Thread.ofVirtual().start(out);
        Thread errThread = Thread.ofVirtual().start(err);

        boolean finished;
        try {
            finished = process.waitFor(timeout.toMillis(), TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            process.descendants().forEach(ProcessHandle::destroyForcibly);
            process.destroyForcibly();
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Przerwano oczekiwanie na skrypt " + scriptPath, e);
        }
        if (!finished) {
            logger.warn("Python process pid={} exceeded {} — killing it", process.pid(), timeout);
            process.descendants().forEach(ProcessHandle::destroyForcibly);
            process.destroyForcibly();
            try {
                process.waitFor(10, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        joinQuietly(outThread);
        joinQuietly(errThread);
        int exit = finished ? process.exitValue() : -1;
        return new Result(exit, out.text(), err.text(), !finished);
    }

    private static void joinQuietly(Thread t) {
        try {
            t.join(5_000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /** Reads a stream to the end, keeping the head (stdout) or the tail (stderr). */
    private static final class CappedReader implements Runnable {
        private final InputStream in;
        private final int cap;
        private final boolean keepTail;
        private final ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        private volatile boolean truncated;

        CappedReader(InputStream in, int cap, boolean keepTail) {
            this.in = in;
            this.cap = cap;
            this.keepTail = keepTail;
        }

        @Override
        public void run() {
            byte[] chunk = new byte[8192];
            try (in) {
                int n;
                while ((n = in.read(chunk)) != -1) {
                    synchronized (buffer) {
                        buffer.write(chunk, 0, n);
                        if (buffer.size() > cap) {
                            truncated = true;
                            if (keepTail) {
                                byte[] all = buffer.toByteArray();
                                buffer.reset();
                                buffer.write(all, all.length - cap / 2, cap / 2);
                            } else {
                                byte[] all = buffer.toByteArray();
                                buffer.reset();
                                buffer.write(all, 0, cap);
                            }
                        }
                    }
                }
            } catch (IOException ignored) {
                // process killed — keep what was read
            }
        }

        String text() {
            synchronized (buffer) {
                String s = buffer.toString(StandardCharsets.UTF_8);
                return truncated && keepTail ? "…" + s : s;
            }
        }
    }
}

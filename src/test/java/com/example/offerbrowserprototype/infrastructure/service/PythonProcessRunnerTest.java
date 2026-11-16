package com.example.offerbrowserprototype.infrastructure.service;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

// Uruchamia prawdziwy interpreter Pythona — test pomijany, gdy Pythona brak w PATH
class PythonProcessRunnerTest {

    @TempDir static Path tmp;
    private static final PythonProcessRunner runner = new PythonProcessRunner("python");

    @BeforeAll
    static void requirePython() {
        boolean available;
        try {
            available = new ProcessBuilder("python", "--version").start().waitFor() == 0;
        } catch (Exception e) {
            available = false;
        }
        assumeTrue(available, "python not available");
    }

    private static String script(String name, String code) throws Exception {
        Path p = tmp.resolve(name);
        Files.writeString(p, code, StandardCharsets.UTF_8);
        return p.toString();
    }

    @Test
    void oddziela_stdout_od_stderr_i_zachowuje_polskie_znaki() throws Exception {
        String s = script("ok.py", """
                import sys, json
                print("log na stderr", file=sys.stderr)
                print(json.dumps({"city": "Łódź", "arg": sys.argv[1]}, ensure_ascii=False))
                """);

        PythonProcessRunner.Result r = runner.run(s, List.of("a b; rm -rf /"), Duration.ofSeconds(30));

        assertThat(r.succeeded()).isTrue();
        assertThat(r.lastStdoutLine()).isEqualTo("{\"city\": \"Łódź\", \"arg\": \"a b; rm -rf /\"}");
        assertThat(r.stdout()).doesNotContain("log na stderr");
        assertThat(r.stderrTail()).contains("log na stderr");
    }

    @Test
    void zabija_proces_po_przekroczeniu_limitu_czasu() throws Exception {
        String s = script("slow.py", "import time\ntime.sleep(60)\n");
        long start = System.nanoTime();

        PythonProcessRunner.Result r = runner.run(s, List.of(), Duration.ofSeconds(2));

        assertThat(r.timedOut()).isTrue();
        assertThat(r.succeeded()).isFalse();
        assertThat(Duration.ofNanos(System.nanoTime() - start)).isLessThan(Duration.ofSeconds(20));
    }

    @Test
    void ogranicza_rozmiar_logow_i_nie_blokuje_sie_na_pelnym_buforze() throws Exception {
        String s = script("chatty.py", """
                import sys
                for i in range(200000):
                    print("x" * 50, file=sys.stderr)
                print("done")
                """);

        PythonProcessRunner.Result r = runner.run(s, List.of(), Duration.ofSeconds(60));

        assertThat(r.succeeded()).isTrue();
        assertThat(r.lastStdoutLine()).isEqualTo("done");
        assertThat(r.stderrTail().length()).isLessThanOrEqualTo(PythonProcessRunner.MAX_STDERR_BYTES + 1);
    }

    @Test
    void zwraca_kod_wyjscia_bledu() throws Exception {
        String s = script("fail.py", "import sys\nsys.exit(3)\n");

        assertThat(runner.run(s, List.of(), Duration.ofSeconds(30)).exitCode()).isEqualTo(3);
    }
}

package com.example.offerbrowserprototype.infrastructure.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;

@Service
public class PythonScriptService {


    @Value("${python.path}")
    private String pythonPath;


    @Value("${python.script.path}")
    private String scriptRelativePath;

    public String runScript() {
        try {

            String scriptPath = getClass()
                    .getClassLoader()
                    .getResource(scriptRelativePath)
                    .getPath();


            if (System.getProperty("os.name").toLowerCase().contains("win")) {
                scriptPath = scriptPath.substring(1).replace("/", "\\");
            }

            System.out.println("Using Python interpreter: " + pythonPath);
            System.out.println("Executing script at: " + scriptPath);


            ProcessBuilder processBuilder = new ProcessBuilder(pythonPath, scriptPath);
            processBuilder.redirectErrorStream(true);


            Process process = processBuilder.start();


            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuilder output = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }

            int exitCode = process.waitFor();
            if (exitCode != 0) {
                System.out.println("Script Error: " + output);
                throw new RuntimeException("Script failed with exit code: " + exitCode);
            }

            System.out.println("Script Output: " + output);
            return output.toString();

        } catch (Exception e) {
            throw new RuntimeException("Error running Python script: " + e.getMessage());
        }
    }
}

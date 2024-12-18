package com.example.offerbrowserprototype.infrastructure.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

@Service
public class PythonScriptService {

    // Pobranie ścieżki do Pythona z pliku properties
    @Value("${python.path}")
    private String pythonPath;

    // Pobranie względnej ścieżki do skryptu z properties
    @Value("${python.script.path}")
    private String scriptRelativePath;

    public String runScript() {
        try {
            // Dynamiczne uzyskanie pełnej ścieżki do skryptu
            String scriptPath = getClass()
                    .getClassLoader()
                    .getResource(scriptRelativePath)
                    .getPath();

            // Obsługa ścieżki dla systemu Windows
            if (System.getProperty("os.name").toLowerCase().contains("win")) {
                scriptPath = scriptPath.substring(1).replace("/", "\\");
            }

            System.out.println("Using Python interpreter: " + pythonPath);
            System.out.println("Executing script at: " + scriptPath);

            // Budowanie polecenia ProcessBuilder
            ProcessBuilder processBuilder = new ProcessBuilder(pythonPath, scriptPath);
            processBuilder.redirectErrorStream(true);

            // Uruchomienie skryptu
            Process process = processBuilder.start();

            // Odczyt wyjścia skryptu
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

package com.example.offerbrowserprototype.infrastructure.service;

import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

@Service
public class PythonScriptService {
    // Absolutna ścieżka do skryptu Python
    private static final String SCRIPT_PATH = "C:\\projekty\\OfferBrowserPrototype\\src\\main\\scripts\\scraper.py";
    private static final String PYTHON_PATH = "C:\\Users\\przem\\AppData\\Local\\Programs\\Python\\Python313\\python.exe";

    public String runScript() {
        try {
            // Logowanie dla upewnienia się, że ścieżki są poprawne
            System.out.println("Python path: " + PYTHON_PATH);
            System.out.println("Script path: " + SCRIPT_PATH);

            // Sprawdzenie, czy plik istnieje
            if (!new File(SCRIPT_PATH).exists()) {
                throw new RuntimeException("Python script file not found: " + SCRIPT_PATH);
            }

            // Budowanie polecenia
            ProcessBuilder processBuilder = new ProcessBuilder(PYTHON_PATH, SCRIPT_PATH);
            processBuilder.redirectErrorStream(true); // Przekierowanie błędów na wyjście standardowe

            // Uruchomienie procesu
            Process process = processBuilder.start();

            // Odczyt wyjścia ze skryptu
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuilder output = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }

            // Sprawdzenie kodu wyjścia
            int exitCode = process.waitFor();
            if (exitCode != 0) {
                System.out.println("Script Error: " + output);
                throw new RuntimeException("Script failed with exit code: " + exitCode);
            }

            // Zwrócenie wyników
            return output.toString();

        } catch (Exception e) {
            throw new RuntimeException("Error running Python script: " + e.getMessage(), e);
        }
    }
}

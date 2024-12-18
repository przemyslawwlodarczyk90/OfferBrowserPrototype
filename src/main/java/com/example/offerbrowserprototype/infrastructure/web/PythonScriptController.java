package com.example.offerbrowserprototype.infrastructure.web;

import com.example.offerbrowserprototype.infrastructure.service.PythonScriptService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/python-script")
public class PythonScriptController {

    private final PythonScriptService scriptService;

    public PythonScriptController(PythonScriptService scriptService) {
        this.scriptService = scriptService;
    }

    @GetMapping("/run")
    public ResponseEntity<String> runPythonScript() {
        String result = scriptService.runScript();
        return ResponseEntity.ok(result);
    }
}

package com.example.offerbrowserprototype.infrastructure.web;

import com.example.offerbrowserprototype.domain.cv.UserCvHandler;
import com.example.offerbrowserprototype.infrastructure.service.CvStorageService.StoredCv;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/users/me/cv")
@Tag(name = "CV", description = "The logged-in user's CV file (one per user; the database stores only its location)")
@PreAuthorize("isAuthenticated()")
public class UserCvController {

    private final UserCvHandler cvHandler;

    public UserCvController(UserCvHandler cvHandler) {
        this.cvHandler = cvHandler;
    }

    @GetMapping
    @Operation(summary = "CV details", description = "File name, size, type and upload time; 404 when no CV is uploaded.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "CV details returned"),
            @ApiResponse(responseCode = "404", description = "No CV uploaded")
    })
    public ResponseEntity<Map<String, Object>> info(@AuthenticationPrincipal UserDetails principal) {
        return cvHandler.info(principal.getUsername())
                .map(cv -> ResponseEntity.ok(toMap(cv)))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "Nie masz jeszcze wgranego CV", "maxBytes", cvHandler.maxBytes())));
    }

    @GetMapping("/file")
    @Operation(summary = "Download the CV")
    public ResponseEntity<Resource> download(@AuthenticationPrincipal UserDetails principal,
                                             @RequestParam(defaultValue = "false") boolean inline) {
        StoredCv cv = cvHandler.info(principal.getUsername()).orElse(null);
        if (cv == null) {
            return ResponseEntity.notFound().build();
        }
        ContentDisposition disposition = (inline ? ContentDisposition.inline() : ContentDisposition.attachment())
                .filename(cv.fileName(), StandardCharsets.UTF_8).build();
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, disposition.toString())
                .header("X-Content-Type-Options", "nosniff")
                .contentType(MediaType.parseMediaType(cv.contentType()))
                .contentLength(cv.size())
                .body(new FileSystemResource(cvHandler.file(principal.getUsername())));
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Upload or replace the CV", description = "PDF, DOC, DOCX, ODT or RTF up to cv.max-size-mb. "
            + "The previous file is deleted after the new one is saved.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "CV saved"),
            @ApiResponse(responseCode = "400", description = "Empty file, wrong format or content not matching the extension"),
            @ApiResponse(responseCode = "413", description = "File too large")
    })
    public ResponseEntity<Map<String, Object>> upload(@AuthenticationPrincipal UserDetails principal,
                                                      @RequestParam("file") MultipartFile file) throws IOException {
        try (InputStream in = file.getInputStream()) {
            StoredCv cv = cvHandler.upload(principal.getUsername(), file.getOriginalFilename(), file.getSize(), in);
            return ResponseEntity.status(HttpStatus.CREATED).body(toMap(cv));
        }
    }

    @DeleteMapping
    @Operation(summary = "Delete the CV")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "CV deleted"),
            @ApiResponse(responseCode = "404", description = "No CV uploaded")
    })
    public ResponseEntity<Void> delete(@AuthenticationPrincipal UserDetails principal) {
        cvHandler.delete(principal.getUsername());
        return ResponseEntity.noContent().build();
    }

    private Map<String, Object> toMap(StoredCv cv) {
        Map<String, Object> m = new LinkedHashMap<>();
        m.put("fileName", cv.fileName());
        m.put("size", cv.size());
        m.put("contentType", cv.contentType());
        m.put("uploadedAt", cv.uploadedAt().toString());
        m.put("maxBytes", cvHandler.maxBytes());
        return m;
    }
}

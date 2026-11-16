package com.example.offerbrowserprototype.infrastructure.web;

import com.example.offerbrowserprototype.infrastructure.backup.DatabaseJsonBackupService;
import com.example.offerbrowserprototype.infrastructure.backup.DatabaseJsonBackupService.BackupInfo;
import com.example.offerbrowserprototype.infrastructure.backup.DatabaseJsonBackupService.RestoreResult;
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
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/backups")
@Tag(name = "Admin – database backup", description = "Full database backup to JSON (backup.dir) and restore from it")
@PreAuthorize("hasRole('ADMIN')")
public class AdminBackupController {

    private final DatabaseJsonBackupService backups;

    public AdminBackupController(DatabaseJsonBackupService backups) {
        this.backups = backups;
    }

    @GetMapping
    @Operation(summary = "Backups in the backup folder", description = "Newest first, with row counts per table.")
    public Map<String, Object> list() {
        List<BackupInfo> files = backups.list();
        return Map.of("directory", backups.directory().toAbsolutePath().normalize().toString(), "backups", files);
    }

    @PostMapping
    @Operation(summary = "Create a full backup now", description = "All tables of the database in one consistent snapshot.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Backup written"),
            @ApiResponse(responseCode = "409", description = "Another backup/restore is in progress")
    })
    public ResponseEntity<BackupInfo> create(@AuthenticationPrincipal UserDetails principal) {
        return ResponseEntity.status(HttpStatus.CREATED).body(backups.createBackup(username(principal), "manual"));
    }

    @GetMapping("/{fileName}")
    @Operation(summary = "Download a backup file")
    public ResponseEntity<Resource> download(@PathVariable String fileName) {
        Path file = backups.file(fileName);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, ContentDisposition.attachment().filename(fileName).build().toString())
                .contentType(MediaType.APPLICATION_JSON)
                .contentLength(file.toFile().length())
                .body(new FileSystemResource(file));
    }

    @DeleteMapping("/{fileName}")
    @Operation(summary = "Delete a backup file")
    public ResponseEntity<Void> delete(@PathVariable String fileName) {
        backups.delete(fileName);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{fileName}/restore")
    @Operation(summary = "Restore the database from a backup in the folder",
               description = "Saves a safety backup of the current state first, then replaces the data of every table "
                       + "in the file in one transaction (all or nothing).")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Database restored"),
            @ApiResponse(responseCode = "400", description = "Not a backup of this application / broken data — database unchanged"),
            @ApiResponse(responseCode = "409", description = "Scraper or another backup operation running")
    })
    public RestoreResult restore(@PathVariable String fileName, @AuthenticationPrincipal UserDetails principal) {
        return backups.restore(fileName, username(principal));
    }

    @PostMapping(value = "/restore-upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Upload a backup JSON and restore the database from it",
               description = "The file is first saved in the backup folder (uploaded_…json), then restored like any other backup.")
    public RestoreResult restoreUpload(@RequestParam("file") MultipartFile file,
                                       @AuthenticationPrincipal UserDetails principal) throws IOException {
        String stored;
        try (InputStream in = file.getInputStream()) {
            stored = backups.storeUpload(file.getOriginalFilename(), in);
        }
        return backups.restore(stored, username(principal));
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<Map<String, String>> failed(IllegalStateException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", String.valueOf(e.getMessage())));
    }

    private static String username(UserDetails principal) {
        return principal == null ? null : principal.getUsername();
    }
}

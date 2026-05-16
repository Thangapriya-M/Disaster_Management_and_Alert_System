package com.dmas.controller;

import com.dmas.model.Alert;
import com.dmas.service.AlertService;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/alerts")

@CrossOrigin(origins = "*")
public class AlertController {

    private final AlertService alertService;

    public AlertController(AlertService alertService) {
        this.alertService = alertService;
    }

    @GetMapping
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(alertService.getAll().stream()
                .map(this::toMap).collect(Collectors.toList()));
    }

    @GetMapping("/active")
    public ResponseEntity<?> getActive() {
        return ResponseEntity.ok(alertService.getActive().stream()
                .map(this::toMap).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        try { return ResponseEntity.ok(toMap(alertService.getById(id))); }
        catch (Exception e) { return ResponseEntity.notFound().build(); }
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> create(@RequestBody Map<String, Object> body, Authentication auth) {
        try {
            Long disasterId = body.get("disasterId") != null
                    ? Long.parseLong(body.get("disasterId").toString()) : null;
            Alert a = alertService.create(
                    (String) body.get("title"),
                    (String) body.get("message"),
                    Alert.AlertType.valueOf((String) body.get("alertType")),
                    Alert.Severity.valueOf((String) body.get("severity")),
                    (String) body.getOrDefault("affectedZone", ""),
                    disasterId, auth.getName()
            );
            return ResponseEntity.ok(toMap(a));
        } catch (Exception e) { return ResponseEntity.badRequest().body(Map.of("error", e.getMessage())); }
    }

    @PutMapping("/{id}/deactivate")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deactivate(@PathVariable Long id) {
        return ResponseEntity.ok(toMap(alertService.deactivate(id)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        alertService.delete(id);
        return ResponseEntity.ok(Map.of("message", "Deleted"));
    }

    private Map<String, Object> toMap(Alert a) {
        return Map.of(
                "id", a.getId(),
                "title", a.getTitle(),
                "message", a.getMessage(),
                "alertType", a.getAlertType().name(),
                "severity", a.getSeverity().name(),
                "active", a.getActive(),
                "affectedZone", a.getAffectedZone() != null ? a.getAffectedZone() : "",
                "disasterTitle", a.getDisaster() != null ? a.getDisaster().getTitle() : "",
                "createdByName", a.getCreatedBy() != null ? a.getCreatedBy().getName() : "",
                "createdAt", a.getCreatedAt() != null ? a.getCreatedAt().toString() : ""
        );
    }
}

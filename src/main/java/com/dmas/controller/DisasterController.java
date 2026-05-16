package com.dmas.controller;

import com.dmas.model.Disaster;
import com.dmas.service.DisasterService;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/disasters")

@CrossOrigin(origins = "*")
public class DisasterController {

    private final DisasterService disasterService;

    public DisasterController(DisasterService disasterService) {
        this.disasterService = disasterService;
    }

    @GetMapping
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(disasterService.getAll().stream()
                .map(this::toMap).collect(Collectors.toList()));
    }

    @GetMapping("/active")
    public ResponseEntity<?> getActive() {
        return ResponseEntity.ok(disasterService.getActive().stream()
                .map(this::toMap).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        try { return ResponseEntity.ok(toMap(disasterService.getById(id))); }
        catch (Exception e) { return ResponseEntity.notFound().build(); }
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> create(@RequestBody Map<String, Object> body, Authentication auth) {
        try {
            Disaster d = disasterService.create(
                    (String) body.get("title"),
                    (String) body.getOrDefault("description", ""),
                    Disaster.Category.valueOf((String) body.get("category")),
                    Disaster.Severity.valueOf((String) body.get("severity")),
                    body.get("latitude")  != null ? Double.parseDouble(body.get("latitude").toString())  : null,
                    body.get("longitude") != null ? Double.parseDouble(body.get("longitude").toString()) : null,
                    body.get("radiusKm")  != null ? Double.parseDouble(body.get("radiusKm").toString())  : null,
                    (String) body.getOrDefault("location", ""),
                    auth.getName()
            );
            return ResponseEntity.ok(toMap(d));
        } catch (Exception e) { return ResponseEntity.badRequest().body(Map.of("error", e.getMessage())); }
    }

    @PutMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateStatus(@PathVariable Long id, @RequestBody Map<String, String> body) {
        try {
            Disaster d = disasterService.updateStatus(id, Disaster.Status.valueOf(body.get("status")));
            return ResponseEntity.ok(toMap(d));
        } catch (Exception e) { return ResponseEntity.badRequest().body(Map.of("error", e.getMessage())); }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        disasterService.delete(id);
        return ResponseEntity.ok(Map.of("message", "Deleted"));
    }

    private Map<String, Object> toMap(Disaster d) {
        return Map.of(
                "id", d.getId(),
                "title", d.getTitle(),
                "description", d.getDescription() != null ? d.getDescription() : "",
                "category", d.getCategory().name(),
                "severity", d.getSeverity().name(),
                "status", d.getStatus().name(),
                "location", d.getLocation() != null ? d.getLocation() : "",
                "latitude", d.getLatitude() != null ? d.getLatitude() : 0.0,
                "longitude", d.getLongitude() != null ? d.getLongitude() : 0.0,
                "createdAt", d.getCreatedAt() != null ? d.getCreatedAt().toString() : ""
        );
    }
}

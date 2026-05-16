package com.dmas.controller;

import com.dmas.model.RescueRequest;
import com.dmas.service.RescueService;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/rescue")

@CrossOrigin(origins = "*")
public class RescueController {

    private final RescueService rescueService;

    public RescueController(RescueService rescueService) {
        this.rescueService = rescueService;
    }

    @GetMapping
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(rescueService.getAll().stream()
                .map(this::toMap).collect(Collectors.toList()));
    }

    @GetMapping("/my")
    public ResponseEntity<?> getMy(Authentication auth) {
        return ResponseEntity.ok(rescueService.getMyRequests(auth.getName()).stream()
                .map(this::toMap).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        try { return ResponseEntity.ok(toMap(rescueService.getById(id))); }
        catch (Exception e) { return ResponseEntity.notFound().build(); }
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody Map<String, Object> body, Authentication auth) {
        try {
            RescueRequest rr = rescueService.create(
                    RescueRequest.SituationType.valueOf((String) body.get("situationType")),
                    (String) body.get("description"),
                    body.get("urgencyLevel") != null ? Integer.parseInt(body.get("urgencyLevel").toString()) : 3,
                    body.get("latitude")  != null ? Double.parseDouble(body.get("latitude").toString())  : null,
                    body.get("longitude") != null ? Double.parseDouble(body.get("longitude").toString()) : null,
                    (String) body.getOrDefault("locationDescription", ""),
                    auth.getName()
            );
            return ResponseEntity.ok(toMap(rr));
        } catch (Exception e) { return ResponseEntity.badRequest().body(Map.of("error", e.getMessage())); }
    }

    @PutMapping("/{id}/assign")
    public ResponseEntity<?> assign(@PathVariable Long id, @RequestBody Map<String, Object> body) {
        try {
            Long responderId = Long.parseLong(body.get("responderId").toString());
            return ResponseEntity.ok(toMap(rescueService.assign(id, responderId)));
        } catch (Exception e) { return ResponseEntity.badRequest().body(Map.of("error", e.getMessage())); }
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<?> updateStatus(@PathVariable Long id, @RequestBody Map<String, String> body) {
        try {
            RescueRequest rr = rescueService.updateStatus(id,
                    RescueRequest.RequestStatus.valueOf(body.get("status")));
            return ResponseEntity.ok(toMap(rr));
        } catch (Exception e) { return ResponseEntity.badRequest().body(Map.of("error", e.getMessage())); }
    }

    private Map<String, Object> toMap(RescueRequest rr) {
        return Map.of(
                "id", rr.getId(),
                "citizenName", rr.getCitizen() != null ? rr.getCitizen().getName() : "",
                "situationType", rr.getSituationType().name(),
                "description", rr.getDescription(),
                "urgencyLevel", rr.getUrgencyLevel() != null ? rr.getUrgencyLevel() : 3,
                "status", rr.getStatus().name(),
                "locationDescription", rr.getLocationDescription() != null ? rr.getLocationDescription() : "",
                "assignedResponderName", rr.getAssignedResponder() != null ? rr.getAssignedResponder().getName() : "Unassigned",
                "submittedAt", rr.getSubmittedAt() != null ? rr.getSubmittedAt().toString() : "",
                "resolvedAt", rr.getResolvedAt() != null ? rr.getResolvedAt().toString() : ""
        );
    }
}

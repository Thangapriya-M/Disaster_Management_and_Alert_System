package com.dmas.controller;

import com.dmas.model.RescueRequest;
import com.dmas.model.User;
import com.dmas.repository.*;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/analytics")

@CrossOrigin(origins = "*")
public class AnalyticsController {

    private final DisasterRepository disasterRepo;
    private final AlertRepository alertRepo;
    private final RescueRequestRepository rescueRepo;
    private final UserRepository userRepo;

    public AnalyticsController(DisasterRepository disasterRepo, AlertRepository alertRepo,
                                RescueRequestRepository rescueRepo, UserRepository userRepo) {
        this.disasterRepo = disasterRepo;
        this.alertRepo = alertRepo;
        this.rescueRepo = rescueRepo;
        this.userRepo = userRepo;
    }

    @GetMapping("/summary")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> summary() {
        Map<String, Object> data = new HashMap<>();
        data.put("totalDisasters", disasterRepo.count());
        data.put("activeDisasters", disasterRepo.findByStatusOrderByCreatedAtDesc(
                com.dmas.model.Disaster.Status.ACTIVE).size());
        data.put("totalAlerts", alertRepo.count());
        data.put("activeAlerts", alertRepo.findByActiveTrueOrderByCreatedAtDesc().size());
        data.put("totalRescueRequests", rescueRepo.count());
        data.put("pendingRequests", rescueRepo.findByStatusOrderBySubmittedAtDesc(
                RescueRequest.RequestStatus.PENDING).size());
        data.put("completedRequests", rescueRepo.findByStatusOrderBySubmittedAtDesc(
                RescueRequest.RequestStatus.COMPLETED).size());
        data.put("totalUsers", userRepo.count());
        data.put("totalResponders", userRepo.findAll().stream()
                .filter(u -> u.getRole() == User.Role.RESPONDER).count());
        data.put("totalCitizens", userRepo.findAll().stream()
                .filter(u -> u.getRole() == User.Role.CITIZEN).count());
        return ResponseEntity.ok(data);
    }
}

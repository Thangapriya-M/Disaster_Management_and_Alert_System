package com.dmas.service;

import com.dmas.model.RescueRequest;
import com.dmas.model.User;
import com.dmas.repository.RescueRequestRepository;
import com.dmas.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class RescueService {

    private final RescueRequestRepository rescueRepo;
    private final UserRepository userRepo;

    public RescueService(RescueRequestRepository rescueRepo, UserRepository userRepo) {
        this.rescueRepo = rescueRepo;
        this.userRepo = userRepo;
    }

    public List<RescueRequest> getAll() {
        return rescueRepo.findAllByOrderBySubmittedAtDesc();
    }

    public List<RescueRequest> getByStatus(RescueRequest.RequestStatus status) {
        return rescueRepo.findByStatusOrderBySubmittedAtDesc(status);
    }

    public List<RescueRequest> getMyRequests(String email) {
        User user = userRepo.findByEmail(email).orElseThrow();
        if (user.getRole() == User.Role.RESPONDER)
            return rescueRepo.findByAssignedResponderIdOrderBySubmittedAtDesc(user.getId());
        return rescueRepo.findByCitizenIdOrderBySubmittedAtDesc(user.getId());
    }

    public RescueRequest getById(Long id) {
        return rescueRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Request not found: " + id));
    }

    public RescueRequest create(RescueRequest.SituationType type, String description,
                                 Integer urgency, Double lat, Double lon,
                                 String locationDesc, String citizenEmail) {
        User citizen = userRepo.findByEmail(citizenEmail)
                .orElseThrow(() -> new RuntimeException("Citizen not found"));
        RescueRequest rr = RescueRequest.builder()
                .citizen(citizen).situationType(type).description(description)
                .urgencyLevel(urgency).latitude(lat).longitude(lon)
                .locationDescription(locationDesc).build();
        return rescueRepo.save(rr);
    }

    public RescueRequest assign(Long requestId, Long responderId) {
        RescueRequest rr = getById(requestId);
        User responder = userRepo.findById(responderId)
                .orElseThrow(() -> new RuntimeException("Responder not found"));
        rr.setAssignedResponder(responder);
        rr.setStatus(RescueRequest.RequestStatus.ASSIGNED);
        return rescueRepo.save(rr);
    }

    public RescueRequest updateStatus(Long id, RescueRequest.RequestStatus status) {
        RescueRequest rr = getById(id);
        rr.setStatus(status);
        if (status == RescueRequest.RequestStatus.COMPLETED)
            rr.setResolvedAt(LocalDateTime.now());
        return rescueRepo.save(rr);
    }
}

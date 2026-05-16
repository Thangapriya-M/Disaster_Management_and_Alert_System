package com.dmas.service;

import com.dmas.model.Disaster;
import com.dmas.model.User;
import com.dmas.repository.DisasterRepository;
import com.dmas.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class DisasterService {

    private final DisasterRepository disasterRepository;
    private final UserRepository userRepository;

    public DisasterService(DisasterRepository disasterRepository, UserRepository userRepository) {
        this.disasterRepository = disasterRepository;
        this.userRepository = userRepository;
    }

    public List<Disaster> getAll() {
        return disasterRepository.findAllByOrderByCreatedAtDesc();
    }

    public List<Disaster> getActive() {
        return disasterRepository.findByStatusOrderByCreatedAtDesc(Disaster.Status.ACTIVE);
    }

    public Disaster getById(Long id) {
        return disasterRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Disaster not found: " + id));
    }

    public Disaster create(String title, String description, Disaster.Category category,
                           Disaster.Severity severity, Double lat, Double lon,
                           Double radius, String location, String adminEmail) {
        User admin = userRepository.findByEmail(adminEmail)
                .orElseThrow(() -> new RuntimeException("Admin not found"));
        Disaster d = Disaster.builder()
                .title(title).description(description).category(category)
                .severity(severity).latitude(lat).longitude(lon)
                .radiusKm(radius).location(location).createdBy(admin).build();
        return disasterRepository.save(d);
    }

    public Disaster updateStatus(Long id, Disaster.Status status) {
        Disaster d = getById(id);
        d.setStatus(status);
        if (status == Disaster.Status.RESOLVED) d.setResolvedAt(LocalDateTime.now());
        return disasterRepository.save(d);
    }

    public void delete(Long id) {
        disasterRepository.deleteById(id);
    }
}

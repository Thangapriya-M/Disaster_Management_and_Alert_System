package com.dmas.service;

import com.dmas.model.Alert;
import com.dmas.model.Disaster;
import com.dmas.model.User;
import com.dmas.repository.AlertRepository;
import com.dmas.repository.DisasterRepository;
import com.dmas.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AlertService {

    private final AlertRepository alertRepository;
    private final DisasterRepository disasterRepository;
    private final UserRepository userRepository;

    public AlertService(AlertRepository alertRepository, DisasterRepository disasterRepository,
                        UserRepository userRepository) {
        this.alertRepository = alertRepository;
        this.disasterRepository = disasterRepository;
        this.userRepository = userRepository;
    }

    public List<Alert> getAll() {
        return alertRepository.findAllByOrderByCreatedAtDesc();
    }

    public List<Alert> getActive() {
        return alertRepository.findByActiveTrueOrderByCreatedAtDesc();
    }

    public Alert getById(Long id) {
        return alertRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Alert not found: " + id));
    }

    public Alert create(String title, String message, Alert.AlertType type,
                        Alert.Severity severity, String zone,
                        Long disasterId, String adminEmail) {
        User admin = userRepository.findByEmail(adminEmail)
                .orElseThrow(() -> new RuntimeException("Admin not found"));
        Alert.AlertBuilder builder = Alert.builder()
                .title(title).message(message).alertType(type)
                .severity(severity).affectedZone(zone).createdBy(admin);
        if (disasterId != null) {
            Disaster d = disasterRepository.findById(disasterId).orElse(null);
            builder.disaster(d);
        }
        return alertRepository.save(builder.build());
    }

    public Alert deactivate(Long id) {
        Alert a = getById(id);
        a.setActive(false);
        return alertRepository.save(a);
    }

    public void delete(Long id) {
        alertRepository.deleteById(id);
    }
}

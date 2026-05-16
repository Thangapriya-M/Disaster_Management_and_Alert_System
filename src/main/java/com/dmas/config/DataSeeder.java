package com.dmas.config;

import com.dmas.model.*;
import com.dmas.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

@Configuration
public class DataSeeder {

    @Bean
    CommandLineRunner seedData(UserRepository userRepo,
                               DisasterRepository disasterRepo,
                               AlertRepository alertRepo,
                               RescueRequestRepository rescueRepo,
                               PasswordEncoder encoder) {
        return args -> {

            // ── USERS ──────────────────────────────────────────────────────
            User admin = userRepo.save(User.builder()
                    .name("Admin Kumar").email("admin@dmas.com")
                    .password(encoder.encode("admin123")).phone("9000000001")
                    .role(User.Role.ADMIN).latitude(13.0827).longitude(80.2707).build());

            User responder1 = userRepo.save(User.builder()
                    .name("Ravi Shankar").email("ravi@dmas.com")
                    .password(encoder.encode("resp123")).phone("9000000002")
                    .role(User.Role.RESPONDER).latitude(13.0500).longitude(80.2500).build());

            User responder2 = userRepo.save(User.builder()
                    .name("Priya Nair").email("priya@dmas.com")
                    .password(encoder.encode("resp123")).phone("9000000003")
                    .role(User.Role.RESPONDER).latitude(13.1200).longitude(80.3000).build());

            User citizen1 = userRepo.save(User.builder()
                    .name("Arjun Devi").email("arjun@dmas.com")
                    .password(encoder.encode("user123")).phone("9000000004")
                    .role(User.Role.CITIZEN).latitude(13.0900).longitude(80.2800).build());

            User citizen2 = userRepo.save(User.builder()
                    .name("Meena Pillai").email("meena@dmas.com")
                    .password(encoder.encode("user123")).phone("9000000005")
                    .role(User.Role.CITIZEN).latitude(13.0600).longitude(80.2600).build());

            // ── DISASTERS ──────────────────────────────────────────────────
            Disaster d1 = disasterRepo.save(Disaster.builder()
                    .title("Chennai Coastal Flooding")
                    .description("Heavy monsoon rainfall causing severe coastal flooding in North Chennai.")
                    .category(Disaster.Category.FLOOD)
                    .severity(Disaster.Severity.HIGH)
                    .status(Disaster.Status.ACTIVE)
                    .latitude(13.1200).longitude(80.2900).radiusKm(15.0)
                    .location("North Chennai, Tamil Nadu")
                    .createdBy(admin).build());

            Disaster d2 = disasterRepo.save(Disaster.builder()
                    .title("Cyclone Mocha - Bay of Bengal")
                    .description("Severe cyclonic storm approaching Tamil Nadu coast. Wind speeds 120-140 km/h.")
                    .category(Disaster.Category.CYCLONE)
                    .severity(Disaster.Severity.CRITICAL)
                    .status(Disaster.Status.MONITORING)
                    .latitude(12.8000).longitude(80.2000).radiusKm(50.0)
                    .location("Bay of Bengal, TN Coast")
                    .createdBy(admin).build());

            Disaster d3 = disasterRepo.save(Disaster.builder()
                    .title("Warehouse Fire - Ambattur Industrial Estate")
                    .description("Major fire outbreak at chemical warehouse. Toxic fumes spreading.")
                    .category(Disaster.Category.FIRE)
                    .severity(Disaster.Severity.HIGH)
                    .status(Disaster.Status.ACTIVE)
                    .latitude(13.1143).longitude(80.1548).radiusKm(3.0)
                    .location("Ambattur, Chennai")
                    .createdBy(admin).build());

            disasterRepo.save(Disaster.builder()
                    .title("Landslide - Nilgiris District")
                    .description("Multiple landslides blocking NH-67. Rescue teams deployed.")
                    .category(Disaster.Category.LANDSLIDE)
                    .severity(Disaster.Severity.MEDIUM)
                    .status(Disaster.Status.ACTIVE)
                    .latitude(11.4102).longitude(76.6950).radiusKm(10.0)
                    .location("Ooty, Nilgiris")
                    .createdBy(admin).build());

            disasterRepo.save(Disaster.builder()
                    .title("Earthquake - Andaman Islands")
                    .description("Magnitude 5.8 earthquake recorded near Andaman Islands.")
                    .category(Disaster.Category.EARTHQUAKE)
                    .severity(Disaster.Severity.MEDIUM)
                    .status(Disaster.Status.RESOLVED)
                    .latitude(11.7401).longitude(92.6586).radiusKm(30.0)
                    .location("Andaman & Nicobar Islands")
                    .resolvedAt(LocalDateTime.now().minusHours(6))
                    .createdBy(admin).build());

            // ── ALERTS ─────────────────────────────────────────────────────
            alertRepo.save(Alert.builder()
                    .title("URGENT: Evacuate Coastal Areas - Chennai Flood")
                    .message("Immediate evacuation order for residents in Tiruvottiyur, Ennore, and Manali. Proceed to designated relief camps.")
                    .alertType(Alert.AlertType.EVACUATION)
                    .severity(Alert.Severity.CRITICAL)
                    .affectedZone("North Chennai Coastal Belt")
                    .disaster(d1).createdBy(admin).build());

            alertRepo.save(Alert.builder()
                    .title("Cyclone Warning - Coastal Districts")
                    .message("Cyclone Mocha expected to make landfall in 48 hours. All fishing suspended. Stock food for 3 days.")
                    .alertType(Alert.AlertType.GENERAL_WARNING)
                    .severity(Alert.Severity.HIGH)
                    .affectedZone("Chennai, Kancheepuram, Villupuram")
                    .disaster(d2).createdBy(admin).build());

            alertRepo.save(Alert.builder()
                    .title("Toxic Fumes Alert - Ambattur Area")
                    .message("Chemical fire at Ambattur releasing toxic fumes. Close windows. Avoid outdoor activities.")
                    .alertType(Alert.AlertType.SHELTER_IN_PLACE)
                    .severity(Alert.Severity.HIGH)
                    .affectedZone("Ambattur, Padi, Anna Nagar West")
                    .disaster(d3).createdBy(admin).build());

            alertRepo.save(Alert.builder()
                    .title("Relief Camps Open - Flood Victims")
                    .message("Government relief camps operational at Royapuram Community Hall and Tiruvottiyur School.")
                    .alertType(Alert.AlertType.RESOURCE_DISTRIBUTION)
                    .severity(Alert.Severity.MEDIUM)
                    .affectedZone("North Chennai")
                    .disaster(d1).createdBy(admin).build());

            // ── RESCUE REQUESTS ────────────────────────────────────────────
            rescueRepo.save(RescueRequest.builder()
                    .citizen(citizen1)
                    .situationType(RescueRequest.SituationType.TRAPPED)
                    .description("Family of 4 trapped on 2nd floor. Ground floor flooded. Elderly mother with mobility issues.")
                    .urgencyLevel(5)
                    .latitude(13.1100).longitude(80.2950)
                    .locationDescription("18/4 Kamarajar Street, Tiruvottiyur")
                    .status(RescueRequest.RequestStatus.ASSIGNED)
                    .assignedResponder(responder1).build());

            rescueRepo.save(RescueRequest.builder()
                    .citizen(citizen2)
                    .situationType(RescueRequest.SituationType.MEDICAL)
                    .description("Pregnant woman needs immediate evacuation. Roads flooded.")
                    .urgencyLevel(5)
                    .latitude(13.0950).longitude(80.2880)
                    .locationDescription("7B Nehru Nagar, Ennore")
                    .status(RescueRequest.RequestStatus.ONGOING)
                    .assignedResponder(responder2).build());

            rescueRepo.save(RescueRequest.builder()
                    .citizen(citizen1)
                    .situationType(RescueRequest.SituationType.FLOOD_EVACUATION)
                    .description("10 people including children stranded on rooftop.")
                    .urgencyLevel(4)
                    .latitude(13.1050).longitude(80.3000)
                    .locationDescription("Anna Colony, Manali")
                    .status(RescueRequest.RequestStatus.PENDING).build());

            rescueRepo.save(RescueRequest.builder()
                    .citizen(citizen2)
                    .situationType(RescueRequest.SituationType.OTHER)
                    .description("Elderly couple, 70+, no food for 24 hours.")
                    .urgencyLevel(3)
                    .latitude(13.0800).longitude(80.2700)
                    .locationDescription("42 Gandhi Road, Royapuram")
                    .status(RescueRequest.RequestStatus.COMPLETED).build());

            System.out.println("\n========================================================");
            System.out.println("  DISASTER MANAGEMENT & ALERT SYSTEM - STARTED");
            System.out.println("========================================================");
            System.out.println("  App:     http://localhost:8080");
            System.out.println("  H2 DB:   http://localhost:8080/h2-console");
            System.out.println("--------------------------------------------------------");
            System.out.println("  DEMO ACCOUNTS:");
            System.out.println("  Admin    -> admin@dmas.com   / admin123");
            System.out.println("  Responder-> ravi@dmas.com    / resp123");
            System.out.println("  Citizen  -> arjun@dmas.com   / user123");
            System.out.println("========================================================\n");
        };
    }
}

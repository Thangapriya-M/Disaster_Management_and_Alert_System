package com.dmas.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "alerts")
public class Alert {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(nullable = false, length = 2000)
    private String message;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AlertType alertType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Severity severity;

    @Column(nullable = false)
    private Boolean active = true;

    private String affectedZone;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "disaster_id")
    private Disaster disaster;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    private User createdBy;

    @Column(updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    public enum AlertType {
        EVACUATION, SHELTER_IN_PLACE, RESOURCE_DISTRIBUTION, GENERAL_WARNING
    }

    public enum Severity {
        LOW, MEDIUM, HIGH, CRITICAL
    }

    public Alert() {}

    public Long getId()                  { return id; }
    public String getTitle()             { return title; }
    public String getMessage()           { return message; }
    public AlertType getAlertType()      { return alertType; }
    public Severity getSeverity()        { return severity; }
    public Boolean getActive()           { return active; }
    public String getAffectedZone()      { return affectedZone; }
    public Disaster getDisaster()        { return disaster; }
    public User getCreatedBy()           { return createdBy; }
    public LocalDateTime getCreatedAt()  { return createdAt; }

    public void setId(Long id)                  { this.id = id; }
    public void setTitle(String title)          { this.title = title; }
    public void setMessage(String message)      { this.message = message; }
    public void setAlertType(AlertType t)       { this.alertType = t; }
    public void setSeverity(Severity s)         { this.severity = s; }
    public void setActive(Boolean active)       { this.active = active; }
    public void setAffectedZone(String zone)    { this.affectedZone = zone; }
    public void setDisaster(Disaster disaster)  { this.disaster = disaster; }
    public void setCreatedBy(User createdBy)    { this.createdBy = createdBy; }
    public void setCreatedAt(LocalDateTime dt)  { this.createdAt = dt; }

    public static AlertBuilder builder() { return new AlertBuilder(); }

    public static class AlertBuilder {
        private String title, message, affectedZone;
        private AlertType alertType;
        private Severity severity;
        private Boolean active = true;
        private Disaster disaster;
        private User createdBy;

        public AlertBuilder title(String v)        { this.title = v; return this; }
        public AlertBuilder message(String v)      { this.message = v; return this; }
        public AlertBuilder alertType(AlertType v) { this.alertType = v; return this; }
        public AlertBuilder severity(Severity v)   { this.severity = v; return this; }
        public AlertBuilder active(Boolean v)      { this.active = v; return this; }
        public AlertBuilder affectedZone(String v) { this.affectedZone = v; return this; }
        public AlertBuilder disaster(Disaster v)   { this.disaster = v; return this; }
        public AlertBuilder createdBy(User v)      { this.createdBy = v; return this; }

        public Alert build() {
            Alert a = new Alert();
            a.title = title; a.message = message; a.alertType = alertType;
            a.severity = severity; a.active = active; a.affectedZone = affectedZone;
            a.disaster = disaster; a.createdBy = createdBy;
            a.createdAt = LocalDateTime.now();
            return a;
        }
    }
}

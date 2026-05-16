package com.dmas.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "disasters")
public class Disaster {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(length = 1000)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Category category;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Severity severity;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status = Status.ACTIVE;

    private Double latitude;
    private Double longitude;
    private Double radiusKm;

    @Column(length = 100)
    private String location;

    @Column(updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    private LocalDateTime resolvedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    private User createdBy;

    public enum Category {
        FLOOD, CYCLONE, EARTHQUAKE, FIRE, TSUNAMI, LANDSLIDE, OTHER
    }

    public enum Severity {
        LOW, MEDIUM, HIGH, CRITICAL
    }

    public enum Status {
        ACTIVE, MONITORING, RESOLVED
    }

    public Disaster() {}

    public Long getId()                   { return id; }
    public String getTitle()              { return title; }
    public String getDescription()        { return description; }
    public Category getCategory()         { return category; }
    public Severity getSeverity()         { return severity; }
    public Status getStatus()             { return status; }
    public Double getLatitude()           { return latitude; }
    public Double getLongitude()          { return longitude; }
    public Double getRadiusKm()           { return radiusKm; }
    public String getLocation()           { return location; }
    public LocalDateTime getCreatedAt()   { return createdAt; }
    public LocalDateTime getResolvedAt()  { return resolvedAt; }
    public User getCreatedBy()            { return createdBy; }

    public void setId(Long id)                   { this.id = id; }
    public void setTitle(String title)           { this.title = title; }
    public void setDescription(String desc)      { this.description = desc; }
    public void setCategory(Category category)   { this.category = category; }
    public void setSeverity(Severity severity)   { this.severity = severity; }
    public void setStatus(Status status)         { this.status = status; }
    public void setLatitude(Double latitude)     { this.latitude = latitude; }
    public void setLongitude(Double longitude)   { this.longitude = longitude; }
    public void setRadiusKm(Double radiusKm)     { this.radiusKm = radiusKm; }
    public void setLocation(String location)     { this.location = location; }
    public void setCreatedAt(LocalDateTime dt)   { this.createdAt = dt; }
    public void setResolvedAt(LocalDateTime dt)  { this.resolvedAt = dt; }
    public void setCreatedBy(User createdBy)     { this.createdBy = createdBy; }

    public static DisasterBuilder builder() { return new DisasterBuilder(); }

    public static class DisasterBuilder {
        private String title, description, location;
        private Category category;
        private Severity severity;
        private Status status = Status.ACTIVE;
        private Double latitude, longitude, radiusKm;
        private LocalDateTime resolvedAt;
        private User createdBy;

        public DisasterBuilder title(String v)          { this.title = v; return this; }
        public DisasterBuilder description(String v)    { this.description = v; return this; }
        public DisasterBuilder location(String v)       { this.location = v; return this; }
        public DisasterBuilder category(Category v)     { this.category = v; return this; }
        public DisasterBuilder severity(Severity v)     { this.severity = v; return this; }
        public DisasterBuilder status(Status v)         { this.status = v; return this; }
        public DisasterBuilder latitude(Double v)       { this.latitude = v; return this; }
        public DisasterBuilder longitude(Double v)      { this.longitude = v; return this; }
        public DisasterBuilder radiusKm(Double v)       { this.radiusKm = v; return this; }
        public DisasterBuilder resolvedAt(LocalDateTime v) { this.resolvedAt = v; return this; }
        public DisasterBuilder createdBy(User v)        { this.createdBy = v; return this; }

        public Disaster build() {
            Disaster d = new Disaster();
            d.title = title; d.description = description; d.location = location;
            d.category = category; d.severity = severity; d.status = status;
            d.latitude = latitude; d.longitude = longitude; d.radiusKm = radiusKm;
            d.resolvedAt = resolvedAt; d.createdBy = createdBy;
            d.createdAt = LocalDateTime.now();
            return d;
        }
    }
}

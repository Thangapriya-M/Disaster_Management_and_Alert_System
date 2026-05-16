package com.dmas.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "rescue_requests")
public class RescueRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "citizen_id", nullable = false)
    private User citizen;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SituationType situationType;

    @Column(nullable = false, length = 1000)
    private String description;

    private Integer urgencyLevel;

    private Double latitude;
    private Double longitude;
    private String locationDescription;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RequestStatus status = RequestStatus.PENDING;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_responder_id")
    private User assignedResponder;

    @Column(updatable = false)
    private LocalDateTime submittedAt = LocalDateTime.now();

    private LocalDateTime resolvedAt;

    public enum SituationType {
        MEDICAL, TRAPPED, FLOOD_EVACUATION, FIRE, STRUCTURAL_COLLAPSE, OTHER
    }

    public enum RequestStatus {
        PENDING, ASSIGNED, ONGOING, COMPLETED, CANCELLED
    }

    public RescueRequest() {}

    public Long getId()                       { return id; }
    public User getCitizen()                  { return citizen; }
    public SituationType getSituationType()   { return situationType; }
    public String getDescription()            { return description; }
    public Integer getUrgencyLevel()          { return urgencyLevel; }
    public Double getLatitude()               { return latitude; }
    public Double getLongitude()              { return longitude; }
    public String getLocationDescription()    { return locationDescription; }
    public RequestStatus getStatus()          { return status; }
    public User getAssignedResponder()        { return assignedResponder; }
    public LocalDateTime getSubmittedAt()     { return submittedAt; }
    public LocalDateTime getResolvedAt()      { return resolvedAt; }

    public void setId(Long id)                            { this.id = id; }
    public void setCitizen(User citizen)                  { this.citizen = citizen; }
    public void setSituationType(SituationType t)         { this.situationType = t; }
    public void setDescription(String description)        { this.description = description; }
    public void setUrgencyLevel(Integer urgencyLevel)     { this.urgencyLevel = urgencyLevel; }
    public void setLatitude(Double latitude)              { this.latitude = latitude; }
    public void setLongitude(Double longitude)            { this.longitude = longitude; }
    public void setLocationDescription(String loc)        { this.locationDescription = loc; }
    public void setStatus(RequestStatus status)           { this.status = status; }
    public void setAssignedResponder(User assignedResponder) { this.assignedResponder = assignedResponder; }
    public void setSubmittedAt(LocalDateTime submittedAt) { this.submittedAt = submittedAt; }
    public void setResolvedAt(LocalDateTime resolvedAt)   { this.resolvedAt = resolvedAt; }

    public static RescueRequestBuilder builder() { return new RescueRequestBuilder(); }

    public static class RescueRequestBuilder {
        private User citizen;
        private SituationType situationType;
        private String description, locationDescription;
        private Integer urgencyLevel;
        private Double latitude, longitude;
        private RequestStatus status = RequestStatus.PENDING;
        private User assignedResponder;
        private LocalDateTime resolvedAt;

        public RescueRequestBuilder citizen(User v)                     { this.citizen = v; return this; }
        public RescueRequestBuilder situationType(SituationType v)      { this.situationType = v; return this; }
        public RescueRequestBuilder description(String v)               { this.description = v; return this; }
        public RescueRequestBuilder locationDescription(String v)       { this.locationDescription = v; return this; }
        public RescueRequestBuilder urgencyLevel(Integer v)             { this.urgencyLevel = v; return this; }
        public RescueRequestBuilder latitude(Double v)                  { this.latitude = v; return this; }
        public RescueRequestBuilder longitude(Double v)                 { this.longitude = v; return this; }
        public RescueRequestBuilder status(RequestStatus v)             { this.status = v; return this; }
        public RescueRequestBuilder assignedResponder(User v)           { this.assignedResponder = v; return this; }
        public RescueRequestBuilder resolvedAt(LocalDateTime v)         { this.resolvedAt = v; return this; }

        public RescueRequest build() {
            RescueRequest r = new RescueRequest();
            r.citizen = citizen; r.situationType = situationType;
            r.description = description; r.locationDescription = locationDescription;
            r.urgencyLevel = urgencyLevel; r.latitude = latitude; r.longitude = longitude;
            r.status = status; r.assignedResponder = assignedResponder;
            r.resolvedAt = resolvedAt; r.submittedAt = LocalDateTime.now();
            return r;
        }
    }
}

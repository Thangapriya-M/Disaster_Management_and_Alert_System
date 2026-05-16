package com.dmas.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, unique = true, length = 150)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(length = 20)
    private String phone;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    private Double latitude;
    private Double longitude;

    @Column(nullable = false)
    private Boolean active = true;

    @Column(updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    public enum Role {
        ADMIN, RESPONDER, CITIZEN
    }

    public User() {}

    public Long getId()                  { return id; }
    public String getName()              { return name; }
    public String getEmail()             { return email; }
    public String getPassword()          { return password; }
    public String getPhone()             { return phone; }
    public Role getRole()                { return role; }
    public Double getLatitude()          { return latitude; }
    public Double getLongitude()         { return longitude; }
    public Boolean getActive()           { return active; }
    public LocalDateTime getCreatedAt()  { return createdAt; }

    public void setId(Long id)                  { this.id = id; }
    public void setName(String name)            { this.name = name; }
    public void setEmail(String email)          { this.email = email; }
    public void setPassword(String password)    { this.password = password; }
    public void setPhone(String phone)          { this.phone = phone; }
    public void setRole(Role role)              { this.role = role; }
    public void setLatitude(Double latitude)    { this.latitude = latitude; }
    public void setLongitude(Double longitude)  { this.longitude = longitude; }
    public void setActive(Boolean active)       { this.active = active; }
    public void setCreatedAt(LocalDateTime dt)  { this.createdAt = dt; }

    public static UserBuilder builder() { return new UserBuilder(); }

    public static class UserBuilder {
        private String name, email, password, phone;
        private Role role;
        private Double latitude, longitude;
        private Boolean active = true;

        public UserBuilder name(String v)       { this.name = v; return this; }
        public UserBuilder email(String v)      { this.email = v; return this; }
        public UserBuilder password(String v)   { this.password = v; return this; }
        public UserBuilder phone(String v)      { this.phone = v; return this; }
        public UserBuilder role(Role v)         { this.role = v; return this; }
        public UserBuilder latitude(Double v)   { this.latitude = v; return this; }
        public UserBuilder longitude(Double v)  { this.longitude = v; return this; }
        public UserBuilder active(Boolean v)    { this.active = v; return this; }

        public User build() {
            User u = new User();
            u.name = name; u.email = email; u.password = password;
            u.phone = phone; u.role = role; u.latitude = latitude;
            u.longitude = longitude; u.active = active;
            u.createdAt = LocalDateTime.now();
            return u;
        }
    }
}

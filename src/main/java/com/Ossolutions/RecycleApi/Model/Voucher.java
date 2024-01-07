package com.Ossolutions.RecycleApi.Model;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "vouchers")
public class Voucher {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private int pointsUsed;

    @Column(unique = true, nullable = false)
    private String serialNumber;

    @Column(nullable = false)
    private LocalDateTime generationDateTime;

    @Column(nullable = false)
    private LocalDateTime expirationDateTime;

    private boolean expired;

    public Voucher(User user, int pointsUsed, String serialNumber, LocalDateTime generationDateTime) {
        this.user = user;
        this.pointsUsed = pointsUsed;
        this.serialNumber = serialNumber;
        this.generationDateTime = generationDateTime;
        this.expirationDateTime = generationDateTime.plusDays(30);
        this.expired = false;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getPointsUsed() {
        return pointsUsed;
    }

    public void setPointsUsed(int pointsUsed) {
        this.pointsUsed = pointsUsed;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public LocalDateTime getGenerationDateTime() {
        return generationDateTime;
    }

    public void setGenerationDateTime(LocalDateTime generationDateTime) {
        this.generationDateTime = generationDateTime;
    }

    public LocalDateTime getExpirationDateTime() {
        return expirationDateTime;
    }

    public void setExpirationDateTime(LocalDateTime expirationDateTime) {
        this.expirationDateTime = expirationDateTime;
    }

    public boolean isExpired() {
        return expired;
    }

    public void setExpired(boolean expired) {
        this.expired = expired;
    }
}


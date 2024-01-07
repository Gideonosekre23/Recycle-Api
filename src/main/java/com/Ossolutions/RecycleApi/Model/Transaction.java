package com.Ossolutions.RecycleApi.Model;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "recycling_center_id", nullable = false)
    private RecyclingCenter recyclingCenter;

    @ManyToMany
    @JoinTable(
            name = "transaction_materials",
            joinColumns = @JoinColumn(name = "transaction_id"),
            inverseJoinColumns = @JoinColumn(name = "material_id"))
    private List<Material> materials;

    private int totalPoints;

    private LocalDateTime transactionDateTime;

    public Transaction(User user, RecyclingCenter recyclingCenter, List<Material> materials, int totalPoints, LocalDateTime transactionDateTime) {
        this.user = user;
        this.recyclingCenter = recyclingCenter;
        this.materials = materials;
        this.totalPoints = totalPoints;
        this.transactionDateTime = transactionDateTime;
    }

    public Transaction() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public RecyclingCenter getRecyclingCenter() {
        return recyclingCenter;
    }

    public void setRecyclingCenter(RecyclingCenter recyclingCenter) {
        this.recyclingCenter = recyclingCenter;
    }

    public List<Material> getMaterials() {
        return materials;
    }

    public void setMaterials(List<Material> materials) {
        this.materials = materials;
    }

    public int getTotalPoints() {
        return totalPoints;
    }

    public void setTotalPoints(int totalPoints) {
        this.totalPoints = totalPoints;
    }

    public LocalDateTime getTransactionDateTime() {
        return transactionDateTime;
    }

    public void setTransactionDateTime(LocalDateTime transactionDateTime) {
        this.transactionDateTime = transactionDateTime;
    }
}

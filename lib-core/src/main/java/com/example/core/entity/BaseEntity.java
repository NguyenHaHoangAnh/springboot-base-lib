package com.example.core.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
public class BaseEntity {
    @Column(name = "created_at")
    @Temporal(TemporalType.DATE)
    private Date createdAt;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "updated_at")
    @Temporal(TemporalType.DATE)
    private Date updatedAt;

    @Column(name = "updated_by")
    private String updatedBy;

    @PrePersist
    public void onCreate() {
        try {
            this.createdAt = new Date();
            this.createdBy = (String) null;
            this.updatedAt = (Date) null;
            this.updatedBy = (String) null;
        } catch (Exception ignored) {}
    }

    @PreUpdate
    public void onUpdate() {
        try {
            this.updatedAt = new Date();
            this.updatedBy = (String) null;
        } catch (Exception ignored) {}
    }
}

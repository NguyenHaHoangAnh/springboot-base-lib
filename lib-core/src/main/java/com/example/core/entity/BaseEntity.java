package com.example.core.entity;

import com.example.lib.auth.util.UserUtil;
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
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;

    @Column(name = "updated_by")
    private String updatedBy;

    @PrePersist
    public void onCreate() {
        try {
            this.createdAt = new Date();
            this.createdBy = (String) UserUtil.getUser().getUsername();
            this.updatedAt = (Date) null;
            this.updatedBy = (String) null;
        } catch (Exception ignored) {}
    }

    @PreUpdate
    public void onUpdate() {
        try {
            this.updatedAt = new Date();
            this.updatedBy = (String) UserUtil.getUser().getUsername();
        } catch (Exception ignored) {}
    }
}

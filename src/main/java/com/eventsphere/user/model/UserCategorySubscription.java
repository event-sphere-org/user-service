package com.eventsphere.user.model;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Timestamp;

@Entity
@Table(name = "user_category_subscription", schema = "public", catalog = "event_sphere")
@Data
public class UserCategorySubscription {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @Basic
    @Column(name = "user_id", nullable = true)
    private Long userId;

    @Basic
    @Column(name = "category_id", nullable = true)
    private Long categoryId;

    @Basic
    @Column(name = "created_at", nullable = true)
    private Timestamp createdAt;
}

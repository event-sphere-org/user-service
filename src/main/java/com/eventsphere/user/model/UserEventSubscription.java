package com.eventsphere.user.model;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Timestamp;

@Entity
@Table(name = "user_event_subscription", schema = "public", catalog = "event_sphere")
@Data
public class UserEventSubscription {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @Basic
    @Column(name = "user_id")
    private Long userId;

    @Basic
    @Column(name = "event_id")
    private Long eventId;

    @Basic
    @Column(name = "created_at")
    private Timestamp createdAt;
}

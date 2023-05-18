package com.eventsphere.user.model;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Timestamp;

@Entity
@Table(name = "user_interest", schema = "public", catalog = "event_sphere")
@Data
public class UserInterest {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @Basic
    @Column(name = "user_id", nullable = true)
    private Long userId;

    @Basic
    @Column(name = "interest", nullable = true, length = 100)
    private String interest;

    @Basic
    @Column(name = "created_at", nullable = true)
    private Timestamp createdAt;
}

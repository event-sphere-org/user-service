package com.eventsphere.user.model;

import com.eventsphere.user.model.dto.EventDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "user_event_subscription", schema = "user_service_schema", catalog = "event_sphere")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class UserEventSubscription {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @ToString.Exclude
    private User user;

    @Basic
    @Column(name = "event_id")
    @JsonIgnore
    private Long eventId;

    @Transient
    private EventDto event;

    @Basic
    @Column(name = "created_at")
    private Timestamp createdAt;

    public UserEventSubscription(User user, Long eventId) {
        this.user = user;
        this.eventId = eventId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        UserEventSubscription that = (UserEventSubscription) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}

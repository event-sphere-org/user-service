package com.eventsphere.user.model.dto;

import lombok.*;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class EventDto {

    private Long id;

    private Long creatorId;

    private String title;

    private String description;

    private String imageUrl;

    private String location;

    private Date date;

    private Time time;

    private Timestamp createdAt;

    private Timestamp updatedAt;

    private CategoryDto category;
}

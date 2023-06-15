package com.eventsphere.user.model.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.sql.Timestamp;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class CategoryDto {

    private Long id;

    private String name;

    private Timestamp createdAt;

    private Timestamp updatedAt;
}

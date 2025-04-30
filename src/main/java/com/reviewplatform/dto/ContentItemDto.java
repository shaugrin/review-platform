package com.reviewplatform.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContentItemDto {
    private String title;
    private String description;
    private Long categoryId;
    private String imageUrl;
    private String externalId;
    private Map<String, Object> metadata;
}
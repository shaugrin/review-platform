package com.reviewplatform.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContentItemSearchCriteria {
    private String query;
    private Long categoryId;
    private String sortBy;
    private Map<String, Object> metadataFilter;
}
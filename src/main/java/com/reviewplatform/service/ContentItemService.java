// ContentItemService.java
package com.reviewplatform.service;

import com.reviewplatform.dto.ContentItemDto;
import com.reviewplatform.dto.ContentItemSearchCriteria;
import com.reviewplatform.exception.ResourceNotFoundException;
import com.reviewplatform.model.Category;
import com.reviewplatform.model.ContentItem;
import com.reviewplatform.repository.CategoryRepository;
import com.reviewplatform.repository.ContentItemRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ContentItemService {

    private final ContentItemRepository contentItemRepository;
    private final CategoryRepository categoryRepository;
    private final ObjectMapper objectMapper;

    @Transactional(readOnly = true)
    public ContentItem getContentItemById(Long id) {
        return contentItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Content item not found with id: " + id));
    }

    @Transactional(readOnly = true)
    public Page<ContentItem> getAllContentItems(Pageable pageable) {
        return contentItemRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public Page<ContentItem> getContentItemsByCategory(Long categoryId, Pageable pageable) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + categoryId));
        return contentItemRepository.findByCategory(category, pageable);
    }

    @Transactional(readOnly = true)
    public Page<ContentItem> searchContentItems(ContentItemSearchCriteria criteria, Pageable pageable) {
        if (criteria.getQuery() != null && !criteria.getQuery().isBlank()) {
            if (criteria.getCategoryId() != null) {
                return contentItemRepository.searchByCategory(criteria.getQuery(), criteria.getCategoryId(), pageable);
            } else {
                return contentItemRepository.search(criteria.getQuery(), pageable);
            }
        }

        if (criteria.getMetadataFilter() != null && !criteria.getMetadataFilter().isEmpty()) {
            try {
                String jsonFilter = objectMapper.writeValueAsString(criteria.getMetadataFilter());
                return contentItemRepository.findByMetadataContaining(jsonFilter, pageable);
            } catch (JsonProcessingException e) {
                throw new IllegalArgumentException("Invalid metadata filter", e);
            }
        }

        if ("top_rated".equals(criteria.getSortBy())) {
            return contentItemRepository.findAllByOrderByAvgRatingDesc(pageable);
        } else if ("newest".equals(criteria.getSortBy())) {
            return contentItemRepository.findAllByOrderByCreatedAtDesc(pageable);
        } else if ("most_popular".equals(criteria.getSortBy())) {
            return contentItemRepository.findAllByOrderByReviewCountDesc(pageable);
        }

        return contentItemRepository.findAll(pageable);
    }

    @Transactional
    public ContentItem createContentItem(ContentItemDto contentItemDto) {
        Category category = categoryRepository.findById(contentItemDto.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + contentItemDto.getCategoryId()));

        ContentItem contentItem = new ContentItem();
        contentItem.setTitle(contentItemDto.getTitle());
        contentItem.setDescription(contentItemDto.getDescription());
        contentItem.setCategory(category);
        contentItem.setImageUrl(contentItemDto.getImageUrl());
        contentItem.setExternalId(contentItemDto.getExternalId());

        // Handle metadata
        if (contentItemDto.getMetadata() != null) {
            contentItem.setMetadata(contentItemDto.getMetadata());
        } else {
            contentItem.setMetadata(new HashMap<>());
        }

        return contentItemRepository.save(contentItem);
    }

    @Transactional
    public ContentItem updateContentItem(Long contentItemId, ContentItemDto contentItemDto) {
        ContentItem contentItem = getContentItemById(contentItemId);

        if (contentItemDto.getTitle() != null) {
            contentItem.setTitle(contentItemDto.getTitle());
        }

        if (contentItemDto.getDescription() != null) {
            contentItem.setDescription(contentItemDto.getDescription());
        }

        if (contentItemDto.getCategoryId() != null) {
            Category category = categoryRepository.findById(contentItemDto.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + contentItemDto.getCategoryId()));
            contentItem.setCategory(category);
        }

        if (contentItemDto.getImageUrl() != null) {
            contentItem.setImageUrl(contentItemDto.getImageUrl());
        }

        if (contentItemDto.getExternalId() != null) {
            contentItem.setExternalId(contentItemDto.getExternalId());
        }

        // Update metadata
        if (contentItemDto.getMetadata() != null) {
            Map<String, Object> updatedMetadata = new HashMap<>(contentItem.getMetadata() != null ? contentItem.getMetadata() : new HashMap<>());
            updatedMetadata.putAll(contentItemDto.getMetadata());
            contentItem.setMetadata(updatedMetadata);
        }

        return contentItemRepository.save(contentItem);
    }

    @Transactional
    public void deleteContentItem(Long contentItemId) {
        if (!contentItemRepository.existsById(contentItemId)) {
            throw new ResourceNotFoundException("Content item not found with id: " + contentItemId);
        }
        contentItemRepository.deleteById(contentItemId);
    }
}
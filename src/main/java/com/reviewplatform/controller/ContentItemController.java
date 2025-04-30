package com.reviewplatform.controller;

import com.reviewplatform.dto.ContentItemDto;
import com.reviewplatform.dto.ContentItemSearchCriteria;
import com.reviewplatform.exception.ResourceNotFoundException;
import com.reviewplatform.model.ContentItem;
import com.reviewplatform.service.ContentItemService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/content-items")
public class ContentItemController {

    private final ContentItemService contentItemService;

    public ContentItemController(ContentItemService contentItemService) {
        this.contentItemService = contentItemService;
    }

    @GetMapping
    public ResponseEntity<Page<ContentItem>> getAllContentItems(
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(contentItemService.getAllContentItems(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ContentItem> getContentItemById(@PathVariable Long id) {
        return ResponseEntity.ok(contentItemService.getContentItemById(id));
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<Page<ContentItem>> getContentItemsByCategory(
            @PathVariable Long categoryId,
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(contentItemService.getContentItemsByCategory(categoryId, pageable));
    }

    @PostMapping("/search")
    public ResponseEntity<Page<ContentItem>> searchContentItems(
            @RequestBody ContentItemSearchCriteria criteria,
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(contentItemService.searchContentItems(criteria, pageable));
    }

    @PostMapping
    public ResponseEntity<ContentItem> createContentItem(
            @RequestBody @Valid ContentItemDto contentItemDto) {
        ContentItem createdItem = contentItemService.createContentItem(contentItemDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdItem);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ContentItem> updateContentItem(
            @PathVariable Long id,
            @RequestBody @Valid ContentItemDto contentItemDto) {
        return ResponseEntity.ok(contentItemService.updateContentItem(id, contentItemDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteContentItem(@PathVariable Long id) {
        contentItemService.deleteContentItem(id);
        return ResponseEntity.noContent().build();
    }

    // Exception Handling
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(ResourceNotFoundException.class)
    public String handleResourceNotFound(ResourceNotFoundException ex) {
        return ex.getMessage();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    public String handleIllegalArgument(IllegalArgumentException ex) {
        return ex.getMessage();
    }
}
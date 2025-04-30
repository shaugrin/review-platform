package com.reviewplatform.controller;

import com.reviewplatform.dto.ReviewDto;
import com.reviewplatform.exception.ResourceNotFoundException;
import com.reviewplatform.model.Review;
import com.reviewplatform.service.ReviewService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Review> getReviewById(@PathVariable Long id) {
        return ResponseEntity.ok(reviewService.getReviewById(id));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<Page<Review>> getReviewsByUser(
            @PathVariable Long userId,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(reviewService.getReviewsByUser(userId, pageable));
    }

    @GetMapping("/content-item/{contentItemId}")
    public ResponseEntity<Page<Review>> getReviewsByContentItem(
            @PathVariable Long contentItemId,
            @RequestParam(required = false) String sortBy,
            @PageableDefault(size = 10) Pageable pageable) {
        return ResponseEntity.ok(reviewService.getReviewsByContentItem(contentItemId, pageable, sortBy));
    }

    @PostMapping("/user/{userId}")
    public ResponseEntity<Review> createReview(
            @PathVariable Long userId,
            @RequestBody @Valid ReviewDto reviewDto) {
        Review createdReview = reviewService.createReview(userId, reviewDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdReview);
    }

    @PutMapping("/{reviewId}/user/{userId}")
    public ResponseEntity<Review> updateReview(
            @PathVariable Long reviewId,
            @PathVariable Long userId,
            @RequestBody @Valid ReviewDto reviewDto) {
        return ResponseEntity.ok(reviewService.updateReview(reviewId, userId, reviewDto));
    }

    @DeleteMapping("/{reviewId}/user/{userId}")
    public ResponseEntity<Void> deleteReview(
            @PathVariable Long reviewId,
            @PathVariable Long userId) {
        reviewService.deleteReview(reviewId, userId);
        return ResponseEntity.noContent().build();
    }

    // Exception Handling
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(ResourceNotFoundException.class)
    public String handleResourceNotFound(ResourceNotFoundException ex) {
        return ex.getMessage();
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(IllegalArgumentException.class)
    public String handleIllegalArgument(IllegalArgumentException ex) {
        return ex.getMessage();
    }
}
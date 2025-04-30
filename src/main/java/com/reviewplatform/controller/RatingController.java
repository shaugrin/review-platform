package com.reviewplatform.controller;

import com.reviewplatform.dto.RatingDto;
import com.reviewplatform.exception.ResourceNotFoundException;
import com.reviewplatform.model.Rating;
import com.reviewplatform.service.RatingService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/ratings")
public class RatingController {

    private final RatingService ratingService;

    public RatingController(RatingService ratingService) {
        this.ratingService = ratingService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Rating> getRatingById(@PathVariable Long id) {
        return ResponseEntity.ok(ratingService.getRatingById(id));
    }

    @GetMapping("/user/{userId}/content-item/{contentItemId}")
    public ResponseEntity<Rating> getUserRatingForContentItem(
            @PathVariable Long userId,
            @PathVariable Long contentItemId) {
        return ResponseEntity.ok(ratingService.getUserRatingForContentItem(userId, contentItemId));
    }

    @GetMapping("/content-item/{contentItemId}/average")
    public ResponseEntity<BigDecimal> getAverageRating(
            @PathVariable Long contentItemId) {
        return ResponseEntity.ok(ratingService.getAverageRatingForContentItem(contentItemId));
    }

    @PostMapping("/user/{userId}")
    public ResponseEntity<Rating> createOrUpdateRating(
            @PathVariable Long userId,
            @RequestBody @Valid RatingDto ratingDto) {
        Rating rating = ratingService.createOrUpdateRating(userId, ratingDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(rating);
    }

    @DeleteMapping("/{ratingId}/user/{userId}")
    public ResponseEntity<Void> deleteRating(
            @PathVariable Long ratingId,
            @PathVariable Long userId) {
        ratingService.deleteRating(ratingId, userId);
        return ResponseEntity.noContent().build();
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(ResourceNotFoundException.class)
    public String handleResourceNotFound(ResourceNotFoundException ex) {
        return ex.getMessage();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    public String handleInvalidRating(IllegalArgumentException ex) {
        return ex.getMessage();
    }
}
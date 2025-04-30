package com.reviewplatform.controller;

import com.reviewplatform.exception.ResourceAlreadyExistsException;
import com.reviewplatform.exception.ResourceNotFoundException;
import com.reviewplatform.model.Like;
import com.reviewplatform.service.LikeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/likes")
public class LikeController {

    private final LikeService likeService;

    public LikeController(LikeService likeService) {
        this.likeService = likeService;
    }

    @GetMapping("/review/{reviewId}/user/{userId}")
    public ResponseEntity<Boolean> hasUserLikedReview(
            @PathVariable Long reviewId,
            @PathVariable Long userId) {
        return ResponseEntity.ok(likeService.hasUserLikedReview(userId, reviewId));
    }

    @GetMapping("/review/{reviewId}/count")
    public ResponseEntity<Long> getLikeCount(
            @PathVariable Long reviewId) {
        return ResponseEntity.ok(likeService.getReviewLikeCount(reviewId));
    }

    @PostMapping("/review/{reviewId}/user/{userId}")
    public ResponseEntity<Like> likeReview(
            @PathVariable Long reviewId,
            @PathVariable Long userId) {
        Like like = likeService.likeReview(userId, reviewId);
        return ResponseEntity.status(HttpStatus.CREATED).body(like);
    }

    @DeleteMapping("/review/{reviewId}/user/{userId}")
    public ResponseEntity<Void> unlikeReview(
            @PathVariable Long reviewId,
            @PathVariable Long userId) {
        likeService.unlikeReview(userId, reviewId);
        return ResponseEntity.noContent().build();
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(ResourceNotFoundException.class)
    public String handleResourceNotFound(ResourceNotFoundException ex) {
        return ex.getMessage();
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(ResourceAlreadyExistsException.class)
    public String handleDuplicateLike(ResourceAlreadyExistsException ex) {
        return ex.getMessage();
    }
}
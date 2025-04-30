// ReviewService.java
package com.reviewplatform.service;

import com.reviewplatform.dto.ReviewDto;
import com.reviewplatform.exception.ResourceNotFoundException;
import com.reviewplatform.model.ContentItem;
import com.reviewplatform.model.Review;
import com.reviewplatform.model.User;
import com.reviewplatform.repository.ContentItemRepository;
import com.reviewplatform.repository.ReviewRepository;
import com.reviewplatform.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final ContentItemRepository contentItemRepository;

    @Transactional(readOnly = true)
    public Review getReviewById(Long id) {
        return reviewRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found with id: " + id));
    }

    @Transactional(readOnly = true)
    public Page<Review> getReviewsByUser(Long userId, Pageable pageable) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        return reviewRepository.findByUser(user, pageable);
    }

    @Transactional(readOnly = true)
    public Page<Review> getReviewsByContentItem(Long contentItemId, Pageable pageable, String sortBy) {
        ContentItem contentItem = contentItemRepository.findById(contentItemId)
                .orElseThrow(() -> new ResourceNotFoundException("Content item not found with id: " + contentItemId));

        if ("newest".equals(sortBy)) {
            return reviewRepository.findByContentItemOrderByCreatedAtDesc(contentItem, pageable);
        } else if ("popular".equals(sortBy)) {
            return reviewRepository.findByContentItemOrderByLikeCountDesc(contentItem, pageable);
        } else {
            return reviewRepository.findByContentItem(contentItem, pageable);
        }
    }

    @Transactional
    public Review createReview(Long userId, ReviewDto reviewDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        ContentItem contentItem = contentItemRepository.findById(reviewDto.getContentItemId())
                .orElseThrow(() -> new ResourceNotFoundException("Content item not found with id: " + reviewDto.getContentItemId()));

        Review review = new Review();
        review.setUser(user);
        review.setContentItem(contentItem);
        review.setTitle(reviewDto.getTitle());
        review.setContent(reviewDto.getContent());
        review.setLikeCount(0);

        return reviewRepository.save(review);
    }

    @Transactional
    public Review updateReview(Long reviewId, Long userId, ReviewDto reviewDto) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found with id: " + reviewId));

        // Verify the review belongs to the user
        if (!review.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("Review does not belong to user with id: " + userId);
        }

        if (reviewDto.getTitle() != null) {
            review.setTitle(reviewDto.getTitle());
        }

        if (reviewDto.getContent() != null) {
            review.setContent(reviewDto.getContent());
        }

        return reviewRepository.save(review);
    }

    @Transactional
    public void deleteReview(Long reviewId, Long userId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found with id: " + reviewId));

        // Verify the review belongs to the user or the user is an admin
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        if (!review.getUser().getId().equals(userId) && user.getRole() != User.UserRole.ADMIN) {
            throw new IllegalArgumentException("Not authorized to delete this review");
        }

        reviewRepository.delete(review);
    }
}

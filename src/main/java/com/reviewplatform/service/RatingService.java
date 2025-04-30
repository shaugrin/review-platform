// RatingService.java
package com.reviewplatform.service;

import com.reviewplatform.dto.RatingDto;
import com.reviewplatform.exception.ResourceNotFoundException;
import com.reviewplatform.model.ContentItem;
import com.reviewplatform.model.Rating;
import com.reviewplatform.model.Review;
import com.reviewplatform.model.User;
import com.reviewplatform.repository.ContentItemRepository;
import com.reviewplatform.repository.RatingRepository;
import com.reviewplatform.repository.ReviewRepository;
import com.reviewplatform.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RatingService {

    private final RatingRepository ratingRepository;
    private final UserRepository userRepository;
    private final ContentItemRepository contentItemRepository;
    private final ReviewRepository reviewRepository;

    @Transactional(readOnly = true)
    public Rating getRatingById(Long id) {
        return ratingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Rating not found with id: " + id));
    }

    @Transactional(readOnly = true)
    public Rating getUserRatingForContentItem(Long userId, Long contentItemId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        ContentItem contentItem = contentItemRepository.findById(contentItemId)
                .orElseThrow(() -> new ResourceNotFoundException("Content item not found with id: " + contentItemId));

        return ratingRepository.findByUserAndContentItem(user, contentItem)
                .orElseThrow(() -> new ResourceNotFoundException("Rating not found for user and content item"));
    }

    @Transactional(readOnly = true)
    public BigDecimal getAverageRatingForContentItem(Long contentItemId) {
        if (!contentItemRepository.existsById(contentItemId)) {
            throw new ResourceNotFoundException("Content item not found with id: " + contentItemId);
        }

        BigDecimal avgRating = ratingRepository.calculateAverageRatingForContentItem(contentItemId);
        return avgRating != null ? avgRating : BigDecimal.ZERO;
    }

    @Transactional
    public Rating createOrUpdateRating(Long userId, RatingDto ratingDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        ContentItem contentItem = contentItemRepository.findById(ratingDto.getContentItemId())
                .orElseThrow(() -> new ResourceNotFoundException("Content item not found with id: " + ratingDto.getContentItemId()));

        // Check if the rating is associated with a review
        Review review = null;
        if (ratingDto.getReviewId() != null) {
            review = reviewRepository.findById(ratingDto.getReviewId())
                    .orElseThrow(() -> new ResourceNotFoundException("Review not found with id: " + ratingDto.getReviewId()));

            // Ensure the review belongs to the user
            if (!review.getUser().getId().equals(userId)) {
                throw new IllegalArgumentException("Review does not belong to user with id: " + userId);
            }
        }

        // Check if user already rated this content
        Optional<Rating> existingRating = ratingRepository.findByUserAndContentItem(user, contentItem);

        Rating rating;
        if (existingRating.isPresent()) {
            rating = existingRating.get();
            rating.setValue(ratingDto.getValue());
            if (review != null) {
                rating.setReview(review);
            }
        } else {
            rating = new Rating();
            rating.setUser(user);
            rating.setContentItem(contentItem);
            rating.setValue(ratingDto.getValue());
            if (review != null) {
                rating.setReview(review);
            }
        }

        return ratingRepository.save(rating);
    }

    @Transactional
    public void deleteRating(Long ratingId, Long userId) {
        Rating rating = ratingRepository.findById(ratingId)
                .orElseThrow(() -> new ResourceNotFoundException("Rating not found with id: " + ratingId));

        // Verify the rating belongs to the user
        if (!rating.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("Rating does not belong to user with id: " + userId);
        }

        ratingRepository.delete(rating);
    }
}
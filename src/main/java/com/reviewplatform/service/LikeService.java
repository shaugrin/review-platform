// LikeService.java
package com.reviewplatform.service;

import com.reviewplatform.exception.ResourceAlreadyExistsException;
import com.reviewplatform.exception.ResourceNotFoundException;
import com.reviewplatform.model.Like;
import com.reviewplatform.model.Review;
import com.reviewplatform.model.User;
import com.reviewplatform.repository.LikeRepository;
import com.reviewplatform.repository.ReviewRepository;
import com.reviewplatform.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final LikeRepository likeRepository;
    private final UserRepository userRepository;
    private final ReviewRepository reviewRepository;

    @Transactional(readOnly = true)
    public boolean hasUserLikedReview(Long userId, Long reviewId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found with id: " + reviewId));

        return likeRepository.existsByUserAndReview(user, review);
    }

    @Transactional(readOnly = true)
    public long getReviewLikeCount(Long reviewId) {
        if (!reviewRepository.existsById(reviewId)) {
            throw new ResourceNotFoundException("Review not found with id: " + reviewId);
        }

        return likeRepository.countByReviewId(reviewId);
    }

    @Transactional
    public Like likeReview(Long userId, Long reviewId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found with id: " + reviewId));

        // Check if user already liked this review
        if (likeRepository.existsByUserAndReview(user, review)) {
            throw new ResourceAlreadyExistsException("User already liked this review");
        }

        Like like = new Like();
        like.setUser(user);
        like.setReview(review);

        return likeRepository.save(like);
    }

    @Transactional
    public void unlikeReview(Long userId, Long reviewId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found with id: " + reviewId));

        likeRepository.deleteByUserAndReview(user, review);
    }
}
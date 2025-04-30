package com.reviewplatform.repository;

import com.reviewplatform.model.Like;
import com.reviewplatform.model.Review;
import com.reviewplatform.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {

    Optional<Like> findByUserAndReview(User user, Review review);

    boolean existsByUserAndReview(User user, Review review);

    @Query("SELECT COUNT(l) FROM Like l WHERE l.review.id = :reviewId")
    Long countByReviewId(@Param("reviewId") Long reviewId);

    void deleteByUserAndReview(User user, Review review);
}

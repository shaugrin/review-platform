package com.reviewplatform.repository;

import com.reviewplatform.model.ContentItem;
import com.reviewplatform.model.Review;
import com.reviewplatform.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    Page<Review> findByUser(User user, Pageable pageable);

    Page<Review> findByContentItem(ContentItem contentItem, Pageable pageable);

    Page<Review> findByContentItemOrderByCreatedAtDesc(ContentItem contentItem, Pageable pageable);

    Page<Review> findByContentItemOrderByLikeCountDesc(ContentItem contentItem, Pageable pageable);

    @Query("SELECT COUNT(r) FROM Review r WHERE r.contentItem.id = :contentItemId")
    Long countByContentItemId(Long contentItemId);

    @Query("SELECT r FROM Review r JOIN FETCH r.user WHERE r.id = :id")
    Review findByIdWithUser(Long id);
}

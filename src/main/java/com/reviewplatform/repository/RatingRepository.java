package com.reviewplatform.repository;

import com.reviewplatform.model.ContentItem;
import com.reviewplatform.model.Rating;
import com.reviewplatform.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Optional;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Long> {

    Optional<Rating> findByUserAndContentItem(User user, ContentItem contentItem);

    @Query("SELECT AVG(r.value) FROM Rating r WHERE r.contentItem.id = :contentItemId")
    BigDecimal calculateAverageRatingForContentItem(@Param("contentItemId") Long contentItemId);

    @Query("SELECT COUNT(r) FROM Rating r WHERE r.contentItem.id = :contentItemId")
    Long countByContentItemId(@Param("contentItemId") Long contentItemId);

    boolean existsByUserAndContentItem(User user, ContentItem contentItem);
}

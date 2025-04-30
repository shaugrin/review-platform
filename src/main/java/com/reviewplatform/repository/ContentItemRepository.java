package com.reviewplatform.repository;

import com.reviewplatform.model.Category;
import com.reviewplatform.model.ContentItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ContentItemRepository extends JpaRepository<ContentItem, Long> {

    Page<ContentItem> findByCategory(Category category, Pageable pageable);

    Optional<ContentItem> findByExternalId(String externalId);

    @Query("SELECT c FROM ContentItem c WHERE LOWER(c.title) LIKE LOWER(CONCAT('%', :query, '%')) OR LOWER(c.description) LIKE LOWER(CONCAT('%', :query, '%'))")
    Page<ContentItem> search(@Param("query") String query, Pageable pageable);

    @Query("SELECT c FROM ContentItem c WHERE c.category.id = :categoryId AND LOWER(c.title) LIKE LOWER(CONCAT('%', :query, '%'))")
    Page<ContentItem> searchByCategory(@Param("query") String query, @Param("categoryId") Long categoryId, Pageable pageable);

    Page<ContentItem> findAllByOrderByAvgRatingDesc(Pageable pageable);

    Page<ContentItem> findAllByOrderByCreatedAtDesc(Pageable pageable);

    Page<ContentItem> findAllByOrderByReviewCountDesc(Pageable pageable);

    @Query(value = "SELECT c.* FROM content_items c WHERE c.metadata @> :filter\\:\\:jsonb", nativeQuery = true)
    Page<ContentItem> findByMetadataContaining(@Param("filter") String jsonFilter, Pageable pageable);
}

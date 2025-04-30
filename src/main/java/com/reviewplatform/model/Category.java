package com.reviewplatform.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "categories")
@Data
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String name;

    private String description;

    private String icon;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    private Set<ContentItem> contentItems = new HashSet<>();
}

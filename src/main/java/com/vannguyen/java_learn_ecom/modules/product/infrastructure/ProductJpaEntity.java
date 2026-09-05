package com.vannguyen.java_learn_ecom.modules.product.infrastructure;

import com.vannguyen.java_learn_ecom.modules.product.domain.ProductStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
@Entity
@Table(name = "products")
public class ProductJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", insertable = false, updatable = false)
    private CategoryJpaEntity category;

    @OneToMany(mappedBy = "product")
    private List<ProductVariantJpaEntity> variants = new ArrayList<>();

    @Column(name = "category_id", nullable = false)
    private Long categoryId;

    @Column(nullable = false, length = 150)
    private String name;

    @Column(nullable = false, length = 2000)
    private String description;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal price;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private ProductStatus status;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    protected ProductJpaEntity() {
    }

    public ProductJpaEntity(
            Long id,
            Long categoryId,
            String name,
            String description,
            BigDecimal price,
            ProductStatus status
    ) {
        this.id = id;
        this.categoryId = categoryId;
        this.name = name;
        this.description = description;
        this.price = price;
        this.status = status;
    }

    @PrePersist
    void prePersist() {
        LocalDateTime now = LocalDateTime.now();

        this.createdAt = now;
        this.updatedAt = now;
    }

    @PreUpdate
    void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public ProductStatus getStatus() {
        return status;
    }

    public CategoryJpaEntity getCategory() {
        return category;
    }
    public List<ProductVariantJpaEntity> getVariants() {
        return variants;
    }
}
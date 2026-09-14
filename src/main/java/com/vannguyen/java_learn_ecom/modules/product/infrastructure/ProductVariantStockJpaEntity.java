package com.vannguyen.java_learn_ecom.modules.product.infrastructure;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "product_variant_stocks")
public class ProductVariantStockJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "variant_id", nullable = false, unique = true)
    private Long variantId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "variant_id", insertable = false, updatable = false)
    private ProductVariantJpaEntity variant;

    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false)
    private boolean available;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    protected ProductVariantStockJpaEntity() {
    }

    public ProductVariantStockJpaEntity(
            Long id,
            Long variantId,
            int quantity,
            boolean available
    ) {
        this.id = id;
        this.variantId = variantId;
        this.quantity = quantity;
        this.available = available;
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

    public Long getVariantId() {
        return variantId;
    }

    public ProductVariantJpaEntity getVariant() {
        return variant;
    }

    public int getQuantity() {
        return quantity;
    }

    public boolean isAvailable() {
        return available;
    }
}
package com.vannguyen.java_learn_ecom.modules.product.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface SpringDataProductVariantStockJpaRepository
        extends JpaRepository<ProductVariantStockJpaEntity, Long> {

    Optional<ProductVariantStockJpaEntity> findByVariantId(Long variantId);

    boolean existsByVariantId(Long variantId);

    @Modifying
    @Query("""
            update ProductVariantStockJpaEntity stock
            set stock.quantity = stock.quantity - :quantity
            where stock.variantId = :variantId
              and stock.available = true
              and stock.quantity >= :quantity
            """)
    int decreaseStockIfEnough(Long variantId, int quantity);

    @Modifying
    @Query("""
        update ProductVariantStockJpaEntity stock
        set stock.quantity = stock.quantity + :quantity
        where stock.variantId = :variantId
        """)
    int restoreStock(Long variantId, int quantity);


    @Modifying
    @Query("""
        update ProductVariantStockJpaEntity stock
        set stock.available = :available
        where stock.variantId = :variantId
        """)
    int updateAvailability(Long variantId, boolean available);
}

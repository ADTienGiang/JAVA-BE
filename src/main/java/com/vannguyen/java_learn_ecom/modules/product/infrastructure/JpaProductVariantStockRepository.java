package com.vannguyen.java_learn_ecom.modules.product.infrastructure;

import com.vannguyen.java_learn_ecom.modules.product.domain.ProductVariantStock;
import com.vannguyen.java_learn_ecom.modules.product.domain.ProductVariantStockRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class JpaProductVariantStockRepository implements ProductVariantStockRepository {

    private final SpringDataProductVariantStockJpaRepository springDataProductVariantStockJpaRepository;
    private final ProductVariantStockJpaMapper productVariantStockJpaMapper;

    public JpaProductVariantStockRepository(
            SpringDataProductVariantStockJpaRepository springDataProductVariantStockJpaRepository,
            ProductVariantStockJpaMapper productVariantStockJpaMapper
    ) {
        this.springDataProductVariantStockJpaRepository = springDataProductVariantStockJpaRepository;
        this.productVariantStockJpaMapper = productVariantStockJpaMapper;
    }

    @Override
    public ProductVariantStock save(ProductVariantStock stock) {
        ProductVariantStockJpaEntity entity = productVariantStockJpaMapper.toEntity(stock);
        ProductVariantStockJpaEntity savedEntity = springDataProductVariantStockJpaRepository.save(entity);

        return productVariantStockJpaMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<ProductVariantStock> findByVariantId(Long variantId) {
        return springDataProductVariantStockJpaRepository.findByVariantId(variantId)
                .map(productVariantStockJpaMapper::toDomain);
    }

    @Override
    public boolean existsByVariantId(Long variantId) {
        return springDataProductVariantStockJpaRepository.existsByVariantId(variantId);
    }


    @Override
    public boolean decreaseIfEnough(Long variantId, int quantity) {
        int updatedRows = springDataProductVariantStockJpaRepository.decreaseStockIfEnough(
                variantId,
                quantity
        );

        return updatedRows == 1;
    }

    @Override
    public boolean restore(Long variantId, int quantity) {
        int updatedRows = springDataProductVariantStockJpaRepository.restoreStock(
                variantId,
                quantity
        );

        return updatedRows == 1;
    }

    @Override
    public boolean updateAvailability(Long variantId, boolean available) {
        int updatedRows = springDataProductVariantStockJpaRepository.updateAvailability(
                variantId,
                available
        );

        return updatedRows == 1;
    }
}
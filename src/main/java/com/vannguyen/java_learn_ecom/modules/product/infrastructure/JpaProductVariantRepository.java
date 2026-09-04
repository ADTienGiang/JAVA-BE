package com.vannguyen.java_learn_ecom.modules.product.infrastructure;

import com.vannguyen.java_learn_ecom.modules.product.domain.ProductVariant;
import com.vannguyen.java_learn_ecom.modules.product.domain.ProductVariantRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@Primary
public class JpaProductVariantRepository implements ProductVariantRepository {

    private final SpringDataProductVariantJpaRepository springDataProductVariantJpaRepository;
    private final ProductVariantJpaMapper productVariantJpaMapper;

    public JpaProductVariantRepository(
            SpringDataProductVariantJpaRepository springDataProductVariantJpaRepository,
            ProductVariantJpaMapper productVariantJpaMapper
    ) {
        this.springDataProductVariantJpaRepository = springDataProductVariantJpaRepository;
        this.productVariantJpaMapper = productVariantJpaMapper;
    }

    @Override
    public ProductVariant save(ProductVariant variant) {
        ProductVariantJpaEntity entity = productVariantJpaMapper.toEntity(variant);
        ProductVariantJpaEntity savedEntity = springDataProductVariantJpaRepository.save(entity);

        return productVariantJpaMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<ProductVariant> findById(Long id) {
        return springDataProductVariantJpaRepository.findById(id)
                .map(productVariantJpaMapper::toDomain);
    }

    @Override
    public List<ProductVariant> findAllActiveByProductId(Long productId) {
        return springDataProductVariantJpaRepository.findAllByProductIdAndActiveTrue(productId)
                .stream()
                .map(productVariantJpaMapper::toDomain)
                .toList();
    }

    @Override
    public boolean existsBySku(String sku) {
        return springDataProductVariantJpaRepository.existsBySku(sku.trim().toUpperCase());
    }
}
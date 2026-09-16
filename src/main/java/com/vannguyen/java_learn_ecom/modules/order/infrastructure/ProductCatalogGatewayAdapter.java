package com.vannguyen.java_learn_ecom.modules.order.infrastructure;

import com.vannguyen.java_learn_ecom.common.exception.BusinessException;
import com.vannguyen.java_learn_ecom.common.exception.ResourceNotFoundException;
import com.vannguyen.java_learn_ecom.modules.order.application.ProductCatalogGateway;
import com.vannguyen.java_learn_ecom.modules.order.application.ProductItemSnapshot;
import com.vannguyen.java_learn_ecom.modules.product.domain.Product;
import com.vannguyen.java_learn_ecom.modules.product.domain.ProductRepository;
import com.vannguyen.java_learn_ecom.modules.product.domain.ProductStatus;
import com.vannguyen.java_learn_ecom.modules.product.domain.ProductVariant;
import com.vannguyen.java_learn_ecom.modules.product.domain.ProductVariantRepository;
import org.springframework.stereotype.Component;

@Component
public class ProductCatalogGatewayAdapter implements ProductCatalogGateway {

    private final ProductRepository productRepository;
    private final ProductVariantRepository productVariantRepository;

    public ProductCatalogGatewayAdapter(
            ProductRepository productRepository,
            ProductVariantRepository productVariantRepository
    ) {
        this.productRepository = productRepository;
        this.productVariantRepository = productVariantRepository;
    }

    @Override
    public ProductItemSnapshot getSellableProductItem(Long productId, Long variantId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        if (product.getStatus() != ProductStatus.ACTIVE) {
            throw new BusinessException("Product is not active");
        }

        ProductVariant variant = productVariantRepository.findById(variantId)
                .orElseThrow(() -> new ResourceNotFoundException("Product variant not found"));

        if (!variant.getProductId().equals(productId)) {
            throw new BusinessException("Product variant does not belong to product");
        }

        if (!variant.isActive()) {
            throw new BusinessException("Product variant is not active");
        }

        return new ProductItemSnapshot(
                product.getId(),
                variant.getId(),
                product.getName(),
                variant.getName(),
                variant.getPrice()
        );
    }
}
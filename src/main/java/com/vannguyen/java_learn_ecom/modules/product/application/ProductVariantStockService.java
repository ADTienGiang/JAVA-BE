package com.vannguyen.java_learn_ecom.modules.product.application;

import com.vannguyen.java_learn_ecom.common.exception.BusinessException;
import com.vannguyen.java_learn_ecom.common.exception.ResourceNotFoundException;
import com.vannguyen.java_learn_ecom.modules.product.domain.ProductVariant;
import com.vannguyen.java_learn_ecom.modules.product.domain.ProductVariantRepository;
import com.vannguyen.java_learn_ecom.modules.product.domain.ProductVariantStock;
import com.vannguyen.java_learn_ecom.modules.product.domain.ProductVariantStockRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductVariantStockService {

    private final ProductVariantRepository productVariantRepository;
    private final ProductVariantStockRepository productVariantStockRepository;

    public ProductVariantStockService(
            ProductVariantRepository productVariantRepository,
            ProductVariantStockRepository productVariantStockRepository
    ) {
        this.productVariantRepository = productVariantRepository;
        this.productVariantStockRepository = productVariantStockRepository;
    }

    @Transactional
    public ProductVariantStock initialize(InitializeProductVariantStockCommand command) {
        ProductVariant variant = productVariantRepository.findById(command.variantId())
                .orElseThrow(() -> new ResourceNotFoundException("Product variant not found"));

        if (!variant.getProductId().equals(command.productId())) {
            throw new BusinessException("Product variant does not belong to product");
        }

        if (!variant.isActive()) {
            throw new BusinessException("Cannot initialize stock for inactive variant");
        }

        if (productVariantStockRepository.existsByVariantId(command.variantId())) {
            throw new BusinessException("Product variant stock already exists");
        }

        ProductVariantStock stock = ProductVariantStock.initialize(
                command.variantId(),
                command.quantity()
        );

        return productVariantStockRepository.save(stock);
    }

    @Transactional(readOnly = true)
    public ProductVariantStock getByVariantId(Long productId, Long variantId) {
        ProductVariant variant = productVariantRepository.findById(variantId)
                .orElseThrow(() -> new ResourceNotFoundException("Product variant not found"));

        if (!variant.getProductId().equals(productId)) {
            throw new BusinessException("Product variant does not belong to product");
        }

        return productVariantStockRepository.findByVariantId(variantId)
                .orElseThrow(() -> new ResourceNotFoundException("Product variant stock not found"));
    }

    @Transactional
    public ProductVariantStock decrease(DecreaseProductVariantStockCommand command) {
        ProductVariant variant = productVariantRepository.findById(command.variantId())
                .orElseThrow(() -> new ResourceNotFoundException("Product variant not found"));

        if (!variant.getProductId().equals(command.productId())) {
            throw new BusinessException("Product variant does not belong to product");
        }

        if (!variant.isActive()) {
            throw new BusinessException("Cannot decrease stock for inactive variant");
        }

        boolean decreased = productVariantStockRepository.decreaseIfEnough(
                command.variantId(),
                command.quantity()
        );

        if (!decreased) {
            throw new BusinessException("Insufficient stock");
        }

        return productVariantStockRepository.findByVariantId(command.variantId())
                .orElseThrow(() -> new ResourceNotFoundException("Product variant stock not found"));
    }



    @Transactional
    public ProductVariantStock restore(RestoreProductVariantStockCommand command) {
        ProductVariant variant = productVariantRepository.findById(command.variantId())
                .orElseThrow(() -> new ResourceNotFoundException("Product variant not found"));

        if (!variant.getProductId().equals(command.productId())) {
            throw new BusinessException("Product variant does not belong to product");
        }

        boolean restored = productVariantStockRepository.restore(
                command.variantId(),
                command.quantity()
        );

        if (!restored) {
            throw new ResourceNotFoundException("Product variant stock not found");
        }

        return productVariantStockRepository.findByVariantId(command.variantId())
                .orElseThrow(() -> new ResourceNotFoundException("Product variant stock not found"));
    }


    @Transactional
    public ProductVariantStock markAvailable(Long productId, Long variantId) {
        ProductVariant variant = productVariantRepository.findById(variantId)
                .orElseThrow(() -> new ResourceNotFoundException("Product variant not found"));

        if (!variant.getProductId().equals(productId)) {
            throw new BusinessException("Product variant does not belong to product");
        }

        if (!variant.isActive()) {
            throw new BusinessException("Cannot mark stock available for inactive variant");
        }

        boolean updated = productVariantStockRepository.updateAvailability(variantId, true);

        if (!updated) {
            throw new ResourceNotFoundException("Product variant stock not found");
        }

        return productVariantStockRepository.findByVariantId(variantId)
                .orElseThrow(() -> new ResourceNotFoundException("Product variant stock not found"));
    }

    @Transactional
    public ProductVariantStock markUnavailable(Long productId, Long variantId) {
        ProductVariant variant = productVariantRepository.findById(variantId)
                .orElseThrow(() -> new ResourceNotFoundException("Product variant not found"));

        if (!variant.getProductId().equals(productId)) {
            throw new BusinessException("Product variant does not belong to product");
        }

        boolean updated = productVariantStockRepository.updateAvailability(variantId, false);

        if (!updated) {
            throw new ResourceNotFoundException("Product variant stock not found");
        }

        return productVariantStockRepository.findByVariantId(variantId)
                .orElseThrow(() -> new ResourceNotFoundException("Product variant stock not found"));
    }


    @Transactional(readOnly = true)
    public ProductVariantSellableResult getSellableState(Long productId, Long variantId) {
        ProductVariant variant = productVariantRepository.findById(variantId)
                .orElseThrow(() -> new ResourceNotFoundException("Product variant not found"));

        if (!variant.getProductId().equals(productId)) {
            throw new BusinessException("Product variant does not belong to product");
        }

        ProductVariantStock stock = productVariantStockRepository.findByVariantId(variantId)
                .orElseThrow(() -> new ResourceNotFoundException("Product variant stock not found"));

        boolean sellable = variant.isActive()
                && stock.isAvailable()
                && stock.getQuantity() > 0;

        return new ProductVariantSellableResult(
                variantId,
                variant.isActive(),
                stock.isAvailable(),
                stock.getQuantity(),
                sellable,
                resolveSellableReason(variant, stock)
        );
    }

    private String resolveSellableReason(ProductVariant variant, ProductVariantStock stock) {
        if (!variant.isActive()) {
            return "Variant is inactive";
        }

        if (!stock.isAvailable()) {
            return "Stock is unavailable";
        }

        if (stock.getQuantity() <= 0) {
            return "Stock quantity is zero";
        }

        return "Sellable";
    }
}
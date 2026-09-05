package com.vannguyen.java_learn_ecom.modules.product.application;

import com.vannguyen.java_learn_ecom.common.exception.BusinessException;
import com.vannguyen.java_learn_ecom.common.exception.ResourceNotFoundException;
import com.vannguyen.java_learn_ecom.modules.product.domain.Product;
import com.vannguyen.java_learn_ecom.modules.product.domain.ProductRepository;
import com.vannguyen.java_learn_ecom.modules.product.domain.ProductStatus;
import com.vannguyen.java_learn_ecom.modules.product.domain.ProductVariant;
import com.vannguyen.java_learn_ecom.modules.product.domain.ProductVariantRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;
@Service
public class ProductVariantService {

    private static final Logger log = LoggerFactory.getLogger(ProductVariantService.class);

    private final ProductVariantRepository productVariantRepository;
    private final ProductRepository productRepository;

    public ProductVariantService(
            ProductVariantRepository productVariantRepository,
            ProductRepository productRepository
    ) {
        this.productVariantRepository = productVariantRepository;
        this.productRepository = productRepository;
    }
    @Transactional
    public ProductVariant create(CreateProductVariantCommand command) {
        log.info("Create product variant request received: productId={}, sku={}",
                command.productId(), command.sku());

        Product product = productRepository.findById(command.productId())
                .orElseThrow(() -> {
                    log.warn("Create product variant rejected because product was not found: productId={}",
                            command.productId());
                    return new ResourceNotFoundException("Product not found");
                });

        if (product.getStatus() != ProductStatus.ACTIVE) {
            log.warn("Create product variant rejected because product is not active: productId={}, status={}",
                    product.getId(), product.getStatus());
            throw new BusinessException("Cannot create variant for inactive product");
        }

        if (productVariantRepository.existsBySku(command.sku())) {
            log.warn("Create product variant rejected because sku already exists: sku={}", command.sku());
            throw new BusinessException("Product variant sku already exists");
        }

        ProductVariant variant = ProductVariant.create(
                command.productId(),
                command.sku(),
                command.name(),
                command.price()
        );

        ProductVariant savedVariant = productVariantRepository.save(variant);

        log.info("Product variant created successfully: id={}, productId={}, sku={}",
                savedVariant.getId(), savedVariant.getProductId(), savedVariant.getSku());

        return savedVariant;
    }

    @Transactional(readOnly = true)
    public List<ProductVariant> findAllActiveByProductId(Long productId) {
        log.info("Find all active product variants: productId={}", productId);

        if (productRepository.findById(productId).isEmpty()) {
            log.warn("Find product variants rejected because product was not found: productId={}", productId);
            throw new ResourceNotFoundException("Product not found");
        }

        return productVariantRepository.findAllActiveByProductId(productId);
    }
    @Transactional
    public ProductVariant update(UpdateProductVariantCommand command) {
        log.info("Update product variant request received: productId={}, variantId={}, sku={}",
                command.productId(), command.variantId(), command.sku());

        if (productRepository.findById(command.productId()).isEmpty()) {
            log.warn("Update product variant rejected because product was not found: productId={}",
                    command.productId());
            throw new ResourceNotFoundException("Product not found");
        }

        ProductVariant variant = productVariantRepository.findById(command.variantId())
                .orElseThrow(() -> {
                    log.warn("Update product variant rejected because variant was not found: variantId={}",
                            command.variantId());
                    return new ResourceNotFoundException("Product variant not found");
                });

        if (!variant.getProductId().equals(command.productId())) {
            log.warn("Update product variant rejected because variant does not belong to product: productId={}, variantId={}",
                    command.productId(), command.variantId());
            throw new BusinessException("Product variant does not belong to product");
        }

        String normalizedSku = command.sku().trim().toUpperCase();

        if (!variant.getSku().equals(normalizedSku) && productVariantRepository.existsBySku(command.sku())) {
            log.warn("Update product variant rejected because sku already exists: variantId={}, sku={}",
                    command.variantId(), command.sku());
            throw new BusinessException("Product variant sku already exists");
        }

        variant.update(
                command.sku(),
                command.name(),
                command.price()
        );

        ProductVariant savedVariant = productVariantRepository.save(variant);

        log.info("Product variant updated successfully: id={}, productId={}, sku={}",
                savedVariant.getId(), savedVariant.getProductId(), savedVariant.getSku());

        return savedVariant;
    }

    @Transactional
    public ProductVariant deactivate(Long productId, Long variantId) {
        log.info("Deactivate product variant request received: productId={}, variantId={}",
                productId, variantId);

        if (productRepository.findById(productId).isEmpty()) {
            log.warn("Deactivate product variant rejected because product was not found: productId={}",
                    productId);
            throw new ResourceNotFoundException("Product not found");
        }

        ProductVariant variant = productVariantRepository.findById(variantId)
                .orElseThrow(() -> {
                    log.warn("Deactivate product variant rejected because variant was not found: variantId={}",
                            variantId);
                    return new ResourceNotFoundException("Product variant not found");
                });

        if (!variant.getProductId().equals(productId)) {
            log.warn("Deactivate product variant rejected because variant does not belong to product: productId={}, variantId={}",
                    productId, variantId);
            throw new BusinessException("Product variant does not belong to product");
        }

        variant.deactivate();

        ProductVariant savedVariant = productVariantRepository.save(variant);

        log.info("Product variant deactivated successfully: id={}, productId={}",
                savedVariant.getId(), savedVariant.getProductId());

        return savedVariant;
    }
    
}
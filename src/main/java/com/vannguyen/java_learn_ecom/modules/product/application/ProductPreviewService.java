package com.vannguyen.java_learn_ecom.modules.product.application;

import com.vannguyen.java_learn_ecom.common.exception.BusinessException;
import com.vannguyen.java_learn_ecom.modules.product.domain.ProductStatus;
import com.vannguyen.java_learn_ecom.modules.product.presentation.ProductDetailResponse;
import com.vannguyen.java_learn_ecom.modules.product.presentation.ProductPreviewResponse;
import com.vannguyen.java_learn_ecom.modules.product.application.ProductPreviewCommand;
import com.vannguyen.java_learn_ecom.modules.product.application.ProductPreviewResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ProductPreviewService {

    private static final Logger log = LoggerFactory.getLogger(ProductPreviewService.class);

    public ProductPreviewResult preview(ProductPreviewCommand command) {
        log.info("Preview product request received: name={}, price={}", command.name(), command.price());

        if (command.name().toLowerCase().contains("test")) {
            log.warn("Product preview rejected because name contains forbidden word: name={}", command.name());
            throw new BusinessException("Product name cannot contain test");
        }

        ProductPreviewResult result = new ProductPreviewResult(
                command.name(),
                command.price(),
                ProductStatus.PREVIEW_ONLY
        );

        log.info("Product preview created successfully: name={}", result.name());

        return result;
    }

    public ProductDetailResponse getPreviewById(Long id) {
        log.info("Get product preview by id: id={}", id);

        return new ProductDetailResponse(
                id,
                "Sample Product " + id,
                BigDecimal.valueOf(100),
                ProductStatus.PREVIEW_ONLY
        );
    }

    public List<ProductPreviewResponse> searchPreview(String keyword) {
        log.info("Search product preview: keyword={}", keyword);

        return List.of(
                new ProductPreviewResponse(keyword + " 1", BigDecimal.valueOf(100), ProductStatus.PREVIEW_ONLY),
                new ProductPreviewResponse(keyword + " 2", BigDecimal.valueOf(100), ProductStatus.PREVIEW_ONLY)
        );
    }
}
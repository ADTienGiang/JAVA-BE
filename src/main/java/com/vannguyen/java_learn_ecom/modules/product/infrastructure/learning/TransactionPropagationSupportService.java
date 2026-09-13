package com.vannguyen.java_learn_ecom.modules.product.infrastructure.learning;

import com.vannguyen.java_learn_ecom.modules.product.domain.ProductStatus;
import com.vannguyen.java_learn_ecom.modules.product.infrastructure.ProductJpaEntity;
import com.vannguyen.java_learn_ecom.modules.product.infrastructure.SpringDataProductJpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class TransactionPropagationSupportService {

    private final SpringDataProductJpaRepository productRepository;

    public TransactionPropagationSupportService(SpringDataProductJpaRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void createProductWithRequired(Long categoryId) {
        ProductJpaEntity product = new ProductJpaEntity(
                null,
                categoryId,
                "Propagation REQUIRED Inner Demo " + System.currentTimeMillis(),
                "This product joins the outer transaction",
                BigDecimal.valueOf(30.00),
                ProductStatus.ACTIVE
        );

        productRepository.save(product);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void createProductWithRequiresNew(Long categoryId) {
        ProductJpaEntity product = new ProductJpaEntity(
                null,
                categoryId,
                "Propagation REQUIRES_NEW Inner Demo " + System.currentTimeMillis(),
                "This product commits in its own transaction",
                BigDecimal.valueOf(40.00),
                ProductStatus.ACTIVE
        );

        productRepository.save(product);
    }
}
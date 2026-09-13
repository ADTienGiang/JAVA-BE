package com.vannguyen.java_learn_ecom.modules.product.infrastructure.learning;

import com.vannguyen.java_learn_ecom.modules.product.domain.ProductStatus;
import com.vannguyen.java_learn_ecom.modules.product.infrastructure.ProductJpaEntity;
import com.vannguyen.java_learn_ecom.modules.product.infrastructure.SpringDataProductJpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import jakarta.persistence.EntityManager;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import java.math.BigDecimal;
import org.springframework.transaction.annotation.Isolation;
@Service
public class TransactionLearningService {
    private final TransactionPropagationSupportService propagationSupportService;
    private final SpringDataProductJpaRepository productRepository;
    private final EntityManager entityManager;

    public TransactionLearningService(
            SpringDataProductJpaRepository productRepository,
            EntityManager entityManager,
            TransactionPropagationSupportService propagationSupportService
    ) {
        this.productRepository = productRepository;
        this.entityManager = entityManager;
        this.propagationSupportService = propagationSupportService;
    }

    public void createProductThenFailWithoutServiceTransaction(Long categoryId) {
        ProductJpaEntity product = new ProductJpaEntity(
                null,
                categoryId,
                "No Service Transaction Demo " + System.currentTimeMillis(),
                "This product is saved before an intentional failure",
                BigDecimal.valueOf(10.00),
                ProductStatus.ACTIVE
        );

        ProductJpaEntity savedProduct = productRepository.save(product);

        throw new RuntimeException(
                "Intentional failure after saving product id=" + savedProduct.getId()
        );
    }

    @Transactional
    public void createProductThenFailWithServiceTransaction(Long categoryId) {
        ProductJpaEntity product = new ProductJpaEntity(
                null,
                categoryId,
                "Service Transaction Rollback Demo " + System.currentTimeMillis(),
                "This product should be rolled back after an intentional failure",
                BigDecimal.valueOf(20.00),
                ProductStatus.ACTIVE
        );

        ProductJpaEntity savedProduct = productRepository.save(product);

        throw new RuntimeException(
                "Intentional failure after saving product id=" + savedProduct.getId()
        );
    }



    public TransactionBoundaryLearningResult inspectBoundaryWithoutServiceTransaction(Long productId) {
        boolean transactionActiveBeforeRepositoryCall =
                TransactionSynchronizationManager.isActualTransactionActive();

        ProductJpaEntity product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found with id=" + productId));

        boolean transactionActiveAfterRepositoryCall =
                TransactionSynchronizationManager.isActualTransactionActive();

        boolean productManagedAfterRepositoryCall = entityManager.contains(product);

        return new TransactionBoundaryLearningResult(
                productId,
                transactionActiveBeforeRepositoryCall,
                transactionActiveAfterRepositoryCall,
                productManagedAfterRepositoryCall,
                null
        );
    }

    @Transactional(readOnly = true)
    public TransactionBoundaryLearningResult inspectBoundaryWithServiceTransaction(Long productId) {
        boolean transactionActiveBeforeRepositoryCall =
                TransactionSynchronizationManager.isActualTransactionActive();

        boolean transactionReadOnly =
                TransactionSynchronizationManager.isCurrentTransactionReadOnly();

        ProductJpaEntity product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found with id=" + productId));

        boolean transactionActiveAfterRepositoryCall =
                TransactionSynchronizationManager.isActualTransactionActive();

        boolean productManagedAfterRepositoryCall = entityManager.contains(product);

        return new TransactionBoundaryLearningResult(
                productId,
                transactionActiveBeforeRepositoryCall,
                transactionActiveAfterRepositoryCall,
                productManagedAfterRepositoryCall,
                transactionReadOnly
        );
    }

    public record TransactionBoundaryLearningResult(
            Long productId,
            boolean transactionActiveBeforeRepositoryCall,
            boolean transactionActiveAfterRepositoryCall,
            boolean productManagedAfterRepositoryCall,
            Boolean transactionReadOnly
    ) {
    }


    @Transactional(readOnly = true)
    public TransactionProxyLearningResult inspectTransactionalMethodCalledThroughProxy() {
        return new TransactionProxyLearningResult(
                "Called through Spring proxy",
                TransactionSynchronizationManager.isActualTransactionActive(),
                TransactionSynchronizationManager.isCurrentTransactionReadOnly()
        );
    }

    public TransactionProxyLearningResult inspectSelfInvocationProblem() {
        return inspectTransactionalMethodCalledThroughProxy();
    }

    public record TransactionProxyLearningResult(
            String scenario,
            boolean transactionActive,
            boolean transactionReadOnly
    ) {
    }



    @Transactional
    public void createProductsThenFailWithRequired(Long categoryId) {
        ProductJpaEntity outerProduct = new ProductJpaEntity(
                null,
                categoryId,
                "Propagation REQUIRED Outer Demo " + System.currentTimeMillis(),
                "This product is created in the outer transaction",
                BigDecimal.valueOf(25.00),
                ProductStatus.ACTIVE
        );

        productRepository.save(outerProduct);

        propagationSupportService.createProductWithRequired(categoryId);

        throw new RuntimeException("Intentional failure after REQUIRED inner call");
    }

    @Transactional
    public void createProductsThenFailWithRequiresNew(Long categoryId) {
        ProductJpaEntity outerProduct = new ProductJpaEntity(
                null,
                categoryId,
                "Propagation REQUIRES_NEW Outer Demo " + System.currentTimeMillis(),
                "This product is created in the outer transaction",
                BigDecimal.valueOf(35.00),
                ProductStatus.ACTIVE
        );

        productRepository.save(outerProduct);

        propagationSupportService.createProductWithRequiresNew(categoryId);

        throw new RuntimeException("Intentional failure after REQUIRES_NEW inner call");
    }
//
@Transactional(readOnly = true)
public IsolationLearningResult inspectDefaultIsolation() {
    String isolationLevel = (String) entityManager
            .createNativeQuery("show transaction_isolation")
            .getSingleResult();

    return new IsolationLearningResult(
            "default",
            isolationLevel,
            TransactionSynchronizationManager.isActualTransactionActive(),
            TransactionSynchronizationManager.isCurrentTransactionReadOnly()
    );
}

    @Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED)
    public IsolationLearningResult inspectReadCommittedIsolation() {
        String isolationLevel = (String) entityManager
                .createNativeQuery("show transaction_isolation")
                .getSingleResult();

        return new IsolationLearningResult(
                "read committed",
                isolationLevel,
                TransactionSynchronizationManager.isActualTransactionActive(),
                TransactionSynchronizationManager.isCurrentTransactionReadOnly()
        );
    }

    @Transactional(readOnly = true, isolation = Isolation.REPEATABLE_READ)
    public IsolationLearningResult inspectRepeatableReadIsolation() {
        String isolationLevel = (String) entityManager
                .createNativeQuery("show transaction_isolation")
                .getSingleResult();

        return new IsolationLearningResult(
                "repeatable read",
                isolationLevel,
                TransactionSynchronizationManager.isActualTransactionActive(),
                TransactionSynchronizationManager.isCurrentTransactionReadOnly()
        );
    }

    public record IsolationLearningResult(
            String scenario,
            String databaseIsolationLevel,
            boolean transactionActive,
            boolean transactionReadOnly
    ) {
    }


    @Transactional
    public LostUpdateLearningResult updateProductPriceSlowly(
            Long productId,
            BigDecimal newPrice,
            long delayMillis
    ) {
        ProductJpaEntity product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found with id=" + productId));

        BigDecimal priceWhenLoaded = product.getPrice();

        Long versionWhenLoaded = product.getVersion();

        sleep(delayMillis);

        product.changePriceForLearning(newPrice);

        return new LostUpdateLearningResult(
                productId,
                priceWhenLoaded,
                versionWhenLoaded,
                newPrice,
                delayMillis
        );
    }

    private void sleep(long delayMillis) {
        try {
            Thread.sleep(delayMillis);
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Thread was interrupted", exception);
        }
    }

    public record LostUpdateLearningResult(
            Long productId,
            BigDecimal priceWhenLoaded,
            Long versionWhenLoaded,
            BigDecimal requestedNewPrice,
            long delayMillis
    ) {
    }




    @Transactional
    public PessimisticLockLearningResult updateProductPriceSlowlyWithPessimisticLock(
            Long productId,
            BigDecimal newPrice,
            long delayMillis
    ) {
        ProductJpaEntity product = productRepository.findByIdForUpdate(productId)
                .orElseThrow(() -> new RuntimeException("Product not found with id=" + productId));

        BigDecimal priceWhenLocked = product.getPrice();
        Long versionWhenLocked = product.getVersion();

        sleep(delayMillis);

        product.changePriceForLearning(newPrice);

        return new PessimisticLockLearningResult(
                productId,
                priceWhenLocked,
                versionWhenLocked,
                newPrice,
                delayMillis
        );
    }

    public record PessimisticLockLearningResult(
            Long productId,
            BigDecimal priceWhenLocked,
            Long versionWhenLocked,
            BigDecimal requestedNewPrice,
            long delayMillis
    ) {
    }



    @Transactional
    public DeadlockLearningResult lockTwoProductsInOrder(
            Long firstProductId,
            Long secondProductId,
            long delayMillis
    ) {
        ProductJpaEntity firstProduct = productRepository.findByIdForUpdate(firstProductId)
                .orElseThrow(() -> new RuntimeException("First product not found with id=" + firstProductId));

        sleep(delayMillis);

        ProductJpaEntity secondProduct = productRepository.findByIdForUpdate(secondProductId)
                .orElseThrow(() -> new RuntimeException("Second product not found with id=" + secondProductId));

        return new DeadlockLearningResult(
                firstProduct.getId(),
                secondProduct.getId(),
                delayMillis
        );
    }

    public record DeadlockLearningResult(
            Long firstLockedProductId,
            Long secondLockedProductId,
            long delayMillis
    ) {
    }
}
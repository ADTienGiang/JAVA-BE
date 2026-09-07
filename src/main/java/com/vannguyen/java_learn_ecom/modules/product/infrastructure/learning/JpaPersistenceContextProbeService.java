package com.vannguyen.java_learn_ecom.modules.product.infrastructure.learning;

import com.vannguyen.java_learn_ecom.common.exception.ResourceNotFoundException;
import com.vannguyen.java_learn_ecom.modules.product.infrastructure.ProductJpaEntity;
import com.vannguyen.java_learn_ecom.modules.product.infrastructure.SpringDataProductJpaRepository;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.vannguyen.java_learn_ecom.modules.product.domain.ProductStatus;
import jakarta.persistence.EntityManagerFactory;
import java.math.BigDecimal;
import org.hibernate.LazyInitializationException;
import java.util.List;

import com.vannguyen.java_learn_ecom.modules.product.infrastructure.SpringDataProductJpaRepository.ProductVariantCountProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;


@Service
public class JpaPersistenceContextProbeService {

    private final SpringDataProductJpaRepository productRepository;
    private final EntityManager entityManager;
    private final EntityManagerFactory entityManagerFactory;

    public JpaPersistenceContextProbeService(
            SpringDataProductJpaRepository productRepository,
            EntityManager entityManager,
            EntityManagerFactory entityManagerFactory
    ) {
        this.productRepository = productRepository;
        this.entityManager = entityManager;
        this.entityManagerFactory = entityManagerFactory;
    }

    @Transactional(readOnly = true)
    public PersistenceContextProbeResult inspect(Long productId) {
        ProductJpaEntity firstLoad = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));

        ProductJpaEntity secondLoad = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));

        return new PersistenceContextProbeResult(
                productId,
                firstLoad == secondLoad,
                entityManager.contains(firstLoad),
                entityManager.contains(secondLoad),
                System.identityHashCode(firstLoad),
                System.identityHashCode(secondLoad)
        );
    }

    public record PersistenceContextProbeResult(
            Long productId,
            boolean sameJavaObject,
            boolean firstLoadManaged,
            boolean secondLoadManaged,
            int firstObjectHash,
            int secondObjectHash
    ) {
    }

    @Transactional(readOnly = true)
    public EntityLifecycleProbeResult inspectTransientAndManaged(Long productId) {
        ProductJpaEntity managedProduct = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));

        ProductJpaEntity transientProduct = new ProductJpaEntity(
                null,
                managedProduct.getCategoryId(),
                "Transient Product",
                "This object is not saved",
                BigDecimal.valueOf(10.00),
                ProductStatus.DRAFT
        );

        return new EntityLifecycleProbeResult(
                productId,
                entityManager.contains(managedProduct),
                entityManager.contains(transientProduct),
                managedProduct.getId(),
                transientProduct.getId()
        );
    }

    public record EntityLifecycleProbeResult(
            Long loadedProductId,
            boolean managedProductInPersistenceContext,
            boolean transientProductInPersistenceContext,
            Long managedProductId,
            Long transientProductId
    ) {
    }

    @Transactional
    public PersistLifecycleProbeResult persistTransientProduct(Long categoryId) {
        ProductJpaEntity transientProduct = new ProductJpaEntity(
                null,
                categoryId,
                "Hibernate Lifecycle Demo",
                "Created from a transient entity",
                BigDecimal.valueOf(12.50),
                ProductStatus.DRAFT
        );

        boolean beforeSaveManaged = entityManager.contains(transientProduct);

        ProductJpaEntity savedProduct = productRepository.save(transientProduct);

        boolean afterSaveOriginalObjectManaged = entityManager.contains(transientProduct);
        boolean afterSaveReturnedObjectManaged = entityManager.contains(savedProduct);

        return new PersistLifecycleProbeResult(
                beforeSaveManaged,
                afterSaveOriginalObjectManaged,
                afterSaveReturnedObjectManaged,
                transientProduct == savedProduct,
                savedProduct.getId()
        );
    }

    public record PersistLifecycleProbeResult(
            boolean beforeSaveManaged,
            boolean afterSaveOriginalObjectManaged,
            boolean afterSaveReturnedObjectManaged,
            boolean sameJavaObject,
            Long savedProductId
    ) {
    }

    @Transactional(readOnly = true)
    public DetachedLifecycleProbeResult inspectDetached(Long productId) {
        ProductJpaEntity managedProduct = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));

        boolean beforeDetachManaged = entityManager.contains(managedProduct);
        int beforeDetachHash = System.identityHashCode(managedProduct);

        entityManager.detach(managedProduct);

        boolean afterDetachManaged = entityManager.contains(managedProduct);

        ProductJpaEntity loadedAgain = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));

        return new DetachedLifecycleProbeResult(
                productId,
                beforeDetachManaged,
                afterDetachManaged,
                entityManager.contains(loadedAgain),
                managedProduct == loadedAgain,
                beforeDetachHash,
                System.identityHashCode(loadedAgain)
        );
    }

    public record DetachedLifecycleProbeResult(
            Long productId,
            boolean beforeDetachManaged,
            boolean afterDetachManaged,
            boolean loadedAgainManaged,
            boolean sameJavaObjectAfterReload,
            int detachedObjectHash,
            int loadedAgainObjectHash
    ) {
    }

    @Transactional
    public RemovedLifecycleProbeResult inspectRemoved(Long categoryId) {
        ProductJpaEntity transientProduct = new ProductJpaEntity(
                null,
                categoryId,
                "Hibernate Removed Demo",
                "This product will be deleted in the same transaction",
                BigDecimal.valueOf(15.00),
                ProductStatus.DRAFT
        );

        boolean beforeSaveManaged = entityManager.contains(transientProduct);

        ProductJpaEntity savedProduct = productRepository.save(transientProduct);

        boolean afterSaveManaged = entityManager.contains(savedProduct);
        Long savedProductId = savedProduct.getId();

        entityManager.remove(savedProduct);

        boolean afterRemoveManaged = entityManager.contains(savedProduct);

        entityManager.flush();

        boolean existsAfterFlush = productRepository.existsById(savedProductId);

        return new RemovedLifecycleProbeResult(
                savedProductId,
                beforeSaveManaged,
                afterSaveManaged,
                afterRemoveManaged,
                existsAfterFlush
        );
    }

    public record RemovedLifecycleProbeResult(
            Long productId,
            boolean beforeSaveManaged,
            boolean afterSaveManaged,
            boolean afterRemoveManaged,
            boolean existsAfterFlush
    ) {
    }


    @Transactional
    public DirtyCheckingProbeResult inspectDirtyChecking(Long categoryId) {
        ProductJpaEntity product = new ProductJpaEntity(
                null,
                categoryId,
                "Hibernate Dirty Checking Demo",
                "This product is created only for dirty checking experiment",
                BigDecimal.valueOf(20.00),
                ProductStatus.DRAFT
        );

        ProductJpaEntity savedProduct = productRepository.save(product);
        entityManager.flush();

        Long productId = savedProduct.getId();
        String nameBeforeChange = savedProduct.getName();
        boolean managedBeforeChange = entityManager.contains(savedProduct);

        savedProduct.changeNameForLearning("Hibernate Dirty Checking Demo Updated");

        boolean managedAfterChange = entityManager.contains(savedProduct);

        entityManager.flush();
        entityManager.clear();

        ProductJpaEntity reloadedProduct = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));

        String nameAfterReload = reloadedProduct.getName();

        productRepository.delete(reloadedProduct);
        entityManager.flush();

        return new DirtyCheckingProbeResult(
                productId,
                managedBeforeChange,
                managedAfterChange,
                nameBeforeChange,
                nameAfterReload,
                true
        );
    }

    public record DirtyCheckingProbeResult(
            Long productId,
            boolean managedBeforeChange,
            boolean managedAfterChange,
            String nameBeforeChange,
            String nameAfterReload,
            boolean updatedWithoutCallingSaveAgain
    ) {
    }

    @Transactional
    public DetachedDirtyCheckingProbeResult inspectDetachedDirtyChecking(Long categoryId) {
        ProductJpaEntity product = new ProductJpaEntity(
                null,
                categoryId,
                "Detached Dirty Checking Demo",
                "This product is created only for detached dirty checking experiment",
                BigDecimal.valueOf(25.00),
                ProductStatus.DRAFT
        );

        ProductJpaEntity savedProduct = productRepository.save(product);
        entityManager.flush();

        Long productId = savedProduct.getId();
        String nameBeforeDetach = savedProduct.getName();
        boolean managedBeforeDetach = entityManager.contains(savedProduct);

        entityManager.detach(savedProduct);

        boolean managedAfterDetach = entityManager.contains(savedProduct);

        savedProduct.changeNameForLearning("Detached Dirty Checking Demo Updated");

        entityManager.flush();
        entityManager.clear();

        ProductJpaEntity reloadedProduct = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));

        String nameAfterReload = reloadedProduct.getName();

        productRepository.delete(reloadedProduct);
        entityManager.flush();

        return new DetachedDirtyCheckingProbeResult(
                productId,
                managedBeforeDetach,
                managedAfterDetach,
                nameBeforeDetach,
                nameAfterReload,
                nameBeforeDetach.equals(nameAfterReload)
        );
    }

    public record DetachedDirtyCheckingProbeResult(
            Long productId,
            boolean managedBeforeDetach,
            boolean managedAfterDetach,
            String nameBeforeDetach,
            String nameAfterReload,
            boolean detachedChangeWasIgnored
    ) {
    }


    @Transactional
    public MergeDetachedProbeResult inspectMergeDetached(Long categoryId) {
        ProductJpaEntity product = new ProductJpaEntity(
                null,
                categoryId,
                "Merge Detached Demo",
                "This product is created only for merge experiment",
                BigDecimal.valueOf(30.00),
                ProductStatus.DRAFT
        );

        ProductJpaEntity savedProduct = productRepository.save(product);
        entityManager.flush();

        Long productId = savedProduct.getId();

        entityManager.detach(savedProduct);

        boolean detachedObjectManagedBeforeMerge = entityManager.contains(savedProduct);

        savedProduct.changeNameForLearning("Merge Detached Demo Updated");

        ProductJpaEntity mergedProduct = entityManager.merge(savedProduct);

        boolean detachedObjectManagedAfterMerge = entityManager.contains(savedProduct);
        boolean mergedObjectManagedAfterMerge = entityManager.contains(mergedProduct);
        boolean sameJavaObject = savedProduct == mergedProduct;

        entityManager.flush();
        entityManager.clear();

        ProductJpaEntity reloadedProduct = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));

        String nameAfterReload = reloadedProduct.getName();

        productRepository.delete(reloadedProduct);
        entityManager.flush();

        return new MergeDetachedProbeResult(
                productId,
                detachedObjectManagedBeforeMerge,
                detachedObjectManagedAfterMerge,
                mergedObjectManagedAfterMerge,
                sameJavaObject,
                nameAfterReload
        );
    }

    public record MergeDetachedProbeResult(
            Long productId,
            boolean detachedObjectManagedBeforeMerge,
            boolean detachedObjectManagedAfterMerge,
            boolean mergedObjectManagedAfterMerge,
            boolean sameJavaObject,
            String nameAfterReload
    ) {
    }


    @Transactional(readOnly = true)
    public TransactionBoundaryProbeResult inspectFirstTransaction(Long productId) {
        ProductJpaEntity product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));

        return new TransactionBoundaryProbeResult(
                productId,
                entityManager.contains(product),
                System.identityHashCode(product)
        );
    }

    @Transactional(readOnly = true)
    public TransactionBoundaryProbeResult inspectSecondTransaction(Long productId) {
        ProductJpaEntity product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));

        return new TransactionBoundaryProbeResult(
                productId,
                entityManager.contains(product),
                System.identityHashCode(product)
        );
    }

    public record TransactionBoundaryProbeResult(
            Long productId,
            boolean managed,
            int objectHash
    ) {
    }

    @Transactional(readOnly = true)
    public LazyLoadingCategoryProbeResult inspectLazyLoadingCategory(Long productId) {
        ProductJpaEntity product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));

        boolean productManaged = entityManager.contains(product);

        boolean categoryLoadedBeforeAccess = entityManagerFactory
                .getPersistenceUnitUtil()
                .isLoaded(product, "category");

        String categoryName = product.getCategory().getName();

        boolean categoryLoadedAfterAccess = entityManagerFactory
                .getPersistenceUnitUtil()
                .isLoaded(product, "category");

        return new LazyLoadingCategoryProbeResult(
                productId,
                productManaged,
                categoryLoadedBeforeAccess,
                categoryName,
                categoryLoadedAfterAccess
        );
    }

    public record LazyLoadingCategoryProbeResult(
            Long productId,
            boolean productManaged,
            boolean categoryLoadedBeforeAccess,
            String categoryName,
            boolean categoryLoadedAfterAccess
    ) {
    }


    public LazyInitializationProbeResult inspectLazyLoadingOutsideTransaction(Long productId) {
        ProductJpaEntity product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));

        boolean categoryLoadedBeforeAccess = entityManagerFactory
                .getPersistenceUnitUtil()
                .isLoaded(product, "category");

        try {
            String categoryName = product.getCategory().getName();

            return new LazyInitializationProbeResult(
                    productId,
                    categoryLoadedBeforeAccess,
                    false,
                    null,
                    categoryName
            );
        } catch (LazyInitializationException exception) {
            return new LazyInitializationProbeResult(
                    productId,
                    categoryLoadedBeforeAccess,
                    true,
                    exception.getClass().getSimpleName(),
                    null
            );
        }
    }

    public record LazyInitializationProbeResult(
            Long productId,
            boolean categoryLoadedBeforeAccess,
            boolean lazyInitializationExceptionHappened,
            String exceptionName,
            String categoryName
    ) {
    }
    @Transactional(readOnly = true)
    public NPlusOneProbeResult inspectNPlusOneCategory() {
        List<ProductJpaEntity> products = productRepository.findAll();

        List<ProductCategoryView> items = products.stream()
                .map(product -> new ProductCategoryView(
                        product.getId(),
                        product.getName(),
                        product.getCategory().getName()
                ))
                .toList();

        return new NPlusOneProbeResult(
                items.size(),
                items
        );
    }

    public record NPlusOneProbeResult(
            int productCount,
            List<ProductCategoryView> items
    ) {
    }

    public record ProductCategoryView(
            Long productId,
            String productName,
            String categoryName
    ) {
    }

    @Transactional(readOnly = true)
    public NPlusOneProbeResult inspectNPlusOneCategoryFixed() {
        List<ProductJpaEntity> products = productRepository.findAllWithCategory();

        List<ProductCategoryView> items = products.stream()
                .map(product -> new ProductCategoryView(
                        product.getId(),
                        product.getName(),
                        product.getCategory().getName()
                ))
                .toList();

        return new NPlusOneProbeResult(
                items.size(),
                items
        );
    }


    @Transactional(readOnly = true)
    public NPlusOneProbeResult inspectNPlusOneCategoryFixedByJoinFetch() {
        List<ProductJpaEntity> products = productRepository.findAllWithCategoryUsingJoinFetch();

        List<ProductCategoryView> items = products.stream()
                .map(product -> new ProductCategoryView(
                        product.getId(),
                        product.getName(),
                        product.getCategory().getName()
                ))
                .toList();

        return new NPlusOneProbeResult(
                items.size(),
                items
        );
    }

    @Transactional(readOnly = true)
    public NPlusOneVariantsProbeResult inspectNPlusOneVariants() {
        List<ProductJpaEntity> products = productRepository.findAll();

        List<ProductVariantCountView> items = products.stream()
                .map(product -> new ProductVariantCountView(
                        product.getId(),
                        product.getName(),
                        product.getVariants().size()
                ))
                .toList();

        return new NPlusOneVariantsProbeResult(
                items.size(),
                items
        );
    }

    public record NPlusOneVariantsProbeResult(
            int productCount,
            List<ProductVariantCountView> items
    ) {
    }

    public record ProductVariantCountView(
            Long productId,
            String productName,
            int variantCount
    ) {
    }

    @Transactional(readOnly = true)
    public NPlusOneVariantsProbeResult inspectNPlusOneVariantsFixedByJoinFetch() {
        List<ProductJpaEntity> products = productRepository.findAllWithVariantsUsingJoinFetch();

        List<ProductVariantCountView> items = products.stream()
                .map(product -> new ProductVariantCountView(
                        product.getId(),
                        product.getName(),
                        product.getVariants().size()
                ))
                .toList();

        return new NPlusOneVariantsProbeResult(
                items.size(),
                items
        );
    }

    @Transactional(readOnly = true)
    public ProductVariantCountPageProbeResult inspectVariantCountProjection(int page, int size) {
        Page<ProductVariantCountProjection> result = productRepository.findProductVariantCounts(
                PageRequest.of(page - 1, size)
        );

        List<ProductVariantCountView> items = result.getContent()
                .stream()
                .map(row -> new ProductVariantCountView(
                        row.getProductId(),
                        row.getProductName(),
                        (int) row.getVariantCount()
                ))
                .toList();

        return new ProductVariantCountPageProbeResult(
                page,
                size,
                result.getTotalElements(),
                result.getTotalPages(),
                items
        );
    }

    public record ProductVariantCountPageProbeResult(
            int page,
            int size,
            long totalItems,
            int totalPages,
            List<ProductVariantCountView> items
    ) {
    }

    @Transactional
    public AutoFlushProbeResult inspectAutoFlushBeforeQuery(Long categoryId) {
        ProductJpaEntity product = new ProductJpaEntity(
                null,
                categoryId,
                "Auto Flush Demo",
                "This product is created only for auto flush experiment",
                BigDecimal.valueOf(35.00),
                ProductStatus.DRAFT
        );

        ProductJpaEntity savedProduct = productRepository.save(product);

        Long productId = savedProduct.getId();

        savedProduct.changeNameForLearning("Auto Flush Demo Updated");

        boolean managedBeforeQuery = entityManager.contains(savedProduct);

        List<ProductJpaEntity> products = productRepository.findAll();

        ProductJpaEntity reloadedProduct = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));

        String nameAfterQuery = reloadedProduct.getName();

        productRepository.delete(reloadedProduct);
        entityManager.flush();

        return new AutoFlushProbeResult(
                productId,
                managedBeforeQuery,
                products.size(),
                nameAfterQuery
        );
    }

    public record AutoFlushProbeResult(
            Long productId,
            boolean managedBeforeQuery,
            int productCount,
            String nameAfterQuery
    ) {
    }


    @Transactional(readOnly = true)
    public ClearPersistenceContextProbeResult inspectClearPersistenceContext(Long productId) {
        ProductJpaEntity firstLoad = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));

        boolean firstManagedBeforeClear = entityManager.contains(firstLoad);
        int firstObjectHash = System.identityHashCode(firstLoad);

        entityManager.clear();

        boolean firstManagedAfterClear = entityManager.contains(firstLoad);

        ProductJpaEntity secondLoad = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));

        return new ClearPersistenceContextProbeResult(
                productId,
                firstManagedBeforeClear,
                firstManagedAfterClear,
                entityManager.contains(secondLoad),
                firstLoad == secondLoad,
                firstObjectHash,
                System.identityHashCode(secondLoad)
        );
    }

    public record ClearPersistenceContextProbeResult(
            Long productId,
            boolean firstManagedBeforeClear,
            boolean firstManagedAfterClear,
            boolean secondManagedAfterReload,
            boolean sameJavaObjectAfterClear,
            int firstObjectHash,
            int secondObjectHash
    ) {
    }




    @Transactional
    public RefreshProbeResult inspectRefresh(Long productId) {
        ProductJpaEntity product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));

        String nameFromDatabase = product.getName();

        product.changeNameForLearning("Changed In Memory Only");

        String nameBeforeRefresh = product.getName();

        entityManager.refresh(product);

        String nameAfterRefresh = product.getName();

        return new RefreshProbeResult(
                productId,
                entityManager.contains(product),
                nameFromDatabase,
                nameBeforeRefresh,
                nameAfterRefresh
        );
    }

    public record RefreshProbeResult(
            Long productId,
            boolean managedAfterRefresh,
            String nameFromDatabase,
            String nameBeforeRefresh,
            String nameAfterRefresh
    ) {
    }


    @Transactional
    public SaveAndFlushProbeResult inspectSaveAndFlush(Long categoryId) {
        ProductJpaEntity product = new ProductJpaEntity(
                null,
                categoryId,
                "Save And Flush Demo",
                "This product is created only for saveAndFlush experiment",
                BigDecimal.valueOf(40.00),
                ProductStatus.DRAFT
        );

        boolean beforeSaveManaged = entityManager.contains(product);

        ProductJpaEntity savedProduct = productRepository.saveAndFlush(product);

        boolean afterSaveManaged = entityManager.contains(savedProduct);
        Long productId = savedProduct.getId();

        productRepository.delete(savedProduct);
        entityManager.flush();

        return new SaveAndFlushProbeResult(
                productId,
                beforeSaveManaged,
                afterSaveManaged,
                true
        );
    }

    public record SaveAndFlushProbeResult(
            Long productId,
            boolean beforeSaveManaged,
            boolean afterSaveManaged,
            boolean flushedImmediately
    ) {
    }




    @Transactional
    public void inspectFlushThenRollback(Long categoryId) {
        ProductJpaEntity product = new ProductJpaEntity(
                null,
                categoryId,
                "Flush Then Rollback Demo",
                "This product should be rolled back",
                BigDecimal.valueOf(45.00),
                ProductStatus.DRAFT
        );

        productRepository.save(product);

        entityManager.flush();

        throw new RuntimeException("Rollback after flush for learning");
    }



}
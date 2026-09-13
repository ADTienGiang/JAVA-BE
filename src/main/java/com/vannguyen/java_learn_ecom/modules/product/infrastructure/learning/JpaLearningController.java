package com.vannguyen.java_learn_ecom.modules.product.infrastructure.learning;

import com.vannguyen.java_learn_ecom.common.api.ApiResponse;
import com.vannguyen.java_learn_ecom.modules.product.infrastructure.learning.JpaPersistenceContextProbeService.PersistenceContextProbeResult;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.vannguyen.java_learn_ecom.modules.product.infrastructure.learning.JpaPersistenceContextProbeService.EntityLifecycleProbeResult;
import com.vannguyen.java_learn_ecom.modules.product.infrastructure.learning.JpaPersistenceContextProbeService.PersistLifecycleProbeResult;
import com.vannguyen.java_learn_ecom.modules.product.infrastructure.learning.JpaPersistenceContextProbeService.DetachedLifecycleProbeResult;
import com.vannguyen.java_learn_ecom.modules.product.infrastructure.learning.JpaPersistenceContextProbeService.RemovedLifecycleProbeResult;
import com.vannguyen.java_learn_ecom.modules.product.infrastructure.learning.JpaPersistenceContextProbeService.DirtyCheckingProbeResult;
import com.vannguyen.java_learn_ecom.modules.product.infrastructure.learning.JpaPersistenceContextProbeService.DetachedDirtyCheckingProbeResult;
import com.vannguyen.java_learn_ecom.modules.product.infrastructure.learning.JpaPersistenceContextProbeService.MergeDetachedProbeResult;
import com.vannguyen.java_learn_ecom.modules.product.infrastructure.learning.JpaPersistenceContextProbeService.TransactionBoundaryProbeResult;
import com.vannguyen.java_learn_ecom.modules.product.infrastructure.learning.JpaPersistenceContextProbeService.LazyLoadingCategoryProbeResult;
import com.vannguyen.java_learn_ecom.modules.product.infrastructure.learning.JpaPersistenceContextProbeService.LazyInitializationProbeResult;
import com.vannguyen.java_learn_ecom.modules.product.infrastructure.learning.JpaPersistenceContextProbeService.NPlusOneProbeResult;
import com.vannguyen.java_learn_ecom.modules.product.infrastructure.learning.JpaPersistenceContextProbeService.NPlusOneVariantsProbeResult;
import com.vannguyen.java_learn_ecom.modules.product.infrastructure.learning.JpaPersistenceContextProbeService.ProductVariantCountPageProbeResult;
import com.vannguyen.java_learn_ecom.modules.product.infrastructure.learning.JpaPersistenceContextProbeService.AutoFlushProbeResult;
import com.vannguyen.java_learn_ecom.modules.product.infrastructure.learning.JpaPersistenceContextProbeService.ClearPersistenceContextProbeResult;
import com.vannguyen.java_learn_ecom.modules.product.infrastructure.learning.JpaPersistenceContextProbeService.RefreshProbeResult;
import com.vannguyen.java_learn_ecom.modules.product.infrastructure.learning.JpaPersistenceContextProbeService.SaveAndFlushProbeResult;
import com.vannguyen.java_learn_ecom.modules.product.domain.ProductStatus;
import com.vannguyen.java_learn_ecom.modules.product.infrastructure.learning.JpaPersistenceContextProbeService.JpqlBasicProbeResult;
import com.vannguyen.java_learn_ecom.modules.product.infrastructure.learning.JpaPersistenceContextProbeService.JpqlMultiConditionProbeResult;
import com.vannguyen.java_learn_ecom.modules.product.infrastructure.learning.JpaPersistenceContextProbeService.JpqlDynamicConditionProbeResult;
import com.vannguyen.java_learn_ecom.modules.product.infrastructure.learning.JpaPersistenceContextProbeService.NativeQueryProbeResult;
import com.vannguyen.java_learn_ecom.modules.product.infrastructure.learning.JpaPersistenceContextProbeService.NativeProjectionProbeResult;
import com.vannguyen.java_learn_ecom.modules.product.infrastructure.learning.JpaPersistenceContextProbeService.ModifyingQueryProbeResult;
import com.vannguyen.java_learn_ecom.modules.product.infrastructure.learning.JpaPersistenceContextProbeService.ModifyingClearAutomaticallyProbeResult;
import com.vannguyen.java_learn_ecom.modules.product.infrastructure.learning.JpaPersistenceContextProbeService.ModifyingFlushAndClearProbeResult;





@RestController
@RequestMapping("/api/learning/jpa")
public class JpaLearningController {

    private final JpaPersistenceContextProbeService probeService;

    public JpaLearningController(JpaPersistenceContextProbeService probeService) {
        this.probeService = probeService;
    }

    @GetMapping("/products/{id}/persistence-context")
    public ResponseEntity<ApiResponse<PersistenceContextProbeResult>> inspectPersistenceContext(
            @PathVariable Long id
    ) {
        PersistenceContextProbeResult result = probeService.inspect(id);

        return ResponseEntity.ok(ApiResponse.success(result));
    }

    @GetMapping("/products/{id}/entity-lifecycle/transient-managed")
    public ResponseEntity<ApiResponse<EntityLifecycleProbeResult>> inspectTransientAndManaged(
            @PathVariable Long id
    ) {
        EntityLifecycleProbeResult result = probeService.inspectTransientAndManaged(id);

        return ResponseEntity.ok(ApiResponse.success(result));
    }

    @PostMapping("/categories/{categoryId}/entity-lifecycle/persist-product")
    public ResponseEntity<ApiResponse<PersistLifecycleProbeResult>> persistTransientProduct(
            @PathVariable Long categoryId
    ) {
        PersistLifecycleProbeResult result = probeService.persistTransientProduct(categoryId);

        return ResponseEntity.ok(ApiResponse.success(result));
    }

    @GetMapping("/products/{id}/entity-lifecycle/detached")
    public ResponseEntity<ApiResponse<DetachedLifecycleProbeResult>> inspectDetached(
            @PathVariable Long id
    ) {
        DetachedLifecycleProbeResult result = probeService.inspectDetached(id);

        return ResponseEntity.ok(ApiResponse.success(result));
    }

    @PostMapping("/categories/{categoryId}/entity-lifecycle/remove-product")
    public ResponseEntity<ApiResponse<RemovedLifecycleProbeResult>> inspectRemoved(
            @PathVariable Long categoryId
    ) {
        RemovedLifecycleProbeResult result = probeService.inspectRemoved(categoryId);

        return ResponseEntity.ok(ApiResponse.success(result));
    }

    @PostMapping("/categories/{categoryId}/dirty-checking")
    public ResponseEntity<ApiResponse<DirtyCheckingProbeResult>> inspectDirtyChecking(
            @PathVariable Long categoryId
    ) {
        DirtyCheckingProbeResult result = probeService.inspectDirtyChecking(categoryId);

        return ResponseEntity.ok(ApiResponse.success(result));
    }

    @PostMapping("/categories/{categoryId}/detached-dirty-checking")
    public ResponseEntity<ApiResponse<DetachedDirtyCheckingProbeResult>> inspectDetachedDirtyChecking(
            @PathVariable Long categoryId
    ) {
        DetachedDirtyCheckingProbeResult result = probeService.inspectDetachedDirtyChecking(categoryId);

        return ResponseEntity.ok(ApiResponse.success(result));
    }

    @PostMapping("/categories/{categoryId}/merge-detached")
    public ResponseEntity<ApiResponse<MergeDetachedProbeResult>> inspectMergeDetached(
            @PathVariable Long categoryId
    ) {
        MergeDetachedProbeResult result = probeService.inspectMergeDetached(categoryId);

        return ResponseEntity.ok(ApiResponse.success(result));
    }

    @GetMapping("/products/{id}/transaction-boundary/first")
    public ResponseEntity<ApiResponse<TransactionBoundaryProbeResult>> inspectFirstTransaction(
            @PathVariable Long id
    ) {
        TransactionBoundaryProbeResult result = probeService.inspectFirstTransaction(id);

        return ResponseEntity.ok(ApiResponse.success(result));
    }

    @GetMapping("/products/{id}/transaction-boundary/second")
    public ResponseEntity<ApiResponse<TransactionBoundaryProbeResult>> inspectSecondTransaction(
            @PathVariable Long id
    ) {
        TransactionBoundaryProbeResult result = probeService.inspectSecondTransaction(id);

        return ResponseEntity.ok(ApiResponse.success(result));
    }

    @GetMapping("/products/{id}/lazy-loading/category")
    public ResponseEntity<ApiResponse<LazyLoadingCategoryProbeResult>> inspectLazyLoadingCategory(
            @PathVariable Long id
    ) {
        LazyLoadingCategoryProbeResult result = probeService.inspectLazyLoadingCategory(id);

        return ResponseEntity.ok(ApiResponse.success(result));
    }

    @GetMapping("/products/{id}/lazy-loading/outside-transaction")
    public ResponseEntity<ApiResponse<LazyInitializationProbeResult>> inspectLazyLoadingOutsideTransaction(
            @PathVariable Long id
    ) {
        LazyInitializationProbeResult result = probeService.inspectLazyLoadingOutsideTransaction(id);

        return ResponseEntity.ok(ApiResponse.success(result));
    }

    @GetMapping("/n-plus-one/categories")
    public ResponseEntity<ApiResponse<NPlusOneProbeResult>> inspectNPlusOneCategory() {
        NPlusOneProbeResult result = probeService.inspectNPlusOneCategory();

        return ResponseEntity.ok(ApiResponse.success(result));
    }

    @GetMapping("/n-plus-one/categories/fixed")
    public ResponseEntity<ApiResponse<NPlusOneProbeResult>> inspectNPlusOneCategoryFixed() {
        NPlusOneProbeResult result = probeService.inspectNPlusOneCategoryFixed();

        return ResponseEntity.ok(ApiResponse.success(result));
    }

    @GetMapping("/n-plus-one/categories/fixed-join-fetch")
    public ResponseEntity<ApiResponse<NPlusOneProbeResult>> inspectNPlusOneCategoryFixedByJoinFetch() {
        NPlusOneProbeResult result = probeService.inspectNPlusOneCategoryFixedByJoinFetch();

        return ResponseEntity.ok(ApiResponse.success(result));
    }

    @GetMapping("/n-plus-one/variants")
    public ResponseEntity<ApiResponse<NPlusOneVariantsProbeResult>> inspectNPlusOneVariants() {
        NPlusOneVariantsProbeResult result = probeService.inspectNPlusOneVariants();

        return ResponseEntity.ok(ApiResponse.success(result));
    }


    @GetMapping("/n-plus-one/variants/fixed-join-fetch")
    public ResponseEntity<ApiResponse<NPlusOneVariantsProbeResult>> inspectNPlusOneVariantsFixedByJoinFetch() {
        NPlusOneVariantsProbeResult result = probeService.inspectNPlusOneVariantsFixedByJoinFetch();

        return ResponseEntity.ok(ApiResponse.success(result));
    }

    @GetMapping("/projection/variant-counts")
    public ResponseEntity<ApiResponse<ProductVariantCountPageProbeResult>> inspectVariantCountProjection(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        ProductVariantCountPageProbeResult result = probeService.inspectVariantCountProjection(page, size);

        return ResponseEntity.ok(ApiResponse.success(result));
    }

    @PostMapping("/categories/{categoryId}/auto-flush")
    public ResponseEntity<ApiResponse<AutoFlushProbeResult>> inspectAutoFlushBeforeQuery(
            @PathVariable Long categoryId
    ) {
        AutoFlushProbeResult result = probeService.inspectAutoFlushBeforeQuery(categoryId);

        return ResponseEntity.ok(ApiResponse.success(result));
    }

    @GetMapping("/products/{id}/persistence-context/clear")
    public ResponseEntity<ApiResponse<ClearPersistenceContextProbeResult>> inspectClearPersistenceContext(
            @PathVariable Long id
    ) {
        ClearPersistenceContextProbeResult result = probeService.inspectClearPersistenceContext(id);

        return ResponseEntity.ok(ApiResponse.success(result));
    }

    @PostMapping("/products/{id}/refresh")
    public ResponseEntity<ApiResponse<RefreshProbeResult>> inspectRefresh(
            @PathVariable Long id
    ) {
        RefreshProbeResult result = probeService.inspectRefresh(id);

        return ResponseEntity.ok(ApiResponse.success(result));
    }


    @PostMapping("/categories/{categoryId}/save-and-flush")
    public ResponseEntity<ApiResponse<SaveAndFlushProbeResult>> inspectSaveAndFlush(
            @PathVariable Long categoryId
    ) {
        SaveAndFlushProbeResult result = probeService.inspectSaveAndFlush(categoryId);

        return ResponseEntity.ok(ApiResponse.success(result));
    }





    @PostMapping("/categories/{categoryId}/flush-then-rollback")
    public ResponseEntity<ApiResponse<String>> inspectFlushThenRollback(
            @PathVariable Long categoryId
    ) {
        probeService.inspectFlushThenRollback(categoryId);

        return ResponseEntity.ok(ApiResponse.success("This line should not be reached"));
    }

    @GetMapping("/jpql/products")
    public ResponseEntity<ApiResponse<JpqlBasicProbeResult>> inspectJpqlBasic(
            @RequestParam(defaultValue = "ACTIVE") ProductStatus status
    ) {
        JpqlBasicProbeResult result = probeService.inspectJpqlBasic(status);

        return ResponseEntity.ok(ApiResponse.success(result));
    }


    @GetMapping("/jpql/products/filter")
    public ResponseEntity<ApiResponse<JpqlMultiConditionProbeResult>> inspectJpqlMultiCondition(
            @RequestParam(required = false) ProductStatus status,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) String keyword
    ) {
        JpqlMultiConditionProbeResult result = probeService.inspectJpqlMultiCondition(status, categoryId,keyword);

        return ResponseEntity.ok(ApiResponse.success(result));
    }


    @GetMapping("/jpql/products/dynamic-filter")
    public ResponseEntity<ApiResponse<JpqlDynamicConditionProbeResult>> inspectJpqlDynamicCondition(
            @RequestParam(required = false) ProductStatus status,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) String keyword
    ) {
        JpqlDynamicConditionProbeResult result = probeService.inspectJpqlDynamicCondition(
                status,
                categoryId,
                keyword
        );

        return ResponseEntity.ok(ApiResponse.success(result));
    }


    @GetMapping("/native/products")
    public ResponseEntity<ApiResponse<NativeQueryProbeResult>> inspectNativeQuery(
            @RequestParam(defaultValue = "") String keyword
    ) {
        NativeQueryProbeResult result = probeService.inspectNativeQuery(keyword);

        return ResponseEntity.ok(ApiResponse.success(result));
    }



    @GetMapping("/native/projection/products")
    public ResponseEntity<ApiResponse<NativeProjectionProbeResult>> inspectNativeProjection(
            @RequestParam(defaultValue = "") String keyword
    ) {
        NativeProjectionProbeResult result = probeService.inspectNativeProjection(keyword);

        return ResponseEntity.ok(ApiResponse.success(result));
    }



    @PostMapping("/products/{id}/modifying-query/status")
    public ResponseEntity<ApiResponse<ModifyingQueryProbeResult>> inspectModifyingQuery(
            @PathVariable Long id,
            @RequestParam ProductStatus status
    ) {
        ModifyingQueryProbeResult result = probeService.inspectModifyingQuery(id, status);

        return ResponseEntity.ok(ApiResponse.success(result));
    }

    @PostMapping("/products/{id}/modifying-query/status-clear-auto")
    public ResponseEntity<ApiResponse<ModifyingClearAutomaticallyProbeResult>> inspectModifyingClearAutomatically(
            @PathVariable Long id,
            @RequestParam ProductStatus status
    ) {
        ModifyingClearAutomaticallyProbeResult result = probeService.inspectModifyingClearAutomatically(id, status);

        return ResponseEntity.ok(ApiResponse.success(result));
    }


    @PostMapping("/categories/{categoryId}/modifying-query/flush-and-clear-auto")
    public ResponseEntity<ApiResponse<ModifyingFlushAndClearProbeResult>> inspectModifyingFlushAndClearAutomatically(
            @PathVariable Long categoryId,
            @RequestParam ProductStatus status
    ) {
        ModifyingFlushAndClearProbeResult result = probeService.inspectModifyingFlushAndClearAutomatically(
                categoryId,
                status
        );

        return ResponseEntity.ok(ApiResponse.success(result));
    }
    
}
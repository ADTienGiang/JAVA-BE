package com.vannguyen.java_learn_ecom.modules.product.infrastructure.learning;

import com.vannguyen.java_learn_ecom.common.api.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.vannguyen.java_learn_ecom.modules.product.infrastructure.learning.TransactionLearningService.TransactionBoundaryLearningResult;
import com.vannguyen.java_learn_ecom.modules.product.infrastructure.learning.TransactionLearningService.TransactionProxyLearningResult;
import com.vannguyen.java_learn_ecom.modules.product.infrastructure.learning.TransactionLearningService.IsolationLearningResult;
import com.vannguyen.java_learn_ecom.modules.product.infrastructure.learning.TransactionLearningService.LostUpdateLearningResult;
import com.vannguyen.java_learn_ecom.modules.product.infrastructure.learning.TransactionLearningService.PessimisticLockLearningResult;
import java.math.BigDecimal;
import com.vannguyen.java_learn_ecom.modules.product.infrastructure.learning.TransactionLearningService.DeadlockLearningResult;

@RestController
@RequestMapping("/api/learning/transactions")
public class TransactionLearningController {

    private final TransactionLearningService transactionLearningService;

    public TransactionLearningController(TransactionLearningService transactionLearningService) {
        this.transactionLearningService = transactionLearningService;
    }

    @PostMapping("/categories/{categoryId}/create-then-fail/without-service-transaction")
    public ResponseEntity<ApiResponse<String>> createThenFailWithoutServiceTransaction(
            @PathVariable Long categoryId
    ) {
        transactionLearningService.createProductThenFailWithoutServiceTransaction(categoryId);

        return ResponseEntity.ok(ApiResponse.success("This line should not be reached"));
    }

    @PostMapping("/categories/{categoryId}/create-then-fail/with-service-transaction")
    public ResponseEntity<ApiResponse<String>> createThenFailWithServiceTransaction(
            @PathVariable Long categoryId
    ) {
        transactionLearningService.createProductThenFailWithServiceTransaction(categoryId);

        return ResponseEntity.ok(ApiResponse.success("This line should not be reached"));
    }

    @GetMapping("/products/{productId}/boundary/without-service-transaction")
    public ResponseEntity<ApiResponse<TransactionBoundaryLearningResult>> inspectBoundaryWithoutServiceTransaction(
            @PathVariable Long productId
    ) {
        TransactionBoundaryLearningResult result =
                transactionLearningService.inspectBoundaryWithoutServiceTransaction(productId);

        return ResponseEntity.ok(ApiResponse.success(result));
    }

    @GetMapping("/products/{productId}/boundary/with-service-transaction")
    public ResponseEntity<ApiResponse<TransactionBoundaryLearningResult>> inspectBoundaryWithServiceTransaction(
            @PathVariable Long productId
    ) {
        TransactionBoundaryLearningResult result =
                transactionLearningService.inspectBoundaryWithServiceTransaction(productId);

        return ResponseEntity.ok(ApiResponse.success(result));
    }

    @GetMapping("/proxy/direct-call")
    public ResponseEntity<ApiResponse<TransactionProxyLearningResult>> inspectTransactionalMethodCalledThroughProxy() {
        TransactionProxyLearningResult result =
                transactionLearningService.inspectTransactionalMethodCalledThroughProxy();

        return ResponseEntity.ok(ApiResponse.success(result));
    }

    @GetMapping("/proxy/self-invocation")
    public ResponseEntity<ApiResponse<TransactionProxyLearningResult>> inspectSelfInvocationProblem() {
        TransactionProxyLearningResult result =
                transactionLearningService.inspectSelfInvocationProblem();

        return ResponseEntity.ok(ApiResponse.success(result));
    }

    @PostMapping("/categories/{categoryId}/propagation/required")
    public ResponseEntity<ApiResponse<String>> inspectRequiredPropagation(
            @PathVariable Long categoryId
    ) {
        transactionLearningService.createProductsThenFailWithRequired(categoryId);

        return ResponseEntity.ok(ApiResponse.success("This line should not be reached"));
    }

    @PostMapping("/categories/{categoryId}/propagation/requires-new")
    public ResponseEntity<ApiResponse<String>> inspectRequiresNewPropagation(
            @PathVariable Long categoryId
    ) {
        transactionLearningService.createProductsThenFailWithRequiresNew(categoryId);

        return ResponseEntity.ok(ApiResponse.success("This line should not be reached"));
    }



    @GetMapping("/isolation/default")
    public ResponseEntity<ApiResponse<IsolationLearningResult>> inspectDefaultIsolation() {
        IsolationLearningResult result = transactionLearningService.inspectDefaultIsolation();

        return ResponseEntity.ok(ApiResponse.success(result));
    }

    @GetMapping("/isolation/read-committed")
    public ResponseEntity<ApiResponse<IsolationLearningResult>> inspectReadCommittedIsolation() {
        IsolationLearningResult result = transactionLearningService.inspectReadCommittedIsolation();

        return ResponseEntity.ok(ApiResponse.success(result));
    }

    @GetMapping("/isolation/repeatable-read")
    public ResponseEntity<ApiResponse<IsolationLearningResult>> inspectRepeatableReadIsolation() {
        IsolationLearningResult result = transactionLearningService.inspectRepeatableReadIsolation();

        return ResponseEntity.ok(ApiResponse.success(result));
    }


    @PostMapping("/products/{productId}/lost-update")
    public ResponseEntity<ApiResponse<LostUpdateLearningResult>> updateProductPriceSlowly(
            @PathVariable Long productId,
            @RequestParam BigDecimal newPrice,
            @RequestParam(defaultValue = "5000") long delayMillis
    ) {
        LostUpdateLearningResult result = transactionLearningService.updateProductPriceSlowly(
                productId,
                newPrice,
                delayMillis
        );

        return ResponseEntity.ok(ApiResponse.success(result));
    }


    @PostMapping("/products/{productId}/pessimistic-lock")
    public ResponseEntity<ApiResponse<PessimisticLockLearningResult>> updateProductPriceWithPessimisticLock(
            @PathVariable Long productId,
            @RequestParam BigDecimal newPrice,
            @RequestParam(defaultValue = "5000") long delayMillis
    ) {
        PessimisticLockLearningResult result =
                transactionLearningService.updateProductPriceSlowlyWithPessimisticLock(
                        productId,
                        newPrice,
                        delayMillis
                );

        return ResponseEntity.ok(ApiResponse.success(result));
    }


    @PostMapping("/deadlock")
    public ResponseEntity<ApiResponse<DeadlockLearningResult>> inspectDeadlock(
            @RequestParam Long firstProductId,
            @RequestParam Long secondProductId,
            @RequestParam(defaultValue = "5000") long delayMillis
    ) {
        DeadlockLearningResult result = transactionLearningService.lockTwoProductsInOrder(
                firstProductId,
                secondProductId,
                delayMillis
        );

        return ResponseEntity.ok(ApiResponse.success(result));
    }
}
package com.vannguyen.java_learn_ecom.modules.order.presentation;

import com.vannguyen.java_learn_ecom.common.api.ApiResponse;
import com.vannguyen.java_learn_ecom.common.dto.PageQuery;
import com.vannguyen.java_learn_ecom.common.dto.PageResult;
import com.vannguyen.java_learn_ecom.modules.order.application.CreateOrderCommand;
import com.vannguyen.java_learn_ecom.modules.order.application.CreateOrderUseCase;
import com.vannguyen.java_learn_ecom.modules.order.application.GetOrderUseCase;
import com.vannguyen.java_learn_ecom.modules.order.application.ListOrdersUseCase;
import com.vannguyen.java_learn_ecom.modules.order.domain.Order;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.vannguyen.java_learn_ecom.modules.order.application.CancelOrderUseCase;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final CreateOrderUseCase createOrderUseCase;
    private final GetOrderUseCase getOrderUseCase;
    private final ListOrdersUseCase listOrdersUseCase;
    private final OrderMapper orderMapper;
    private final CancelOrderUseCase cancelOrderUseCase;

    public OrderController(
            CreateOrderUseCase createOrderUseCase,
            GetOrderUseCase getOrderUseCase,
            ListOrdersUseCase listOrdersUseCase,
            CancelOrderUseCase cancelOrderUseCase,
            OrderMapper orderMapper
    ) {
        this.createOrderUseCase = createOrderUseCase;
        this.getOrderUseCase = getOrderUseCase;
        this.listOrdersUseCase = listOrdersUseCase;
        this.cancelOrderUseCase = cancelOrderUseCase;
        this.orderMapper = orderMapper;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<OrderResponse>> create(
            @Valid @RequestBody CreateOrderRequest request
    ) {
        CreateOrderCommand command = orderMapper.toCommand(request);
        Order order = createOrderUseCase.create(command);
        OrderResponse response = orderMapper.toResponse(order);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.created(response));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<OrderResponse>> getById(
            @PathVariable Long id
    ) {
        Order order = getOrderUseCase.getById(id);
        OrderResponse response = orderMapper.toResponse(order);

        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PageResult<OrderResponse>>> listByCustomer(
            @RequestParam Long customerId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        PageResult<Order> orderPage = listOrdersUseCase.listByCustomer(
                customerId,
                new PageQuery(page, size)
        );

        List<OrderResponse> items = orderPage.items()
                .stream()
                .map(orderMapper::toResponse)
                .toList();

        PageResult<OrderResponse> response = new PageResult<>(
                items,
                orderPage.page(),
                orderPage.size(),
                orderPage.totalItems(),
                orderPage.totalPages(),
                orderPage.hasNext(),
                orderPage.hasPrevious()
        );

        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<ApiResponse<OrderResponse>> cancel(
            @PathVariable Long id
    ) {
        Order order = cancelOrderUseCase.cancel(id);
        OrderResponse response = orderMapper.toResponse(order);

        return ResponseEntity.ok(ApiResponse.success(response));
    }
}   
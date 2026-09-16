package com.vannguyen.java_learn_ecom.modules.order.application;

import com.vannguyen.java_learn_ecom.common.dto.PageQuery;
import com.vannguyen.java_learn_ecom.common.dto.PageResult;
import com.vannguyen.java_learn_ecom.common.exception.BusinessException;
import com.vannguyen.java_learn_ecom.modules.order.domain.Order;
import com.vannguyen.java_learn_ecom.modules.order.domain.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ListOrdersUseCase {

    private final OrderRepository orderRepository;

    public ListOrdersUseCase(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Transactional(readOnly = true)
    public PageResult<Order> listByCustomer(Long customerId, PageQuery pageQuery) {
        if (customerId == null) {
            throw new BusinessException("Customer id must not be null");
        }

        return orderRepository.findByCustomerId(customerId, pageQuery);
    }
}
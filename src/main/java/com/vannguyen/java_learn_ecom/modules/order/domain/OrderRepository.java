package com.vannguyen.java_learn_ecom.modules.order.domain;

import com.vannguyen.java_learn_ecom.common.dto.PageQuery;
import com.vannguyen.java_learn_ecom.common.dto.PageResult;

import java.util.Optional;

public interface OrderRepository {

    Order save(Order order);

    Optional<Order> findById(Long id);

    PageResult<Order> findByCustomerId(Long customerId, PageQuery pageQuery);
}
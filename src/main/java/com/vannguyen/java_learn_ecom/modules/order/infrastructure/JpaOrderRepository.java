package com.vannguyen.java_learn_ecom.modules.order.infrastructure;

import com.vannguyen.java_learn_ecom.common.dto.PageQuery;
import com.vannguyen.java_learn_ecom.common.dto.PageResult;
import com.vannguyen.java_learn_ecom.modules.order.domain.Order;
import com.vannguyen.java_learn_ecom.modules.order.domain.OrderRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class JpaOrderRepository implements OrderRepository {

    private final SpringDataOrderJpaRepository springDataOrderJpaRepository;
    private final OrderJpaMapper orderJpaMapper;

    public JpaOrderRepository(
            SpringDataOrderJpaRepository springDataOrderJpaRepository,
            OrderJpaMapper orderJpaMapper
    ) {
        this.springDataOrderJpaRepository = springDataOrderJpaRepository;
        this.orderJpaMapper = orderJpaMapper;
    }

    @Override
    public Order save(Order order) {
        OrderJpaEntity entity = orderJpaMapper.toEntity(order);
        OrderJpaEntity savedEntity = springDataOrderJpaRepository.save(entity);

        return orderJpaMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Order> findById(Long id) {
        return springDataOrderJpaRepository.findWithItemsById(id)
                .map(orderJpaMapper::toDomain);
    }

    @Override
    public PageResult<Order> findByCustomerId(Long customerId, PageQuery pageQuery) {
        Pageable pageable = PageRequest.of(
                pageQuery.page() - 1,
                pageQuery.size()
        );

        Page<OrderJpaEntity> orderPage = springDataOrderJpaRepository.findByCustomerIdOrderByIdDesc(
                customerId,
                pageable
        );

        List<Order> orders = orderPage.getContent()
                .stream()
                .map(orderJpaMapper::toDomain)
                .toList();

        return new PageResult<>(
                orders,
                pageQuery.page(),
                pageQuery.size(),
                orderPage.getTotalElements(),
                orderPage.getTotalPages(),
                orderPage.hasNext(),
                orderPage.hasPrevious()
        );
    }
}
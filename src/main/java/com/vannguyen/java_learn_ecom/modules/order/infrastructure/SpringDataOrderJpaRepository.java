package com.vannguyen.java_learn_ecom.modules.order.infrastructure;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SpringDataOrderJpaRepository extends JpaRepository<OrderJpaEntity, Long> {

    @EntityGraph(attributePaths = "items")
    Optional<OrderJpaEntity> findWithItemsById(Long id);

    @EntityGraph(attributePaths = "items")
    Page<OrderJpaEntity> findByCustomerIdOrderByIdDesc(Long customerId, Pageable pageable);
}
package com.vannguyen.java_learn_ecom.modules.product.infrastructure;

import com.vannguyen.java_learn_ecom.modules.product.domain.ProductStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
public interface SpringDataProductJpaRepository
        extends JpaRepository<ProductJpaEntity, Long>, JpaSpecificationExecutor<ProductJpaEntity> {

    List<ProductJpaEntity> findAllByStatus(ProductStatus status);

    @EntityGraph(attributePaths = "category")
    @Query("select p from ProductJpaEntity p")
    List<ProductJpaEntity> findAllWithCategory();

    @Query("select p from ProductJpaEntity p join fetch p.category")
    List<ProductJpaEntity> findAllWithCategoryUsingJoinFetch();

    @Query("""
        select distinct p
        from ProductJpaEntity p
        left join fetch p.variants
        """)
    List<ProductJpaEntity> findAllWithVariantsUsingJoinFetch();



    @Query(
            value = """
                select p.id as productId,
                       p.name as productName,
                       count(v.id) as variantCount
                from ProductJpaEntity p
                left join p.variants v
                group by p.id, p.name
                """,
            countQuery = """
                select count(p.id)
                from ProductJpaEntity p
                """
    )
    Page<ProductVariantCountProjection> findProductVariantCounts(Pageable pageable);

    interface ProductVariantCountProjection {

        Long getProductId();

        String getProductName();

        long getVariantCount();
    }
}
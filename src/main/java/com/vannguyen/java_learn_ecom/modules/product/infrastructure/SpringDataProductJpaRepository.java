package com.vannguyen.java_learn_ecom.modules.product.infrastructure;

import com.vannguyen.java_learn_ecom.modules.product.domain.ProductStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.Lock;
import java.util.Optional;
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



    @Query("""
        select p
        from ProductJpaEntity p
        where p.status = :status
        order by p.id desc
        """)
    List<ProductJpaEntity> findByStatusUsingJpql(ProductStatus status);


    //:status
    //:categoryId
    //:keyword
    @Query("""
        select p
        from ProductJpaEntity p
        where p.status = :status
        and p.categoryId = :categoryId
        and lower(p.name) like lower(concat('%', :keyword, '%'))
        order by p.id desc
        """)
    List<ProductJpaEntity> findByStatusCategoryAndKeywordUsingJpql(
            ProductStatus status,
            Long categoryId,
            String keyword);


    @Query("""
        select p
        from ProductJpaEntity p
        where (:status is null or p.status = :status)
        and (:categoryId is null or p.categoryId = :categoryId)
        and (:hasKeyword = false or lower(p.name) like lower(concat('%', :keyword, '%')))
        order by p.id desc
        """)
    List<ProductJpaEntity> searchUsingDynamicJpql(
            ProductStatus status,
            Long categoryId,
            boolean hasKeyword,
            String keyword
    );



    @Query(
            value = """
                select *
                from products
                where name ilike concat('%', :keyword, '%')
                order by id desc
                """,
            nativeQuery = true
    )
    List<ProductJpaEntity> searchByNameUsingNativeSql(String keyword);






    @Query(
            value = """
                select p.id as productId,
                       p.name as productName,
                       c.name as categoryName
                from products p
                join categories c on c.id = p.category_id
                where p.name ilike concat('%', :keyword, '%')
                order by p.id desc
                """,
            nativeQuery = true
    )
    List<ProductCategoryNativeProjection> searchProductCategoriesUsingNativeProjection(String keyword);

    interface ProductCategoryNativeProjection {

        Long getProductId();

        String getProductName();

        String getCategoryName();
    }


    @Modifying
    @Query("""
        update ProductJpaEntity p
        set p.status = :status
        where p.id = :id
        """)
    int updateStatusByIdUsingModifyingQuery(Long id, ProductStatus status);


    @Modifying(clearAutomatically = true)
    @Query("""
        update ProductJpaEntity p
        set p.status = :status
        where p.id = :id
        """)
    int updateStatusByIdAndClearAutomatically(Long id, ProductStatus status);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("""
        update ProductJpaEntity p
        set p.status = :status
        where p.id = :id
        """)
    int updateStatusByIdFlushAndClearAutomatically(Long id, ProductStatus status);


    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
    select p
    from ProductJpaEntity p
    where p.id = :id
    """)
    Optional<ProductJpaEntity> findByIdForUpdate(Long id);

}
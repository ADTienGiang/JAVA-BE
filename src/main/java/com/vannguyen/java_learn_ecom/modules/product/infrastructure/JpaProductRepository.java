package com.vannguyen.java_learn_ecom.modules.product.infrastructure;
import com.vannguyen.java_learn_ecom.common.dto.PageResult;
import com.vannguyen.java_learn_ecom.modules.product.application.ProductSearchQuery;
import com.vannguyen.java_learn_ecom.modules.product.domain.Product;
import com.vannguyen.java_learn_ecom.modules.product.domain.ProductRepository;
import com.vannguyen.java_learn_ecom.modules.product.domain.ProductStatus;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import static com.vannguyen.java_learn_ecom.modules.product.infrastructure.ProductJpaSpecifications.hasCategoryId;
import static com.vannguyen.java_learn_ecom.modules.product.infrastructure.ProductJpaSpecifications.keywordContains;
import static com.vannguyen.java_learn_ecom.modules.product.infrastructure.ProductJpaSpecifications.priceGreaterThanOrEqual;
import static com.vannguyen.java_learn_ecom.modules.product.infrastructure.ProductJpaSpecifications.priceLessThanOrEqual;
import static com.vannguyen.java_learn_ecom.modules.product.infrastructure.ProductJpaSpecifications.hasStatus;

@Repository
public class JpaProductRepository implements ProductRepository {

    private final SpringDataProductJpaRepository springDataProductJpaRepository;
    private final ProductJpaMapper productJpaMapper;

    public JpaProductRepository(
            SpringDataProductJpaRepository springDataProductJpaRepository,
            ProductJpaMapper productJpaMapper
    ) {
        this.springDataProductJpaRepository = springDataProductJpaRepository;
        this.productJpaMapper = productJpaMapper;
    }

    @Override
    public Product save(Product product) {
        ProductJpaEntity entity = productJpaMapper.toEntity(product);
        ProductJpaEntity savedEntity = springDataProductJpaRepository.save(entity);

        return productJpaMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Product> findById(Long id) {
        return springDataProductJpaRepository.findById(id)
                .map(productJpaMapper::toDomain);
    }

    @Override
    public List<Product> findAllActive() {
        return springDataProductJpaRepository.findAllByStatus(ProductStatus.ACTIVE)
                .stream()
                .map(productJpaMapper::toDomain)
                .toList();
    }

    @Override
    public PageResult<Product> search(ProductSearchQuery query) {
        Specification<ProductJpaEntity> specification = Specification
                .where(hasStatus(ProductStatus.ACTIVE))
                .and(keywordContains(query.keyword()))
                .and(hasCategoryId(query.categoryId()))
                .and(priceGreaterThanOrEqual(query.minPrice()))
                .and(priceLessThanOrEqual(query.maxPrice()));

        Pageable pageable = PageRequest.of(
                query.pageQuery().page() - 1,
                query.pageQuery().size(),
                sort(query)
        );

        Page<ProductJpaEntity> productPage = springDataProductJpaRepository.findAll(
                specification,
                pageable
        );

        List<Product> products = productPage.getContent()
                .stream()
                .map(productJpaMapper::toDomain)
                .toList();

        return new PageResult<>(
                products,
                query.pageQuery().page(),
                query.pageQuery().size(),
                productPage.getTotalElements(),
                productPage.getTotalPages(),
                productPage.hasNext(),
                productPage.hasPrevious()
        );
    }

    private Sort sort(ProductSearchQuery query) {
        Sort.Direction direction = query.sortDirection().isAscending()
                ? Sort.Direction.ASC
                : Sort.Direction.DESC;

        String property = switch (query.sortField()) {
            case NAME -> "name";
            case PRICE -> "price";
        };

        return Sort.by(direction, property);
    }

}
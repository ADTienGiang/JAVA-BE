package com.vannguyen.java_learn_ecom.modules.product.application;

import com.vannguyen.java_learn_ecom.common.exception.BusinessException;
import com.vannguyen.java_learn_ecom.common.exception.ResourceNotFoundException;
import com.vannguyen.java_learn_ecom.modules.product.domain.Category;
import com.vannguyen.java_learn_ecom.modules.product.domain.CategoryRepository;
import com.vannguyen.java_learn_ecom.modules.product.domain.Product;
import com.vannguyen.java_learn_ecom.modules.product.domain.ProductRepository;
import com.vannguyen.java_learn_ecom.modules.product.domain.ProductStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.List;
import com.vannguyen.java_learn_ecom.common.dto.PageResult;
import org.springframework.transaction.annotation.Transactional;
@Service
public class ProductService {

    private static final Logger log = LoggerFactory.getLogger(ProductService.class);

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public ProductService(
            ProductRepository productRepository,
            CategoryRepository categoryRepository
    ) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    @Transactional
    public Product create(CreateProductCommand command) {
        log.info("Create product request received: categoryId={}, name={}",
                command.categoryId(), command.name());

        Category category = categoryRepository.findById(command.categoryId())
                .orElseThrow(() -> {
                    log.warn("Create product rejected because category was not found: categoryId={}",
                            command.categoryId());
                    return new ResourceNotFoundException("Category not found");
                });

        if (!category.isActive()) {
            log.warn("Create product rejected because category is inactive: categoryId={}",
                    command.categoryId());
            throw new BusinessException("Cannot create product in inactive category");
        }

        Product product = new Product(
                null,
                command.categoryId(),
                command.name(),
                command.description(),
                command.price(),
                ProductStatus.ACTIVE
        );

        Product savedProduct = productRepository.save(product);

        log.info("Product created successfully: id={}, categoryId={}, name={}",
                savedProduct.getId(), savedProduct.getCategoryId(), savedProduct.getName());

        return savedProduct;
    }

    @Transactional(readOnly = true)
    public Product findById(Long id) {
        log.info("Find product by id: id={}", id);

        return productRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Product not found: id={}", id);
                    return new ResourceNotFoundException("Product not found");
                });
    }
    @Transactional(readOnly = true)
    public List<Product> findAllActive() {
        log.info("Find all active products");

        return productRepository.findAllActive();
    }
    @Transactional
    public Product update(UpdateProductCommand command) {
        log.info("Update product request received: id={}, categoryId={}, name={}",
                command.id(), command.categoryId(), command.name());

        Product product = findById(command.id());

        Category category = categoryRepository.findById(command.categoryId())
                .orElseThrow(() -> {
                    log.warn("Update product rejected because category was not found: productId={}, categoryId={}",
                            command.id(), command.categoryId());
                    return new ResourceNotFoundException("Category not found");
                });

        if (!category.isActive()) {
            log.warn("Update product rejected because category is inactive: productId={}, categoryId={}",
                    command.id(), command.categoryId());
            throw new BusinessException("Cannot move product to inactive category");
        }

        product.update(
                command.categoryId(),
                command.name(),
                command.description(),
                command.price()
        );

        Product savedProduct = productRepository.save(product);

        log.info("Product updated successfully: id={}, categoryId={}, name={}",
                savedProduct.getId(), savedProduct.getCategoryId(), savedProduct.getName());

        return savedProduct;
    }
    @Transactional
    public Product deactivate(Long id) {
        log.info("Deactivate product request received: id={}", id);

        Product product = findById(id);

        product.deactivate();

        Product savedProduct = productRepository.save(product);

        log.info("Product deactivated successfully: id={}", savedProduct.getId());

        return savedProduct;
    }
    @Transactional(readOnly = true)
    public PageResult<Product> search(ProductSearchQuery query) {
        log.info("Search products: keyword={}, categoryId={}, minPrice={}, maxPrice={}",
                query.keyword(), query.categoryId(), query.minPrice(), query.maxPrice());

        if (query.minPrice() != null
                && query.maxPrice() != null
                && query.minPrice().compareTo(query.maxPrice()) > 0) {
            log.warn("Search product rejected because minPrice is greater than maxPrice: minPrice={}, maxPrice={}",
                    query.minPrice(), query.maxPrice());
            throw new BusinessException("Minimum price must not be greater than maximum price");
        }

        return productRepository.search(query);
    }
}
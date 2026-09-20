package com.vannguyen.java_learn_ecom.modules.product.infrastructure.cache;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vannguyen.java_learn_ecom.modules.product.application.ProductCache;
import com.vannguyen.java_learn_ecom.modules.product.domain.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Optional;

@Component
public class RedisProductCache implements ProductCache {

    private static final Logger log = LoggerFactory.getLogger(RedisProductCache.class);

    private static final String KEY_PREFIX = "product:detail:v1:";
    private static final Duration TTL = Duration.ofMinutes(10);

    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;

    public RedisProductCache(
            StringRedisTemplate redisTemplate,
            ObjectMapper objectMapper
    ) {
        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;
    }

    @Override
    public Optional<Product> getById(Long id) {
        try {
            String json = redisTemplate.opsForValue().get(key(id));

            if (json == null) {
                log.info("Product cache miss: id={}", id);
                return Optional.empty();
            }

            ProductCacheValue cacheValue = objectMapper.readValue(json, ProductCacheValue.class);

            log.info("Product cache hit: id={}", id);

            return Optional.of(toDomain(cacheValue));
        } catch (Exception exception) {
            log.warn("Product cache read failed. Fallback to database: id={}", id, exception);
            return Optional.empty();
        }
    }

    @Override
    public void put(Product product) {
        if (product.getId() == null) {
            return;
        }

        try {
            ProductCacheValue cacheValue = toCacheValue(product);
            String json = objectMapper.writeValueAsString(cacheValue);

            redisTemplate.opsForValue().set(key(product.getId()), json, TTL);

            log.info("Product cached: id={}, ttlSeconds={}", product.getId(), TTL.toSeconds());
        } catch (Exception exception) {
            log.warn("Product cache write failed. Continue without cache: id={}",
                    product.getId(), exception);
        }
    }

    @Override
    public void evictById(Long id) {
        try {
            redisTemplate.delete(key(id));

            log.info("Product cache evicted: id={}", id);
        } catch (Exception exception) {
            log.warn("Product cache eviction failed. Continue without cache: id={}",
                    id, exception);
        }
    }

    private String key(Long id) {
        return KEY_PREFIX + id;
    }

    private ProductCacheValue toCacheValue(Product product) {
        return new ProductCacheValue(
                product.getId(),
                product.getVersion(),
                product.getCategoryId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getStatus()
        );
    }

    private Product toDomain(ProductCacheValue cacheValue) {
        return new Product(
                cacheValue.id(),
                cacheValue.version(),
                cacheValue.categoryId(),
                cacheValue.name(),
                cacheValue.description(),
                cacheValue.price(),
                cacheValue.status()
        );
    }
}
package com.vannguyen.java_learn_ecom.modules.order.infrastructure;

import com.vannguyen.java_learn_ecom.modules.order.application.StockGateway;
import com.vannguyen.java_learn_ecom.modules.product.application.DecreaseProductVariantStockCommand;
import com.vannguyen.java_learn_ecom.modules.product.application.ProductVariantStockService;
import com.vannguyen.java_learn_ecom.modules.product.application.RestoreProductVariantStockCommand;
import org.springframework.stereotype.Component;

@Component
public class StockGatewayAdapter implements StockGateway {

    private final ProductVariantStockService productVariantStockService;

    public StockGatewayAdapter(ProductVariantStockService productVariantStockService) {
        this.productVariantStockService = productVariantStockService;
    }

    @Override
    public void decrease(Long productId, Long variantId, int quantity) {
        productVariantStockService.decrease(
                new DecreaseProductVariantStockCommand(
                        productId,
                        variantId,
                        quantity
                )
        );
    }

    @Override
    public void restore(Long productId, Long variantId, int quantity) {
        productVariantStockService.restore(
                new RestoreProductVariantStockCommand(
                        productId,
                        variantId,
                        quantity
                )
        );
    }
}
package com.ntq.training.infra.validator;

import com.ntq.training.dal.entity.Product;
import com.ntq.training.infra.constants.ValidationMessages;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ProductValidator implements Validator<Product> {
    @Override
    public List<String> validate(Product product) {
        return Stream.of(
                        CommonValidator.isNonEmpty(product.getId()) ? "" : ValidationMessages.PRODUCT_ID_EMPTY,
                        CommonValidator.isNonEmpty(product.getName()) ? "" : ValidationMessages.PRODUCT_NAME_EMPTY,
                        CommonValidator.isPositive(product.getPrice()) ? "" : ValidationMessages.PRODUCT_PRICE_NEGATIVE,
                        CommonValidator.isNonNegative(product.getStockAvailable()) ? "" : ValidationMessages.PRODUCT_STOCK_NEGATIVE
                )
                .filter(error -> !error.isEmpty())
                .collect(Collectors.toList());
    }
}

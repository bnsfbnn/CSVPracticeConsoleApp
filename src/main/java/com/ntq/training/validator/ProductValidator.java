package com.ntq.training.validator;

import com.ntq.training.constants.ValidationUniqueFields;
import com.ntq.training.dal.entity.Product;
import com.ntq.training.constants.ValidationMessages;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ProductValidator implements Validator<Product> {

    @Override
    public List<String> validate(Product product, Map<String, Set<String>> existFields) {
        Set<String> existingProductIds = existFields.getOrDefault(ValidationUniqueFields.PRODUCT_IDS.getFieldName(), new HashSet<>());
        return Stream.of(
                        CommonValidator.isNonEmpty(product.getId()) ? "" : ValidationMessages.PRODUCT_ID_EMPTY,
                        !isDuplicateId(product.getId(), existingProductIds) ? "" : ValidationMessages.PRODUCT_ID_DUPLICATE,
                        CommonValidator.isNonEmpty(product.getName()) ? "" : ValidationMessages.PRODUCT_NAME_EMPTY,
                        CommonValidator.isPositive(product.getPrice()) ? "" : ValidationMessages.PRODUCT_PRICE_NEGATIVE,
                        CommonValidator.isPositive(product.getStockQuantity()) ? "" : ValidationMessages.PRODUCT_STOCK_NEGATIVE
                )
                .filter(error -> !error.isEmpty())
                .collect(Collectors.toList());
    }

    private boolean isDuplicateId(String productId, Set<String> existedIds) {
        return existedIds.contains(productId) || existedIds.add(productId);
    }
}

package com.ntq.training.infra.validator.parserimpl;

import com.ntq.training.dal.entity.Product;
import com.ntq.training.infra.constants.LogMessages;
import com.ntq.training.infra.validator.CommonParserValidator;
import com.ntq.training.infra.validator.ParserValidator;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ProductParserValidator implements ParserValidator<Product> {

    @Override
    public List<String> validate(Product product) {
        return Stream.of(
                        CommonParserValidator.isPositive(product.getPrice()) ? "" : LogMessages.PRODUCT_PRICE_NEGATIVE,
                        CommonParserValidator.isPositive(product.getStockQuantity()) ? "" : LogMessages.PRODUCT_STOCK_NEGATIVE
                )
                .filter(error -> !error.isEmpty())
                .collect(Collectors.toList());
    }
}

package com.ntq.training.validator.parserimpl;

import com.ntq.training.dal.entity.Product;
import com.ntq.training.constants.ValidationMessages;
import com.ntq.training.validator.CommonParserValidator;
import com.ntq.training.validator.ParserValidator;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ProductParserValidator implements ParserValidator<Product> {

    @Override
    public List<String> validate(Product product) {
        return Stream.of(
                        CommonParserValidator.isPositive(product.getPrice()) ? "" : ValidationMessages.PRODUCT_PRICE_NEGATIVE,
                        CommonParserValidator.isPositive(product.getStockQuantity()) ? "" : ValidationMessages.PRODUCT_STOCK_NEGATIVE
                )
                .filter(error -> !error.isEmpty())
                .collect(Collectors.toList());
    }
}

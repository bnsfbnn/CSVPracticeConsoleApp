package com.ntq.training.validator;

import com.ntq.training.dal.entity.Product;

import java.util.List;

public interface ParserValidator<T> {
    List<String> validate(T entity);
}
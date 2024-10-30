package com.ntq.training.infra.validator;

import java.util.List;

public interface ParserValidator<T> {
    List<String> validate(T entity);
}

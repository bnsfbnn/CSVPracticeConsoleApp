package com.ntq.training.infra.validator;

import java.util.List;

public interface Validator<T> {
    List<String> validate(T t);
}

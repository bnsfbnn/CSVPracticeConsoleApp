package com.ntq.training.validator;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface Validator<T> {
    List<String> validate(T t, Map<String, Set<String>> maps);
}

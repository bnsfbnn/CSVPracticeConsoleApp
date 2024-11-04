package com.ntq.training.infra.validator;

import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

@Slf4j
public class UniqueValidator<T> {

    public Map<Integer, T> validate(Map<Integer, T> entities, Function<T, String> uniquenessExtractor, Class<T> objectClass) {
        Set<String> seenValues = new HashSet<>();
        Map<Integer, T> validatedEntities = new HashMap<>();

        for (Map.Entry<Integer, T> entry : entities.entrySet()) {
            Integer lineNumber = entry.getKey();
            T entity = entry.getValue();
            String uniqueValue = uniquenessExtractor.apply(entity);

            if (seenValues.contains(uniqueValue)) {
                String objectName = objectClass.getSimpleName();
                log.error("UNIQUE VALIDATION ERROR: Row {} in the {} input file is duplicated at value {} , this line will not be added to the output file.", lineNumber, objectName.toLowerCase(), uniqueValue);
            } else {
                seenValues.add(uniqueValue);
                validatedEntities.put(lineNumber, entity);
            }
        }
        return validatedEntities;
    }
}

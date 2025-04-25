package com.scheduler.scheduler.dto;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.lang.reflect.Field;

public abstract class AuthRequestDto {

    private boolean isExcludedFields(String fieldName) {

        final Set<String> excludedFields = Set.of(
            "userId"
        );
        return excludedFields.contains(fieldName);
    }

    public Map<String, Object> toClaims() {
        Map<String, Object> claims = new HashMap<>();
        try {
            for (Field field : this.getClass().getDeclaredFields()) {
                field.setAccessible(true);
                String fieldName = field.getName();
                if (!isExcludedFields(fieldName)) {
                    Object value = field.get(this);
                    if (value != null) {
                        claims.put(fieldName, value);
                    }
                }
            }
        } catch (Exception e) {
            return null;
        }
        return claims;
    }

}
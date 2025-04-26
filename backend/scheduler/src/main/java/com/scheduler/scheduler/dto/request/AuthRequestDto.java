package com.scheduler.scheduler.dto.request;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.lang.reflect.Field;

public abstract class AuthRequestDto {

    private boolean isExcludedFields(String fieldName) {

        final Set<String> excludedFields = Set.of(
            "userId", "password"
        );
        return excludedFields.contains(fieldName);
    }

    public Map<String, Object> toClaims(Map<String, Object> extraClaims) {
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

                if (extraClaims != null) {
                    extraClaims.forEach((key, value) -> {
                        if (value != null) {
                            if (!claims.containsKey(key)) {
                                claims.put(key, value);
                            }
                        }
                    });
                }
            }
        } catch (Exception e) {
            return null;
        }


        return claims;
    }

}
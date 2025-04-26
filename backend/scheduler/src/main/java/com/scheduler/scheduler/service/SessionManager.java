package com.scheduler.scheduler.service;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class SessionManager {

    private static final String SESSION_KEY_PREFIX = "user:";
    private static final String SESSION_KEY_SUFFIX = ":sessions";

    private final RedisTemplate<String, Object> redisTemplate;

    public SessionManager(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    private String buildSessionKey(String userId) {
        return SESSION_KEY_PREFIX + userId + SESSION_KEY_SUFFIX;
    }

    private void validateParameters(String userId, String param) {
        validateUserId(userId);
        Assert.hasText(param, "Parameter cannot be null or empty");
    }

    private void validateUserId(String userId) {
        Assert.hasText(userId, "User ID cannot be null or empty");
    }

    private Map<String, String> getAllSessions(String userId) {
        validateUserId(userId);
        Map<Object, Object> entries = redisTemplate.opsForHash().entries(buildSessionKey(userId));
        
        return entries.entrySet().stream()
                .collect(Collectors.toMap(
                        e -> e.getKey().toString(),
                        e -> e.getValue().toString()
                ));
    }

    /*
     * Guarantee only one session per device for each user,
     * but each user can have multiple sessions via login with different device
     */
    public String createSession(String userId, String deviceId) {
        validateParameters(userId, deviceId);
        String key = buildSessionKey(userId);

        if (getAllSessions(userId).containsValue(deviceId)) {
            return null;
        }

        String sessionId = UUID.randomUUID().toString();
        redisTemplate.opsForHash().put(key, sessionId, deviceId);
        return sessionId;
    }


    public boolean isSessionValid(String userId, String sessionId) {
        validateParameters(userId, sessionId);
        return redisTemplate.opsForHash().hasKey(buildSessionKey(userId), sessionId);
    }

    public void removeSessionBySession(String userId, String sessionId) {
        validateParameters(userId, sessionId);
        redisTemplate.opsForHash().delete(buildSessionKey(userId), sessionId);
    }

    public void removeSessionsByDevice(String userId, String deviceId) {
        validateParameters(userId, deviceId);
        String key = buildSessionKey(userId);
        
        getAllSessions(userId).entrySet().stream()
            .filter(entry -> deviceId.equals(entry.getValue()))
            .map(Map.Entry::getKey)
            .forEach(sessionId -> redisTemplate.opsForHash().delete(key, sessionId));
    }

    public List<String> listActiveDevices(String userId) {
        validateUserId(userId);
        return getAllSessions(userId).values().stream()
                .map(Object::toString)
                .collect(Collectors.toList());
    }

    public void removeAllSessions(String userId) {
        validateUserId(userId);
        redisTemplate.delete(buildSessionKey(userId));
    }

    public boolean removeAllSessionsExceptDevice(String userId, String deviceIdToKeep) {
        validateParameters(userId, deviceIdToKeep);
        String key = buildSessionKey(userId);
        Map<String, String> sessions = getAllSessions(userId);
        
        List<String> sessionsToRemove = sessions.entrySet().stream()
                .filter(entry -> !deviceIdToKeep.equals(entry.getValue()))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
        
        if (sessionsToRemove.isEmpty()) {
            return false;
        }
        
        // Remove all except the specified device
        redisTemplate.opsForHash().delete(key, sessionsToRemove.toArray());
        return true;
    }

    
}

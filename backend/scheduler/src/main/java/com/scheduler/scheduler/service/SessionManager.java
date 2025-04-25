package com.scheduler.scheduler.service;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class SessionManager {
    private final RedisTemplate<String, Object> redisTemplate;

    public SessionManager(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }
    /*
     * Guarantee only one session per device for each user,
     * but each user can have multiple sessions via login with different device
     */
    public String createSession(String userId, String deviceId) {
        String key = "user:" + userId + ":sessions";

        Map<Object, Object> sessions = redisTemplate.opsForHash().entries(key);
        if (sessions.containsValue(deviceId)) {
            return null;
        }

        String sessionId = UUID.randomUUID().toString();
        redisTemplate.opsForHash().put(key, sessionId, deviceId);
        return sessionId;
    }

    public boolean isSessionValid(String userId, String sessionId) {
        String key = "user:" + userId + ":sessions";
        return redisTemplate.opsForHash().hasKey(key, sessionId);
    }

    public void removeSessionBySession(String userId, String sessionId) {
        String key = "user:" + userId + ":sessions";
        redisTemplate.opsForHash().delete(key, sessionId);
    }

    public void removeSessionByDevice(String userId, String deviceId) {
        String key = "user:" + userId + ":sessions";
        Map<Object, Object> sessions = redisTemplate.opsForHash().entries(key);

        for (Map.Entry<Object, Object> entry : sessions.entrySet()) {
            if (deviceId.equals(entry.getValue())) {
                redisTemplate.opsForHash().delete(key, entry.getKey());
                break;
            }
        }
    }

    public List<String> listActiveDevices(String userId) {
        String key = "user:" + userId + ":sessions";
        Map<Object, Object> sessions = redisTemplate.opsForHash().entries(key);
    
        return sessions.values().stream()
                .map(Object::toString)
                .toList();
    }
}

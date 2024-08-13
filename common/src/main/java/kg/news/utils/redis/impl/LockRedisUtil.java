package kg.news.utils.redis.impl;

import jakarta.annotation.Resource;
import kg.news.utils.redis.ILockRedisUtil;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class LockRedisUtil implements ILockRedisUtil {
    @Resource
    RedisTemplate<String, String> redisTemplate;
    /**
     * 获取分布式锁
     *
     * @param lock       锁名称
     * @param thread     执行线程
     * @param expireTime 过期时间，单位为秒
     * @return boolean 是否成功
     */
    public boolean lock(String lock, Thread thread, long expireTime) {
        return Boolean.TRUE.equals(redisTemplate.opsForValue().setIfAbsent(lock, thread.getName(), expireTime, TimeUnit.SECONDS));
    }

    /**
     * 释放分布式锁
     *
     * @param lock 锁名称
     */
    public void unlock(String lock) {
        if (exists(lock)) {
            remove(lock);
        }
    }

    private boolean exists(String key) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }

    private void remove(String key) {
        redisTemplate.delete(key);
    }
}

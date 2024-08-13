package kg.news.utils.redis.impl;

import jakarta.annotation.Resource;
import kg.news.utils.redis.IStringRedisUtil;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class StringRedisUtil implements IStringRedisUtil {
    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 写入缓存
     *
     * @param key    键
     * @param offset 位置
     * @param isShow 是否显示
     * @return boolean 是否成功
     */
    public boolean setBit(String key, long offset, boolean isShow) {
        try {
            ValueOperations<String, Object> operations = redisTemplate.opsForValue();
            operations.setBit(key, offset, isShow);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 写入缓存
     *
     * @param key   键
     * @param value 值
     * @return boolean 是否成功
     */
    public boolean set(final String key, Object value) {
        boolean result = false;
        try {
            ValueOperations<String, Object> operations = redisTemplate.opsForValue();
            operations.set(key, value);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 写入缓存设置时效时间
     *
     * @param key        键
     * @param value      值
     * @param expireTime 过期时间（秒）
     * @return boolean 是否成功
     */
    public boolean set(final String key, Object value, Long expireTime) {
        boolean result = false;
        try {
            ValueOperations<String, Object> operations = redisTemplate.opsForValue();
            operations.set(key, value);
            redisTemplate.expire(key, expireTime, TimeUnit.SECONDS);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 读取缓存
     *
     * @param key    键
     * @param offset 位置
     * @return boolean 是否成功
     */
    public boolean getBit(String key, long offset) {
        boolean result = false;
        try {
            ValueOperations<String, Object> operations = redisTemplate.opsForValue();
            result = Boolean.TRUE.equals(operations.getBit(key, offset));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 读取缓存
     *
     * @param key 键
     * @return Object 值
     */
    public Object get(final String key) {
        ValueOperations<String, Object> operations = redisTemplate.opsForValue();
        return operations.get(key);
    }
}

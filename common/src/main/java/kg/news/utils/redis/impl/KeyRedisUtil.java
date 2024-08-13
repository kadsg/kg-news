package kg.news.utils.redis.impl;

import jakarta.annotation.Resource;
import kg.news.utils.redis.IKeyRedisUtil;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class KeyRedisUtil implements IKeyRedisUtil {
    @Resource
    RedisTemplate<String, Object> redisTemplate;

    /**
     * 根据keys批量删除value
     *
     * @param keys 键
     */
    public void remove(final String... keys) {
        for (String key : keys) {
            remove(key);
        }
    }

    /**
     * 根据key删除对应的value
     *
     * @param key 键
     */
    public void remove(final String key) {
        if (exists(key)) {
            redisTemplate.delete(key);
        }
    }

    /**
     * 判断缓存中是否有对应的key
     *
     * @param key 键
     * @return boolean 是否存在
     */
    public boolean exists(final String key) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }

    /**
     * 键自增并返回值
     * 若键不存在则创建
     *
     * @param key 键
     */
    public Long increment(String key) {
        if (!exists(key)) {
            set(key, String.valueOf(0));
        }
        return redisTemplate.opsForValue().increment(key);
    }

    private void set(String key, String value) {
        redisTemplate.opsForValue().set(key, value);
    }
}

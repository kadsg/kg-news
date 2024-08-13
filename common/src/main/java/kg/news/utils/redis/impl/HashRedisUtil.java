package kg.news.utils.redis.impl;

import jakarta.annotation.Resource;
import kg.news.utils.redis.IHashRedisUtil;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class HashRedisUtil implements IHashRedisUtil {
    @Resource
    RedisTemplate<String, Object> redisTemplate;

    /**
     * 哈希添加
     *
     * @param key     键
     * @param hashKey 哈希键
     * @param value   值
     */
    public void hmSet(String key, Object hashKey, Object value) {
        HashOperations<String, Object, Object> hash = redisTemplate.opsForHash();
        hash.put(key, hashKey, value);
    }

    /**
     * 哈希获取数据
     *
     * @param key     键
     * @param hashKey 哈希键
     * @return Object 值
     */
    public Object hmGet(String key, Object hashKey) {
        HashOperations<String, Object, Object> hash = redisTemplate.opsForHash();
        return hash.get(key, hashKey);
    }

    /**
     * 获取哈希key对应的所有键值
     *
     * @param key 键
     */
    public Map<Object, Object> hmGetAll(String key) {
        HashOperations<String, Object, Object> hash = redisTemplate.opsForHash();
        return hash.entries(key);
    }

    /**
     * 删除哈希key对应的键值
     *
     * @param key    键
     * @param field 字段
     */
    public void hmDelete(String key, Object field) {
        HashOperations<String, Object, Object> hash = redisTemplate.opsForHash();
        hash.delete(key, field);
    }
}

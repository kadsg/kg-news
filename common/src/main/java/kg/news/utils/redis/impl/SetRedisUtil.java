package kg.news.utils.redis.impl;

import jakarta.annotation.Resource;
import kg.news.utils.redis.ISetRedisUtil;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class SetRedisUtil implements ISetRedisUtil {
    @Resource
    RedisTemplate<String, Object> redisTemplate;
    /**
     * 向集合添加value
     *
     * @param key   键
     * @param value 值
     */
    public void sAdd(String key, Object value) {
        SetOperations<String, Object> set = redisTemplate.opsForSet();
        set.add(key, value);
    }

    /**
     * 根据集合key获取集合中的所有元素
     *
     * @param key 键
     * @return Set<Object> 值
     */
    public Set<Object> sMembers(String key) {
        SetOperations<String, Object> set = redisTemplate.opsForSet();
        return set.members(key);
    }

    /**
     * 判断集合key中是否存在元素member
     *
     * @param key 键
     * @param member 元素
     *
     * @return boolean 是否存在
     */
    public boolean isMember(String key, Object member) {
        SetOperations<String, Object> set = redisTemplate.opsForSet();
        return Boolean.TRUE.equals(set.isMember(key, member));
    }

    /**
     * 从集合key中删除元素member
     *
     * @param key   键
     * @param member 值
     */
    public void sRemove(String key, Object member) {
        SetOperations<String, Object> set = redisTemplate.opsForSet();
        set.remove(key, member);
    }
}

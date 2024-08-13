package kg.news.utils.redis.impl;

import jakarta.annotation.Resource;
import kg.news.utils.redis.IListRedisUtil;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ListRedisUtil implements IListRedisUtil {
    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 向列表的最左端添加值
     *
     * @param k 键
     * @param v 值
     */
    public void lPush(String k, Object v) {
        ListOperations<String, Object> list = redisTemplate.opsForList();
        list.leftPush(k, v);
    }

    /**
     * 向列表的最右端获取值
     *
     * @param k 键
     * @return Object 值
     */
    public Object rPop(String k) {
        ListOperations<String, Object> list = redisTemplate.opsForList();
        return list.rightPop(k);
    }

    /**
     * 获取列表元素
     *
     * @param k  键
     * @param l  开始
     * @param l1 结束
     * @return List<Object> 值
     */
    public List<Object> lRange(String k, long l, long l1) {
        ListOperations<String, Object> list = redisTemplate.opsForList();
        return list.range(k, l, l1);
    }
}

package kg.news.utils;

import jakarta.annotation.Resource;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Redis 工具类
 */
@Component
public class RedisUtils {
    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 获取 RedisTemplate
     *
     * @return RedisTemplate<String, Object>
     */
    public RedisTemplate<String, Object> getRedisTemplate() {
        return redisTemplate;
    }

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
     * 根据key批量删除value
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
     * 读取缓存
     *
     * @param key 键
     * @return Object 值
     */
    public Object get(final String key) {
        ValueOperations<String, Object> operations = redisTemplate.opsForValue();
        return operations.get(key);
    }

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

    /**
     * 向集合添加value
     *
     * @param key   键
     * @param value 值
     */
    public void add(String key, Object value) {
        SetOperations<String, Object> set = redisTemplate.opsForSet();
        set.add(key, value);
    }

    /**
     * 根据集合key获取集合中的所有元素
     *
     * @param key 键
     * @return Set<Object> 值
     */
    public Set<Object> setMembers(String key) {
        SetOperations<String, Object> set = redisTemplate.opsForSet();
        return set.members(key);
    }

    /**
     * 向有序集合中添加value
     *
     * @param key   键
     * @param value 值
     * @param score 分数
     */
    public void zAdd(String key, Object value, double score) {
        ZSetOperations<String, Object> zset = redisTemplate.opsForZSet();
        zset.add(key, value, score);
    }

    /**
     * 根据分数范围获取有序集合中的value
     *
     * @param key    键
     * @param score  分数下界
     * @param score1 分数上界
     * @return Set<Object> 值
     */
    public Set<Object> rangeByScore(String key, double score, double score1) {
        ZSetOperations<String, Object> zset = redisTemplate.opsForZSet();
        return zset.rangeByScore(key, score, score1);
    }

    /**
     * 在有序集合key中获取排名
     *
     * @param key   集合名称
     * @param value 值
     */
    public Long zRank(String key, Object value) {
        ZSetOperations<String, Object> zset = redisTemplate.opsForZSet();
        return zset.rank(key, value);
    }

    /**
     * 根据排序获取有序集合key中的value
     *
     * @param key   集合名称
     * @param start 开始
     * @param end   结束
     * @return Set<ZSetOperations.TypedTuple < Object>> 值
     */
    public Set<ZSetOperations.TypedTuple<Object>> zRankWithScore(String key, long start, long end) {
        ZSetOperations<String, Object> zset = redisTemplate.opsForZSet();
        return zset.rangeWithScores(key, start, end);
    }

    /**
     * 获取有序集合key中value的分数
     *
     * @param key   键
     * @param value 值
     * @return Double 分数
     */
    public Double zSetScore(String key, Object value) {
        ZSetOperations<String, Object> zset = redisTemplate.opsForZSet();
        return zset.score(key, value);
    }

    /**
     * 为有序集合key中的value追加分数
     *
     * @param key   键
     * @param value 值
     * @param score 分数
     */
    public void incrementScore(String key, Object value, double score) {
        ZSetOperations<String, Object> zset = redisTemplate.opsForZSet();
        zset.incrementScore(key, value, score);
    }

    /**
     * 根据从有序集合获取排名
     *
     * @param key   集合名称
     * @param start 开始
     * @param end   结束
     * @return Set<ZSetOperations.TypedTuple < Object>> 值
     */
    public Set<ZSetOperations.TypedTuple<Object>> reverseZRankWithScore(String key, long start, long end) {
        ZSetOperations<String, Object> zset = redisTemplate.opsForZSet();
        return zset.reverseRangeByScoreWithScores(key, start, end);
    }

    /**
     * 根据排序从有序集合key中从高到低获取value
     *
     * @param key   集合名称
     * @param start 开始
     * @param end   结束
     * @return Set<ZSetOperations.TypedTuple < Object>> 值
     */
    public Set<ZSetOperations.TypedTuple<Object>> reverseZRankWithRank(String key, long start, long end) {
        ZSetOperations<String, Object> zset = redisTemplate.opsForZSet();
        return zset.reverseRangeWithScores(key, start, end);
    }

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
}
package kg.news.utils.redis.impl;

import jakarta.annotation.Resource;
import kg.news.utils.redis.IZSetRedisUtil;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class ZSetRedisUtil implements IZSetRedisUtil {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 向有序集合key中添加value
     *
     * @param key   集合
     * @param value 值
     * @param score 分数
     */
    public void zAdd(String key, Object value, double score) {
        ZSetOperations<String, Object> zset = redisTemplate.opsForZSet();
        zset.add(key, value, score);
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
     * 检查有序集合key中是否存在value
     *
     * @param key   集合
     * @param value 值
     *
     * @return boolean 是否存在
     */
    public boolean zIsMember(String key, Object value) {
        ZSetOperations<String, Object> zset = redisTemplate.opsForZSet();
        return zset.rank(key, value) != null;
    }

    /**
     * 获取有序集合key的元素个数
     *
     * @param key 键
     *
     * @return Long 元素个数
     */
    public Long zCard(String key) {
        ZSetOperations<String, Object> zset = redisTemplate.opsForZSet();
        return zset.zCard(key);
    }

    /**
     * 从有序集合key中删除value
     *
     * @param key   集合
     * @param value 值
     */
    public void zRemove(String key, Object value) {
        ZSetOperations<String, Object> zset = redisTemplate.opsForZSet();
        zset.remove(key, value);
    }

    /**
     * 获取有序集合key中value的分数
     *
     * @param key   键
     * @param value 值
     * @return Double 分数
     */
    public Double zGetScore(String key, Object value) {
        ZSetOperations<String, Object> zset = redisTemplate.opsForZSet();
        return zset.score(key, value);
    }

    /**
     * 在有序集合key中获取value的排名
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
     * @return Set<ZSetOperations.TypedTuple < Object>> 携带分数的值
     */
    public Set<ZSetOperations.TypedTuple<Object>> zRankWithScore(String key, long start, long end) {
        ZSetOperations<String, Object> zset = redisTemplate.opsForZSet();
        return zset.rangeWithScores(key, start, end);
    }

    /**
     * 根据排序从有序集合key中从高到低获取value
     *
     * @param key   集合名称
     * @param start 开始
     * @param end   结束
     * @return Set<ZSetOperations.TypedTuple <Object>> 携带分数的值
     */
    public Set<ZSetOperations.TypedTuple<Object>> reverseZRankWithRank(String key, long start, long end) {
        ZSetOperations<String, Object> zset = redisTemplate.opsForZSet();
        return zset.reverseRangeWithScores(key, start, end);
    }

    /**
     * 根据排序从有序集合key中从低到高获取value
     *
     * @param key   集合名称
     * @param start 开始排名
     * @param end   结束排名
     * @return Set<ZSetOperations.TypedTuple <Object>> 值
     */
    public Set<ZSetOperations.TypedTuple<Object>> zRankWithRank(String key, long start, long end) {
        ZSetOperations<String, Object> zset = redisTemplate.opsForZSet();
        return zset.rangeWithScores(key, start, end);
    }

    /**
     * 根据分数范围获取有序集合key中符合条件的所有value
     *
     * @param key  集合
     * @param min  分数下界
     * @param max  分数上界
     * @return Set<Object> 值
     */
    public Set<Object> rangeByScore(String key, double min, double max) {
        ZSetOperations<String, Object> zset = redisTemplate.opsForZSet();
        return zset.rangeByScore(key, min, max);
    }

    /**
     * 根据分数从有序集合key中倒序地获取元素
     *
     * @param key 集合名称
     * @param min 最低分
     * @param max 最高分
     * @return Set<ZSetOperations.TypedTuple < Object>> 携带分数的值
     */
    public Set<ZSetOperations.TypedTuple<Object>> reverseZRankWithScore(String key, long min, long max) {
        ZSetOperations<String, Object> zset = redisTemplate.opsForZSet();
        return zset.reverseRangeByScoreWithScores(key, min, max);
    }

    /**
     * 根据排序移除有序集合key中的value
     *
     * @param key   集合名称
     * @param start 开始
     * @param end   结束
     */
    public void removeRange(String key, long start, long end) {
        ZSetOperations<String, Object> zset = redisTemplate.opsForZSet();
        zset.removeRange(key, start, end);
    }
}

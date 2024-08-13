package kg.news.utils.redis;

import org.springframework.data.redis.core.ZSetOperations;

import java.util.Set;

public interface IZSetRedisUtil {

    /**
     * 向有序集合key中添加value
     *
     * @param key   集合
     * @param value 值
     * @param score 分数
     */
    void zAdd(String key, Object value, double score);

    /**
     * 为有序集合key中的value追加分数
     *
     * @param key   键
     * @param value 值
     * @param score 分数
     */
    void incrementScore(String key, Object value, double score);

    /**
     * 检查有序集合key中是否存在value
     *
     * @param key   集合
     * @param value 值
     *
     * @return boolean 是否存在
     */
    boolean zIsMember(String key, Object value);

    /**
     * 获取有序集合key的元素个数
     *
     * @param key 键
     *
     * @return Long 元素个数
     */
    Long zCard(String key);

    /**
     * 从有序集合key中删除value
     *
     * @param key   集合
     * @param value 值
     */
    void zRemove(String key, Object value);

    /**
     * 获取有序集合key中value的分数
     *
     * @param key   键
     * @param value 值
     * @return Double 分数
     */
    Double zGetScore(String key, Object value);

    /**
     * 在有序集合key中获取value的排名
     *
     * @param key   集合名称
     * @param value 值
     */
    Long zRank(String key, Object value);

    /**
     * 根据排序获取有序集合key中的value
     *
     * @param key   集合名称
     * @param start 开始
     * @param end   结束
     * @return Set<ZSetOperations.TypedTuple < Object>> 携带分数的值
     */
    Set<ZSetOperations.TypedTuple<Object>> zRankWithScore(String key, long start, long end);

    /**
     * 根据排序从有序集合key中从高到低获取value
     *
     * @param key   集合名称
     * @param start 开始
     * @param end   结束
     * @return Set<ZSetOperations.TypedTuple <Object>> 携带分数的值
     */
    Set<ZSetOperations.TypedTuple<Object>> reverseZRankWithRank(String key, long start, long end);

    /**
     * 根据排序从有序集合key中从低到高获取value
     *
     * @param key   集合名称
     * @param start 开始排名
     * @param end   结束排名
     * @return Set<ZSetOperations.TypedTuple <Object>> 值
     */
    Set<ZSetOperations.TypedTuple<Object>> zRankWithRank(String key, long start, long end);

    /**
     * 根据分数范围获取有序集合key中符合条件的所有value
     *
     * @param key  集合
     * @param min  分数下界
     * @param max  分数上界
     * @return Set<Object> 值
     */
    Set<Object> rangeByScore(String key, double min, double max);

    /**
     * 根据分数从有序集合key中倒序地获取元素
     *
     * @param key 集合名称
     * @param min 最低分
     * @param max 最高分
     * @return Set<ZSetOperations.TypedTuple < Object>> 携带分数的值
     */
    Set<ZSetOperations.TypedTuple<Object>> reverseZRankWithScore(String key, long min, long max);

    /**
     * 根据排序移除有序集合key中的value
     *
     * @param key   集合名称
     * @param start 开始
     * @param end   结束
     */
    void removeRange(String key, long start, long end);
}

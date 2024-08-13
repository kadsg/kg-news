package kg.news.utils.redis;

public interface IKeyRedisUtil {

    /**
     * 根据keys批量删除value
     *
     * @param keys 键
     */
    void remove(final String... keys);

    /**
     * 根据key删除对应的value
     *
     * @param key 键
     */
    void remove(final String key);

    /**
     * 判断缓存中是否有对应的key
     *
     * @param key 键
     * @return boolean 是否存在
     */
    boolean exists(final String key);

    /**
     * 键自增并返回值
     * 若键不存在则创建
     *
     * @param key 键
     */
    Long increment(String key);
}

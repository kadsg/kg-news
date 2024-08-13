package kg.news.utils.redis;

public interface IStringRedisUtil {
    /**
     * 写入缓存
     *
     * @param key    键
     * @param offset 位置
     * @param isShow 是否显示
     * @return boolean 是否成功
     */
    boolean setBit(String key, long offset, boolean isShow);

    /**
     * 写入缓存
     *
     * @param key   键
     * @param value 值
     * @return boolean 是否成功
     */
    boolean set(final String key, Object value);

    /**
     * 写入缓存设置时效时间
     *
     * @param key        键
     * @param value      值
     * @param expireTime 过期时间（秒）
     * @return boolean 是否成功
     */
    boolean set(final String key, Object value, Long expireTime);

    /**
     * 读取缓存
     *
     * @param key    键
     * @param offset 位置
     * @return boolean 是否成功
     */
    boolean getBit(String key, long offset);

    /**
     * 读取缓存
     *
     * @param key 键
     * @return Object 值
     */
    Object get(final String key);
}

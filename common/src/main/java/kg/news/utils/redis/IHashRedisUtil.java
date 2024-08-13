package kg.news.utils.redis;

import java.util.Map;

public interface IHashRedisUtil {
    /**
     * 哈希添加
     *
     * @param key     键
     * @param hashKey 哈希键
     * @param value   值
     */
    void hmSet(String key, Object hashKey, Object value);

    /**
     * 哈希获取数据
     *
     * @param key     键
     * @param hashKey 哈希键
     * @return Object 值
     */
    Object hmGet(String key, Object hashKey);

    /**
     * 获取哈希key对应的所有键值
     *
     * @param key 键
     */
    Map<Object, Object> hmGetAll(String key);

    /**
     * 删除哈希key对应的键值
     *
     * @param key    键
     * @param field 字段
     */
    void hmDelete(String key, Object field);
}

package kg.news.utils.redis;

import java.util.Set;

public interface ISetRedisUtil {
    /**
     * 向集合添加value
     *
     * @param key   键
     * @param value 值
     */
    void sAdd(String key, Object value);

    /**
     * 根据集合key获取集合中的所有元素
     *
     * @param key 键
     * @return Set<Object> 值
     */
    Set<Object> sMembers(String key);

    /**
     * 判断集合key中是否存在元素member
     *
     * @param key 键
     * @param member 元素
     *
     * @return boolean 是否存在
     */
    boolean isMember(String key, Object member);

    /**
     * 从集合key中删除元素member
     *
     * @param key   键
     * @param member 值
     */
    void sRemove(String key, Object member);
}

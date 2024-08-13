package kg.news.utils.redis;

import java.util.List;

public interface IListRedisUtil {

    /**
     * 向列表的最左端添加值
     *
     * @param k 键
     * @param v 值
     */
    void lPush(String k, Object v);

    /**
     * 向列表的最右端获取值
     *
     * @param k 键
     * @return Object 值
     */
    Object rPop(String k);

    /**
     * 获取列表元素
     *
     * @param k  键
     * @param l  开始
     * @param l1 结束
     * @return List<Object> 值
     */
    List<Object> lRange(String k, long l, long l1);
}

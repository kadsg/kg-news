package kg.news.utils.redis;

public interface ILockRedisUtil {
    /**
     * 获取分布式锁
     *
     * @param lock       锁名称
     * @param thread     执行线程
     * @param expireTime 过期时间，单位为秒
     * @return boolean 是否成功
     */
    boolean lock(String lock, Thread thread, long expireTime);

    /**
     * 释放分布式锁
     *
     * @param lock 锁名称
     */
    void unlock(String lock);
}

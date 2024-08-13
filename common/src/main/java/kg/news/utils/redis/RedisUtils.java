package kg.news.utils.redis;

import jakarta.annotation.Resource;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
public class RedisUtils implements IKeyRedisUtil, ILockRedisUtil,
        IHashRedisUtil, IStringRedisUtil, ISetRedisUtil, IListRedisUtil, IZSetRedisUtil {

    @Resource(name = "keyRedisUtil")
    private IKeyRedisUtil keyRedisUtil;
    @Resource(name = "lockRedisUtil")
    private ILockRedisUtil lockRedisUtil;
    @Resource(name = "hashRedisUtil")
    private IHashRedisUtil hashRedisUtil;
    @Resource(name = "stringRedisUtil")
    private IStringRedisUtil stringRedisUtil;
    @Resource(name = "setRedisUtil")
    private ISetRedisUtil setRedisUtil;
    @Resource(name = "listRedisUtil")
    private IListRedisUtil listRedisUtil;
    @Resource(name = "ZSetRedisUtil")
    private IZSetRedisUtil zSetRedisUtil;

    public void hmSet(String key, Object hashKey, Object value) {
        hashRedisUtil.hmSet(key, hashKey, value);
    }

    public Object hmGet(String key, Object hashKey) {
        return hashRedisUtil.hmGet(key, hashKey);
    }

    public Map<Object, Object> hmGetAll(String key) {
        return hashRedisUtil.hmGetAll(key);
    }

    public void hmDelete(String key, Object field) {
        hashRedisUtil.hmDelete(key, field);
    }

    public void remove(String... keys) {
        keyRedisUtil.remove(keys);
    }

    public void remove(String key) {
        keyRedisUtil.remove(key);
    }

    public boolean exists(String key) {
        return keyRedisUtil.exists(key);
    }

    public Long increment(String key) {
        return keyRedisUtil.increment(key);
    }

    public void lPush(String k, Object v) {
        listRedisUtil.lPush(k, v);
    }

    public Object rPop(String k) {
        return listRedisUtil.rPop(k);
    }

    public List<Object> lRange(String k, long l, long l1) {
        return listRedisUtil.lRange(k, l, l1);
    }

    public boolean lock(String lock, Thread thread, long expireTime) {
        return lockRedisUtil.lock(lock, thread, expireTime);
    }

    public void unlock(String lock) {
        lockRedisUtil.unlock(lock);
    }

    public void sAdd(String key, Object value) {
        setRedisUtil.sAdd(key, value);
    }

    public Set<Object> sMembers(String key) {
        return setRedisUtil.sMembers(key);
    }

    public boolean isMember(String key, Object member) {
        return setRedisUtil.isMember(key, member);
    }

    public void sRemove(String key, Object member) {
        setRedisUtil.sRemove(key, member);
    }

    public boolean setBit(String key, long offset, boolean isShow) {
        return stringRedisUtil.setBit(key, offset, isShow);
    }

    public boolean set(String key, Object value) {
        return stringRedisUtil.set(key, value);
    }

    public boolean set(String key, Object value, Long expireTime) {
        return stringRedisUtil.set(key, value, expireTime);
    }

    public boolean getBit(String key, long offset) {
        return stringRedisUtil.getBit(key, offset);
    }

    public Object get(String key) {
        return stringRedisUtil.get(key);
    }

    public void zAdd(String key, Object value, double score) {
        zSetRedisUtil.zAdd(key, value, score);
    }

    public void incrementScore(String key, Object value, double score) {
        zSetRedisUtil.incrementScore(key, value, score);
    }

    public boolean zIsMember(String key, Object value) {
        return zSetRedisUtil.zIsMember(key, value);
    }

    public Long zCard(String key) {
        return zSetRedisUtil.zCard(key);
    }

    public void zRemove(String key, Object value) {
        zSetRedisUtil.zRemove(key, value);
    }

    public Double zGetScore(String key, Object value) {
        return zSetRedisUtil.zGetScore(key, value);
    }

    public Long zRank(String key, Object value) {
        return zSetRedisUtil.zRank(key, value);
    }

    public Set<ZSetOperations.TypedTuple<Object>> zRankWithScore(String key, long start, long end) {
        return zSetRedisUtil.zRankWithScore(key, start, end);
    }

    public Set<ZSetOperations.TypedTuple<Object>> reverseZRankWithRank(String key, long start, long end) {
        return zSetRedisUtil.reverseZRankWithRank(key, start, end);
    }

    public Set<ZSetOperations.TypedTuple<Object>> zRankWithRank(String key, long start, long end) {
        return zSetRedisUtil.zRankWithRank(key, start, end);
    }

    public Set<Object> rangeByScore(String key, double min, double max) {
        return zSetRedisUtil.rangeByScore(key, min, max);
    }

    public Set<ZSetOperations.TypedTuple<Object>> reverseZRankWithScore(String key, long min, long max) {
        return zSetRedisUtil.reverseZRankWithScore(key, min, max);
    }

    public void removeRange(String key, long start, long end) {
        zSetRedisUtil.removeRange(key, start, end);
    }
}

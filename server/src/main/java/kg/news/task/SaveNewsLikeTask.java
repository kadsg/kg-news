package kg.news.task;

import jakarta.annotation.Resource;
import kg.news.constant.NewsConstant;
import kg.news.entity.Favorite;
import kg.news.entity.News;
import kg.news.repository.FavoriteRepository;
import kg.news.repository.NewsRepository;
import kg.news.utils.redis.RedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 更新用户新闻点赞数据
 */
@Component
@Slf4j
public class SaveNewsLikeTask {
    @Resource
    private RedisUtils redisUtils;
    @Resource
    private NewsRepository newsRepository;
    @Resource
    private FavoriteRepository favoriteRepository;
    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Transactional(rollbackFor = Exception.class)
    @Scheduled(fixedRate = 20000)
    public void saveNewsLike() {
        log.info("正在更新用户新闻点赞数据，当前时间：{}", LocalDateTime.now());
        // 1. 从 Redis 中获取用户新闻点赞数据
        Set<String> keys = redisTemplate.keys(NewsConstant.NEWS_LIKE + ':' + "*");
        if (keys == null) {
            return;
        }
        keys.forEach(key -> {
            // 获取新闻
            Long newsId = Long.parseLong(key.split(":")[1]);
            News news = newsRepository.findById(newsId).orElse(null);
            if (news == null) {
                return;
            }
            // 获取新闻的点赞数据
            log.info("正在更新新闻 {} 的点赞数据", news.getTitle());
            Map<Object, Object> newsLikeMap = redisUtils.hmGetAll(key);
            if (newsLikeMap == null) {
                return;
            }
            AtomicInteger cnt = new AtomicInteger();
            // 2. 保存用户点赞数据到数据库
            newsLikeMap.forEach((userId, info) -> {
                String lockKey = NewsConstant.NEWS_LIKE_USER_LOCK + ':' + newsId + ':' + userId;
                short count = Short.parseShort(info.toString());
                // 第一道防护措施：使用 Redis 分布式锁，防止多个线程同时对用户点赞数据进行写操作
                // 如果不存在锁，说明用户点赞数据已经更新完毕，此时可以对数据进行处理
                if (!redisUtils.exists(lockKey)) {
                    // 获取用户的点赞数据
                    Favorite favorite = favoriteRepository.findByNewsIdAndUserId(newsId, Long.parseLong(userId.toString()));
                    // 保存用户新闻点赞数据到数据库
                    if (favorite == null) {
                        favorite = new Favorite();
                        favorite.setNewsId(newsId);
                        favorite.setUserId(Long.parseLong(userId.toString()));
                        favorite.setFavorFlag(true);
                        cnt.addAndGet(1);
                    } else {
                        if (!(favorite.isFavorFlag() && count == 1 || !favorite.isFavorFlag() && count == -1)) {
                            // 如果用户已经点赞，且 count 为 1，或者用户已经取消点赞，且 count 为 -1，则不进行处理（数据一致）
                            // 否则，更新用户新闻点赞数据
                            cnt.addAndGet(count);
                            favorite.setFavorFlag(!favorite.isFavorFlag());
                        }
                    }
                    // 第二道防护措施：使用 Redis 分布式锁，防止多个线程同时对用户点赞数据进行写操作
                    // 如果不存在锁，说明用户点赞数据更新完毕，将数据写入数据库并清空用户新闻点赞数据缓存
                    if (!redisUtils.exists(lockKey)) {
                        // 写入锁
                        boolean hasLock = redisUtils.lock(lockKey, Thread.currentThread(), 5L);
                        if (!hasLock) {
                            // 如果获取锁失败，说明用户点赞数据在处理过程中被其他线程修改，此时需要重新处理
                            // 跳出，处理下一条用户点赞数据
                            cnt.set(0);
                            return;
                        }
                        // 3. 清空用户新闻点赞数据
                        redisUtils.hmDelete(key, userId);
                        log.info("用户 {} 对新闻 {} 的数据缓存已清空", userId, news.getTitle());
                        favoriteRepository.save(favorite);
                        log.info("用户 {} 对新闻 {} 的点赞数据已更新完毕", userId, news.getTitle());
                    } else {
                        // 如果存在锁，说明用户点赞数据在处理过程中被其他线程修改，此时需要重新处理
                        log.info("第二次获取锁失败，用户 {} 对新闻 {} 的数据正在处理中，数据处理未保存，丢弃", userId, news.getTitle());
                        cnt.set(0);
                    }
                } else {
                    log.info("第一次获取锁失败，用户 {} 对新闻 {} 的数据正在更新，数据不作处理", userId, news.getTitle());
                }
            });
            // 4. 更新新闻点赞数
            news.setLikeCount(news.getLikeCount() + cnt.get());
            news.setUpdateTime(LocalDateTime.now());
            newsRepository.save(news);
        });
        log.info("更新用户新闻点赞数据结束，当前时间：{}", LocalDateTime.now());
    }
}

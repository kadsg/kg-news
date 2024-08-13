package kg.news.task;

import jakarta.annotation.Resource;
import kg.news.constant.NewsConstant;
import kg.news.repository.NewsRepository;
import kg.news.service.HistoryService;
import kg.news.utils.RedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Component
@Slf4j
public class SaveNewsReadTask {
    @Resource
    private RedisUtils redisUtils;
    @Resource
    private NewsRepository newsRepository;
    @Resource
    private HistoryService historyService;

    @Transactional(rollbackFor = Exception.class)
    @Scheduled(fixedRate = 20000)
    public void saveNewsRead() {
        Set<String> keys = redisUtils.getRedisTemplate().keys(NewsConstant.NEWS_READ + ':' + "*");
        if (keys == null) {
            return;
        }
        // newsId:countAmount
        Map<Long, Integer> newsReadCntMap = new HashMap<>();
        keys.forEach(key -> {
            Long currentId = Long.parseLong(key.split(":")[1]);
            int size = keys.size();
            // 指定范围可以避免错删新数据
            redisUtils.zRankWithRank(key, 0, size - 1).forEach(
                tuple -> {
                    assert tuple != null;
                    Long newsId = Long.parseLong(tuple.getValue().toString());
                    log.info("正在更新用户新闻阅读数据，用户: {}， 新闻： {}", currentId, newsId);
                    long second = tuple.getScore().longValue();
                    LocalDateTime readTime = LocalDateTime.ofEpochSecond(second, 0, ZoneOffset.of("+8"));
                    // 保存历史记录
                    historyService.save(newsId, currentId, readTime);
                    newsReadCntMap.put(newsId, newsReadCntMap.getOrDefault(newsId, 0) + 1);
                    redisUtils.zRemove(key, newsId.toString());
                }
            );
        });
        // 更新新闻阅读量
        newsReadCntMap.forEach((newsId, cnt) -> newsRepository.findById(newsId).ifPresent(news -> {
            log.info("新闻 {} 的阅读量增加了{}", news.getId(), cnt);
            news.setViewCount(news.getViewCount() + cnt);
            newsRepository.save(news);
        }));
    }
}

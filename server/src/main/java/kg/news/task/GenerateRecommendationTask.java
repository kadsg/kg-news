package kg.news.task;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import kg.news.entity.News;
import kg.news.entity.User;
import kg.news.repository.NewsKeyWordRepository;
import kg.news.repository.UserInterestRepository;
import kg.news.service.NewsService;
import kg.news.service.RecommendService;
import kg.news.service.UserService;
import kg.news.utils.KeyWordUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.xm.Similarity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Slf4j
public class GenerateRecommendationTask {
    private static final int RECOMMENDATION_COUNT = 20; // 推荐文章数量
    private final UserService userService;
    private final NewsService newsService;
    private final RecommendService recommendService;
    private final UserInterestRepository userInterestRepository;
    private final NewsKeyWordRepository newsKeyWordRepository;
    private final KeyWordUtil keyWordUtil;



    public GenerateRecommendationTask(UserService userService, NewsService newsService,
                                      RecommendService recommendService, UserInterestRepository userInterestRepository,
                                      NewsKeyWordRepository newsKeyWordRepository,
                                      KeyWordUtil keyWordUtil) {
        this.userService = userService;
        this.newsService = newsService;
        this.recommendService = recommendService;
        this.userInterestRepository = userInterestRepository;
        this.newsKeyWordRepository = newsKeyWordRepository;
        this.keyWordUtil = keyWordUtil;
    }

    /**
     * 为所有用户生成推荐
     * 适用于小数据集，即用户量和文章量都不多的情况
     * 评判数据量多少的标准：用户量和文章量都超过1000
     * 否则该推荐系统需要分布式计算
     * 每十分钟执行一次
     */
    @Scheduled(fixedRate = 1000 * 60 * 10)
    public void generateRecommendation() {
        log.info("开始为所有用户生成推荐，开始时间{}", LocalDateTime.now());
        // 1. 获取所有用户（不包含媒体、管理员）
        List<User> userList = userService.queryAllUser();
        // 2. 遍历所有用户，获取它们的兴趣词
        List<News> newsList = newsService.getAllNews();
        // 3. 为用户生成推荐
        generateUserRecommendations(userList, newsList);
        log.info("生成推荐结束，结束时间{}", LocalDateTime.now());
    }

    /**
     * 为用户生成推荐
     * @param userList 用户列表
     * @param newsList 文章列表
     */
    private void generateUserRecommendations(List<User> userList, List<News> newsList) {
        // 转换为：用户ID:兴趣词
        Map<Long, String> userInterestMap = getUserInterests(userList);
        // 转换为：文章ID:关键词
        Map<Long, String> newsKeyWordMap = getNewsKeywords(newsList);
        // 为每个用户生成推荐
        userInterestMap.forEach((userId, userInterest) -> {
            // 计算每条新闻与该用户的匹配值（格式为新闻Id:匹配值）
            Map<Long, Double> matchValueMap = calculateMatchValue(userInterest, newsKeyWordMap);
            if (matchValueMap.isEmpty()) {
                return;
            }
            // 获取匹配值最高的前N条新闻
            Map<Long, Double> topNMatchValueMap = getTopNRecommendations(matchValueMap);
            // 保存推荐结果
            saveRecommendations(userId, topNMatchValueMap);
        });
    }

    /**
     * 获取文章关键词
     * @param newsList 文章列表
     * @return 文章ID:关键词(Json)
     */
    private Map<Long, String> getNewsKeywords(List<News> newsList) {
        Map<Long, String> newsKeyWordMap = new HashMap<>();
        newsList.forEach(news -> {
            String newsKeyWord = newsKeyWordRepository.findByNewsId(news.getId()).getKeyWord();
            newsKeyWordMap.put(news.getId(), newsKeyWord);
        });
        return newsKeyWordMap;
    }

    /**
     * 获取用户兴趣词
     * @param userList 用户列表
     * @return 用户ID:兴趣词(Json)
     */
    private Map<Long, String> getUserInterests(List<User> userList) {
        Map<Long, String> userInterestMap = new HashMap<>();
        userList.forEach(user -> {
            String userInterest = userInterestRepository.findByUserId(user.getId()).getInterest();
            userInterestMap.put(user.getId(), userInterest);
        });
        return userInterestMap;
    }

    /**
     * 计算匹配值
     * @param userInterest 用户兴趣词
     * @param newsKeyWordMap 文章关键词
     * @return 文章ID:匹配值
     */
    private Map<Long, Double> calculateMatchValue(String userInterest, Map<Long, String> newsKeyWordMap) {
        if (userInterest == null || "".equals(userInterest)) {
            return new HashMap<>();
        }
        // 新闻Id:匹配值
        Map<Long, Double> matchValueMap = new HashMap<>();
        // 将当前用户兴趣词转换为Map（兴趣词：权重）
//        Map<String, Double> currentUserInterestMap = JSONObject.parseObject(userInterest, Map.class);
        Map<String, Double> currentUserInterestMap = JSON.parseObject(userInterest, new TypeReference<Map<String, BigDecimal>>() {})
                .entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().doubleValue()));
        // 将用户兴趣词与文章所有关键词进行逐个比较
        for (Map.Entry<Long, String> entry : newsKeyWordMap.entrySet()) {
            Long newsId = entry.getKey();
            // 将当前文章关键词转换为Map（关键词：权重）
            String newsKeyWord = entry.getValue();
//            Map<String, Double> currentNewsKeyWordMap = JSONObject.parseObject(newsKeyWord, Map.class);
            Map<String, Double> currentNewsKeyWordMap = JSON.parseObject(newsKeyWord, new TypeReference<Map<String, BigDecimal>>() {})
                    .entrySet()
                    .stream()
                    .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().doubleValue()));
            double matchValue = 0;
            // 计算用户兴趣词与文章关键词的匹配度
            for (Map.Entry<String, Double> userInterestEntry : currentUserInterestMap.entrySet()) {
                for (Map.Entry<String, Double> newsKeyWordEntry : currentNewsKeyWordMap.entrySet()) {
                    // 计算相似度
                    double tempSimilarity = Similarity.cilinSimilarity(userInterestEntry.getKey(), newsKeyWordEntry.getKey());
                    // 计算匹配值
                    double tempMatchValue = userInterestEntry.getValue() * newsKeyWordEntry.getValue() * tempSimilarity;
                    matchValue += tempMatchValue;
                }
            }
            // 将匹配结果存入Map
            matchValueMap.put(newsId, matchValue);
        }
        return matchValueMap;
    }

    /**
     * 获取前N个推荐结果
     * @param matchValueMap 匹配值
     * @return 前N个推荐结果
     */
    private Map<Long, Double> getTopNRecommendations(Map<Long, Double> matchValueMap) {
        Map<Long, Double> sortedMatchValueMap = keyWordUtil.sortMapByValue(matchValueMap);
        Map<Long, Double> topNMatchValueMap = new HashMap<>();
        int count = 0;
        int num = Math.min(sortedMatchValueMap.size(), RECOMMENDATION_COUNT);
        for (Map.Entry<Long, Double> entry : sortedMatchValueMap.entrySet()) {
            if (count < num) {
                topNMatchValueMap.put(entry.getKey(), entry.getValue());
                ++count;
            } else {
                break;
            }
        }
        return topNMatchValueMap;
    }

    /**
     * 保存推荐结果
     * @param userId 用户ID
     * @param topNMatchValueMap 前N个推荐结果
     */
    private void saveRecommendations(Long userId, Map<Long, Double> topNMatchValueMap) {
        for (Map.Entry<Long, Double> entry : topNMatchValueMap.entrySet()) {
            recommendService.addRecommend(userId, entry.getKey());
        }
    }
}

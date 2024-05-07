package kg.news.service.impl;

import kg.news.service.NewsRecommendationService;
import kg.news.service.NewsService;
import kg.news.service.RecommendService;
import kg.news.vo.NewsSummaryVO;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class NewsRecommendationServiceImpl implements NewsRecommendationService {
    private static final int PAGE_NUM = 0; // 页码
    private static final int PAGE_SIZE = 20; // 每页大小
    private final NewsService newsService;
    private final RecommendService recommendService;

    public NewsRecommendationServiceImpl(NewsService newsService, RecommendService recommendService) {
        this.newsService = newsService;
        this.recommendService = recommendService;
    }

//    public List<NewsSummaryVO> recommendByCollaborativeFiltering(Long userId) {
//        List<NewsSummaryVO> recommendedNews = new ArrayList<>();
//        FastByIDMap<PreferenceArray> userPreferences = new FastByIDMap<>();
//        List<History> allHistory = historyService.getAllHistory();
//
//        for (History history : allHistory) {
//            long currentUserId = history.getUserId();
//            long newsId = history.getNewsId();
//
//            // If this user's preferences are not in the map yet, create a new PreferenceArray for them
//            if (!userPreferences.containsKey(currentUserId)) {
//                userPreferences.put(currentUserId, new GenericUserPreferenceArray(new ArrayList<>()));
//            }
//
//            // Add this history item as a preference
//            PreferenceArray preferences = userPreferences.get(currentUserId);
//            preferences.set(preferences.length(), new GenericPreference(userId, newsId, PREFERENCE_VALUE));
//        }
//
//        try {
//            // Step 1: Create a data model
//            DataModel model = new GenericDataModel(userPreferences);
//
//            // Step 2: Create a user similarity object
//            UserSimilarity similarity = new PearsonCorrelationSimilarity(model);
//
//            // Step 3: Create a user neighborhood object
//            UserNeighborhood neighborhood = new NearestNUserNeighborhood(NEIGHBORHOOD_SIZE, similarity, model);
//
//            // Step 4: Create a recommender
//            UserBasedRecommender recommender = new GenericUserBasedRecommender(model, neighborhood, similarity);
//
//            // Generate recommendations
//            List<RecommendedItem> recommendations = recommender.recommend(userId, RECOMMEND_SIZE);
//
//            for (RecommendedItem recommendation : recommendations) {
//                News news = newsService.queryNews(recommendation.getItemID());
//                NewsSummaryVO newsSummaryVO = new NewsSummaryVO();
//                BeanUtils.copyProperties(news, newsSummaryVO);
//                recommendedNews.add(newsSummaryVO);
//            }
//        } catch (TasteException e) {
//            e.printStackTrace();
//        }
//        return recommendedNews;
//    }

    public List<NewsSummaryVO> getRecommendationByUserId(Long userId) {
        return recommendService.getRecommendationByUserId(userId);
    }

    public List<NewsSummaryVO> recommendByHotNews() {
        Pageable pageable = PageRequest.of(PAGE_NUM, PAGE_SIZE);
        // 1. 获取浏览量最高的N个新闻
        List<NewsSummaryVO> viewHot = newsService.queryViewHotNews(pageable);
        // 2. 获取点赞数最高的N个新闻
        List<NewsSummaryVO> likedHot = newsService.queryLikedNews(pageable);
        // 3. 获取评论数最高的N个新闻
        List<NewsSummaryVO> commentedHot = newsService.queryCommentedNews(pageable);
        // 4. 合并这些新闻，去除重复的新闻，News类需要重写equals方法
        Set<NewsSummaryVO> hotNews = new HashSet<>();
        hotNews.addAll(viewHot);
        hotNews.addAll(likedHot);
        hotNews.addAll(commentedHot);
        // 5. 返回推荐列表
        return new ArrayList<>(hotNews);
    }
}

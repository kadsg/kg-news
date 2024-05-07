package kg.news.service;

import kg.news.vo.NewsSummaryVO;

import java.util.List;

public interface NewsRecommendationService {
    /**
     * 获取用户的新闻推荐
     * @param userId 用户ID
     * @return 推荐的新闻列表
     */
    List<NewsSummaryVO> getRecommendationByUserId(Long userId);

    /**
     * 基于热点新闻的推荐
     *
     * @return 推荐的新闻列表
     */
    List<NewsSummaryVO> recommendByHotNews();
}

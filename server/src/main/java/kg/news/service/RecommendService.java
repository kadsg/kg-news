package kg.news.service;

import kg.news.vo.NewsSummaryVO;

import java.util.List;

public interface RecommendService {
    /**
     * 将推荐的新闻存入数据库
     * 如果已经存在，则忽略
     *
     * @param userId 用户ID
     * @param newsId 新闻ID
     */
    void addRecommend(Long userId, Long newsId);

    /**
     * 获取用户的新闻推荐结果
     * 仅获取未读的新闻
     *
     * @param userId 用户ID
     * @return 推荐的新闻列表
     */
    List<NewsSummaryVO> getRecommendationByUserId(Long userId);

    /**
     * 删除用户的新闻推荐结果
     *
     * @param userId 用户ID
     * @param newsId 新闻ID
     */
    void removeRecommend(Long userId, Long newsId);
}

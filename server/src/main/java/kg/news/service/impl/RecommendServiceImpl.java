package kg.news.service.impl;


import kg.news.entity.News;
import kg.news.entity.Recommend;
import kg.news.repository.NewsRepository;
import kg.news.repository.RecommendRepository;
import kg.news.service.RecommendService;
import kg.news.vo.NewsSummaryVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class RecommendServiceImpl implements RecommendService {
    private final RecommendRepository recommendRepository;
    private final NewsRepository newsRepository;

    public RecommendServiceImpl(RecommendRepository recommendRepository,
                                NewsRepository newsRepository) {
        this.recommendRepository = recommendRepository;
        this.newsRepository = newsRepository;
    }

    public void addRecommend(Long userId, Long newsId) {
        Recommend recommend = recommendRepository.findByUserIdAndNewsId(userId, newsId);
        if (recommend == null) {
            LocalDateTime now = LocalDateTime.now();
            recommend = Recommend.builder()
                    .userId(userId)
                    .newsId(newsId)
                    .createTime(now)
                    .build();
            recommendRepository.save(recommend);
        }
    }

    public List<NewsSummaryVO> getRecommendationByUserId(Long userId) {
        List<Recommend> recommends = recommendRepository.findRecommendsByReadFlagIsFalseAndUserId(userId);
        return recommends.stream()
                .map(recommend -> {
                    News news = newsRepository.findById(recommend.getNewsId()).orElse(null);
                    if (news == null) {
                        return null;
                    }
                    NewsSummaryVO newsSummaryVO = new NewsSummaryVO();
                    BeanUtils.copyProperties(news, newsSummaryVO);
                    return newsSummaryVO;
                }).toList();
    }
}

package kg.news.service.impl;


import kg.news.entity.News;
import kg.news.entity.Recommend;
import kg.news.repository.NewsRepository;
import kg.news.repository.RecommendRepository;
import kg.news.repository.UserRepository;
import kg.news.service.RecommendService;
import kg.news.vo.NewsSummaryVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
public class RecommendServiceImpl implements RecommendService {
    private final RecommendRepository recommendRepository;
    private final NewsRepository newsRepository;
    private final UserRepository userRepository;

    public RecommendServiceImpl(RecommendRepository recommendRepository,
                                NewsRepository newsRepository,
                                UserRepository userRepository) {
        this.recommendRepository = recommendRepository;
        this.newsRepository = newsRepository;
        this.userRepository = userRepository;
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
        List<NewsSummaryVO> newsSummaryVOList = new java.util.ArrayList<>(recommends.stream()
                .map(recommend -> {
                    News news = newsRepository.findById(recommend.getNewsId()).orElse(null);
                    if (news == null || news.getDeleteFlag()) {
                        return null;
                    }
                    NewsSummaryVO newsSummaryVO = new NewsSummaryVO();
                    BeanUtils.copyProperties(news, newsSummaryVO);
                    newsSummaryVO.setNewsId(news.getId());
                    Long mediaId = news.getCreateUser();
                    userRepository.findById(mediaId).ifPresent(user -> {
                        newsSummaryVO.setMediaId(user.getId());
                        newsSummaryVO.setMediaName(user.getNickname());
                    });
                    newsSummaryVO.setPostTime(news.getCreateTime());
                    return newsSummaryVO;
                }).toList());
        newsSummaryVOList.removeIf(Objects::isNull);
        return newsSummaryVOList;
    }

    public void removeRecommend(Long userId, Long newsId) {
        Recommend recommend = recommendRepository.findByUserIdAndNewsId(userId, newsId);
        if (recommend != null) {
            recommend.setReadFlag(true);
            recommendRepository.save(recommend);
        }
    }
}

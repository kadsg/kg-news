package kg.news.service.impl;

import kg.news.entity.Favorite;
import kg.news.entity.NewsKeyWord;
import kg.news.entity.UserInterest;
import kg.news.repository.FavoriteRepository;
import kg.news.repository.NewsKeyWordRepository;
import kg.news.repository.UserInterestRepository;
import kg.news.service.FavoriteService;
import kg.news.utils.KeyWordUtil;
import org.springframework.stereotype.Service;


@Service
public class FavoriteServiceImpl implements FavoriteService {
    private final FavoriteRepository favoriteRepository;
    private final NewsKeyWordRepository newsKeyWordRepository;
    private final UserInterestRepository userInterestRepository;

    public FavoriteServiceImpl(FavoriteRepository favoriteRepository, NewsKeyWordRepository newsKeyWordRepository, UserInterestRepository userInterestRepository) {
        this.favoriteRepository = favoriteRepository;
        this.newsKeyWordRepository = newsKeyWordRepository;
        this.userInterestRepository = userInterestRepository;
    }

    public void save(Favorite favorite) {
        // 先保存收藏信息
        favoriteRepository.save(favorite);
        Long userId = favorite.getUserId();
        UserInterest interest = userInterestRepository.findByUserId(userId);

        // TODO: 此处可以异步处理
        // 获取新闻关键词
        NewsKeyWord newsKeyWord = newsKeyWordRepository.findByNewsId(favorite.getNewsId());
        String newsKeyWordJson = newsKeyWord.getKeyWord();
        String userKeyWordJson = interest.getInterest();

        double threshold = 0.7; // 相似度阈值
        double convertValue = 1.0; // 权重转换值

        // 如果是收藏操作，在用户关键词表中添加关键词或者增加相关关键词的权重
        // 如果是取消收藏操作，在用户关键词表中删除关键词或者减少相关关键词的权重
        if (favorite.isFavorFlag()) {
            userKeyWordJson = KeyWordUtil.updateKeyWord(newsKeyWordJson, userKeyWordJson, KeyWordUtil.UPDATE_TYPE.INCREASE, threshold, convertValue);
        } else {
            userKeyWordJson = KeyWordUtil.updateKeyWord(newsKeyWordJson, userKeyWordJson, KeyWordUtil.UPDATE_TYPE.DECREASE, threshold, convertValue);
        }
        interest.setInterest(userKeyWordJson);
        userInterestRepository.save(interest);
    }
}

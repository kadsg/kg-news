package kg.news.service;

import kg.news.entity.Favorite;

public interface FavoriteService {
    /**
     * 保存收藏信息
     * @param favorite 收藏信息
     */
    void save(Favorite favorite);
}

package kg.news.service.impl;

import kg.news.dto.NewsTagDTO;
import kg.news.entity.NewsTag;
import kg.news.enumration.OperationType;
import kg.news.repository.NewsTagRepository;
import kg.news.service.NewsTagService;
import kg.news.utils.ServiceUtil;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;

@Service
public class NewsTagServiceImpl implements NewsTagService {
    private final NewsTagRepository newsTagRepository;

    public NewsTagServiceImpl(NewsTagRepository newsTagRepository) {
        this.newsTagRepository = newsTagRepository;
    }

    public void addNewsTag(NewsTagDTO newsTagDTO) {
        NewsTag newsTag = NewsTag.builder()
                .name(newsTagDTO.getName())
                .description(newsTagDTO.getDescription())
                .build();
        try {
            ServiceUtil.autoFill(newsTag, OperationType.INSERT);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        newsTagRepository.save(newsTag);
    }
}

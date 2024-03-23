package kg.news.service.impl;

import kg.news.constant.NewsTagConstant;
import kg.news.dto.NewsTagDTO;
import kg.news.entity.NewsTag;
import kg.news.enumration.OperationType;
import kg.news.exception.NewsTagException;
import kg.news.repository.NewsTagRepository;
import kg.news.service.NewsTagService;
import kg.news.utils.ServiceUtil;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class NewsTagServiceImpl implements NewsTagService {
    private final NewsTagRepository newsTagRepository;

    public NewsTagServiceImpl(NewsTagRepository newsTagRepository) {
        this.newsTagRepository = newsTagRepository;
    }

    public void addNewsTag(NewsTagDTO newsTagDTO) {
        if (newsTagRepository.findByName(newsTagDTO.getName()) != null) {
            throw new NewsTagException(NewsTagConstant.TAG_EXIST);
        }
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


    public List<Map<Long, String>> getAllNewsTag() {
        Iterable<NewsTag> iterable = newsTagRepository.findAll();
        List<Map<Long, String>> newsTags = new ArrayList<>();
        for (NewsTag newsTag : iterable) {
            newsTags.add(Map.of(newsTag.getId(), newsTag.getName()));
        }
        return newsTags;
    }

    public void deleteNewsTag(List<Long> newsTagId) {
        Iterable<NewsTag> newsTags = newsTagRepository.findAllById(newsTagId);
        newsTags.forEach(tag -> {
            if (tag.getDeleteFlag()) {
                throw new NewsTagException(NewsTagConstant.TAG_NOT_FOUND);
            }
            tag.setDeleteFlag(true);
            try {
                ServiceUtil.autoFill(tag, OperationType.UPDATE);
            } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        });
        newsTagRepository.saveAll(newsTags);
    }

    public void updateNewsTag(NewsTagDTO newsTagDTO) {
        Long id = newsTagDTO.getId();
        NewsTag newsTag = newsTagRepository.findById(id).orElse(null);
        if (newsTag == null) {
            throw new NewsTagException(NewsTagConstant.TAG_NOT_FOUND);
        }
        newsTag.setName(newsTagDTO.getName());
        newsTag.setDescription(newsTagDTO.getDescription());
        try {
            ServiceUtil.autoFill(newsTag, OperationType.UPDATE);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        newsTagRepository.save(newsTag);
    }
}

package kg.news.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import kg.news.constant.NewsTagConstant;
import kg.news.dto.NewsTagDTO;
import kg.news.dto.NewsTagQueryDTO;
import kg.news.entity.News;
import kg.news.entity.NewsTag;
import kg.news.enumration.OperationType;
import kg.news.exception.NewsTagException;
import kg.news.mapper.NewsTagMapper;
import kg.news.repository.NewsRepository;
import kg.news.repository.NewsTagRepository;
import kg.news.result.PageResult;
import kg.news.service.NewsTagService;
import kg.news.utils.ServiceUtil;
import kg.news.vo.NewsTagVO;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

@Service
public class NewsTagServiceImpl implements NewsTagService {
    private final NewsTagRepository newsTagRepository;
    private final NewsTagMapper newsTagMapper;
    private final NewsRepository newsRepository;

    public NewsTagServiceImpl(NewsTagRepository newsTagRepository, NewsTagMapper newsTagMapper, NewsRepository newsRepository) {
        this.newsTagRepository = newsTagRepository;
        this.newsTagMapper = newsTagMapper;
        this.newsRepository = newsRepository;
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

    public PageResult<NewsTagVO> getAllNewsTag() {
        Iterable<NewsTag> iterable = newsTagRepository.findAll();
        List<NewsTagVO> newsTagVOList = new ArrayList<>();
        iterable.forEach(tag -> {
            long newsAmount = newsRepository.countByTagId(tag.getId());
            NewsTagVO newsTagVO = NewsTagVO.builder()
                    .tagId(tag.getId())
                    .tagName(tag.getName())
                    .description(tag.getDescription())
                    .createTime(tag.getCreateTime())
                    .updateTime(tag.getUpdateTime())
                    .createUserId(tag.getCreateUser())
                    .updateUserId(tag.getUpdateUser())
                    .count(newsAmount)
                    .deleteFlag(tag.getDeleteFlag())
                    .build();
            newsTagVOList.add(newsTagVO);
        });
        return new PageResult<>(1, newsTagVOList.size(), newsTagVOList.size(), newsTagVOList);
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

    public NewsTag queryNewsTag(Long tagId) {
        return newsTagRepository.findById(tagId).orElse(null);
    }

    public PageResult<NewsTagVO> queryNewsTag(NewsTagQueryDTO newsTagQueryDTO) {
        int pageNum = newsTagQueryDTO.getPageNum();
        int pageSize = newsTagQueryDTO.getPageSize();
        if (pageNum <= 0 || pageSize <= 0) {
            pageNum = 1;
            pageSize = 10;
        }
        PageHelper.startPage(pageNum, pageSize);
        Page<NewsTag> newsTagPage = newsTagMapper.queryNewsTag(newsTagQueryDTO);
        List<NewsTagVO> newsTagVOList = newsTagPage.getResult().stream().map(tag -> {
            long newsAmount = newsRepository.countByTagId(tag.getId());
            return NewsTagVO.builder()
                    .tagId(tag.getId())
                    .tagName(tag.getName())
                    .description(tag.getDescription())
                    .createTime(tag.getCreateTime())
                    .updateTime(tag.getUpdateTime())
                    .createUserId(tag.getCreateUser())
                    .updateUserId(tag.getUpdateUser())
                    .count(newsAmount)
                    .deleteFlag(tag.getDeleteFlag())
                    .build();
        }).toList();
        return new PageResult<>(newsTagPage.getPageNum(), newsTagPage.getPageSize(), newsTagPage.getTotal(), newsTagVOList);
    }

    public NewsTagVO getNewsTag(Long tagId) {
        NewsTag newsTag = newsTagRepository.findById(tagId).orElse(null);
        if (newsTag != null) {
            long newsAmount = newsRepository.countByTagId(newsTag.getId());
            return NewsTagVO.builder()
                    .tagId(newsTag.getId())
                    .tagName(newsTag.getName())
                    .description(newsTag.getDescription())
                    .createTime(newsTag.getCreateTime())
                    .updateTime(newsTag.getUpdateTime())
                    .createUserId(newsTag.getCreateUser())
                    .updateUserId(newsTag.getUpdateUser())
                    .count(newsAmount)
                    .deleteFlag(newsTag.getDeleteFlag())
                    .build();
        }
        return null;
    }

    public List<NewsTagVO> getPublishedNewsTags(Long userId) {
        List<News> newsList = newsRepository.findByCreateUser(userId);
        if (newsList.isEmpty()) {
            return new ArrayList<>();
        }
        List<Long> tagIdList = newsList.stream().map(News::getTagId).toList();
        Iterable<NewsTag> newsTags = newsTagRepository.findAllById(tagIdList);
        List<NewsTagVO> newsTagVOList = new ArrayList<>();
        newsTags.forEach(tag -> {
            long newsAmount = newsRepository.countByTagId(tag.getId());
            NewsTagVO newsTagVO = NewsTagVO.builder()
                    .tagId(tag.getId())
                    .tagName(tag.getName())
                    .description(tag.getDescription())
                    .createTime(tag.getCreateTime())
                    .updateTime(tag.getUpdateTime())
                    .createUserId(tag.getCreateUser())
                    .updateUserId(tag.getUpdateUser())
                    .count(newsAmount)
                    .deleteFlag(tag.getDeleteFlag())
                    .build();
            newsTagVOList.add(newsTagVO);
        });
        return newsTagVOList;
    }
}

package kg.news.service;

import kg.news.dto.HistoryQueryDTO;
import kg.news.entity.History;
import kg.news.result.PageResult;
import kg.news.vo.HistoryVO;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

public interface HistoryService {
    /**
     * 保存浏览历史记录
     *
     * @param newsId 新闻ID
     * @param userId 用户ID
     * @param time   时间
     */
    void save(Long newsId, Long userId, LocalDateTime time);

    /**
     * 分页获取浏览历史记录
     *
     * @param historyQueryDTO 查询条件
     */
    PageResult<HistoryVO> queryHistory(HistoryQueryDTO historyQueryDTO);

    /**
     * 分页获取指定用户的浏览历史记录
     *
     * @param userId 用户id
     */
    List<History> getHistoryByUserId(Long userId, Pageable pageable);

    /**
     * 删除浏览历史记录
     *
     * @param id 浏览历史记录id
     */
    void delete(Long id);

    /**
     * 获取所有浏览历史记录
     */
    List<History> getAllHistory();
}

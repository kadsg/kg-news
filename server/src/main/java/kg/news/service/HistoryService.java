package kg.news.service;

import kg.news.dto.HistoryQueryDTO;
import kg.news.dto.HistorySaveDTO;
import kg.news.result.PageResult;
import kg.news.vo.HistoryVO;

public interface HistoryService {
    /**
     * 保存浏览历史记录
     *
     * @param historySaveDTO 浏览历史记录
     */
    void save(HistorySaveDTO historySaveDTO);

    /**
     * 分页获取浏览历史记录
     *
     * @param historyQueryDTO 查询条件
     */
    PageResult<HistoryVO> queryHistory(HistoryQueryDTO historyQueryDTO);

    /**
     * 删除浏览历史记录
     *
     * @param id 浏览历史记录id
     */
    void delete(Long id);
}

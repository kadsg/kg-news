package kg.news.controller;

import kg.news.dto.HistoryQueryDTO;
import kg.news.dto.HistorySaveDTO;
import kg.news.result.PageResult;
import kg.news.result.Result;
import kg.news.vo.HistoryVO;
import org.springframework.web.bind.annotation.*;

import kg.news.service.HistoryService;

/**
 * 浏览历史记录
 */
@RestController
@RequestMapping("/history")
public class HistoryController {
    private final HistoryService historyService;

    public HistoryController(HistoryService historyService) {
        this.historyService = historyService;
    }

    /**
     * 保存浏览历史记录
     *
     * @param historySaveDTO 浏览历史记录
     */
    @PostMapping("/save")
    public Result<Object> save(HistorySaveDTO historySaveDTO) {
        historyService.save(historySaveDTO);
        return Result.success();
    }

    /**
     * 分页获取浏览历史记录
     *
     * @param historyQueryDTO 查询条件
     */
    @GetMapping("/query")
    public Result<PageResult<HistoryVO>> queryHistory(HistoryQueryDTO historyQueryDTO) {
        PageResult<HistoryVO> historyVOList = historyService.queryHistory(historyQueryDTO);
        return Result.success(historyVOList);
    }

    /**
     * 删除浏览历史记录
     *
     * @param id 浏览历史记录id
     */
    @DeleteMapping("/delete/{id}")
    public Result<Object> delete(@PathVariable Long id) {
        historyService.delete(id);
        return Result.success();
    }
}

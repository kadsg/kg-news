package kg.news.controller;

import kg.news.dto.CommentQueryDTO;
import kg.news.dto.CommentSaveDTO;
import kg.news.result.PageResult;
import kg.news.result.Result;
import kg.news.service.CommentService;
import kg.news.vo.CommentVO;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.InvocationTargetException;

/**
 * 评论管理控制器
 */
@CrossOrigin
@RestController("/comment")
public class CommentController {
    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    /**
     * 获取新闻评论列表（顶层评论）
     *
     * @param commentDTO 评论查询DTO
     * @return 新闻评论列表
     */
    @GetMapping("/list")
    public Result<PageResult<CommentVO>> getNewsCommentList(CommentQueryDTO commentDTO) {
        PageResult<CommentVO> commentVOPageResult = commentService.queryNewsCommentList(commentDTO);
        return Result.success(commentVOPageResult);
    }

    /**
     * 删除评论
     *
     * @param commentId 评论ID
     */
    @DeleteMapping("/{id}")
    public Result<Object> deleteComment(@PathVariable("id") Long commentId) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        commentService.deleteComment(commentId);
        return Result.success();
    }

    /**
     * 点赞评论或取消
     *
     * @param commentId 评论ID
     */
    @PutMapping("/favor/{id}")
    public Result<Object> likeComment(@PathVariable("id") Long commentId) {
        commentService.likeComment(commentId);
        return Result.success();
    }

    /**
     * ”踩“评论或取消
     *
     * @param commentId 评论ID
     */
    @PutMapping("/dislike/{id}")
    public Result<Object> dislikeComment(@PathVariable("id") Long commentId) {
        commentService.dislikeComment(commentId);
        return Result.success();
    }

    /**
     * 评论
     *
     * @param commentSaveDTO 评论保存DTO
     */
    @PostMapping("/comment")
    public Result<Object> comment(@RequestBody CommentSaveDTO commentSaveDTO) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        commentService.comment(commentSaveDTO);
        return Result.success();
    }
}

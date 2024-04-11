package kg.news.mapper;

import com.github.pagehelper.Page;
import kg.news.dto.CommentQueryDTO;
import kg.news.vo.CommentVO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CommentMapper {
    /**
     * 获取新闻评论列表
     *
     * @param commentDTO 评论查询DTO
     * @return 新闻评论列表
     */
    Page<CommentVO> queryNewsCommentList(CommentQueryDTO commentDTO);
}

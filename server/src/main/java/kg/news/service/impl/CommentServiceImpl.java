package kg.news.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import kg.news.constant.CommentConstant;
import kg.news.context.BaseContext;
import kg.news.dto.CommentQueryDTO;
import kg.news.dto.CommentSaveDTO;
import kg.news.entity.Comment;
import kg.news.entity.CommentLike;
import kg.news.enumration.OperationType;
import kg.news.exception.CommentException;
import kg.news.mapper.CommentMapper;
import kg.news.repository.CommentRepository;
import kg.news.repository.LikeRepository;
import kg.news.result.PageResult;
import kg.news.service.CommentService;
import kg.news.service.UserService;
import kg.news.utils.ServiceUtil;
import kg.news.vo.CommentVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Objects;

@Service
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final LikeRepository likeRepository;
    private final CommentMapper commentMapper;
    private final UserService userService;

    public CommentServiceImpl(CommentRepository commentRepository, LikeRepository likeRepository, CommentMapper commentMapper, UserService userService) {
        this.commentRepository = commentRepository;
        this.likeRepository = likeRepository;
        this.commentMapper = commentMapper;
        this.userService = userService;
    }

    public PageResult<CommentVO> queryNewsCommentList(CommentQueryDTO commentDTO) {
        int page = commentDTO.getPage();
        int size = commentDTO.getSize();
        if (page <= 0 || size <= 0) {
            page = 1;
            size = 10;
        }
        PageHelper.startPage(page, size);
        Page<CommentVO> commentVOS = commentMapper.queryNewsCommentList(commentDTO);
        commentVOS.getResult().forEach(commentVO -> {
            Long authorId = commentVO.getAuthorId();
            commentVO.setAuthorName(userService.queryUserById(authorId).getNickname());
        });
        List<CommentVO> result = commentVOS.getResult();
        if (result.isEmpty()) {
            throw new CommentException(CommentConstant.COMMENT_NOT_FOUND);
        }
        return new PageResult<>((long) result.size(), result);
    }

    public void deleteComment(Long commentId) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        Comment comment = commentRepository.findById(commentId).orElse(null);
        if (comment == null || comment.getDeleteFlag()) {
            throw new CommentException(CommentConstant.COMMENT_NOT_FOUND);
        }
        if (!Objects.equals(comment.getCreateUser(), BaseContext.getCurrentId())) {
            throw new CommentException(CommentConstant.NOT_COMMENT_MANAGER);
        }
        comment.setDeleteFlag(true);
        ServiceUtil.autoFill(comment, OperationType.UPDATE);
        commentRepository.save(comment);
    }

    @Transactional
    public void likeComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId).orElse(null);
        Long userId = BaseContext.getCurrentId();
        CommentLike commentLike = likeRepository.findById(commentId).orElse(null);
        if (comment == null || comment.getDeleteFlag()) {
            throw new CommentException(CommentConstant.COMMENT_NOT_FOUND);
        }
        // 如果没有点赞记录，则创建一条
        if (commentLike == null) {
            commentLike = CommentLike.builder()
                    .commentId(commentId)
                    .userId(userId)
                    .likeFlag(true)
                    .build();
            comment.setLikeCount(comment.getLikeCount() + 1);
        } else {
            // 如果有点赞记录，则取反
            if (commentLike.isLikeFlag()) {
                comment.setLikeCount(comment.getLikeCount() - 1);
            } else {
                comment.setLikeCount(comment.getLikeCount() + 1);
            }
            commentLike.setLikeFlag(!commentLike.isLikeFlag());
        }
        likeRepository.save(commentLike);
        commentRepository.save(comment);
    }

    @Transactional
    public void dislikeComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId).orElse(null);
        Long userId = BaseContext.getCurrentId();
        CommentLike commentLike = likeRepository.findById(commentId).orElse(null);
        if (comment == null || comment.getDeleteFlag()) {
            throw new CommentException(CommentConstant.COMMENT_NOT_FOUND);
        }
        // 如果没有踩记录，则创建一条
        if (commentLike == null) {
            commentLike = CommentLike.builder()
                    .commentId(commentId)
                    .userId(userId)
                    .dislikeFlag(true)
                    .build();
            comment.setUnlikeCount(comment.getUnlikeCount() + 1);
        } else {
            // 如果有点赞记录，则取反
            if (commentLike.isLikeFlag()) {
                comment.setUnlikeCount(comment.getUnlikeCount() - 1);
            } else {
                comment.setUnlikeCount(comment.getUnlikeCount() + 1);
            }
            commentLike.setDislikeFlag(!commentLike.isDislikeFlag());
        }
        likeRepository.save(commentLike);
        commentRepository.save(comment);
    }

    public void comment(CommentSaveDTO commentSaveDTO) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        Comment comment = Comment.builder()
                .newsId(commentSaveDTO.getNewsId())
                .parentId(commentSaveDTO.getParentId())
                .replyId(commentSaveDTO.getReplyId())
                .content(commentSaveDTO.getContent())
                .build();
        ServiceUtil.autoFill(comment, OperationType.INSERT);
        commentRepository.save(comment);
    }
}

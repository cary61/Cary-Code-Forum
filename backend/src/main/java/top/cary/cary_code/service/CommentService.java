package top.cary.cary_code.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import top.cary.cary_code.entity.Action;
import top.cary.cary_code.entity.Article;
import top.cary.cary_code.entity.Comment;
import top.cary.cary_code.entity.User;
import top.cary.cary_code.entity.unum.ActionTypeEnum;
import top.cary.cary_code.entity.unum.TextTypeEnum;
import top.cary.cary_code.mapper.ActionMapper;
import top.cary.cary_code.mapper.ArticleMapper;
import top.cary.cary_code.mapper.CommentMapper;
import top.cary.cary_code.mapper.UserMapper;
import top.cary.cary_code.utils.Response;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CommentService {

    @Value("${user.default-avatar}")
    private String defaultAvatar;

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    ActionMapper actionMapper;

    @Autowired
    UserService userService;

    public Response getCommentsByArticle(Integer articleId, Integer uid) {
        List<Comment> list;
        try {
            list = commentMapper.getCommentsByArticle(articleId);
        } catch (Exception e) {
            return Response.fail();
        }
        list.stream().forEach(comment -> fillFields(comment, uid));
        return Response.ok(list);
    }

    public Response addComment(Comment comment) {
        if (userService.banned(comment.getUid())) {
            return Response.failWithMsg("禁言中，无法操作");
        }
        LocalDateTime now = LocalDateTime.now();
        comment.setCreateTime(now);
        try {
            commentMapper.add(comment);
            updateArticleCommentsAmount(comment.getArticleId(), 1);
            articleMapper.updateLatestOperateTime(comment.getArticleId(), now);
        } catch (Exception e) {
            return Response.failWithMsg("评论失败");
        }
        return Response.ok();
    }

    public Response deleteComment(Integer id, Integer uid) {
        try {
            Comment comment = commentMapper.get(id);
            commentMapper.delete(id);
            commentMapper.deleteByCommentId(id);
            updateArticleCommentsAmount(comment.getArticleId(),-1);
        } catch (Exception e) {
            return Response.failWithMsg("删除评论失败");
        }
        return Response.ok();
    }

    void fillFields(Comment comment, Integer readerUid) {
        User user = userMapper.get(comment.getUid());
        // set avatar
        String avatar = user.getAvatar();
        if (StringUtils.isBlank(avatar)) {
            avatar = defaultAvatar;
        }
        comment.setAvatar(avatar);
        // set liked
        Action action = Action.builder()
                .uid(readerUid)
                .actionType(ActionTypeEnum.LIKE)
                .textId(comment.getId())
                .textType(TextTypeEnum.COMMENT)
                .build();
        Action result = actionMapper.get(action);
        if (result != null) {
            comment.setLiked(true);
        } else {
            comment.setLiked(false);
        }
        // set hated
        action.setActionType(ActionTypeEnum.HATE);
        result = actionMapper.get(action);
        if (result != null) {
            comment.setHated(true);
        } else {
            comment.setHated(false);
        }
    }

    private void updateArticleCommentsAmount(int articleId, int diff) {
        try {
            Article article = articleMapper.get(articleId);
            int commentsAmount = article.getCommentsAmount();
            articleMapper.updateCommentsAmount(articleId, commentsAmount + diff);
        } catch (Exception e) {
//            e.printStackTrace();
        }
    }
}

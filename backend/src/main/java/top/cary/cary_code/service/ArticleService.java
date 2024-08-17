package top.cary.cary_code.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import top.cary.cary_code.entity.Action;
import top.cary.cary_code.entity.Article;
import top.cary.cary_code.entity.User;
import top.cary.cary_code.entity.unum.ActionTypeEnum;
import top.cary.cary_code.entity.unum.TextTypeEnum;
import top.cary.cary_code.mapper.ActionMapper;
import top.cary.cary_code.mapper.ArticleMapper;
import top.cary.cary_code.mapper.CommentMapper;
import top.cary.cary_code.mapper.UserMapper;
import top.cary.cary_code.utils.Response;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class ArticleService {

    @Value("${user.default-avatar}")
    private String defaultAvatar;

    @Value("${web-config.article-page-size}")
    private int articlePageSize;

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private ActionMapper actionMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserService userService;

    private Article get(Integer id) {
        Article article;
        try {
            article = articleMapper.get(id);
            article.setNickname(userMapper.get(article.getUid()).getNickname());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return article;
    }

    public Response getArticle(Integer id) {
        Article article = get(id);
        if (article == null) {
            return Response.failWithMsg("文章不存在");
        }
        fillFields(article, 0);
        return Response.ok(article);
    }

    public Response getArticleList(Integer uid) {
        List<Article> list = articleMapper.getTop(articlePageSize);
        list.stream().forEach(article -> fillFields(article, uid));
        return Response.ok(list);
    }

    public Response getRecentLikedArticles(String name) {
        User user = userMapper.getByName(name);
        if (user == null) {
            return Response.failWithMsg("用户不存在");
        }
        List<Action> recentLikeActions;
        List<Article> list;
        try {
            recentLikeActions = actionMapper.getRecent(user.getUid(), "ARTICLE", "LIKE");
            list = new ArrayList<>(recentLikeActions.size());
            for (Action recentLikeAction : recentLikeActions) {
                Article article = articleMapper.get(recentLikeAction.getTextId());
                if (article != null) {
                    list.add(article);
                }
            }
            list.stream().forEach(e -> fillFields(e, 0));
        } catch (Exception e) {
            return Response.failWithMsg("#异常");
        }
        return Response.ok(list);
    }

    public Response getHottestArticles() {
        List<Article> list;
        try {
            list = articleMapper.getHottestArticles(articlePageSize);
        } catch (Exception e) {
            return Response.failWithMsg("#错误");
        }
        return Response.ok(list);
    }

    public Response getArticleListByName(String name) {
        User user = userMapper.getByName(name);
        if (user == null) {
            return Response.failWithMsg("用户不存在");
        }
        List<Article> list;
        try {
            list = articleMapper.getListByUid(user.getUid());
            list.stream().forEach(e -> fillFields(e, 0));
        } catch (Exception e) {
            return Response.failWithMsg("#异常");
        }
        return Response.ok(list);
    }

    public Response addArticle(Article article) {
        if (userService.banned(article.getUid())) {
            return Response.failWithMsg("禁言中，无法操作");
        }
        article.setId(null);
        LocalDateTime localDateTime = LocalDateTime.now();
        article.setCreateTime(localDateTime);
        article.setUpdateTime(localDateTime);
        article.setLatestOperateTime(localDateTime);
        try {
            article.setNickname(userMapper.get(article.getUid()).getNickname());
            articleMapper.add(article);
        } catch (Exception e) {
            e.printStackTrace();
            return Response.failWithMsg("失败");
        }
        return Response.ok();
    }

    public Response updateArticle(Article articleIn) {
        if (userService.banned(articleIn.getUid())) {
            return Response.failWithMsg("禁言中，无法操作");
        }
        Integer id = articleIn.getId();
        Integer uid = articleIn.getUid();
        String content = articleIn.getContent();
        Article article = get(id);
        if (article == null) {
            return Response.failWithMsg("文章不存在");
        }
        if (!Objects.equals(uid, article.getUid())) {
            return Response.failWithMsg("不能修改别人的文章");
        }
        boolean isOk;
        LocalDateTime now = LocalDateTime.now();
        articleIn.setUpdateTime(now);
        articleIn.setLatestOperateTime(now);
        try {
            isOk = articleMapper.update(articleIn);
        } catch (Exception e) {
            isOk = false;
        }
        return Response.resultOf(isOk);
    }

    public Response deleteArticle(Integer id, Integer uid) {
        Article article = get(id);
        if (article == null) {
            return Response.failWithMsg("文章不存在");
        }
        if (!Objects.equals(uid, article.getUid())) {
            return Response.failWithMsg("不能删除别人的文章");
        }
        boolean isOk;
        try {
            isOk = articleMapper.delete(id)
                    && commentMapper.deleteByArticleId(id) >= 0;
        } catch (Exception e) {
            isOk = false;
        }
        return Response.resultOf(isOk);
    }

    void fillFields(Article article, Integer readerUid) {
        User user = userMapper.get(article.getUid());
        // set avatar
        String avatar = user.getAvatar();
        if (StringUtils.isBlank(avatar)) {
            avatar = defaultAvatar;
        }
        article.setAvatar(avatar);
        article.setName(user.getName());
        // set liked
        Action action = Action.builder()
                .uid(readerUid)
                .actionType(ActionTypeEnum.LIKE)
                .textId(article.getId())
                .textType(TextTypeEnum.ARTICLE)
                .build();
        Action result = actionMapper.get(action);
        if (result != null) {
            article.setLiked(true);
        } else {
            article.setLiked(false);
        }
        // set hated
        action.setActionType(ActionTypeEnum.HATE);
        result = actionMapper.get(action);
        if (result != null) {
            article.setHated(true);
        } else {
            article.setHated(false);
        }
    }
}

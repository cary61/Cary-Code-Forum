package top.cary.cary_code.service;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.cary.cary_code.entity.Article;
import top.cary.cary_code.entity.Comment;
import top.cary.cary_code.entity.User;
import top.cary.cary_code.mapper.ArticleMapper;
import top.cary.cary_code.mapper.CommentMapper;
import top.cary.cary_code.mapper.UserMapper;
import top.cary.cary_code.utils.matcher.Matcher;
import top.cary.cary_code.utils.Response;

import java.util.ArrayList;
import java.util.List;

@Service
public class ManageService {

    @Autowired
    UserMapper userMapper;

    @Autowired
    ArticleMapper articleMapper;

    @Autowired
    CommentMapper commentMapper;

    @Autowired
    ArticleService articleService;

    @Autowired
    CommentService commentService;

    @Autowired
    Matcher matcher;

    public Response getUserByUid(Integer uid) {
        User user;
        try {
            user = userMapper.get(uid);
        } catch (Exception e) {
            return Response.failWithMsg("#错误");
        }
        User[] result;
        if (user != null) {
            result = new User[]{user};
        } else {
            result = new User[0];
        }
        return Response.ok(result);
    }

    public Response getUserByName(String name) {
        User user;
        try {
            user = userMapper.getByName(name);
        } catch (Exception e) {
            return Response.failWithMsg("#错误");
        }
        User[] result;
        if (user != null) {
            result = new User[]{user};
        } else {
            result = new User[0];
        }
        return Response.ok(result);
    }

    public Response getUserByNickname(String nickname) {
        User user;
        try {
            user = userMapper.getByNickname(nickname);
            System.out.println(user);
        } catch (Exception e) {
            return Response.failWithMsg("#错误");
        }
        User[] result;
        if (user != null) {
            result = new User[]{user};
        } else {
            result = new User[0];
        }
        return Response.ok(result);
    }

    public Response deleteUser(Integer uid) {
        try {
            userMapper.delete(uid);
        } catch (Exception e) {
            return Response.failWithMsg("删除失败");
        }
        return Response.ok();
    }

    public Response banUser(Integer uid, String banUntil) {
        try {
            userMapper.ban(uid, banUntil);
        } catch (Exception e) {
            return Response.failWithMsg("禁言失败");
        }
        return Response.ok();
    }

    public Response getArticleById(Integer id) {
        Article article;
        try {
            article = articleMapper.get(id);
        } catch (Exception e) {
            return Response.failWithMsg("#错误");
        }
        List<Article> list = new ArrayList<>(1);
        if (article != null) {
            articleService.fillFields(article, 1);
            list.add(article);
        }
        return Response.ok(list);
    }

    public Response getArticleByTitle(String title) {
        List<Pair<Article,Integer>> list = new ArrayList<>();
        try {
            List<Article> articles = articleMapper.getAll();
            for (Article article : articles) {
                int matchScores = matcher.fuzzyMatch(title, article.getTitle());
                if (matchScores >= 1) {
                    articleService.fillFields(article, 1);
                    list.add(Pair.of(article, matchScores));
                }
            }
        } catch (Exception e) {
            return Response.failWithMsg("#错误");
        }
        return Response.ok(list.stream()
                .sorted((x, y) -> y.getRight() - x.getRight())
                .map(e -> e.getLeft())
                .toList());
    }

    public Response getArticleByContent(String content) {
        List<Pair<Article,Integer>> list = new ArrayList<>();
        try {
            List<Article> articles = articleMapper.getAll();
            for (Article article : articles) {
                int matchScores = matcher.fuzzyMatch(content, article.getContent());
                if (matchScores >= 1) {
                    articleService.fillFields(article, 1);
                    list.add(Pair.of(article, matchScores));
                }
            }
        } catch (Exception e) {
            return Response.failWithMsg("#错误");
        }
        return Response.ok(list.stream()
                .sorted((x, y) -> y.getRight() - x.getRight())
                .map(e -> e.getLeft())
                .toList());
    }

    public Response deleteArticle(Integer id) {
        try {
            articleMapper.delete(id);
        } catch (Exception e) {
            return Response.failWithMsg("#错误");
        }
        return Response.ok();
    }

    public Response getCommentById(Integer id) {
        Comment comment;
        try {
            comment = commentMapper.get(id);
        } catch (Exception e) {
            return Response.failWithMsg("#错误");
        }
        List<Comment> list = new ArrayList<>(1);
        if (comment != null) {
            commentService.fillFields(comment, 1);
            list.add(comment);
        }
        return Response.ok(list);
    }

    public Response getCommentByContent(String content) {
        List<Pair<Comment,Integer>> list = new ArrayList<>();
        try {
            List<Comment> articles = commentMapper.getAll();
            for (Comment comment : articles) {
                int matchScores = matcher.fuzzyMatch(content, comment.getContent());
                if (matchScores >= 1) {
                    commentService.fillFields(comment, 1);
                    list.add(Pair.of(comment, matchScores));
                }
            }
        } catch (Exception e) {
            return Response.failWithMsg("#错误");
        }
        return Response.ok(list.stream()
                .sorted((x, y) -> y.getRight() - x.getRight())
                .map(e -> e.getLeft())
                .toList());
    }

    public Response deleteComment(Integer id) {
        try {
            commentMapper.delete(id);
        } catch (Exception e) {
            return Response.failWithMsg("#错误");
        }
        return Response.ok();
    }
}

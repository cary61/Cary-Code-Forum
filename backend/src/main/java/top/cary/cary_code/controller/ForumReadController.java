package top.cary.cary_code.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.cary.cary_code.mapper.CommentMapper;
import top.cary.cary_code.service.ArticleService;
import top.cary.cary_code.service.CommentService;
import top.cary.cary_code.utils.Response;

@RestController
@RequestMapping("/forum_read")
public class ForumReadController {

    @Autowired
    private ArticleService articleService;

    @Autowired
    CommentService commentService;

    @GetMapping("/article_list")
    public Response getArticleList(@RequestParam(defaultValue = "1") Integer uid) {
        return articleService.getArticleList(uid);
    }

    @GetMapping("/article_list_by_name")
    public Response getArticleListByName(@RequestParam String name) {
        return articleService.getArticleListByName(name);
    }

    @GetMapping("/recent_liked_articles")
    public Response getRecentLikedArticles(@RequestParam String name) {
        return articleService.getRecentLikedArticles(name);
    }

    @GetMapping("/hottest_articles")
    public Response getHottestArticles() {
        return articleService.getHottestArticles();
    }

    @GetMapping("/article")
    public Response getArticle(@RequestParam("article_id") Integer articleId) {
        return articleService.getArticle(articleId);
    }

    @GetMapping("/comments")
    public Response getComments(@RequestParam("article_id") Integer articleId,
                                @RequestParam(defaultValue = "1") Integer uid) {
        return commentService.getCommentsByArticle(articleId, uid);
    }
}

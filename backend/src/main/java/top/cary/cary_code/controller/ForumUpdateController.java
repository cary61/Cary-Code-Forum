package top.cary.cary_code.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.cary.cary_code.entity.Article;
import top.cary.cary_code.entity.Comment;
import top.cary.cary_code.service.ArticleService;
import top.cary.cary_code.service.CommentService;
import top.cary.cary_code.utils.Response;

@RestController
@RequestMapping("/forum_update")
public class ForumUpdateController {

    @Autowired
    private ArticleService articleService;

    @Autowired
    private CommentService commentService;

    @PostMapping("/article")
    public Response addArticle(@RequestBody Article article,
                               HttpServletRequest httpServletRequest) {
        return articleService.addArticle(article);
    }

    @PutMapping("article")
    public Response updateArticle(@RequestBody Article article,
                                  HttpServletRequest httpServletRequest) {
        return articleService.updateArticle(article);
    }

    @DeleteMapping("article")
    public Response deleteArticle(@RequestParam Integer id,
                                  @RequestParam Integer uid,
                                  HttpServletRequest httpServletRequest) {
        return articleService.deleteArticle(id, uid);
    }

    @PostMapping("/comment")
    public Response addComment(@RequestBody Comment comment,
                               HttpServletRequest httpServletRequest) {
        return commentService.addComment(comment);
    }

    @DeleteMapping("/comment")
    public Response deleteComment(@RequestParam Integer id,
                                  @RequestParam Integer uid,
                                  HttpServletRequest httpServletRequest) {
        return commentService.deleteComment(id, uid);
    }
}

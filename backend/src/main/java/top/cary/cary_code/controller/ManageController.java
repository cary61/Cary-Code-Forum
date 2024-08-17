package top.cary.cary_code.controller;

import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.cary.cary_code.entity.dto.UserBanRecord;
import top.cary.cary_code.service.ManageService;
import top.cary.cary_code.utils.Response;

@RestController
@RequestMapping("/manage")
public class ManageController {

    @Autowired
    ManageService manageService;

    @GetMapping("/user_by_uid")
    public Response getUserByUid(@RequestParam Integer uid) {
        return manageService.getUserByUid(uid);
    }

    @GetMapping("/user_by_name")
    public Response getUserByName(@RequestParam String name) {
        return manageService.getUserByName(name);
    }

    @GetMapping("/user_by_nickname")
    public Response getUserByNickname(@RequestParam String nickname) {
        return manageService.getUserByNickname(nickname);
    }

    @DeleteMapping("/user")
    public Response deleteUser(@RequestParam Integer uid) {
        return manageService.deleteUser(uid);
    }

    @PostMapping("/user_ban")
    public Response banUser(@RequestBody UserBanRecord userBanRecord) {
        return manageService.banUser(userBanRecord.getUid(), userBanRecord.getBanUntil());
    }

    @GetMapping("/article_by_id")
    public Response getArticleById(@RequestParam Integer id) {
        return manageService.getArticleById(id);
    }

    @GetMapping("/article_by_title")
    public Response getArticleByTitle(@RequestParam String title) {
        return manageService.getArticleByTitle(title);
    }

    @GetMapping("/article_by_content")
    public Response getArticleByContent(@RequestParam String content) {
        return manageService.getArticleByContent(content);
    }

    @DeleteMapping("/article")
    public Response deleteArticle(@RequestParam Integer id) {
        return manageService.deleteArticle(id);
    }

    @GetMapping("/comment_by_id")
    public Response getCommentById(@RequestParam Integer id) {
        return manageService.getCommentById(id);
    }

    @GetMapping("/comment_by_content")
    public Response getCommentByContent(@RequestParam String content) {
        return manageService.getCommentByContent(content);
    }

    @DeleteMapping("/comment")
    public Response deleteComment(@RequestParam Integer id) {
        return manageService.deleteComment(id);
    }
}

package top.cary.cary_code.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.cary.cary_code.entity.User;
import top.cary.cary_code.entity.dto.SimpleUser;
import top.cary.cary_code.service.UserService;
import top.cary.cary_code.utils.Response;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public Response register(@RequestBody SimpleUser user) {
        return userService.register(user);
    }

    @GetMapping("")
    public Response getUser(@RequestParam Integer uid) {
        return userService.getUser(uid);
    }

    @GetMapping("/by_name")
    public Response getUserByName(@RequestParam String name) {
        return userService.getUserByName(name);
    }

    @GetMapping("/by_token")
    public Response getUserByToken(HttpServletRequest httpServletRequest) {
        return userService.getUserByToken(httpServletRequest);
    }

    @PostMapping("/login")
    public Response login(@RequestBody SimpleUser user,
                          @Autowired HttpServletResponse httpServletResponse) {
        return userService.login(user.getName(), user.getPassword(), httpServletResponse);
    }

    @PostMapping("/logout")
    public Response logout(@RequestBody SimpleUser user,
                           HttpServletRequest httpServletRequest,
                           HttpServletResponse httpServletResponse) {
        return userService.logout(user.getName(), httpServletRequest, httpServletResponse);
    }

    @PutMapping("")
    public Response updateUserInformation(@RequestBody User user) {
        return userService.updateUser(user);
    }
}

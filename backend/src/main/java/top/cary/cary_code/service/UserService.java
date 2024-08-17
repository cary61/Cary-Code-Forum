package top.cary.cary_code.service;

import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.apache.commons.lang3.StringUtils;
import top.cary.cary_code.entity.User;
import top.cary.cary_code.entity.dto.SimpleUser;
import top.cary.cary_code.entity.dto.UserDTO;
import top.cary.cary_code.entity.unum.AuthorityEnum;
import top.cary.cary_code.mapper.ArticleMapper;
import top.cary.cary_code.mapper.CommentMapper;
import top.cary.cary_code.mapper.UserMapper;
import top.cary.cary_code.utils.encryption.Hasher;
import top.cary.cary_code.utils.encryption.JwtUtils;
import top.cary.cary_code.utils.Response;

import java.time.LocalDateTime;
import java.util.Objects;


@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private Hasher hasher;

    @Autowired
    private JwtUtils jwtUtils;

    @Value("${user.default-avatar}")
    private String defaultAvatar;

    @Value("${user.default-signature}")
    private String defaultSignature;

    public Response updateUser(User user) {
        User oldUser = userMapper.get(user.getUid());
        if (oldUser == null) {
            return Response.fail();
        }
        if (!Objects.equals(oldUser.getNickname(), user.getNickname())) {
            boolean isOK = updateNickname(user);
            if (!isOK) {
                return Response.failWithMsg("服务器内部错误#1，请联系网站管理员");
            }
        }
        boolean isOK = userMapper.update(user);
        if (!isOK) {
            return Response.failWithMsg("服务器内部错误#2，请联系网站管理员");
        }
        userMapper.update(user);
        return Response.ok();
    }

    public Response register(SimpleUser user) {
        String name = user.getName();
        String nickname = user.getNickname();
        String password = user.getPassword();
        if (StringUtils.isBlank(name)) {
            return Response.failWithMsg("用户名不能为空");
        }
        if (StringUtils.isBlank(password)) {
            return Response.failWithMsg("密码不能为空");
        }
        String passwordHash = hasher.hash(password);
        try {
            userMapper.add(name, passwordHash, nickname,
                    defaultAvatar, defaultSignature,
                    AuthorityEnum.USER, LocalDateTime.now());
        } catch (Exception e) {
            e.printStackTrace();
            return Response.failWithMsg("用户名重复");
        }
        return Response.ok();
    }

    public Response getUser(Integer uid) {
        UserDTO userDTO;
        try {
            userDTO = userMapper.get(uid).toDTO();
        } catch (Exception e) {
            return Response.failWithMsg("未查找到用户");
        }
        if (StringUtils.isBlank(userDTO.getAvatar())) {
            userDTO.setAvatar(defaultAvatar);
        }
        if (StringUtils.isBlank(userDTO.getSignature())) {
            userDTO.setSignature(defaultSignature);
        }
        return Response.ok(userDTO);
    }

    public Response getUserByName(String name) {
        UserDTO userDTO;
        try {
            userDTO = userMapper.getByName(name).toDTO();
        } catch (Exception e) {
            return Response.failWithMsg("未查找到用户");
        }
        if (StringUtils.isBlank(userDTO.getAvatar())) {
            userDTO.setAvatar(defaultAvatar);
        }
        if (StringUtils.isBlank(userDTO.getSignature())) {
            userDTO.setSignature("这个人很懒，还没有设置签名╮(╯▽╰)╭");
        }
        return Response.ok(userDTO);
    }

    public Response getUserByToken(HttpServletRequest httpServletRequest) {
        DecodedJWT decodedJWT = jwtUtils.decodeJwt(jwtUtils.getToken(httpServletRequest));
        if (decodedJWT == null) {
            return Response.failWithMsg("用户未登录");
        }
        User user = jwtUtils.parseUser(decodedJWT);
        UserDTO userDTO = userMapper.get(user.getUid()).toDTO();
        if (StringUtils.isBlank(userDTO.getAvatar())) {
            userDTO.setAvatar(defaultAvatar);
        }
        if (StringUtils.isBlank(userDTO.getSignature())) {
            userDTO.setSignature("这个人很懒，还没有设置签名╮(╯▽╰)╭");
        }
        return Response.ok(userDTO);
    }

    public Response login(String name, String password, HttpServletResponse httpServletResponse) {
        User user = userMapper.getByName(name);
        if (user == null) {
            return Response.failWithMsg("用户不存在");
        }
        String passwordHash = hasher.hash(password);
        if (!Objects.equals(passwordHash, user.getPasswordHash())) {
            return Response.failWithMsg("密码错误");
        }
        String token = jwtUtils.createToken(user);
        Cookie loginCookie = new Cookie("token", token);
        loginCookie.setMaxAge(7 * 24 * 60 * 60);
        loginCookie.setPath("/");
        httpServletResponse.addCookie(loginCookie);
        return Response.ok(user);
    }

    public Response logout(String name,
                           HttpServletRequest httpServletRequest,
                           HttpServletResponse httpServletResponse) {
        String token = jwtUtils.getToken(httpServletRequest);
        if (token == null) {
            return Response.fail();
        }
        User user = jwtUtils.parseUser(jwtUtils.decodeJwt(token));
        if (user == null) {
            return Response.fail();
        }
        Cookie cookie = new Cookie("token", null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        httpServletResponse.addCookie(cookie);
        return Response.ok();
    }

    boolean banned(User user) {
        if (user.getBannedUntil() == null) {
            return false;
        }
        LocalDateTime now = LocalDateTime.now();
        return !now.isAfter(user.getBannedUntil());
    }

    boolean banned(Integer uid) {
        User user;
        try {
            user = userMapper.get(uid);
        } catch (Exception e) {
            return false;
        }
        return banned(user);
    }

    private boolean updateNickname(User user) {
        try {
            articleMapper.updateNickname(user);
            commentMapper.updateNickname(user);
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}

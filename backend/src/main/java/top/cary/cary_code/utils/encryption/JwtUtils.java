package top.cary.cary_code.utils.encryption;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import top.cary.cary_code.entity.User;
import top.cary.cary_code.entity.unum.AuthorityEnum;

import java.util.*;

@Component
public class JwtUtils {
    private String privateKey;
    private Algorithm algorithm;

    public JwtUtils(@Value("${jwt.private-key}") String privateKey) {
        this.privateKey = privateKey;
        this.algorithm = Algorithm.HMAC256(privateKey);
    }

    public String createToken(User user) {
        return JWT.create()
                .withClaim("uid", user.getUid())
                .withClaim("passwordHash", user.getPasswordHash())
                .withClaim("authority", user.getAuthority().name())
                .withExpiresAt(expireTime())
                .withIssuedAt(new Date())
                .sign(algorithm);
    }

    public String getToken(HttpServletRequest httpServletRequest) {
        Cookie[] cookies = httpServletRequest.getCookies();
        if (cookies == null) {
            return null;
        }
        Optional<Cookie> optionalCookie = Arrays.stream(cookies)
                .filter(e -> Objects.equals(e.getName(), "token"))
                .findFirst();
        if (optionalCookie.isEmpty()) {
            return null;
        }
        return optionalCookie.get().getValue();
    }

    public DecodedJWT decodeJwt(String token) {
        if (token == null) {
            return null;
        }
        JWTVerifier jwtVerifier = JWT.require(algorithm).build();
        DecodedJWT decodedJWT;
        try {
            decodedJWT = jwtVerifier.verify(token);
            return new Date().after(decodedJWT.getExpiresAt()) ? null : decodedJWT;
        } catch (Exception e) {
            return null;
        }
    }

    public User parseUser(DecodedJWT decodedJWT) {
        return User.builder()
                .uid(decodedJWT.getClaim("uid").asInt())
                .passwordHash(decodedJWT.getClaim("passwordHash").asString())
                .authority(AuthorityEnum.valueOf(decodedJWT.getClaim("authority").asString()))
                .build();
    }

    Date expireTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR, 24);
        return calendar.getTime();
    }

}

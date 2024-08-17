package top.cary.cary_code.controller.aop;

import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import top.cary.cary_code.entity.User;
import top.cary.cary_code.utils.encryption.JwtUtils;
import top.cary.cary_code.utils.Response;

//@Component
//@Aspect
public class CheckerAspect {

//    @Autowired
    JwtUtils jwtUtils;

    @Around("within(top.cary.cary_code.controller.ForumUpdateController)")
    public Object checkForumUpdateControllerLogin(ProceedingJoinPoint joinPoint) throws Throwable {
        return checkLogin(joinPoint);
    }

    @Around("within(top.cary.cary_code.controller.ActionController)")
    public Object checkActionControllerLogin(ProceedingJoinPoint joinPoint) throws Throwable {
        return checkLogin(joinPoint);
    }

    public Object checkLogin(ProceedingJoinPoint joinPoint) throws Throwable {
        User user = getLoginUser(joinPoint);
        if (user == null) {
            return Response.failWithMsg("请先登录");
        }
        return joinPoint.proceed();
    }

    private User getLoginUser(JoinPoint joinPoint) {
        HttpServletRequest httpServletRequest = getHttpServletRequest(joinPoint);
        String token = jwtUtils.getToken(httpServletRequest);
        if (token == null) {
            return null;
        }
        DecodedJWT decodedJWT = jwtUtils.decodeJwt(token);
        if (decodedJWT == null) {
            return null;
        }
        return jwtUtils.parseUser(decodedJWT);
    }

    private HttpServletRequest getHttpServletRequest(JoinPoint joinPoint) {
        for (Object obj : joinPoint.getArgs()) {
            if (obj instanceof HttpServletRequest) {
                return (HttpServletRequest)obj;
            }
        }
        return null;
    }
}

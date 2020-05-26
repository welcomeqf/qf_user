package yq.jwt.Interceptor;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTDecodeException;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import yq.constanct.CodeType;
import yq.exception.ApplicationException;
import yq.jwt.JwtVerfy;
import yq.jwt.contain.LocalUser;
import yq.jwt.entity.PrivilegeMenuQuery;
import yq.jwt.entity.UserLoginQuery;
import yq.jwt.entity.token.AuthLoginVo;
import yq.jwt.islogin.CheckToken;
import yq.jwt.islogin.LoginToken;
import yq.utils.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Date;


/**
 * @Author qf
 * @Date 2019/9/24
 * @Version 1.0
 */
public class AuthenticationInterceptor implements HandlerInterceptor {

    @Autowired
    private LocalUser user;

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object object) throws Exception {

        // 从 http 请求头中取出 token
        String token1 = httpServletRequest.getHeader("Authorization");

        String token = null;
        if (token1 != null) {
            token = token1.substring(7);
        }
        // 如果不是映射到方法直接通过
        if (!(object instanceof HandlerMethod)) {
            return true;
        }

        HandlerMethod handlerMethod = (HandlerMethod) object;
        Method method = handlerMethod.getMethod();
        //检查是否有LoginToken注释，有则跳过认证
        if (method.isAnnotationPresent(LoginToken.class)) {
            LoginToken loginToken = method.getAnnotation(LoginToken.class);
            if (loginToken.required()) {
                return true;
            }
        }

        //检查有没有需要用户权限的注解
        if (method.isAnnotationPresent(CheckToken.class)) {
            CheckToken checkToken = method.getAnnotation(CheckToken.class);
            if (checkToken.required()) {
                // 执行认证
                if (StringUtils.isBlank(token)) {
                    throw new ApplicationException(CodeType.OVENDU_ERROR,"token 为空");
                }
                // 获取 token 中的 user信息
                AuthLoginVo query = new AuthLoginVo();
                try {
                    query.setId(JWT.decode(token).getClaim("id").asInt());
                    query.setAccessKey(JWT.decode(token).getClaim("accessKey").asString());
                    query.setAccessKeySecret(JWT.decode(token).getClaim("accessKeySecret").asString());
                    query.setCustomerCode(JWT.decode(token).getClaim("customerCode").asString());
                    query.setCustomerName(JWT.decode(token).getClaim("customerName").asString());
                    query.setStopped(JWT.decode(token).getClaim("stopped").asBoolean());

                } catch (JWTDecodeException j) {
                    throw new ApplicationException(CodeType.OVENDU_ERROR,"token错误");
                }
                user.setUser(query);

                //获得解密后claims对象
                Date date = new Date();
                Claims jwt = JwtParseUtil.parseJWT(token,query);

                String audience = jwt.getAudience();
                Long erp = Long.parseLong(audience);
                Date erpDate = new Date(erp);
                erpDate.before(date);
                //判断token时间是否过期
                if (erpDate.before(date)) {
                    throw new ApplicationException(CodeType.OVENDU_ERROR);
                }

                Boolean verify = JwtVerfy.isVerify(token, query);
                if (!verify) {
                    throw new ApplicationException(CodeType.OVENDU_ERROR, "身份验证失败");
                }
                return true;
            }
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}

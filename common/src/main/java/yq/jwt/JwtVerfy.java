package yq.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import yq.constanct.CodeType;
import yq.exception.ApplicationException;
import yq.jwt.entity.UserLoginQuery;
import yq.jwt.entity.token.AuthLoginVo;
import yq.utils.StringUtils;

/**
 * @Author qf
 * @Date 2019/9/25
 * @Version 1.0
 */
public class JwtVerfy {

    /**
     * 校验token
     * 在这里可以使用官方的校验，我这里校验的是token中携带的密码于数据库一致的话就校验通过
     * @param token
     * @param user
     * @return
     */
    public static Boolean isVerify(String token, AuthLoginVo user) {
        //签名秘钥，和生成的签名的秘钥一模一样
        String accessKeySecret = user.getAccessKeySecret();

        if (StringUtils.isBlank(accessKeySecret)) {
            throw new ApplicationException(CodeType.OVENDU_ERROR,"token有误，请重新登录");
        }

        //得到DefaultJwtParser
        Claims claims = Jwts.parser()
                //设置签名的秘钥
                .setSigningKey(accessKeySecret)
                //设置需要解析的jwt
                .parseClaimsJws(token).getBody();

        String key = "accessKeySecret";

        if (claims.get(key).equals(accessKeySecret)) {
            return true;
        }

        return false;
    }
}

package yq.jwt.Interceptor;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import yq.constanct.CodeType;
import yq.exception.ApplicationException;
import yq.jwt.entity.token.AuthLoginVo;
import yq.utils.StringUtils;

/**
 * @Author qf
 * @Date 2019/10/1
 * @Version 1.0
 */
public class JwtParseUtil {

    /**
     * Token的解密
     * @param token 加密后的token
     * @param user  用户的对象
     * @return
     */
    public static Claims parseJWT(String token, AuthLoginVo user) {
        //签名秘钥，和生成的签名的秘钥一模一样

        String accessKeySecret = user.getAccessKeySecret();

        if (StringUtils.isBlank(accessKeySecret)) {
            throw new ApplicationException(CodeType.OVENDU_ERROR,"token有误，请重新登录");
        }

        //得到DefaultJwtParser
        Claims claims;
        try {
            claims = Jwts.parser()
                    //设置签名的秘钥
                    .setSigningKey(accessKeySecret)
                    //设置需要解析的jwt
                    .parseClaimsJws(token).getBody();
        }catch (Exception e) {
            throw new ApplicationException(CodeType.OVENDU_ERROR,"token错误");
        }

        return claims;
    }
}

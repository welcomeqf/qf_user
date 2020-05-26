package yq.utils;


import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import yq.constanct.CodeType;
import yq.exception.ApplicationException;
import yq.jwt.entity.UserLoginQuery;
import yq.jwt.entity.token.AuthLoginVo;
import yq.jwt.entity.token.AuthTokenBo;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @Author qf
 * @Date 2019/9/24
 * @Version 1.0
 */
public class JwtUtil {

    /**
     * 用户登录成功后生成Jwt
     * 使用Hs256算法  私匙使用用户密码
     *
     * @param ttlMillis jwt过期时间
     * @param user      登录成功的user对象
     * @return
     */
    public static String createJWT(long ttlMillis, UserLoginQuery user) {
        //指定签名的时候使用的签名算法，也就是header那部分，jjwt已经将这部分内容封装好了。
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

        //生成JWT的时间
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);

        if (user == null) {
            throw new ApplicationException(CodeType.OVENDU_ERROR,"账号异常");
        }

        //创建payload的私有声明（根据特定的业务需要添加，如果要拿这个做验证，一般是需要和jwt的接收方提前沟通好验证方式的）
        Map<String, Object> claims = new HashMap<String, Object>();
        claims.put("id", user.getId());
        claims.put("userName", user.getAccount());
        claims.put("cname",user.getCname());
        claims.put("companyId",user.getCompanyId());
        claims.put("tel",user.getTel());
        claims.put("roleName",user.getRoleName());
        claims.put("menuList",user.getMenuList());
        claims.put("status",user.getStatus());
        claims.put("city",user.getCity());

        //生成签名的时候使用的秘钥secret,这个方法本地封装了的，一般可以从本地配置文件中读取，切记这个秘钥不能外露哦。它就是你服务端的私钥，在任何场景都不应该流露出去。一旦客户端得知这个secret, 那就意味着客户端是可以自我签发jwt了。
        String key = user.getId().toString();

        //生成签发人
        String subject = user.getAccount();



        //下面就是在为payload添加各种标准声明和私有声明了
        //这里其实就是new一个JwtBuilder，设置jwt的body
        JwtBuilder builder = Jwts.builder()
                //如果有私有声明，一定要先设置这个自己创建的私有的声明，这个是给builder的claim赋值，一旦写在标准的声明赋值之后，就是覆盖了那些标准的声明的
                .setClaims(claims)
                //设置jti(JWT ID)：是JWT的唯一标识，根据业务需要，这个可以设置为一个不重复的值，主要用来作为一次性token,从而回避重放攻击。
                .setId(UUID.randomUUID().toString())
                //iat: jwt的签发时间
                .setIssuedAt(now)
                //代表这个JWT的主体，即它的所有人，这个是一个json格式的字符串，可以存放什么userid，roldid之类的，作为什么用户的唯一标志。
                .setSubject(subject)
                //设置签名使用的签名算法和签名使用的秘钥
                .signWith(signatureAlgorithm, key);
        if (ttlMillis >= 0) {
            Long expMillis = nowMillis + ttlMillis;
            String exp = expMillis.toString();
            //设置过期时间
            builder.setAudience(exp);
        }

        return builder.compact();
    }


    /**
     * 生成用户模块的token验证
     * @param ttlMillis
     * @param user
     * @return
     */
    public static AuthTokenBo createAuthJWT(long ttlMillis, AuthLoginVo user) {
        //指定签名的时候使用的签名算法，也就是header那部分，jjwt已经将这部分内容封装好了。
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

        //生成JWT的时间
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);

        if (user == null) {
            throw new ApplicationException(CodeType.OVENDU_ERROR,"账号异常");
        }

        //创建payload的私有声明（根据特定的业务需要添加，如果要拿这个做验证，一般是需要和jwt的接收方提前沟通好验证方式的）
        Map<String, Object> claims = new HashMap<String, Object>();
        claims.put("id", user.getId());
        claims.put("accessKey", user.getAccessKey());
        claims.put("accessKeySecret", user.getAccessKeySecret());
        claims.put("customerCode",user.getCustomerCode());
        claims.put("customerName",user.getCustomerName());
        claims.put("stopped",user.getStopped());

        //生成签名的时候使用的秘钥secret,这个方法本地封装了的，一般可以从本地配置文件中读取，切记这个秘钥不能外露哦。它就是你服务端的私钥，在任何场景都不应该流露出去。一旦客户端得知这个secret, 那就意味着客户端是可以自我签发jwt了。
        String key = user.getAccessKeySecret();

        //生成签发人
        String subject = user.getAccessKey();



        //下面就是在为payload添加各种标准声明和私有声明了
        //这里其实就是new一个JwtBuilder，设置jwt的body
        JwtBuilder builder = Jwts.builder()
              //如果有私有声明，一定要先设置这个自己创建的私有的声明，这个是给builder的claim赋值，一旦写在标准的声明赋值之后，就是覆盖了那些标准的声明的
              .setClaims(claims)
              //设置jti(JWT ID)：是JWT的唯一标识，根据业务需要，这个可以设置为一个不重复的值，主要用来作为一次性token,从而回避重放攻击。
              .setId(UUID.randomUUID().toString())
              //iat: jwt的签发时间
              .setIssuedAt(now)
              //代表这个JWT的主体，即它的所有人，这个是一个json格式的字符串，可以存放什么userid，roldid之类的，作为什么用户的唯一标志。
              .setSubject(subject)
              //设置签名使用的签名算法和签名使用的秘钥
              .signWith(signatureAlgorithm, key);
        AuthTokenBo bo = new AuthTokenBo();
        if (ttlMillis >= 0) {
            Long expMillis = nowMillis + ttlMillis;
            String exp = expMillis.toString();
            //设置过期时间
            builder.setAudience(exp);
            bo.setExpTime(expMillis);
        }

        bo.setToken(builder.compact());
        return bo;
    }


}

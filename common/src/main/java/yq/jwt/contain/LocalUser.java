package yq.jwt.contain;



import org.springframework.stereotype.Component;
import yq.jwt.entity.token.AuthLoginVo;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author qf
 * @Date 2019/9/25
 * @Version 1.0
 */
@Component
public class LocalUser {


    private Map<String,Object> map = new ConcurrentHashMap<>();


    /**
     * 从token获得用户信息
     * @return
     */
    public void setUser (AuthLoginVo vo) {
        map.put("auth",vo);
    }


    /**
     * 得到用户信息
     * @param key
     * @return
     */
    public AuthLoginVo getUser (String key) {
        AuthLoginVo result = (AuthLoginVo) map.get(key);
        return result;
    }
}

package yq.jwt.entity.token;

import lombok.Data;

/**
 * @author qf
 * @date 2019/11/12
 * @vesion 1.0
 **/
@Data
public class AuthLoginVo {

   private Integer id;

   private String accessKey;

   private String accessKeySecret;

   private String customerCode;

   private String customerName;

   private Boolean stopped;
}

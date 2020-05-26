package com.eqlee.user.entity.vo;

import lombok.Data;

/**
 * @author qf
 * @date 2019/11/15
 * @vesion 1.0
 **/
@Data
public class AuthAddVo {

   private String customerName;

   private String accessKey;

   private String accessKeySecret;
}

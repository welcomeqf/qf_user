package com.eqlee.user.entity.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author qf
 * @date 2019/11/15
 * @vesion 1.0
 **/
@Data
public class AuthResultVo {

   private Integer id;

   private String accessKey;

   private String accessKeySecret;

   private String customerCode;

   private String customerName;

   private Boolean stopped;

   private String createTime;

   private Long createUserId;

   private String updateTime;

   private Long updateUserId;
}

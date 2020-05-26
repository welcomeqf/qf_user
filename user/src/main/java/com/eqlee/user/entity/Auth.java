package com.eqlee.user.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * @author qf
 * @date 2019/11/12
 * @vesion 1.0
 **/
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@TableName("AccessAuth")
public class Auth extends Model<Auth> {

   private Integer Id;

   private String AccessKey;

   private String AccessKeySecret;

   private String CustomerCode;

   private String CustomerName;

   private Boolean Stopped;

   private LocalDateTime CreateTime;

   private Long CreateUserId;

   private LocalDateTime UpdateTime;

   private Long UpdateUserId;
}

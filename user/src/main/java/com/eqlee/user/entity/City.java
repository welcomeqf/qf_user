package com.eqlee.user.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author qf
 * @date 2020/1/2
 * @vesion 1.0
 **/
@Data
@Accessors(chain = true)
@TableName("SystemCity")
@EqualsAndHashCode(callSuper = false)
public class City extends Model<City> {

   private Long Id;

   /**
    * 用户ID
    */
   private Long UserId;

   /**
    * 城市
    */
   private String City;
}

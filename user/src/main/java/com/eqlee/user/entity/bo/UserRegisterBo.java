package com.eqlee.user.entity.bo;

import lombok.Data;

import java.util.List;

/**
 * @author qf
 * @date 2020/1/8
 * @vesion 1.0
 **/
@Data
public class UserRegisterBo {


   private String userName;

   private String password;

   private String name;

   private String phone;

   private String roleName;

   private List<CityBo> city;

   private Long companyId;
}

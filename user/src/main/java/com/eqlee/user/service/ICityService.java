package com.eqlee.user.service;

import com.eqlee.user.entity.City;
import com.eqlee.user.entity.bo.CityBo;
import com.eqlee.user.entity.city.CityAddVo;
import yq.jwt.entity.CityJwtBo;

import java.util.List;

/**
 * @author qf
 * @date 2020/1/2
 * @vesion 1.0
 **/
public interface ICityService {

   /**
    * 添加城市
    * @param list
    */
   void addCity (List<CityAddVo> list);

   /**
    * 根据用户ID查询城市
    * @param userId
    * @return
    */
   List<CityJwtBo> queryCity (Long userId);

   /**
    * 修改城市
    * @param list
    */
   void upCity (List<CityAddVo> list);
}

package com.eqlee.user.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.eqlee.user.dao.CityMapper;
import com.eqlee.user.entity.City;
import com.eqlee.user.entity.bo.CityBo;
import com.eqlee.user.entity.city.CityAddVo;
import com.eqlee.user.service.ICityService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yq.constanct.CodeType;
import yq.exception.ApplicationException;
import yq.jwt.entity.CityJwtBo;
import yq.utils.IdGenerator;

import java.util.ArrayList;
import java.util.List;

/**
 * @author qf
 * @date 2020/1/2
 * @vesion 1.0
 **/
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class CityServiceImpl extends ServiceImpl<CityMapper, City> implements ICityService {

   @Autowired
   private IdGenerator idGenerator;


   @Override
   public void addCity(List<CityAddVo> list) {

      for (CityAddVo cityBo : list) {
         City city = new City();
         city.setId(idGenerator.getNumberId());
         city.setCity(cityBo.getCity());
         city.setUserId(cityBo.getUserId());
         int insert = baseMapper.insert(city);

         if (insert <= 0) {
            throw new ApplicationException(CodeType.SERVICE_ERROR, "添加城市失败");
         }
      }
   }


   /**
    * 根据用户ID查询城市
    * @param userId
    * @return
    */
   @Override
   public List<CityJwtBo> queryCity(Long userId) {
      LambdaQueryWrapper<City> wrapper = new LambdaQueryWrapper<City>()
            .eq(City::getUserId,userId);
      List<City> cityList = baseMapper.selectList(wrapper);
      List<CityJwtBo> list = new ArrayList<>();
      for (City city : cityList) {
         CityJwtBo bo = new CityJwtBo();
         bo.setCity(city.getCity());
         list.add(bo);
      }
      return list;
   }


   /**
    * 修改城市
    * @param list
    */
   @Override
   public void upCity(List<CityAddVo> list) {
      Long id = null;
      for (CityAddVo vo : list) {
         id = vo.getUserId();
         break;
      }

      LambdaQueryWrapper<City> wrapper = new LambdaQueryWrapper<City>()
            .eq(City::getUserId,id);
      baseMapper.delete(wrapper);

      //添加
      for (CityAddVo cityBo : list) {
         City city = new City();
         city.setId(idGenerator.getNumberId());
         city.setCity(cityBo.getCity());
         city.setUserId(cityBo.getUserId());
         int insert = baseMapper.insert(city);

         if (insert <= 0) {
            throw new ApplicationException(CodeType.SERVICE_ERROR, "添加城市失败");
         }
      }
   }
}

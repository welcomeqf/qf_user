package com.eqlee.user.dao;

import com.eqlee.user.entity.Auth;
import com.eqlee.user.entity.vo.AuthAddVo;
import com.eqlee.user.entity.vo.AuthResultVo;
import org.springframework.stereotype.Component;
import yq.IBaseMapper.IBaseMapper;

import java.util.List;

/**
 * @author qf
 * @date 2019/11/12
 * @vesion 1.0
 **/
@Component
public interface AuthMapper extends IBaseMapper<Auth> {

   /**
    * 查询所有设备
    * @return
    */
   List<AuthResultVo> listAllAuth ();

   /**
    * 增加
    * @param vo
    * @return
    */
   Integer insertAuth (AuthAddVo vo);

   /**
    * 查询个人信息
    * @param key
    * @return
    */
   AuthResultVo queryAuthInfo (String key);
}

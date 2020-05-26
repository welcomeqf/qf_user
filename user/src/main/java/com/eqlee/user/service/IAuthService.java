package com.eqlee.user.service;

import com.eqlee.user.entity.Auth;
import com.eqlee.user.entity.vo.AuthLoginParamVo;
import com.eqlee.user.entity.vo.AuthResultVo;
import com.eqlee.user.entity.vo.AuthVo;
import yq.jwt.entity.token.AuthLoginVo;

import java.util.List;

/**
 * @author qf
 * @date 2019/11/12
 * @vesion 1.0
 **/
public interface IAuthService {

   /**
    * 增加设备
    * @param vo
    */
   AuthLoginParamVo insertAuth (AuthVo vo);

   /**
    * 查询所有设备
    * @return
    */
   List<AuthResultVo> queryAuth ();

   /**
    * 登录设备
    * @param paramVo
    * @return
    */
   AuthLoginVo loginAuth (AuthLoginParamVo paramVo);
}

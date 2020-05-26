package com.eqlee.user.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.eqlee.user.dao.AuthMapper;
import com.eqlee.user.entity.Auth;
import com.eqlee.user.entity.vo.AuthAddVo;
import com.eqlee.user.entity.vo.AuthLoginParamVo;
import com.eqlee.user.entity.vo.AuthResultVo;
import com.eqlee.user.entity.vo.AuthVo;
import com.eqlee.user.service.IAuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yq.constanct.CodeType;
import yq.exception.ApplicationException;
import yq.jwt.entity.token.AuthLoginVo;
import yq.utils.DateUtil;
import yq.utils.IdGenerator;

import java.util.ArrayList;
import java.util.List;

/**
 * @author qf
 * @date 2019/11/12
 * @vesion 1.0
 **/
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class AuthServiceImpl extends ServiceImpl<AuthMapper, Auth> implements IAuthService {

   @Autowired
   private IdGenerator idGenerator;

   /**
    * 增加设备
    * @param vo
    */
   @Override
   public AuthLoginParamVo insertAuth(AuthVo vo) {

      AuthAddVo auth = new AuthAddVo();
      String key = idGenerator.getOrderCode();
      String secret = idGenerator.getUuid();
      auth.setAccessKey(key);
      auth.setAccessKeySecret(secret);
      auth.setCustomerName(vo.getCustomerName());
      int insert = baseMapper.insertAuth(auth);

      if (insert <= 0) {
         throw new ApplicationException(CodeType.SERVICE_ERROR, "增加设备失败");
      }

      AuthLoginParamVo result = new AuthLoginParamVo();
      result.setAccessKey(key);
      result.setAccessKeySecret(secret);

      return result;

   }


   /**
    * 查询所有设备
    * @return
    */
   @Override
   public List<AuthResultVo> queryAuth() {
      return baseMapper.listAllAuth ();
   }

   /**
    * 登录设备返回设备信息
    * @param paramVo
    * @return
    */
   @Override
   public AuthLoginVo loginAuth(AuthLoginParamVo paramVo) {
      LambdaQueryWrapper<Auth> wrapper = new LambdaQueryWrapper<Auth>()
            .eq(Auth::getAccessKey,paramVo.getAccessKey())
            .eq(Auth::getAccessKeySecret,paramVo.getAccessKeySecret());

      Auth auth = baseMapper.selectOne(wrapper);

      if (auth == null) {
         throw new ApplicationException(CodeType.SERVICE_ERROR, "key或密钥错误");
      }

      if (auth.getStopped()) {
         throw new ApplicationException(CodeType.SERVICE_ERROR, "该账号已停用");
      }

      //装配AuthLoginVo
      AuthLoginVo result = new AuthLoginVo();
      result.setAccessKey(auth.getAccessKey());
      result.setAccessKeySecret(auth.getAccessKeySecret());
      result.setCustomerCode(auth.getCustomerCode());
      result.setCustomerName(auth.getCustomerName());
      result.setId(auth.getId());
      result.setStopped(auth.getStopped());

      return result;
   }
}

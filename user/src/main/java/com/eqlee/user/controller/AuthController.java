package com.eqlee.user.controller;

import com.eqlee.user.entity.Auth;
import com.eqlee.user.entity.query.AuthResult;
import com.eqlee.user.entity.vo.AuthLoginParamVo;
import com.eqlee.user.entity.vo.AuthResultVo;
import com.eqlee.user.entity.vo.AuthVo;
import com.eqlee.user.entity.vo.ResultVo;
import com.eqlee.user.service.IAuthService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import yq.constanct.CodeType;
import yq.exception.ApplicationException;
import yq.jwt.entity.token.AuthLoginVo;
import yq.jwt.entity.token.AuthTokenBo;
import yq.jwt.islogin.CheckToken;
import yq.utils.JwtUtil;
import yq.utils.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author qf
 * @date 2019/11/15
 * @vesion 1.0
 **/
@Slf4j
@Api("AuthApi")
@RestController
@RequestMapping("/v1/auth")
public class AuthController {

   @Autowired
   private IAuthService authService;

   @ApiOperation(value = "增加设备", notes = "增加设备")
   @ApiImplicitParam(name = "customerName", value = "设备名称", required = true, dataType = "String", paramType = "path")
   @PostMapping("/insertAuth")
   @CrossOrigin
   @CheckToken
   public AuthLoginParamVo insertAuth (@RequestBody AuthVo vo) {

      if (StringUtils.isBlank(vo.getCustomerName())) {
         throw new ApplicationException(CodeType.PARAM_ERROR, "参数不能为空");
      }
      return authService.insertAuth(vo);
   }


   @ApiOperation(value = "查询所有设备", notes = "查询所有设备")
   @GetMapping("/queryAuth")
   @CrossOrigin
   @CheckToken
   public List<AuthResultVo> queryAuth () {
      return authService.queryAuth();
   }


   @ApiOperation(value = "获得token", notes = "获得token")
   @ApiImplicitParams({
         @ApiImplicitParam(name = "accessKey", value = "key", required = true, dataType = "String", paramType = "path"),
         @ApiImplicitParam(name = "accessKeySecret", value = "密钥", required = true, dataType = "String", paramType = "path")
   })
   @PostMapping("/loginAuth")
   @CrossOrigin
   public AuthTokenBo loginAuth (@RequestBody AuthLoginParamVo vo) {

      if (StringUtils.isBlank(vo.getAccessKey()) || StringUtils.isBlank(vo.getAccessKeySecret())) {
         throw new ApplicationException(CodeType.PARAM_ERROR, "参数不能为空");
      }
      AuthLoginVo authLoginVo = authService.loginAuth(vo);

      //生成token
      return JwtUtil.createAuthJWT(86400000L, authLoginVo);
   }
}

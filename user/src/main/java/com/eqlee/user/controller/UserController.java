package com.eqlee.user.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.eqlee.user.entity.bo.ParamUserBo;
import com.eqlee.user.entity.bo.UserAdminBo;
import com.eqlee.user.entity.bo.UserBo;
import com.eqlee.user.entity.bo.UserInfoBo;
import com.eqlee.user.entity.query.*;
import com.eqlee.user.entity.vo.ResultVo;
import com.eqlee.user.entity.vo.UserUpdatePasswordVo;
import com.eqlee.user.entity.vo.UserUpdateVo;
import com.eqlee.user.entity.vo.UserVo;
import com.eqlee.user.service.IUserService;
import com.eqlee.user.vilidata.DataUtils;
import com.eqlee.user.vilidata.SignData;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import yq.constanct.CodeType;
import yq.exception.ApplicationException;
import yq.jwt.entity.UserLoginQuery;
import yq.jwt.islogin.CheckToken;
import yq.utils.JwtUtil;
import yq.utils.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @Author qf
 * @Date 2019/9/12
 * @Version 1.0
 */
@Slf4j
@Api
@RestController
@RequestMapping("/v1/app/user")
public class UserController {

    @Autowired
    private IUserService userService;

    @ApiOperation(value = "注册", notes = "注册")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userName", value = "用户名", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "password", value = "密码", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "name", value = "名称", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "phone", value = "电话", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "roleName", value = "角色名", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "companyId", value = "公司Id", required = true, dataType = "Long", paramType = "path")
    })
    @PostMapping("/register")
    @CrossOrigin
    @CheckToken
    public ResultVo register(@RequestBody UserVo userVo) {
        if (StringUtils.isBlank(userVo.getUserName()) || StringUtils.isBlank(userVo.getPassword())
        || StringUtils.isBlank(userVo.getName()) || StringUtils.isBlank(userVo.getPhone()) ||
                userVo.getRoleName() == null || userVo.getCompanyId() == null) {
            throw new ApplicationException(CodeType.PARAM_ERROR);
        }

        userService.register(userVo);

        ResultVo resultVo = new ResultVo();
        resultVo.setResult("ok");
        return resultVo;
    }

    @ApiOperation(value = "登录", notes ="登录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userName", value = "用户名", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "password", value = "密码", required = true, dataType = "String", paramType = "path")
    })
    @GetMapping("/login")
    @CrossOrigin
    @CheckToken
    public Map<String,Object> login(@RequestParam("userName") String userName, @RequestParam("password") String password) throws Exception{
        if (StringUtils.isBlank(userName) || StringUtils.isBlank(password)) {
            throw new ApplicationException(CodeType.PARAM_ERROR);
        }

        LoginQuery query = userService.login(userName, password);

        UserLoginQuery loginQuery = new UserLoginQuery();
        loginQuery.setId(query.getId());
        loginQuery.setTel(query.getTel());
        loginQuery.setRoleName(query.getRoleName());
        loginQuery.setCompanyId(query.getCompanyId());
        loginQuery.setAccount(query.getAccount());
        loginQuery.setCname(query.getCname());
        loginQuery.setCity(query.getCity());
        loginQuery.setMenuList(query.getMenuList());
        loginQuery.setStatus(query.getStatus());

        //JWT验证
        Map<String,Object> map = new HashMap<>();

        //2592000000
        String token = JwtUtil.createJWT(86400000L, loginQuery);
        map.put("token", token);
        map.put("user", loginQuery);

        return map;

    }

    @ApiOperation(value = "注销", notes = "注销")
    @ApiImplicitParam(name = "userName", value = "用户名", required = true, dataType = "String", paramType = "path")
    @GetMapping("/deleteUser/{id}")
    @CrossOrigin
    @CheckToken
    public ResultVo deleteUser(@PathVariable("id") Long id) {
        if (id == null) {
            throw new ApplicationException(CodeType.PARAM_ERROR);
        }
        userService.deleteUser(id);

        ResultVo resultVo = new ResultVo();
        resultVo.setResult("ok");

        return resultVo;
    }

    @ApiOperation(value = "退出账号", notes = "退出账号")
    @ApiImplicitParam(name = "userName", value = "用户名", required = true, dataType = "String", paramType = "path")
    @GetMapping("/exitUser/{userName}")
    @CrossOrigin
    @CheckToken
    public ResultVo exitUser(@PathVariable("userName") String userName) {
        if (StringUtils.isBlank(userName)) {
            throw new ApplicationException(CodeType.PARAM_ERROR);
        }
        userService.exitUser(userName);

        ResultVo resultVo = new ResultVo();
        resultVo.setResult("ok");

        return resultVo;
    }




    @ApiOperation(value = "子用户注册", notes = "子用户注册")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userName", value = "用户名", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "password", value = "密码", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "name", value = "名称", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "phone", value = "电话", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "roleName", value = "角色名字", required = true, dataType = "String", paramType = "path")
    })
    @PostMapping("/downRegister")
    @CrossOrigin
    @CheckToken
    public ResultVo downRegister(@RequestBody UserZiQuery userVo) {
        if (StringUtils.isBlank(userVo.getUserName()) || StringUtils.isBlank(userVo.getPassword())
              || StringUtils.isBlank(userVo.getName()) || StringUtils.isBlank(userVo.getPhone())
              || userVo.getCompanyId() == null) {
            log.error("param is not null.");
            throw new ApplicationException(CodeType.PARAM_ERROR,"参数不能为空");
        }
        userService.dowmRegister(userVo);

        ResultVo resultVo = new ResultVo();
        resultVo.setResult("ok");
        return resultVo;
    }


    /**
     * 该接口作为用户模块以及子用户设置的共同接口
     * @param current
     * @param size
     * @param userName
     * @param roleName
     * @return
     */
    @ApiOperation(value = "对用户名模糊以及角色帅选查询分页用户信息", notes = "对用户名模糊以及角色帅选查询分页用户信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "current", value = "当前页", required = true, dataType = "int", paramType = "path"),
            @ApiImplicitParam(name = "size", value = "每页显示条数", required = true, dataType = "int", paramType = "path"),
            @ApiImplicitParam(name = "userName", value = "用户名或用户名一部分", required = false, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "roleName", value = "角色名", required = false, dataType = "String", paramType = "path")
    })
    @GetMapping("/queryPageUserByName")
    @CrossOrigin
    @CheckToken
    public Page<UserQuery> queryPageUserByName(@RequestParam("current") Integer current,
                                               @RequestParam("size") Integer size,
                                               @RequestParam("userName") String userName,
                                               @RequestParam("roleName") String roleName,
                                               @RequestParam("companyId") Long companyId,
                                               @RequestParam("type") Integer type) {
        if (current == null || size == null) {
            throw new ApplicationException(CodeType.PARAM_ERROR,"分页查询用户参数不能为空");
        }
        Page<UserQuery> page = new Page<>(current,size);
        return userService.queryPageUserByName(page,userName,roleName,companyId,type);

    }

    @ApiOperation(value = "对用户名或角色模糊查询加分页查询", notes = "对用户名或角色模糊查询加分页查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "current", value = "当前页", required = true, dataType = "int", paramType = "path"),
            @ApiImplicitParam(name = "size", value = "每页显示条数", required = true, dataType = "int", paramType = "path"),
            @ApiImplicitParam(name = "userNameOrRole", value = "用户名或角色", required = false, dataType = "String", paramType = "path")
    })
    @GetMapping("/queryUserByNameOrRole")
    @CrossOrigin
    @CheckToken
    public Page<User2Query> queryUserByName(@RequestParam("current") Integer current,
                                            @RequestParam("size") Integer size,
                                            @RequestParam("userNameOrRole") String userNameOrRole) {
        if (current == null || size == null) {
            throw new ApplicationException(CodeType.PARAM_ERROR,"模糊加分页查询用户参数不能为空");
        }
        Page<User2Query> page = new Page<>(current,size);
        return userService.queryUserByName(page,userNameOrRole);

    }

    @ApiOperation(value = "根据用户名和手机号重置密码", notes = "根据用户名和手机号重置密码")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "id", required = true, dataType = "Long", paramType = "path"),
            @ApiImplicitParam(name = "userName", value = "用户名", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "password", value = "新密码", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "oldPassword", value = "旧密码", required = true, dataType = "String", paramType = "path")
    })
    @PostMapping("/updateUserPassword")
    @CrossOrigin
    @CheckToken
    public ResultVo updateUserPassword(@RequestBody UserUpdatePasswordVo vo) {
        if (vo.getId() == null || StringUtils.isBlank(vo.getUserName()) || StringUtils.isBlank(vo.getOldPassword()) ||
            StringUtils.isBlank(vo.getPassword())) {
            throw new ApplicationException(CodeType.PARAM_ERROR, "参数不能为空");
        }
        userService.updateUserPassword(vo);
        ResultVo resultVo = new ResultVo();
        resultVo.setResult("ok");
        return resultVo;
    }

    @ApiOperation(value = "修改用户信息", notes = "修改用户信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "id", required = true, dataType = "Long", paramType = "path"),
            @ApiImplicitParam(name = "cname", value = "姓名", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "tel", value = "手机号", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "newPassword", value = "新密码", required = false, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "stopped", value = "是否停用(false--正常  true--停用)", required = true, dataType = "Boolean", paramType = "path")
    })
    @PostMapping("/updateUser")
    @CrossOrigin
    @CheckToken
    public ResultVo updateUser(@RequestBody UserUpdateVo vo) {
        if (vo.getId() == null || StringUtils.isBlank(vo.getCname()) || StringUtils.isBlank(vo.getTel())) {
            throw new ApplicationException(CodeType.PARAM_ERROR, "参数不能为空");
        }
        userService.updateUser(vo);
        ResultVo resultVo = new ResultVo();
        resultVo.setResult("ok");
        return resultVo;
    }


    @ApiOperation(value = "根据Id查询用户", notes = "根据Id查询用户")
    @ApiImplicitParam(name = "id", value = "id", required = true, dataType = "Long", paramType = "path")
    @GetMapping("/getUserById")
    @CrossOrigin
    @CheckToken
    public UserByIdQuery getUserById (@RequestParam("id") Long id) {
        if (id == null) {
            throw new ApplicationException(CodeType.PARAM_ERROR,"参数不能为空");
        }
        return userService.getUserById(id);
    }


    @ApiOperation(value = "查询ctm运营人员的所有用户的id集合", notes = "查询ctm运营人员的所有用户的id集合")
    @GetMapping("/queryAllAdminId")
    @CrossOrigin
    @CheckToken
    public List<UserAdminBo> queryAllAdminId () {
        return userService.queryAllAdmin("运营人员");
    }

    @ApiOperation(value = "根据公司删除所有用户和角色", notes = "根据公司删除所有用户和角色")
    @GetMapping("/deleteAllUserRole")
    @CrossOrigin
    @CheckToken
    public ResultVo deleteAllUserRole (@RequestParam("id") Long id) {
        userService.deleteAllUser(id);

        ResultVo vo = new ResultVo();
        vo.setResult("OK");
        return vo;
    }

    @ApiOperation(value = "查询ctm财务下的所有用户的id集合", notes = "查询ctm财务下的所有用户的id集合")
    @GetMapping("/queryAllCaiId")
    @CrossOrigin
    @CheckToken
    public List<UserAdminBo> queryAllCaiId () {
        return userService.queryAllAdmin("财务");
    }



    @ApiOperation(value = "查询当前城市的导游", notes = "查询当前城市的导游")
    @PostMapping("/queryLocalCityUser")
    @CrossOrigin
    @CheckToken
    public List<UserBo> queryLocalCityUser (@RequestBody ParamUserBo bo) {
        return userService.queryLocalCityUser(bo.getList());
    }


    @ApiOperation(value = "查询用户信息", notes = "查询用户信息")
    @GetMapping("/queryCompanyUserInfo")
    @CrossOrigin
    @CheckToken
    public UserInfoBo queryCompanyUserInfo (@RequestParam("companyId") Long companyId) {

        if (companyId == null) {
            throw new ApplicationException(CodeType.PARAM_ERROR);
        }

        return userService.queryCompanyUserInfo(companyId);
    }

    @ApiOperation(value = "查询所有角色下的用户", notes = "查询所有角色下的用户")
    @GetMapping("/queryUserByRole")
    @CrossOrigin
    @CheckToken
    public List<UserQuery> queryUserByRole (@RequestParam("roleName") String roleName) {

        if (StringUtils.isBlank(roleName)) {
            throw new ApplicationException(CodeType.PARAM_ERROR);
        }

        return userService.queryUserByRole(roleName);
    }


}

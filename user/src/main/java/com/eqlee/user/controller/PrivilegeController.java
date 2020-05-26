package com.eqlee.user.controller;

import com.eqlee.user.entity.query.PrivilegeQuery;
import com.eqlee.user.entity.vo.ResultVo;
import com.eqlee.user.service.IPrivilegeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import yq.constanct.CodeType;
import yq.exception.ApplicationException;
import yq.jwt.islogin.CheckToken;


/**
 * @Author qf
 * @Date 2019/9/12
 * @Version 1.0
 */
@Api
@RestController
@Slf4j
@RequestMapping("v1/app/privilege")
public class PrivilegeController {

    @Autowired
    private IPrivilegeService privilegeService;

    @ApiOperation(value = "增加权限", notes = "增加权限")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roleId", value = "角色Id", required = true, dataType = "Long", paramType = "path"),
            @ApiImplicitParam(name = "menuList", value = "所有菜单集合", required = true, dataType = "List", paramType = "path")
    })
    @PostMapping("/insertPrivilege")
    @CrossOrigin
    @CheckToken
    public ResultVo insertPrivilege(@RequestBody PrivilegeQuery query) {
        if (query.getRoleId() == null || query.getMenuList().size() == 0) {
            log.error("param is not null.");
            throw new ApplicationException(CodeType.PARAM_ERROR);
        }

        privilegeService.insertAllPrivilege(query.getRoleId(),query.getMenuList());
        ResultVo resultVo = new ResultVo();
        resultVo.setResult("ok");
        return resultVo;
    }
}

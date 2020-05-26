package com.eqlee.user.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.eqlee.user.entity.UserRole;
import com.eqlee.user.entity.bo.RoleAddVo;
import com.eqlee.user.entity.query.RoleAddQuery;
import com.eqlee.user.entity.vo.ResultVo;
import com.eqlee.user.entity.vo.RoleUpdateVo;
import com.eqlee.user.entity.vo.RoleVo;
import com.eqlee.user.service.IRoleService;
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
import yq.utils.StringUtils;

import java.util.List;

/**
 * @Author qf
 * @Date 2019/9/10
 * @Version 1.0
 */
@RestController
@Slf4j
@Api
@RequestMapping("/v1/app/role")
public class RoleController {

    @Autowired
    private IRoleService roleService;


    @ApiOperation(value = "增加角色", notes = "增加角色")
    @ApiImplicitParam(name = "roleName", value = "角色名称", required = true, dataType = "String", paramType = "path")
    @PostMapping("/addRole")
    @CrossOrigin
    @CheckToken
    public ResultVo addRole(@RequestBody RoleAddVo roleVo) {
        if (StringUtils.isBlank(roleVo.getRoleName())) {
            throw new ApplicationException(CodeType.PARAM_ERROR);
        }

        roleService.addRole(roleVo);
        ResultVo resultVo = new ResultVo();
        resultVo.setResult("ok");
        return resultVo;
    }

    @ApiOperation(value = "增加子角色", notes = "增加子角色")
    @ApiImplicitParam(name = "roleName", value = "角色名称", required = true, dataType = "String", paramType = "path")
    @PostMapping("/addZiRole")
    @CrossOrigin
    @CheckToken
    public ResultVo addZiRole(@RequestBody RoleAddQuery roleVo) {
        if (StringUtils.isBlank(roleVo.getRoleName())) {
            throw new ApplicationException(CodeType.PARAM_ERROR);
        }
        roleService.addZiRole(roleVo);
        ResultVo resultVo = new ResultVo();
        resultVo.setResult("ok");
        return resultVo;
    }

    @ApiOperation(value = "删除角色", notes = "删除角色")
    @ApiImplicitParam(name = "id", value = "id", required = true, dataType = "int", paramType = "path")
    @GetMapping("/{id}")
    @CrossOrigin
    @CheckToken
    public ResultVo deleteRole(@PathVariable("id") Long id) {
        if (id == null) {
            throw new ApplicationException(CodeType.PARAM_ERROR);
        }
        roleService.deleteRole(id);
        ResultVo resultVo = new ResultVo();
        resultVo.setResult("ok");
        return resultVo;
    }

    @ApiOperation(value = "修改角色", notes = "修改角色")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "id", required = true, dataType = "int", paramType = "path"),
            @ApiImplicitParam(name = "roleName", value = "角色名", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "stopped", value = "是否停用", required = true, dataType = "Boolean", paramType = "path")
    })
    @PostMapping("/updateRole")
    @CrossOrigin
    @CheckToken
    public ResultVo updateRole(@RequestBody RoleUpdateVo vo) {
        if (vo.getId() == null || StringUtils.isBlank(vo.getRoleName())) {
            throw new ApplicationException(CodeType.PARAM_ERROR);
        }

        UserRole role = new UserRole();
        role.setId(vo.getId());
        role.setRoleName(vo.getRoleName());
        role.setStopped(vo.getStopped());
        roleService.updateRole(role);
        ResultVo resultVo = new ResultVo();
        resultVo.setResult("ok");
        return resultVo;
    }

    @ApiOperation(value = "查询角色", notes = "查询角色")
    @GetMapping("/RoleInfo")
    @CrossOrigin
    @CheckToken
    public List<UserRole> getRole() {
        return roleService.queryAllRole();
    }


    @ApiOperation(value = "分页查询所有角色", notes = "分页查询所有角色")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "current", value = "当前页", required = true, dataType = "int", paramType = "path"),
            @ApiImplicitParam(name = "size", value = "每页显示的条数", required = true, dataType = "int", paramType = "path")
    })
    @GetMapping("/queryPageRole")
    @CrossOrigin
    @CheckToken
    public Page<UserRole> queryPageRole(@RequestParam("current") Integer current,
                                        @RequestParam("size") Integer size) {
        if (current == null || size == null) {
            throw new ApplicationException(CodeType.PARAM_ERROR,"分页查询角色参数不能为空");
        }
        RoleVo roleVo = new RoleVo();
        roleVo.setCurrent(current);
        roleVo.setSize(size);
        return roleService.queryPageRole(roleVo);
    }


    @ApiOperation(value = "分页查询所有子角色", notes = "分页查询所有子角色")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "current", value = "当前页", required = true, dataType = "int", paramType = "path"),
            @ApiImplicitParam(name = "size", value = "每页显示的条数", required = true, dataType = "int", paramType = "path"),
            @ApiImplicitParam(name = "companyId", value = "公司id", required = true, dataType = "Long", paramType = "path")
    })
    @GetMapping("/queryZiPageRole")
    @CrossOrigin
    @CheckToken
    public Page<UserRole> queryZiPageRole(@RequestParam("current") Integer current,
                                        @RequestParam("size") Integer size,
                                          @RequestParam("companyId") Long companyId) {
        if (current == null || size == null) {
            throw new ApplicationException(CodeType.PARAM_ERROR,"分页查询子角色参数不能为空");
        }
        RoleVo roleVo = new RoleVo();
        roleVo.setCurrent(current);
        roleVo.setSize(size);
        roleVo.setCompanyId(companyId);
        return roleService.queryZiPageRole(roleVo);
    }
}

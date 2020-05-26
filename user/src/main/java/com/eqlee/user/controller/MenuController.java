package com.eqlee.user.controller;

import com.eqlee.user.entity.query.UserMenuQuery;
import com.eqlee.user.entity.query.WithQuery;
import com.eqlee.user.entity.vo.MenuVo;
import com.eqlee.user.entity.vo.MenuZiVo;
import com.eqlee.user.entity.vo.ResultVo;
import com.eqlee.user.service.IMenuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import yq.jwt.islogin.CheckToken;

import java.util.List;

/**
 * @Author qf
 * @Date 2019/9/11
 * @Version 1.0
 */
@Slf4j
@Api
@RestController
@RequestMapping("/v1/app/menu")
public class MenuController {

    @Autowired
    private IMenuService menuService;


    @PostMapping("/insert")
    public ResultVo insertMenu () {
        MenuVo vo = new MenuVo();
        vo.setMenuName("订单查询");
        vo.setParent(663684439355854848L);
        vo.setAction("/roadListQuery");
        vo.setIconClass("/static/img/icon_statistics.png");
        menuService.addMenu (vo);
        ResultVo vo1 = new ResultVo();
        vo1.setResult("OK");
        return vo1;
    }



    @ApiOperation(value = "查询所有权限", notes = "查询所有权限")
    @ApiImplicitParam(name = "Id", value = "Id", required = false, dataType = "Long", paramType = "path")
    @GetMapping("/queryMenu")
    @CrossOrigin
    @CheckToken
    public List<UserMenuQuery> queryMenu(@RequestParam("Id") Long Id) {
        return menuService.queryAllMenu(Id);
    }


    @ApiOperation(value = "查看权限", notes = "查看权限")
    @ApiImplicitParam(name = "roleId", value = "角色id", required = true, dataType = "Long", paramType = "path")
    @GetMapping("/queryAll")
    @CrossOrigin
    @CheckToken
    public List<WithQuery> queryAll (@RequestParam("roleId") Long roleId) {

        return menuService.queryAll(roleId);
    }

    @ApiOperation(value = "查看所有父权限", notes = "查看所有父权限")
    @GetMapping("/queryAllParent")
    @CrossOrigin
    @CheckToken
    public List<WithQuery> queryAllParent () {
        return menuService.queryAllParent();
    }

    @ApiOperation(value = "查看所有子角色权限", notes = "查看所有子角色权限")
    @ApiImplicitParam(name = "roleId", value = "角色id", required = true, dataType = "Long", paramType = "path")
    @PostMapping("/queryZiAll")
    @CrossOrigin
    @CheckToken
    public List<WithQuery> queryAll (@RequestBody MenuZiVo vo) {

        return menuService.queryZiAll(vo.getRoleId(),vo.getList());
    }
}

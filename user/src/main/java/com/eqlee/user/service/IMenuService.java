package com.eqlee.user.service;


import com.eqlee.user.entity.UserMenu;
import com.eqlee.user.entity.query.UserMenuQuery;
import com.eqlee.user.entity.query.UserPrivilegeQuery;
import com.eqlee.user.entity.query.WithQuery;
import com.eqlee.user.entity.vo.MenuUpdateVo;
import com.eqlee.user.entity.vo.MenuVo;

import java.util.List;

/**
 * @Author qf
 * @Date 2019/9/11
 * @Version 1.0
 */
public interface IMenuService {

    /**
     * 增加菜单
     * @param vo
     */
    void addMenu(MenuVo vo);

    /**
     * 查询所有菜单
     * @param Id
     * @return
     */
    List<UserMenuQuery> queryAllMenu(Long Id);

    /**
     * 根据菜单名查询菜单信息
     * @param MenuName
     * @return
     */
    UserMenu queryOne(String MenuName);

    /**
     * 根据ID查询菜单信息
     * @param Id
     * @return
     */
    UserMenu queryMenuById(Long Id);

    /**
     * 修改菜单 增加链接地址以及图标
     * @param vo
     */
    void updateMenu(MenuUpdateVo vo);

    /**
     *  查看所有
     * @param roleId
     * @return
     */
    List<WithQuery> queryAll(Long roleId);


    /**
     *  查看所有子菜单
     * @param roleId
     * @return
     */
    List<WithQuery> queryZiAll(Long roleId, List<UserPrivilegeQuery> list);

    /**
     * 修改菜单状态
     * @param id
     * @param start
     */
    void updateStart(Long id, Boolean start);

    /**
     *  查看所有
     * @return
     */
    List<WithQuery> queryAllParent();



}

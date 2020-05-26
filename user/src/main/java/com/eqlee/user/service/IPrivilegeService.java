package com.eqlee.user.service;

import com.eqlee.user.entity.UserPrivilege;
import com.eqlee.user.entity.query.PrivilegeWithQuery;
import yq.jwt.entity.PrivilegeMenuQuery;

import java.util.List;

/**
 * @Author qf
 * @Date 2019/9/11
 * @Version 1.0
 */
public interface IPrivilegeService {

    /**
     * 查询所有
     * @return
     */
    List<UserPrivilege> queryAll();

    /**
     * 一次性增加一个角色的所有权限
     * @param roleId
     * @param menuList
     */
    void insertAllPrivilege(Long roleId, List<PrivilegeWithQuery> menuList);

    /**
     * 根据角色查询所有菜单权限
     * @param roleId
     * @param authId
     * @return
     */
    List<PrivilegeMenuQuery> queryAllMenu(Long roleId, Integer authId);




}

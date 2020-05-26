package com.eqlee.user.service;



import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.eqlee.user.entity.UserRole;
import com.eqlee.user.entity.bo.RoleAddVo;
import com.eqlee.user.entity.query.RoleAddQuery;
import com.eqlee.user.entity.vo.RoleVo;

import java.util.List;

/**
 * @Author qf
 * @Date 2019/9/10
 * @Version 1.0
 */
public interface IRoleService {

    /**
     *    查询角色
     * @param id
     * @return
     */
    UserRole queryById (Long id);

    /**
     * 增加角色
     * @param role
     */
    void addRole(RoleAddVo role);


    /**
     * 增加子角色
     * @param role
     */
    void addZiRole(RoleAddQuery role);

    /**
     * 增加角色（内部用）
     * @param role
     */
    void insertRole(RoleAddQuery role);

    /**
     * 根据ID删除角色
     * @param id
     */
    void deleteRole(Long id);

    /**
     * 修改角色
     * @param role
     */
    void updateRole(UserRole role);

    /**
     * 查询所有角色
     * @return
     */
    List<UserRole> queryAllRole();

    /**
     * 根据角色名查询角色信息
     * @param RoleName
     * @param status
     * @return
     */
    UserRole queryOne(String RoleName, Integer status, Integer id);

    /**
     * 根据角色名查询角色信息
     * @param RoleName
     * @param status
     * @return
     */
    UserRole queryOne1(String RoleName, Integer status, Long companyId, Integer id);

    /**
     * 分页查询所有角色
     * @param roleVo
     * @return
     */
    Page<UserRole> queryPageRole(RoleVo roleVo);

    /**
     * 根据ID查询角色
     * @param roleId
     * @return
     */
    UserRole queryRoleById(Long roleId);


    /**
     * 分页查询所有子角色
     * @param roleVo
     * @return
     */
    Page<UserRole> queryZiPageRole(RoleVo roleVo);

    /**
     * 根据公司id删除用户和角色
     * @param id
     */
    void deleteRole2User (Long id);

}

package com.eqlee.user.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.eqlee.user.dao.PrivilegeMapper;
import com.eqlee.user.entity.UserMenu;
import com.eqlee.user.entity.UserPrivilege;
import com.eqlee.user.entity.UserRole;
import com.eqlee.user.entity.query.PrivilegeWithQuery;
import com.eqlee.user.service.IMenuService;
import com.eqlee.user.service.IPrivilegeService;
import com.eqlee.user.service.IRoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yq.constanct.CodeType;
import yq.exception.ApplicationException;
import yq.jwt.contain.LocalUser;
import yq.jwt.entity.PrivilegeMenuQuery;
import yq.jwt.entity.token.AuthLoginVo;
import yq.utils.IdGenerator;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author qf
 * @Date 2019/9/11
 * @Version 1.0
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class PrivilegeServiceImpl extends ServiceImpl<PrivilegeMapper, UserPrivilege> implements IPrivilegeService {

    @Autowired
    private IRoleService roleService;

    @Autowired
    private IMenuService menuService;

    @Autowired
    private IdGenerator idGenerator;

    @Autowired
    private LocalUser localUser;



    @Override
    public List<UserPrivilege> queryAll() {
        return baseMapper.selectList(null);
    }

    /**
     * 增加所有权限
     * @param roleId
     * @param menuList
     */
    @Override
    public void insertAllPrivilege(Long roleId, List<PrivilegeWithQuery> menuList) {

        AuthLoginVo auth = localUser.getUser("auth");

        UserRole role = roleService.queryRoleById(roleId);

        if (role == null) {
            throw new ApplicationException(CodeType.SERVICE_ERROR,"请传入正确的角色名");
        }

        //先查询数据库是否有   有就修改
        LambdaQueryWrapper<UserPrivilege> wrapper = new LambdaQueryWrapper<UserPrivilege>()
                .eq(UserPrivilege::getSystemRoleId,role.getId())
                .eq(UserPrivilege::getAuthId,auth.getId());
        List<UserPrivilege> selectList = baseMapper.selectList(wrapper);


        if (selectList.size() != 0) {
            //修改

            //先删再增
            LambdaQueryWrapper<UserPrivilege> lambdaQueryWrapper = new LambdaQueryWrapper<UserPrivilege>()
                    .eq(UserPrivilege::getSystemRoleId,roleId)
                    .eq(UserPrivilege::getAuthId,auth.getId());
            int delete = baseMapper.delete(lambdaQueryWrapper);

            if (delete <= 0) {
                throw new ApplicationException(CodeType.SERVICE_ERROR,"删除失败");
            }

        }

        //添加
        List<UserPrivilege> list = new ArrayList<>();

        for (PrivilegeWithQuery menu : menuList) {

            UserPrivilege userPrivilege = new UserPrivilege();
            userPrivilege.setId(idGenerator.getNumberId());
            userPrivilege.setSystemRoleId(roleId);
            userPrivilege.setSystemMenuId(menu.getMenuId());
            userPrivilege.setAuthId(auth.getId());

            list.add(userPrivilege);


        }
        int privilege = baseMapper.insertPrivilege(list);

        if (privilege <= 0) {
            throw new ApplicationException(CodeType.SERVICE_ERROR,"保存失败");
        }

    }

    /**
     * 根据角色名查询所有菜单权限
     * @param roleId
     * @return
     */
    @Override
    public List<PrivilegeMenuQuery> queryAllMenu(Long roleId, Integer authId) {
        LambdaQueryWrapper<UserPrivilege> queryWrapper = new LambdaQueryWrapper<UserPrivilege>()
                .eq(UserPrivilege::getSystemRoleId,roleId)
                .eq(UserPrivilege::getAuthId,authId);
        //查询数据库
        List<UserPrivilege> userPrivileges = baseMapper.selectList(queryWrapper);
        //将数据装配到新集合中返回
        List<PrivilegeMenuQuery> result = new ArrayList<>();

        for (UserPrivilege userPrivilege : userPrivileges) {
            PrivilegeMenuQuery query = new PrivilegeMenuQuery();
            //查询数据库将菜单权限返回
            UserMenu userMenu = menuService.queryMenuById(userPrivilege.getSystemMenuId());
            query.setMenuId(userMenu.getId());
            query.setMenuName(userMenu.getMenuName());
            query.setAction(userMenu.getAction());
            query.setParent(userMenu.getParent());
            query.setIconClass(userMenu.getIconClass());
            query.setIconColor(userMenu.getIconColor());
            result.add(query);
        }


        return result;
    }

}

package com.eqlee.user.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.eqlee.user.dao.RoleMapper;
import com.eqlee.user.entity.UserRole;
import com.eqlee.user.entity.bo.RoleAddVo;
import com.eqlee.user.entity.query.RoleAddQuery;
import com.eqlee.user.entity.vo.RoleVo;
import com.eqlee.user.service.IRoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yq.constanct.CodeType;
import yq.exception.ApplicationException;
import yq.jwt.contain.LocalUser;
import yq.jwt.entity.token.AuthLoginVo;
import yq.utils.IdGenerator;

import java.util.List;

/**
 * @Author qf
 * @Date 2019/9/10
 * @Version 1.0
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class RoleServiceImpl extends ServiceImpl<RoleMapper, UserRole> implements IRoleService {

    @Autowired
    private IdGenerator idGenerator;

    @Autowired
    private LocalUser localUser;

    @Override
    public UserRole queryById(Long id) {
        return baseMapper.selectById(id);
    }

    @Override
    public void addRole(RoleAddVo roleVo) {

        AuthLoginVo auth = localUser.getUser("auth");

        LambdaQueryWrapper<UserRole> lambdaQueryWrapper = new LambdaQueryWrapper<UserRole>()
                .eq(UserRole::getRoleName,roleVo.getRoleName())
                .eq(UserRole::getStatu,0)
                .eq(UserRole::getAuthId,auth.getId());
        UserRole userRole = baseMapper.selectOne(lambdaQueryWrapper);

        if (userRole != null) {
            throw new ApplicationException(CodeType.SERVICE_ERROR,"设置的角色名不能重复");
        }

        UserRole role = new UserRole();
        role.setId(idGenerator.getNumberId());
        role.setRoleName(roleVo.getRoleName());
        role.setAuthId(auth.getId());

        int insert = baseMapper.insert(role);
        if (insert <= 0) {
            log.error("insert role db fail.");
            throw new ApplicationException(CodeType.SERVICE_ERROR,"增加角色数据库失败~");
        }
    }

    /**
     * 增加子角色
     * @param roleVo
     */
    @Override
    public void addZiRole(RoleAddQuery roleVo) {

        AuthLoginVo auth = localUser.getUser("auth");

        LambdaQueryWrapper<UserRole> lambdaQueryWrapper = new LambdaQueryWrapper<UserRole>()
                .eq(UserRole::getRoleName,roleVo.getRoleName())
                .eq(UserRole::getCompanyId,roleVo.getCompanyId())
                .eq(UserRole::getStatu,1)
                .eq(UserRole::getAuthId,auth.getId());
        UserRole userRole = baseMapper.selectOne(lambdaQueryWrapper);

        if (userRole != null) {
            throw new ApplicationException(CodeType.SERVICE_ERROR,"设置的角色名不能重复");
        }

        UserRole role = new UserRole();
        role.setId(idGenerator.getNumberId());
        role.setAuthId(auth.getId());
        role.setCompanyId(roleVo.getCompanyId());
        role.setCreateUserId(roleVo.getCreateUserId());
        role.setRoleName(roleVo.getRoleName());
        role.setStatu(1);

        int insert = baseMapper.insert(role);
        if (insert <= 0) {
            log.error("insert role db fail.");
            throw new ApplicationException(CodeType.SERVICE_ERROR,"增加角色数据库失败~");
        }
    }

    /**
     * 内部用
     * @param roleVo
     */
    @Override
    public void insertRole(RoleAddQuery roleVo) {

        AuthLoginVo auth = localUser.getUser("auth");

        UserRole role = new UserRole();
        role.setId(roleVo.getId());
        role.setAuthId(auth.getId());
        role.setRoleName(roleVo.getRoleName());
        role.setStatu(roleVo.getStatu());
        role.setCompanyId(roleVo.getCompanyId());

        int insert = baseMapper.insert(role);
        if (insert <= 0) {
            log.error("insert role db fail.");
            throw new ApplicationException(CodeType.SERVICE_ERROR,"增加角色数据库失败~");
        }
    }

    /**
     * 删除角色
     * @param id
     */
    @Override
    public synchronized void deleteRole(Long id) {

        //执行业务
        UserRole role = baseMapper.selectById(id);

        if ("同行".equals(role.getRoleName()) || "运营人员".equals(role.getRoleName()) || "导游".equals(role.getRoleName()) || "财务".equals(role.getRoleName())) {
            throw new ApplicationException(CodeType.SERVICE_ERROR, "该角色不能删除");
        }

        int delete = baseMapper.deleteById(id);
        if (delete <= 0) {
            log.error("delete role db fail.");
            throw new ApplicationException(CodeType.SERVICE_ERROR,"删除角色失败");
        }
    }

    /**
     * 修改角色
     * @param role
     */
    @Override
    public void updateRole(UserRole role) {
        int updateById = baseMapper.updateById(role);
        if (updateById <= 0) {
            log.error("update role db fail.");
            throw new ApplicationException(CodeType.SERVICE_ERROR,"修改角色失败");
        }
    }

    @Override
    public List<UserRole> queryAllRole() {

        AuthLoginVo auth = localUser.getUser("auth");

        LambdaQueryWrapper<UserRole> queryWrapper = new LambdaQueryWrapper<UserRole>()
                .eq(UserRole::getStatu,0)
                .eq(UserRole::getAuthId,auth.getId());

        return baseMapper.selectList(queryWrapper);
    }


    /**
     * 根据名称查询角色
     * @param RoleName
     * @param status
     * @return
     */
    @Override
    public UserRole queryOne(String RoleName, Integer status, Integer id) {
        LambdaQueryWrapper<UserRole> queryWrapper = new LambdaQueryWrapper<UserRole>()
                .eq(UserRole::getRoleName,RoleName)
                .eq(UserRole::getStatu,status)
                .eq(UserRole::getAuthId,id);
        UserRole role = baseMapper.selectOne(queryWrapper);
        return role;
    }

    @Override
    public UserRole queryOne1(String RoleName, Integer status, Long companyId, Integer id) {
        LambdaQueryWrapper<UserRole> queryWrapper = new LambdaQueryWrapper<UserRole>()
              .eq(UserRole::getRoleName,RoleName)
              .eq(UserRole::getStatu,status)
              .eq(UserRole::getCompanyId,companyId)
              .eq(UserRole::getAuthId,id);
        UserRole role = baseMapper.selectOne(queryWrapper);
        return role;
    }

    /**
     * 分页查询所有角色
     * @param roleVo
     * @return
     */
    @Override
    public Page<UserRole> queryPageRole(RoleVo roleVo) {

        AuthLoginVo auth = localUser.getUser("auth");

        LambdaQueryWrapper<UserRole> queryWrapper = new LambdaQueryWrapper<UserRole>()
                .eq(UserRole::getStatu,0)
                .eq(UserRole::getAuthId,auth.getId())
                .orderByDesc(UserRole::getId);
        Page<UserRole> page = new Page<>();
        page.setSize(roleVo.getSize());
        page.setCurrent(roleVo.getCurrent());
        baseMapper.selectPage(page,queryWrapper);
        return page;
    }

    /**
     * 根据ID查询角色
     * @param roleId
     * @return
     */
    @Override
    public UserRole queryRoleById(Long roleId) {
        return baseMapper.selectById(roleId);
    }


    /**
     * 分页查询所有子角色
     * @param roleVo
     * @return
     */
    @Override
    public Page<UserRole> queryZiPageRole(RoleVo roleVo) {

        AuthLoginVo auth = localUser.getUser("auth");

        LambdaQueryWrapper<UserRole> queryWrapper = new LambdaQueryWrapper<UserRole>()
                .eq(UserRole::getStatu,1)
                .eq(UserRole::getAuthId,auth.getId())
                .eq(UserRole::getCompanyId,roleVo.getCompanyId())
                .orderByDesc(UserRole::getId);
        Page<UserRole> page = new Page<>();
        page.setSize(roleVo.getSize());
        page.setCurrent(roleVo.getCurrent());
        baseMapper.selectPage(page,queryWrapper);
        return page;
    }

    /**
     * 根据公司删除用户和角色
     * @param id
     */
    @Override
    public void deleteRole2User(Long id) {

        AuthLoginVo auth = localUser.getUser("auth");

        LambdaQueryWrapper<UserRole> wrapper = new LambdaQueryWrapper<UserRole>()
              .eq(UserRole::getCompanyId,id)
              .eq(UserRole::getAuthId,auth.getId());

        baseMapper.delete(wrapper);


    }
}

package com.eqlee.user.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.eqlee.user.dao.UserMapper;
import com.eqlee.user.entity.User;
import com.eqlee.user.entity.UserRole;
import com.eqlee.user.entity.bo.CityBo;
import com.eqlee.user.entity.bo.UserAdminBo;
import com.eqlee.user.entity.bo.UserBo;
import com.eqlee.user.entity.bo.UserInfoBo;
import com.eqlee.user.entity.city.CityAddVo;
import com.eqlee.user.entity.query.*;
import com.eqlee.user.entity.vo.UserUpdatePasswordVo;
import com.eqlee.user.entity.vo.UserUpdateVo;
import com.eqlee.user.entity.vo.UserVo;
import com.eqlee.user.service.ICityService;
import com.eqlee.user.service.IPrivilegeService;
import com.eqlee.user.service.IRoleService;
import com.eqlee.user.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yq.constanct.CodeType;
import yq.exception.ApplicationException;
import yq.jwt.contain.LocalUser;
import yq.jwt.entity.CityJwtBo;
import yq.jwt.entity.PrivilegeMenuQuery;
import yq.jwt.entity.token.AuthLoginVo;
import yq.utils.IdGenerator;
import yq.utils.ShaUtils;
import yq.utils.StringUtils;

import java.time.LocalDateTime;
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
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Autowired
    private IRoleService roleService;

    @Autowired
    private IPrivilegeService privilegeService;

    @Autowired
    private IdGenerator idGenerator;

    @Autowired
    private LocalUser localUser;

    @Autowired
    private ICityService cityService;

    /**
     * 管理人员注册
     * @param userVo
     */
    @Override
    public synchronized void register(UserVo userVo) {

        AuthLoginVo auth = localUser.getUser("auth");

        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<User>()
                .eq(User::getAccount,userVo.getUserName())
                .eq(User::getAuthId,auth.getId());
        User user1 = baseMapper.selectOne(queryWrapper);
        if (user1 != null) {
            throw new ApplicationException(CodeType.SUCC_ERROR,"该账号已被使用");
        }
        String s = ShaUtils.getSha1(userVo.getPassword());
        User user = new User();
        Long userId = idGenerator.getNumberId();
        user.setId(userId);
        user.setAccount(userVo.getUserName());
        user.setPassword(s);
        user.setCName(userVo.getName());
        user.setTel(userVo.getPhone());
        List<String> list1 = new ArrayList<>();
        for (CityBo bo : userVo.getCity()) {
            list1.add(bo.getCity());
        }
        user.setCity(list1.toString());
        user.setCompanyId(userVo.getCompanyId());
        user.setAuthId(auth.getId());
        user.setCompanyName(userVo.getCompanyName());

        //添加城市
        List<CityAddVo> list = new ArrayList<>();
        for (CityBo cityBo : userVo.getCity()) {
            CityAddVo vo = new CityAddVo();
            vo.setCity(cityBo.getCity());
            vo.setUserId(userId);
            list.add(vo);
        }
        cityService.addCity(list);


        UserRole role = roleService.queryOne(userVo.getRoleName(),0,auth.getId());

        if (role == null) {
            throw new ApplicationException(CodeType.SERVICE_ERROR,"该角色不存在");
        }
        user.setSystemRoleId(role.getId());

        if (role.getStopped()) {
            role.setStopped(false);
            roleService.updateRole(role);
        }

        //是否本部公司
        if (userVo.getType() == 1) {
            //本公司
            user.setCompanyType(1);
        } else {
            //外部公司
            user.setCompanyType(0);
        }

        int insert = baseMapper.insert(user);

        if (insert <= 0) {
            log.error("insert user db fail.");
            throw new ApplicationException(CodeType.SERVICE_ERROR,"注册失败");
        }
    }

    /**
     * 登录
     * @param userName
     * @param password
     * @return
     */
    @Override
    public LoginQuery login(String userName, String password) {

        AuthLoginVo auth = localUser.getUser("auth");

        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<User>()
                .eq(User::getAccount,userName)
                .eq(User::getAuthId,auth.getId());
        User user = baseMapper.selectOne(queryWrapper);

        if (user == null) {
            log.error("user login fail.");
            throw new ApplicationException(CodeType.SERVICE_ERROR,"用户名有误");
        }

        if (!ShaUtils.getSha1(password).equals(user.getPassword())) {
            throw new ApplicationException(CodeType.SERVICE_ERROR,"密码错误");
        }

        if (user.getStopped()) {
            throw new ApplicationException(CodeType.SERVICE_ERROR,"该账号还未启用");
        }

        LocalDateTime now = LocalDateTime.now();
        user.setLastLoginTime(now);
        baseMapper.updateById(user);

        //到此处表示已经登录成功签名认证已经通过，将签名信息保存进数据库

        UserRole userRole = roleService.queryRoleById(user.getSystemRoleId());
        if (userRole == null) {
            throw new ApplicationException(CodeType.SERVICE_ERROR,"该账户尚未绑定角色");
        }
        List<PrivilegeMenuQuery> list = privilegeService.queryAllMenu(user.getSystemRoleId(),auth.getId());

        //查询城市
        List<CityJwtBo> boList = cityService.queryCity(user.getId());

        //装配UserLoginQuery
        LoginQuery query = new LoginQuery();
        query.setId(user.getId());
        query.setAccount(user.getAccount());
        query.setCname(user.getCName());
        query.setCity(boList);
        query.setCompanyId(user.getCompanyId());
        query.setPassword(user.getPassword());
        query.setTel(user.getTel());
        query.setStatus(userRole.getStatu());
        query.setRoleName(userRole.getRoleName());
        query.setMenuList(list);
        return query;
    }

    /**
     * 注销删除账号
     * @param id
     */
    @Override
    public synchronized void deleteUser(Long id) {

        AuthLoginVo auth = localUser.getUser("auth");

        //执行业务
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<User>()
                .eq(User::getId,id)
                .eq(User::getAuthId,auth.getId());

        User user = baseMapper.selectOne(queryWrapper);

        if (user.getStopped()) {
            throw new ApplicationException(CodeType.SERVICE_ERROR,"该账号正在使用，不能删除");
        }

        int delete = baseMapper.deleteById(user.getId());

        if (delete <= 0) {
            log.error("delete user fail.");
            throw new ApplicationException(CodeType.SERVICE_ERROR,"注销失败");
        }
    }

    /**
     * 查询用户信息
     * @param userName
     * @return
     */
    @Override
    public User queryUser(String userName) {
        AuthLoginVo auth = localUser.getUser("auth");
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<User>()
                .eq(User::getAccount,userName)
                .eq(User::getAuthId,auth.getId());
        return baseMapper.selectOne(queryWrapper);
    }

    /**
     * 退出账号
     * @param userName
     */
    @Override
    public void exitUser(String userName) {
        AuthLoginVo auth = localUser.getUser("auth");
        //执行业务
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<User>()
                .eq(User::getAccount,userName)
                .eq(User::getAuthId,auth.getId());
        User user = new User();
        user.setStopped(false);
        int update = baseMapper.update(user, queryWrapper);

        if (update <= 0) {
            log.error("exit user fail.");
            throw new ApplicationException(CodeType.SERVICE_ERROR,"退出账号失败");
        }

    }

    /**
     * 注册子账户
     * @param userVo
     */
    @Override
    public synchronized void dowmRegister(UserZiQuery userVo) {

        AuthLoginVo auth = localUser.getUser("auth");

        //执行业务
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<User>()
                .eq(User::getAccount,userVo.getUserName())
                .eq(User::getAuthId,auth.getId());
        User user1 = baseMapper.selectOne(queryWrapper);
        if (user1 != null) {
            throw new ApplicationException(CodeType.SUCC_ERROR,"该账号已被使用");
        }
        String s = ShaUtils.getSha1(userVo.getPassword());
        User user = new User();
        Long userId = idGenerator.getNumberId();
        user.setId(userId);
        user.setAccount(userVo.getUserName());
        user.setPassword(s);
        user.setAuthId(auth.getId());
        user.setCompanyName(userVo.getCompanyName());
        user.setCName(userVo.getName());
        user.setTel(userVo.getPhone());

        List<String> list1 = new ArrayList<>();
        for (CityBo bo : userVo.getCity()) {
            list1.add(bo.getCity());
        }
        user.setCity(list1.toString());
        user.setCompanyId(userVo.getCompanyId());

        //添加城市
        List<CityAddVo> list = new ArrayList<>();
        for (CityBo cityBo : userVo.getCity()) {
            CityAddVo vo = new CityAddVo();
            vo.setCity(cityBo.getCity());
            vo.setUserId(userId);
            list.add(vo);
        }
        cityService.addCity(list);

        UserRole role = roleService.queryOne1(userVo.getRoleName(), 1,userVo.getCompanyId(),auth.getId());

        if (role == null) {
            throw new ApplicationException(CodeType.SERVICE_ERROR,"该角色不存在");
        }

        user.setSystemRoleId(role.getId());

        user.setStatus(1);

        user.setIsSuper(true);
        int insert = baseMapper.insert(user);

        if (insert <= 0) {
            log.error("insert user db fail.");
            throw new ApplicationException(CodeType.SERVICE_ERROR,"注册失败");
        }
    }

    /**
     * 分页高级查询用户列表
     * @param page
     * @return
     */
    @Override
    public Page<UserQuery> queryAllUserByPage(Page<UserQuery> page) {
        AuthLoginVo auth = localUser.getUser("auth");
        return baseMapper.queryUserInfo(page,auth.getId());
    }

    /**
     * 对用户分页数据进行用户模糊以及角色帅选
     * @param page
     * @param userName
     * @param roleName
     * @return
     */
    @Override
    public Page<UserQuery> queryPageUserByName(Page<UserQuery> page,String userName,String roleName,Long companyId,Integer type) {

        AuthLoginVo auth = localUser.getUser("auth");

        if (StringUtils.isBlank(userName)) {
            userName = null;
        }

        if (StringUtils.isBlank(roleName)) {
            roleName = null;
        }

        return baseMapper.queryPageUserByName(page,userName,roleName,auth.getId(),companyId,type);
    }

    /**
     * 模糊查询加分页
     * @param page
     * @param userNameOrRole
     * @return
     */
    @Override
    public Page<User2Query> queryUserByName(Page<User2Query> page, String userNameOrRole) {

        AuthLoginVo auth = localUser.getUser("auth");

        if (StringUtils.isBlank(userNameOrRole)) {
            userNameOrRole = null;
        }

        return baseMapper.queryUserByName(page,userNameOrRole,auth.getId());
    }

    /**
     * 重置密码
     * @param vo
     */
    @Override
    public synchronized void updateUserPassword(UserUpdatePasswordVo vo) {

        AuthLoginVo auth = localUser.getUser("auth");
        //业务
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<User>()
              .eq(User::getAccount,vo.getUserName())
              .eq(User::getAuthId,auth.getId());
        User user = baseMapper.selectOne(queryWrapper);

        if (user == null) {
            throw new ApplicationException(CodeType.SERVICE_ERROR,"该用户不存在");
        }

        String s = ShaUtils.getSha1(vo.getOldPassword());
        if (!user.getPassword().equals(s)) {
            throw new ApplicationException(CodeType.SERVICE_ERROR, "原密码错误");
        }

        User u = new User();
        u.setId(vo.getId());
        u.setPassword(ShaUtils.getSha1(vo.getPassword()));
        int updateById = baseMapper.updateById(u);

        if (updateById <= 0) {
            log.error("update password fail.");
            throw new ApplicationException(CodeType.SERVICE_ERROR,"重置密码失败");
        }
    }

    /**
     * 修改用户信息
     * @param vo
     */
    @Override
    public void updateUser(UserUpdateVo vo) {

        AuthLoginVo auth = localUser.getUser("auth");
        //修改用户
        User user = new User();
        user.setId(vo.getId());
        user.setCName(vo.getCname());
        user.setTel(vo.getTel());
        if (StringUtils.isNotBlank(vo.getNewPassword())) {
            String s = ShaUtils.getSha1(vo.getNewPassword());
            user.setPassword(s);
        }

        List<CityAddVo> list = new ArrayList<>();
        for (CityBo bo : vo.getCity()) {
            CityAddVo vo1 = new CityAddVo();
            vo1.setCity(bo.getCity());
            vo1.setUserId(vo.getId());
            list.add(vo1);
        }
        //修改城市
        cityService.upCity(list);

        if (vo.getCity().size() > 0) {
            List<String> list1 = new ArrayList<>();
            for (CityBo bo : vo.getCity()) {
                list1.add(bo.getCity());
            }
            user.setCity(list1.toString());
        }

        user.setStopped(vo.getStopped());
        user.setUpdateDate(LocalDateTime.now());

        UserRole role = roleService.queryOne(vo.getRoleName(),0,auth.getId());
        user.setSystemRoleId(role.getId());

        int byId = baseMapper.updateById(user);

        if (byId <= 0) {
            log.error("update user fail.");
            throw new ApplicationException(CodeType.SERVICE_ERROR,"修改用户信息失败");
        }
    }

    /**
     * 根据id查询用户信息
     * @param id
     * @return
     */
    @Override
    public UserByIdQuery getUserById(Long id) {
        UserByIdQuery query = baseMapper.queryUserById(id);

        List<CityJwtBo> list = cityService.queryCity(id);
        List<String> cityList = new ArrayList<>();
        for (CityJwtBo bo : list) {
            cityList.add(bo.getCity());
        }
        query.setCityList(cityList);
        return query;
    }

    /**
     * 查询该角色下的所有用户id的集合
     * @param roleName
     * @return
     */
    @Override
    public List<UserAdminBo> queryAllAdmin(String roleName) {
        AuthLoginVo auth = localUser.getUser("auth");

        return baseMapper.queryAllAdmin (roleName,auth.getId());
    }

    /**
     * 根据公司删除所有用户和角色
     * @param id
     */
    @Override
    public void deleteAllUser(Long id) {
        AuthLoginVo auth = localUser.getUser("auth");
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<User>()
              .eq(User::getCompanyId,id)
              .eq(User::getAuthId,auth.getId());

        baseMapper.delete(wrapper);

        //删除角色
        roleService.deleteRole2User(id);
    }


    /**
     * 查询当前城市的导游
     * @param list
     * @return
     */
    @Override
    public List<UserBo> queryLocalCityUser(List<String> list) {

        AuthLoginVo auth = localUser.getUser("auth");
        String city1 = null;
        String city2 = null;
        String city3 = null;
        String city4 = null;
        String city5 = null;
        String city6 = null;
        String city7 = null;
        String city8 = null;
        String city9 = null;

        System.out.println(list);
        if (list != null) {
            for (int i = 0; i < list.size(); i++) {
                city1 = list.get(0);
                if (list.size() >= 2) {
                    city2 = list.get(1);
                }
                if (list.size() >= 3) {
                    city3 = list.get(2);
                }
                if (list.size() >= 4) {
                    city4 = list.get(3);
                }
                if (list.size() >= 5) {
                    city5 = list.get(4);
                }
                if (list.size() >= 6) {
                    city6 = list.get(5);
                }
                if (list.size() >= 7) {
                    city7 = list.get(6);
                }
                if (list.size() >= 8) {
                    city8 = list.get(7);
                }
                if (list.size() >= 9) {
                    city9 = list.get(8);
                }
            }
        }

        return baseMapper.queryLocalCityUserList (city1,city2,city3,city4,city5,city6,city7,city8,city9,auth.getId());
    }

    @Override
    public UserInfoBo queryCompanyUserInfo(Long companyId) {

        AuthLoginVo auth = localUser.getUser("auth");

        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<User>()
              .eq(User::getCompanyId,companyId)
              .eq(User::getStatus,0)
              .eq(User::getCompanyType,0)
              .eq(User::getAuthId,auth.getId());
        User user = baseMapper.selectOne(wrapper);
        UserInfoBo bo = new UserInfoBo();
        bo.setCname(user.getCName());
        bo.setCompanyTel(user.getTel());
        return bo;
    }

    @Override
    public List<UserQuery> queryUserByRole(String roleName) {

        AuthLoginVo auth = localUser.getUser("auth");

        UserRole userRole = roleService.queryOne(roleName, 0, auth.getId());

        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<User>()
              .eq(User::getSystemRoleId,userRole.getId());

        List<User> users = baseMapper.selectList(wrapper);

        List<UserQuery> list = new ArrayList<>();
        for (User user : users) {
            UserQuery query = new UserQuery();
            query.setId(user.getId());
            query.setAccount(user.getAccount());
            query.setCname(user.getCName());
            query.setCompanyId(user.getCompanyId());
            query.setCompanyName(user.getCompanyName());
            query.setRoleName(userRole.getRoleName());
            query.setStatus(user.getStatus());
            query.setTel(user.getTel());
            query.setType(user.getCompanyType());
            query.setStopped(user.getStopped());
            list.add(query);
        }

        return list;
    }

}


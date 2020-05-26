package com.eqlee.user.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.eqlee.user.entity.User;
import com.eqlee.user.entity.bo.UserAdminBo;
import com.eqlee.user.entity.bo.UserBo;
import com.eqlee.user.entity.bo.UserInfoBo;
import com.eqlee.user.entity.query.*;
import com.eqlee.user.entity.vo.UserUpdatePasswordVo;
import com.eqlee.user.entity.vo.UserUpdateVo;
import com.eqlee.user.entity.vo.UserVo;

import java.util.List;


/**
 * @Author qf
 * @Date 2019/9/11
 * @Version 1.0
 */
public interface IUserService {

    /**
     * 注册
     * @param userVo
     */
    void register(UserVo userVo);

    /**
     * 登录
     * @param userName
     * @param password
     * @return
     */
    LoginQuery login(String userName, String password);

    /**
     * 注销
     * @param id
     */
    void deleteUser(Long id);

    /**
     * 根据用户名查询用户信息
     * @param userName
     * @return
     */
    User queryUser(String userName);


    /**
     * 退出账号
     * @param userName
     */
    void exitUser(String userName);


    /**
     * 注册子账户
     * @param userVo
     */
    void dowmRegister(UserZiQuery userVo);

    /**
     * 分页查询所有用户加模糊查询
     * @param page
     * @return
     */
    Page<UserQuery> queryAllUserByPage(Page<UserQuery> page);

    /**
     * 对用户分页数据进行用户模糊以及角色帅选
     * @param page
     * @param userName
     * @param roleName
     * @return
     */
    Page<UserQuery> queryPageUserByName(Page<UserQuery> page, String userName, String roleName,Long companyId,Integer type);

    /**
     * 只模糊查询加分页
     * @param page
     * @param userNameOrRole
     * @return
     */
    Page<User2Query> queryUserByName(Page<User2Query> page, String userNameOrRole);


    /**
     * 根据用户名和手机号重置密码
     * @param vo
     */
    void updateUserPassword(UserUpdatePasswordVo vo);

    /**
     * 修改用户信息
     * @param vo
     */
    void updateUser(UserUpdateVo vo);

    /**
     * 根绝ID查询用户信息
     * @param id
     * @return
     */
    UserByIdQuery getUserById(Long id);

    /**
     * 查询该角色名下的所有用户的id集合
     * @param roleName
     * @return
     */
    List<UserAdminBo> queryAllAdmin (String roleName);

    /**
     * 根据公司删除所有用户
     * @param id
     */
    void deleteAllUser (Long id);


    /**
     *  查询当前城市的导游
     * @param list
     * @return
     */
    List<UserBo> queryLocalCityUser (List<String> list);


    /**
     *  根据公司ID，是否本公司，是否子用户   查询用户信息
     * @param companyId
     * @return
     */
    UserInfoBo queryCompanyUserInfo (Long companyId);


    /**
     * 查询所有角色下的用户
     * @param roleName
     * @return
     */
    List<UserQuery> queryUserByRole (String roleName);



}

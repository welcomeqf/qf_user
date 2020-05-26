package com.eqlee.user.dao;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.eqlee.user.entity.User;
import com.eqlee.user.entity.bo.UserAdminBo;
import com.eqlee.user.entity.bo.UserBo;
import com.eqlee.user.entity.query.User2Query;
import com.eqlee.user.entity.query.UserByIdQuery;
import com.eqlee.user.entity.query.UserQuery;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;
import yq.IBaseMapper.IBaseMapper;

import java.util.List;

/**
 * @Author qf
 * @Date 2019/9/11
 * @Version 1.0
 */
@Component
public interface UserMapper extends IBaseMapper<User> {


    /**
     * 分页查询所有用户信息
     * @param page
     * @return
     */
    Page<UserQuery> queryUserInfo(Page<UserQuery> page,
                                  @Param("id") Integer id);


    /**
     * 对用户分页数据进行用户模糊以及角色帅选
     * @param page
     * @param userName
     * @param roleName
     * @param id
     * @param companyId
     * @return
     */
    Page<UserQuery> queryPageUserByName(Page<UserQuery> page,
                                        @Param("userName") String userName,
                                        @Param("roleName") String roleName,
                                        @Param("id") Integer id,
                                        @Param("companyId") Long companyId,
                                        @Param("type") Integer type);


    /**
     * 模糊查询加分页
     * @param page
     * @param userNameOrRole
     * @return
     */
    Page<User2Query> queryUserByName(Page<User2Query> page,
                                     @Param("userNameOrRole") String userNameOrRole,
                                     @Param("id") Integer id);

    /**
     * 根据id查询用户
     * @param id
     * @return
     */
    UserByIdQuery queryUserById(Long id);

    /**
     * 查询该角色下的所有用户id
     * @param roleName
     * @param id
     * @return
     */
    List<UserAdminBo> queryAllAdmin(@Param("roleName") String roleName,
                                    @Param("id") Integer id);


    /**
     * 查询当前城市的导游
     * @param city1
     * @param city2
     * @param city3
     * @param city4
     * @param city5
     * @param city6
     * @param city7
     * @param city8
     * @param city9
     * @param id
     * @return
     */
    List<UserBo> queryLocalCityUserList (@Param("city1") String city1,
                                     @Param("city2")String city2,
                                     @Param("city3")String city3,
                                     @Param("city4") String city4,
                                     @Param("city5")String city5,
                                     @Param("city6")String city6,
                                     @Param("city7") String city7,
                                     @Param("city8")String city8,
                                     @Param("city9")String city9,
                                         @Param("id") Integer id);

}

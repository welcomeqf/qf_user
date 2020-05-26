package com.eqlee.user.entity.query;


import lombok.Data;
import yq.jwt.entity.CityJwtBo;
import yq.jwt.entity.PrivilegeMenuQuery;

import java.util.List;

/**
 * @Author qf
 * @Date 2019/10/26
 * @Version 1.0
 */
@Data
public class LoginQuery {

    private Long id;

    /**
     * 用户名
     */
    private String account;

    /**
     * 密码
     */
    private String password;

    /**
     * 中文名
     */
    private String cname;

    /**
     * 电话
     */
    private String tel;

    /**
     * 角色名
     */
    private String roleName;

    /**
     * 公司ID
     */
    private Long companyId;

    /**
     * 状态（0--管理员   1--子用户）
     */
    private Integer status;

    /**
     * 所属城市
     */
    private List<CityJwtBo> city;

    /**
     * 权限列表
     */
    private List<PrivilegeMenuQuery> menuList;
}

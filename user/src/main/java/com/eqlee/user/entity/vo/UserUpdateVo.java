package com.eqlee.user.entity.vo;

import com.eqlee.user.entity.bo.CityBo;
import lombok.Data;

import java.util.List;

/**
 * @Author qf
 * @Date 2019/9/26
 * @Version 1.0
 */
@Data
public class UserUpdateVo {


    private Long id;

    /**
     * 中文名
     */
    private String cname;

    /**
     * 新密码
     */
    private String newPassword;

    /**
     * 电话
     */
    private String tel;

    private Long roleId;

    private String roleName;

    /**
     * 是否停用（false--正常  true--停用）
     */
    private Boolean stopped;

    /**
     * 城市
     */
    private List<CityBo> city;
}

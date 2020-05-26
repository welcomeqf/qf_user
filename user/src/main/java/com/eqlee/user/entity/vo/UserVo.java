package com.eqlee.user.entity.vo;


import com.eqlee.user.entity.bo.CityBo;
import lombok.Data;

import java.util.List;

/**
 * @Author qf
 * @Date 2019/9/12
 * @Version 1.0
 */
@Data
public class UserVo {

    private String userName;

    private String password;

    private String name;

    private String phone;

    private Long roleId;

    private String roleName;

    private List<CityBo> city;

    private Long companyId;

    private String companyName;

    /**
     * 0--外部公司 1--本公司
     */
    private Integer type;

}

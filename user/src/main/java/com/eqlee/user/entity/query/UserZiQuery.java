package com.eqlee.user.entity.query;

import com.eqlee.user.entity.bo.CityBo;
import lombok.Data;

import java.util.List;

/**
 * @Author qf
 * @Date 2019/10/24
 * @Version 1.0
 */
@Data
public class UserZiQuery {

    private String userName;

    private String password;

    private String name;

    private String phone;

    private Long companyId;

    private String companyName;

    private String roleName;

    /**
     * 所属城市
     */
    private List<CityBo> city;
}

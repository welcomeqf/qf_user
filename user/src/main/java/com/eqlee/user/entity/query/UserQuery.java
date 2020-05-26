package com.eqlee.user.entity.query;


import lombok.Data;


/**
 * @Author qf
 * @Date 2019/9/19
 * @Version 1.0
 */
@Data
public class UserQuery {

    private Long Id;

    private String Account;

    private String cname;

    private String Tel;

    private String RoleName;

    private Boolean Stopped;

    private String city;

    private Long companyId;

    private String companyName;

    private Integer type;

    private Integer status;
}

package com.eqlee.user.entity.query;

import lombok.Data;

/**
 * @Author qf
 * @Date 2019/10/16
 * @Version 1.0
 */
@Data
public class RoleAddQuery {

    private Long id;

    private String roleName;

    private Integer statu;

    private Long companyId;

    private Long createUserId;
}

package com.eqlee.user.entity.query;

import lombok.Data;

import java.util.List;

/**
 * @Author qf
 * @Date 2019/10/22
 * @Version 1.0
 */
@Data
public class AllMenuQuery {

    private Long id;

    private String menuName;

    private List<WithQuery> userMenu;
}

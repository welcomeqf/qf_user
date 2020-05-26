package com.eqlee.user.dao;

import com.eqlee.user.entity.UserMenu;
import com.eqlee.user.entity.query.WithQuery;
import org.springframework.stereotype.Component;
import yq.IBaseMapper.IBaseMapper;

import java.util.List;

/**
 * @Author qf
 * @Date 2019/9/11
 * @Version 1.0
 */
@Component
public interface MenuMapper extends IBaseMapper<UserMenu> {

    /**
     * 查询所有菜单
     * @return
     */
    List<WithQuery> queryMenu(Integer id);

    /**
     * 查询所有
     * @param id
     * @return
     */
    List<WithQuery> queryListMenu(Integer id);
}

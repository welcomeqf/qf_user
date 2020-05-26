package com.eqlee.user.dao;

import com.eqlee.user.entity.UserPrivilege;
import org.springframework.stereotype.Component;
import yq.IBaseMapper.IBaseMapper;

import java.util.List;


/**
 * @Author qf
 * @Date 2019/9/11
 * @Version 1.0
 */
@Component
public interface PrivilegeMapper extends IBaseMapper<UserPrivilege> {


    /**
     * 批量插入
     * @param list
     * @return
     */
    int insertPrivilege(List<UserPrivilege> list);

    /**
     * 批量修改
     * @param list
     * @return
     */
    int updatePrivilege(List<UserPrivilege> list);

    /**
     * 批量删除
     * @param id
     * @return
     */
    int deletePrivilege(Long id);

}

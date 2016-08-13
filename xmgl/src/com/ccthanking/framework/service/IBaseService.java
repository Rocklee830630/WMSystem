package com.ccthanking.framework.service;

import java.io.Serializable;
import java.util.List;

import com.ccthanking.framework.base.BaseVO;

public interface IBaseService<T extends BaseVO, PK extends Serializable> {

    /**
     * 根据主键查询唯一记录.<br/>
     * 
     * @param id
     *            主键值
     * @return VO, 如果不存在则返回null
     * @since v1.00
     */
    T findById(PK id);

    List<T> find();

    List<T> find(List<PK> ids);

    int remove(PK id);

    int remove(List<PK> ids);

    int update(T bean);

    int insert(T bean);
}

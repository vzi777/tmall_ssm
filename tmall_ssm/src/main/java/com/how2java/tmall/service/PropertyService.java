package com.how2java.tmall.service;

import com.how2java.tmall.pojo.Property;

import java.util.List;

public interface PropertyService {

    /**
     * 增加属性
     * @param p
     */
    void add(Property p);

    /**
     * 删除属性
     * @param id
     */
    void delete(int id);

    /**
     * 修改属性
     * @param p
     */
    void update(Property p);

    /**
     * 根据id获取属性
     * @param id
     * @return
     */
    Property get(int id);

    /**
     * 根据分类cid获取属性
     * @param cid
     * @return
     */
    List<Property> list(int cid);
}
package com.how2java.tmall.service;

import com.how2java.tmall.pojo.Product;
import com.how2java.tmall.pojo.PropertyValue;

import java.util.List;

public interface PropertyValueService {

    /**
     * 初始化属性
     * @param p
     */
    void init(Product p);

    /**
     * 更新属性
     * @param pv
     */
    int update(PropertyValue pv);

    /**
     * 根据产品id和属性id获取属性值对象
     * @param pid
     * @param ptid
     * @return
     */
    PropertyValue get(int pid, int ptid);

    /**
     * 获取该产品的所有属性
     * @param pid
     * @return
     */
    List<PropertyValue> list(int pid);
}
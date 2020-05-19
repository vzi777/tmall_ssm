package com.how2java.tmall.service;

import com.how2java.tmall.pojo.Order;
import com.how2java.tmall.pojo.OrderItem;
import com.how2java.tmall.pojo.User;

import java.util.List;

public interface OrderItemService {

    /**
     * 增加订单
     * @param oi
     */
    void add(OrderItem oi);

    /**
     * 删除订单
     * @param id
     */
    void delete(int id);

    /**
     * 修改订单
     * @param oi
     */
    void update(OrderItem oi);

    /**
     * 根据id获取订单
     * @param id
     * @return
     */
    OrderItem get(int id);

    /**
     * 获取所有订单
     * @return
     */
    List<OrderItem> list();

    /**
     * 为所有订单注入属性
     * @param os
     */
    void fill(List<Order> os);

    /**
     * 为单个订单注入总量、总金额、订单项
     * @param o
     */
    void fill(Order o);

    /**
     * 根据产品获取销量
     * @param pid
     * @return
     */
    int getSaleCount(int  pid);

    /**
     * 查询用户的所有订单项
     * @param uid
     * @return
     */
    List<OrderItem> listByUser(int uid);

    /**
     * 查询订单的所有订单项
     * @param o
     * @return
     */
    List<OrderItem> listByOrder(Order o);
}

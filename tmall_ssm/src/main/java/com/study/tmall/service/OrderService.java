package com.study.tmall.service;

import com.study.tmall.pojo.Order;
import com.study.tmall.pojo.OrderItem;

import java.util.List;

public interface OrderService {

    /**
     * 订单状态常量
     */
    String waitPay = "waitPay";
    String waitDelivery = "waitDelivery";
    String waitConfirm = "waitConfirm";
    String waitReview = "waitReview";
    String finish = "finish";
    String delete = "delete";

    /**
     * 增加订单
     * @param o
     */
    void add(Order o);

    /**
     * 删除订单
     * @param id
     */
    void delete(int id);

    /**
     * 修改订单
     * @param o
     */
    void update(Order o);

    /**
     * 根据id获取订单
     * @param id
     * @return
     */
    Order get(int id);

    /**
     * 获取所有订单
     * @return
     */
    List<Order> list();

    /**
     * 添加订单，并更新订单项oid，计算订单总价
     * @param o
     * @param ois
     */
    float add(Order o,List<OrderItem> ois);

    /**
     * 查询所有状态不为“excluedStatu”的订单
     * @param uid
     * @param excludedStatu
     * @return
     */
    List<Order> list(int uid, String excludedStatu);
}

package com.study.tmall.service.impl;

import com.study.tmall.mapper.OrderItemMapper;
import com.study.tmall.pojo.*;
import com.study.tmall.service.OrderItemService;
import com.study.tmall.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderItemServiceImpl implements OrderItemService {

    @Autowired
    private OrderItemMapper orderItemMapper;
    @Autowired
    private ProductService productService;

    @Override
    public void add(OrderItem oi) {
        orderItemMapper.insert(oi);
    }

    @Override
    public void delete(int id) {
        orderItemMapper.deleteByPrimaryKey(id);
    }

    @Override
    public void update(OrderItem oi) {
        orderItemMapper.updateByPrimaryKeySelective(oi);
    }

    @Override
    public OrderItem get(int id) {
        OrderItem oi = orderItemMapper.selectByPrimaryKey(id);
        //默认为订单项注入产品属性
        setProduct(oi);
        return oi;
    }

    @Override
    public List<OrderItem> list() {
        OrderItemExample example = new OrderItemExample();
        example.setOrderByClause("id desc");
        List<OrderItem> ois = orderItemMapper.selectByExample(example);

        //为OrderItem注入Product属性
        setProduct(ois);
        return ois;
    }

    @Override
    public void fill(List<Order> os) {
        for(Order o : os){
            fill(o);
        }
    }

    @Override
    public void fill(Order o) {
        //注入了product的orderItem集合
        List<OrderItem> ois = listByOrder(o);

        float total = 0;
        int totalNumber = 0;
        for(OrderItem oi : ois){
            totalNumber += oi.getNumber();
            total += oi.getNumber()*oi.getProduct().getPromotePrice();
        }
        //为order注入totalNumber,total,orderItems
        o.setTotalNumber(totalNumber);
        o.setTotal(total);
        o.setOrderItems(ois);
    }

    @Override
    public int getSaleCount(int pid) {
        OrderItemExample example = new OrderItemExample();
        example.createCriteria().andPidEqualTo(pid);
        List<OrderItem> ois = orderItemMapper.selectByExample(example);
        int result = 0;
        for(OrderItem oi : ois){
            result += oi.getNumber();
        }
        return result;
    }

    @Override
    public List<OrderItem> listByUser(int uid) {
        OrderItemExample example = new OrderItemExample();
        //oid不为空表示是已经购买过的订单项了
        example.createCriteria().andUidEqualTo(uid).andOidIsNull();
        example.setOrderByClause("id desc");
        List<OrderItem> ois = orderItemMapper.selectByExample(example);
        //为所有订单项注入产品
        setProduct(ois);
        return ois;
    }

    @Override
    public List<OrderItem> listByOrder(Order o) {
        OrderItemExample example = new OrderItemExample();
        example.createCriteria().andOidEqualTo(o.getId());
        example.setOrderByClause("id desc");
        List<OrderItem> ois = orderItemMapper.selectByExample(example);
        //为所有OrderItem注入Product属性
        setProduct(ois);
        return ois;
    }

    /**
     * 为单个订单项注入产品属性
     */
    public void setProduct(OrderItem oi){
        Product p = productService.get(oi.getPid());
        oi.setProduct(p);
    }

    /**
     * 为所有订单项注入产品属性
     */
    public void setProduct(List<OrderItem> ois){
        for(OrderItem oi : ois){
            setProduct(oi);
            //为产品product注入productImage
            productService.setFirstProductImage(oi.getProduct());
        }
    }
}

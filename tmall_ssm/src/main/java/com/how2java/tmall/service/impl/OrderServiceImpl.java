package com.how2java.tmall.service.impl;

import com.how2java.tmall.mapper.OrderMapper;
import com.how2java.tmall.pojo.Order;
import com.how2java.tmall.pojo.OrderExample;
import com.how2java.tmall.pojo.OrderItem;
import com.how2java.tmall.pojo.User;
import com.how2java.tmall.service.OrderItemService;
import com.how2java.tmall.service.OrderService;
import com.how2java.tmall.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    OrderMapper orderMapper;
    @Autowired
    UserService userService;
    @Autowired
    OrderItemService orderItemService;

    @Override
    public void add(Order o) {
        orderMapper.insert(o);
    }

    @Override
    public void delete(int id) {
        orderMapper.deleteByPrimaryKey(id);
    }

    @Override
    public void update(Order o) {
        orderMapper.updateByPrimaryKeySelective(o);
    }

    @Override
    @Transactional(propagation= Propagation.REQUIRED,rollbackForClassName="Exception")
    public Order get(int id) {
        //根据id查询订单，并注入订单项属性
        Order o = orderMapper.selectByPrimaryKey(id);
        orderItemService.fill(o);
        return o;
    }

    @Override
    public List<Order> list() {
        OrderExample example =new OrderExample();
        example.setOrderByClause("id desc");
        List<Order> os = orderMapper.selectByExample(example);
        //为order注入user属性
        setUser(os);
        return os;
    }

    @Override
    @Transactional(propagation= Propagation.REQUIRED,rollbackForClassName="Exception")
    public float add(Order o, List<OrderItem> ois) {
        //添加订单
        add(o);

        float total = 0;
        for(OrderItem oi : ois){
            //为订单项设置订单关系
            oi.setOid(o.getId());
            orderItemService.update(oi);
            //计算订单总价格
            total += oi.getNumber()*oi.getProduct().getPromotePrice();
        }
        return total;
    }

    @Override
    public List<Order> list(int uid, String excludedStatu) {
        //查询当前用户下状态不为"delete"的订单集合
        OrderExample example = new OrderExample();
        example.createCriteria().andUidEqualTo(uid).andStatusNotEqualTo(excludedStatu);
        example.setOrderByClause("id desc");
        List<Order> os = orderMapper.selectByExample(example);
        return os;
    }

    /**
     * 为单个订单注入用户User属性
     */
    public void setUSer(Order o){
        User u = userService.get(o.getUid());
        o.setUser(u);
    }

    /**
     * 为所有订单注入用户User属性
     * */
    public void setUser(List<Order> os){
        for(Order o : os){
            setUSer(o);
        }
    }
}

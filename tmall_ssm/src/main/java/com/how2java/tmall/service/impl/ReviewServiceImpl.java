package com.how2java.tmall.service.impl;

import com.how2java.tmall.mapper.ReviewMapper;
import com.how2java.tmall.pojo.Review;
import com.how2java.tmall.pojo.ReviewExample;
import com.how2java.tmall.pojo.User;
import com.how2java.tmall.service.ReviewService;
import com.how2java.tmall.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewServiceImpl implements ReviewService {

    @Autowired
    private ReviewMapper reviewMapper;
    @Autowired
    private UserService userService;

    @Override
    public void add(Review r) {
        reviewMapper.insert(r);
    }

    @Override
    public void delete(int id) {
        reviewMapper.deleteByPrimaryKey(id);
    }

    @Override
    public void update(Review r) {
        reviewMapper.updateByPrimaryKeySelective(r);
    }

    @Override
    public Review get(int id) {
        return reviewMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<Review> list(int pid) {
        ReviewExample example = new ReviewExample();
        example.createCriteria().andPidEqualTo(pid);
        example.setOrderByClause("id desc");
        List<Review> rs = reviewMapper.selectByExample(example);
        //给评论集合所有对象注入User属性
        setUser(rs);
        return rs;
    }

    @Override
    public int getCount(int pid) {
        return list(pid).size();
    }

    /**
     * 给单个评论对象注入用户属性
     * @param review
     */
    public void setUser(Review review) {
        User user = userService.get(review.getUid());
        review.setUser(user);
    }

    /**
     * 给所有评论注入用户属性
     * @param rs
     */
    public void setUser(List<Review> rs){
        for(Review r : rs){
            setUser(r);
        }
    }
}

package com.how2java.tmall.service;

import com.how2java.tmall.pojo.Review;

import java.util.List;

public interface ReviewService {

    /**
     * 增加评论
     * @param r
     */
    void add(Review r);

    /**
     * 删除评论
     * @param id
     */
    void delete(int id);

    /**
     * 修改评论
     * @param r
     */
    void update(Review r);

    /**
     * 根据获取单个评论
     * @param id
     * @return
     */
    Review get(int id);

    /**
     * 获取该产品的所有评论
     * @return
     */
    List<Review> list(int pid);

    /**
     * 获取产品对应的评论总数
     * @param pid
     * @return
     */
    int getCount(int pid);
}

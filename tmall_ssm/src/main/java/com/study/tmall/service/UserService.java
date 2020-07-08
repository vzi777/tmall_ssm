package com.study.tmall.service;

import com.study.tmall.pojo.User;

import java.util.List;

public interface UserService {

    /**
     * 增加用户
     * @param u
     */
    void add(User u);

    /**
     * 删除用户
     * @param id
     */
    void delete(int id);

    /**
     * 修改用户
     * @param u
     */
    void update(User u);

    /**
     * 根据id获取用户
     * @param id
     * @return
     */
    User get(int id);

    /**
     * 获取所有用户
     * @return
     */
    List<User> list();

    /**
     * 查询用户是否存在
     */
    boolean isExist(String name);

    /**
     * 根据账号和密码获取用户
     * @param name
     * @param password
     * @return
     */
    User get(String name,String password);
}

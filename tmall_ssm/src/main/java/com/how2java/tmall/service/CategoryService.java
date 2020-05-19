package com.how2java.tmall.service;

import com.how2java.tmall.pojo.Category;

import java.util.List;

public interface CategoryService{

    /**
     * 查询种类
     * @param
     * @return
     */
    List<Category> list();

    /**
     * 添加分类
     * @return
     */
    void add(Category category);

    /**
     * 删除分类
     * @param id
     */
    void delete(int id);

    /**
     * 编辑分类（获取修改的对象）
     * @param id
     * @return
     */
    Category get(int id);

    /**
     * 修改分类
     * @param category
     * @return
     */
    void update(Category category);

}
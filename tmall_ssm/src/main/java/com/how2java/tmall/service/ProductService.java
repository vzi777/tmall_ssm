package com.how2java.tmall.service;

import com.how2java.tmall.pojo.Category;
import com.how2java.tmall.pojo.Product;

import java.util.List;

public interface ProductService {

    /**
     * 根据cid获取所有产品
     * @param cid
     * @return
     */
    List<Product> list(int cid);

    /**
     * 获取某一产品
     * @param id
     * @return
     */
    Product get(int id);

    /**
     * 添加产品
     * @param pd
     */
    void add(Product pd);

    /**
     * 删除产品
     * @param id
     */
    void delete(int id);

    /**
     * 更新产品
     * @param pd
     */
    void update(Product pd);

    /**
     * 为单个产品设置预览图
     * @param p
     * @return
     */
    void setFirstProductImage(Product p);

    /**
     * 为每一个产品设置预览图
     * @param pds
     */
    void setFirstProductImageForAll(List<Product> pds);

    /**
     * 为分类注入其所有的产品集合
     * @param c
     */
    void fill(Category c);

    /**
     * 为多个分类注入其产品集合
     * @param cs
     */
    void fill(List<Category> cs);

    /**
     * 将分类下的产品分行,利于页面显示
     * @param cs
     */
    void fillByRow(List<Category> cs);

    /**
     * 设置该产品的销量和评价数量
     * @param p
     */
    void setSaleAndReviewNumber(Product p);

    /**
     * 设置所有产品的销量和评价数量
     * @param ps
     */
    void setSaleAndReviewNumber(List<Product> ps);

    /**
     * 模糊查询产品
     * @param keyword
     * @return
     */
    List<Product> search(String keyword);
}

package com.study.tmall.service;

import com.study.tmall.pojo.ProductImage;

import java.util.List;

public interface ProductImageService {

    /**
     * 显示单个图片标志
     */
    String type_single = "type_single";

    /**
     * 显示详细图片标志
     */
    String type_detail = "type_detail";

    /**
     * 查询所有产品图片
     * @param pid
     * @param type
     * @return
     */
    List<ProductImage> list(int pid, String type);

    /**
     * 根据产品图片id获取单个产品图片
     * @param id
     * @return
     */
    ProductImage get(int id);

    /**
     * 添加产品图片
     * @param pi
     */
    void add(ProductImage pi);

    /**
     * 删除产品图片
     * @param id
     */
    void delete(int id);

    /**
     * 修改产品图片
     * @param pi
     */
    void update(ProductImage pi);

}

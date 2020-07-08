package com.study.tmall.service.impl;

import com.study.tmall.mapper.ProductMapper;
import com.study.tmall.pojo.Category;
import com.study.tmall.pojo.Product;
import com.study.tmall.pojo.ProductExample;
import com.study.tmall.pojo.ProductImage;
import com.study.tmall.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private ProductImageService productImageService;
    @Autowired
    private OrderItemService orderItemService;
    @Autowired
    private ReviewService reviewService;
    @Autowired
    private CategoryService categoryService;

    @Override
    public List<Product> list(int cid) {
        ProductExample example = new ProductExample();
        example.createCriteria().andCidEqualTo(cid);
        example.setOrderByClause("id desc");
        return productMapper.selectByExample(example);
    }

    @Override
    public Product get(int id) {
        Product p = productMapper.selectByPrimaryKey(id);
        //默认查询产品时，注入第一张预览图和分类属性
        setFirstProductImage(p);
        setCategory(p);
        return p;
    }

    @Override
    public void add(Product pd) {
        productMapper.insert(pd);
    }

    @Override
    public void delete(int id) {
        productMapper.deleteByPrimaryKey(id);
    }

    @Override
    public void update(Product pd) {
        productMapper.updateByPrimaryKeySelective(pd);
    }

    @Override
    public void setFirstProductImage(Product p) {
        List<ProductImage> pis = productImageService.list(p.getId(), ProductImageService.type_single);
        ProductImage pi = null;
        if( !pis.isEmpty() ){
            pi = pis.get(0);
        }
        p.setFirstProductImage(pi);
    }

    @Override
    public void setFirstProductImageForAll(List<Product> pds){
        for(Product p : pds){
            setFirstProductImage(p);
        }
    }

    @Override
    public void fill(Category c) {
        List<Product> pds = list(c.getId());
        //为分类注入产品时，自动为每个产品注入首图
        setFirstProductImageForAll(pds);
        c.setProducts(pds);
    }

    @Override
    public void fill(List<Category> cs) {
        for(Category c : cs){
            fill(c);
        }
    }

    @Override
    public void fillByRow(List<Category> cs) {
        //8个为一行
        int productNumberEachRow = 8;
        //为分类集合注入产品集合属性
        fill(cs);
        for(Category c : cs){
            //分类已经注入产品此时才能取出其产品集合有效
            List<Product> products = c.getProducts();
            //设置每一页的头图
            setFirstProductImageForAll(products);
            //用于存储多个一行产品的小集合
            List<List<Product>> productByRow = new ArrayList<>();
            for(int i = 0; i < products.size(); i += productNumberEachRow){
                int size = i + productNumberEachRow;
                //集合最后不足8个时，所有一起放进去
                size = size > products.size() ? products.size():size;
                //8个产品为一个集合，表示一行产品
                List<Product> productsOfEachRow = products.subList(i,size);
                productByRow.add(productsOfEachRow);
            }
            c.setProductsByRow(productByRow);
        }
    }

    @Override
    public void setSaleAndReviewNumber(Product p) {
        //为当前产品注入销量
        int salCount = orderItemService.getSaleCount(p.getId());
        p.setSaleCount(salCount);
        //为当前产品注入评价量
        int reviewCount = reviewService.getCount(p.getId());
        p.setReviewCount(reviewCount);
    }

    @Override
    public void setSaleAndReviewNumber(List<Product> pds) {
        for(Product p : pds){
            setSaleAndReviewNumber(p);
        }
    }

    @Override
    public List<Product> search(String keyword) {
        List<Product> pds = new ArrayList<>();
        if(keyword.length() != 0){
            ProductExample example = new ProductExample();
            //按名称模糊查询,注意是andxxxLike
            example.createCriteria().andNameLike("%" + keyword + "%");
            example.setOrderByClause("id desc");
            pds = productMapper.selectByExample(example);
            //给产品集注入首图
            setFirstProductImageForAll(pds);
            //给产品集合注入分类
            //setCategory(pds);
        }
        return pds;
    }

    /**
     * 为该产品注入分类属性
     * @param p
     */
    public void setCategory(Product p){
        Category c = categoryService.get(p.getCid());
        p.setCategory(c);
    }

    /**
     * 为该产品注入分类属性
     * @param pds
     */
    public void setCategory(List<Product> pds){
        for(Product p : pds){
            setCategory(p);
        }
    }
}

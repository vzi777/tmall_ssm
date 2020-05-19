package com.how2java.tmall.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.how2java.tmall.pojo.Category;
import com.how2java.tmall.pojo.Product;
import com.how2java.tmall.pojo.Property;
import com.how2java.tmall.service.CategoryService;
import com.how2java.tmall.service.ProductService;
import com.how2java.tmall.util.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("")
public class ProductController {

    @Autowired
    CategoryService categoryService;
    @Autowired
    ProductService productService;

    @RequestMapping("admin_product_list")
    public String list(int cid, Page page, Model model){
        Category c = categoryService.get(cid);  //分页查询需要

        PageHelper.offsetPage(page.getStart(),page.getCount());

        List<Product> pds = productService.list(cid);
        productService.setFirstProductImageForAll(pds);   //设置每个产品的预览图
        int total = (int) new PageInfo<>(pds).getTotal();
        page.setTotal(total);
        page.setParam("&cid=" + cid);   //分页查询时调用product的list()需发送cid

        model.addAttribute("page",page);
        model.addAttribute("pds",pds);
        model.addAttribute("c",c);

        return "admin/listProduct";
    }

    @RequestMapping("admin_product_add")
    public String add(Product pd){
        pd.setCreateDate(new Date());   //设置产品创建时间
        productService.add(pd);

        return "redirect:/admin_product_list?cid=" + pd.getCid();
    }

    @RequestMapping("admin_product_delete")
    public String delete(int id){
        Product pd = productService.get(id);
        productService.delete(id);

        return "redirect:/admin_product_list?cid=" + pd.getCid();
    }

    @RequestMapping("admin_product_edit")
    public String edit(int id, Model model){
        Product pd = productService.get(id);
        Category c = categoryService.get(pd.getCid());
        pd.setCategory(c);
        model.addAttribute("pd",pd);
        return "admin/editProduct";
    }

    @RequestMapping("admin_product_update")
    public String update(Product pd){
        productService.update(pd);

        return "redirect:/admin_product_list?cid=" + pd.getCid();
    }
}

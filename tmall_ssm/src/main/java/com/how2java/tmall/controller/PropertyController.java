package com.how2java.tmall.controller;

import java.util.List;

import com.how2java.tmall.pojo.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.how2java.tmall.pojo.Property;
import com.how2java.tmall.service.CategoryService;
import com.how2java.tmall.service.PropertyService;
import com.how2java.tmall.util.Page;

@Controller
@RequestMapping("")
public class PropertyController {

    @Autowired
    CategoryService categoryService;
    @Autowired
    PropertyService propertyService;

    @RequestMapping("admin_property_list")
    public String list(int cid, Page page, Model model){
        Category c = categoryService.get(cid);  //分页查询需要
        PageHelper.offsetPage(page.getStart(),page.getCount());
        List<Property> ps = propertyService.list(cid);
        int total = (int) new PageInfo<>(ps).getTotal();
        page.setTotal(total);
        page.setParam("&cid=" + cid);   //分页查询时调用property的list()需发送cid

        model.addAttribute("page",page);
        model.addAttribute("ps",ps);
        model.addAttribute("c",c);

        return "admin/listProperty";
    }

    @RequestMapping("admin_property_add")
    public String add(Property p){
        propertyService.add(p);

        return "redirect:/admin_property_list?cid=" + p.getCid();
    }

    @RequestMapping("admin_property_delete")
    public String delete(int id){
        Property p = propertyService.get(id);
        propertyService.delete(id);

        return "redirect:/admin_property_list?cid=" + p.getCid();
    }

    @RequestMapping("admin_property_edit")
    public String edit(int id, Model model){
        Property p = propertyService.get(id);
        Category c = categoryService.get(p.getCid());
        p.setCategory(c);
        model.addAttribute("p",p);
        return "admin/editProperty";
    }

    @RequestMapping("admin_property_update")
    public String update(Property p){
        propertyService.update(p);

        return "redirect:/admin_property_list?cid=" + p.getCid();
    }

}
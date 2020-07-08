package com.study.tmall.controller;

import com.study.tmall.pojo.Category;
import com.study.tmall.pojo.Product;
import com.study.tmall.pojo.PropertyValue;
import com.study.tmall.service.CategoryService;
import com.study.tmall.service.ProductService;
import com.study.tmall.service.PropertyValueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("")
public class ProductValueController {

    @Autowired
    private ProductService productService;
    @Autowired
    private PropertyValueService propertyValueService;
    @Autowired
    private CategoryService categoryService;

    @RequestMapping("admin_propertyValue_edit")
    public String edit(int pid, Model model){
        Product p = productService.get(pid);
        Category c = categoryService.get(p.getCid());
        p.setCategory(c);

        propertyValueService.init(p);
        List<PropertyValue> pvs = propertyValueService.list(p.getId());

        model.addAttribute("p",p);
        model.addAttribute("pvs",pvs);

        return "admin/editPropertyValue";
    }

    @ResponseBody
    @RequestMapping("admin_propertyValue_update")
    public String update(PropertyValue pv){
        int flag = propertyValueService.update(pv);

        return flag == 1 ? "success":"fail";
    }

}

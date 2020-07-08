package com.study.tmall.mapper;

import com.study.tmall.pojo.Category;
import com.study.tmall.pojo.CategoryExample;
import java.util.List;

public interface CategoryMapper {

    //	按主键删除
    int deleteByPrimaryKey(Integer id);

    //	插入数据（返回值为ID），所有的字段都会添加一遍即使没有值
    int insert(Category record);

    //  只给有值的字段赋值（会对传进来的值做非空判断）
    int insertSelective(Category record);

    //  按条件查询
    List<Category> selectByExample(CategoryExample example);

    //  按主键查询
    Category selectByPrimaryKey(Integer id);

    //  按主键更新值不为null的字段
    int updateByPrimaryKeySelective(Category record);

    //  按主键更新
    int updateByPrimaryKey(Category record);
}
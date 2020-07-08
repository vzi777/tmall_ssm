package com.study.tmall.mapper;

import com.study.tmall.pojo.PropertyValue;
import com.study.tmall.pojo.PropertyValueExample;
import java.util.List;

public interface PropertyValueMapper {
    int deleteByPrimaryKey(PropertyValueExample id);

    int insert(PropertyValue record);

    int insertSelective(PropertyValue record);

    List<PropertyValue> selectByExample(PropertyValueExample example);

    PropertyValue selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(PropertyValue record);

    int updateByPrimaryKey(PropertyValue record);
}
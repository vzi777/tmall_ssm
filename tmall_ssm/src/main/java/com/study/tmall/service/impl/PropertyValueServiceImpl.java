package com.study.tmall.service.impl;

import com.study.tmall.mapper.PropertyValueMapper;
import com.study.tmall.pojo.Product;
import com.study.tmall.pojo.Property;
import com.study.tmall.pojo.PropertyValue;
import com.study.tmall.pojo.PropertyValueExample;
import com.study.tmall.service.PropertyService;
import com.study.tmall.service.PropertyValueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PropertyValueServiceImpl implements PropertyValueService {

    @Autowired
    private PropertyValueMapper propertyValueMapper;
    @Autowired
    private PropertyService propertyService;

    //初始时，表中没有属性值的关系数据，无法显示
    @Override
    public void init(Product p) {
        List<Property> pts = propertyService.list(p.getCid());

        for(Property pt : pts){
            PropertyValue pv = get(p.getId(),pt.getId());
            if(null == pv){
                //没有属性值关联信息,初始化信息
                pv = new PropertyValue();
                pv.setPid(p.getId());
                pv.setPtid(pt.getId());
                propertyValueMapper.insert(pv);
            }
        }
    }

    @Override
    public int update(PropertyValue pv) {
        return propertyValueMapper.updateByPrimaryKeySelective(pv);
    }

    @Override
    public PropertyValue get(int pid, int ptid) {
        //使用pid和ptid得到属性值集合，只有第一个值有效，其他为空；由于无法获取pvid，故难以通过pvid获取属性值对象
        PropertyValueExample example = new PropertyValueExample();
        example.createCriteria().andPidEqualTo(pid).andPtidEqualTo(ptid);
        List<PropertyValue> pvs = propertyValueMapper.selectByExample(example);
        if( pvs.isEmpty() ){
            return null;
        }

        return pvs.get(0);
    }

    @Override
    public List<PropertyValue> list(int pid) {
        PropertyValueExample example = new PropertyValueExample();
        example.createCriteria().andPidEqualTo(pid);
        List<PropertyValue> result = propertyValueMapper.selectByExample(example);
        //给每个属性值注入属性参数
        for( PropertyValue pv : result){
            Property property = propertyService.get(pv.getPtid());
            pv.setProperty(property);
        }

        return result;
    }
}

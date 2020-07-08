package com.study.tmall.comparator;;

import java.util.Comparator;

import com.study.tmall.pojo.Product;

public class ProductPriceComparator implements Comparator<Product>{

    @Override
    public int compare(Product p1, Product p2) {
        return Float.compare(p1.getPromotePrice(),p2.getPromotePrice());
    }

}
package com.atguigu.gmall.manager.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.gmall.bean.PmsBaseCatalog3;
import com.atguigu.gmall.manager.mapper.Catalog3Mapper;
import com.atguigu.gmall.service.Catalog3Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service
public class Catalog3ServiceImpl implements Catalog3Service {

    @Autowired
    Catalog3Mapper catalog3Mapper;

    @Override
    public List<PmsBaseCatalog3> getCatalog3(String catalog2Id) {
        PmsBaseCatalog3 pmsBaseCatalog3 = new PmsBaseCatalog3();
        pmsBaseCatalog3.setCatalog2Id(catalog2Id);
        List<PmsBaseCatalog3> catalog3List = catalog3Mapper.select(pmsBaseCatalog3);
        return catalog3List;
    }
}

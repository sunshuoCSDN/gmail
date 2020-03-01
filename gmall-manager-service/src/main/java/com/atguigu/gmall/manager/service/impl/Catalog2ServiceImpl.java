package com.atguigu.gmall.manager.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.gmall.bean.PmsBaseCatalog1;
import com.atguigu.gmall.bean.PmsBaseCatalog2;
import com.atguigu.gmall.manager.mapper.Catalog2Mapper;
import com.atguigu.gmall.service.Catalog2Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service
public class Catalog2ServiceImpl implements Catalog2Service {

    @Autowired
    Catalog2Mapper catalog2Mapper;

    @Override
    public List<PmsBaseCatalog2> getCatalog2(String catalog1Id) {
        PmsBaseCatalog2 pmsBaseCatalog2 = new PmsBaseCatalog2();
        pmsBaseCatalog2.setCatalog1Id(catalog1Id);
        List<PmsBaseCatalog2> catalog2List = catalog2Mapper.select(pmsBaseCatalog2);

        return catalog2List;
    }
}

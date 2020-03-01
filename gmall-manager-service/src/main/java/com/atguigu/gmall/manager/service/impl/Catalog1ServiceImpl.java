package com.atguigu.gmall.manager.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.gmall.bean.PmsBaseCatalog1;
import com.atguigu.gmall.service.Catalog1Service;
import com.atguigu.gmall.manager.mapper.Catalog1Mappper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service
public class Catalog1ServiceImpl implements Catalog1Service {

    @Autowired
    Catalog1Mappper catalog1Mappper;

    @Override
    public List<PmsBaseCatalog1> getCatalog1() {
        List<PmsBaseCatalog1> pmsBaseCatalog1s = catalog1Mappper.selectAll();
        return pmsBaseCatalog1s;
    }
}

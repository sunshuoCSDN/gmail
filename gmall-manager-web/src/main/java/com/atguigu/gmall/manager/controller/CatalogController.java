package com.atguigu.gmall.manager.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.gmall.bean.PmsBaseCatalog1;
import com.atguigu.gmall.bean.PmsBaseCatalog2;
import com.atguigu.gmall.bean.PmsBaseCatalog3;
import com.atguigu.gmall.service.Catalog1Service;

import com.atguigu.gmall.service.Catalog2Service;
import com.atguigu.gmall.service.Catalog3Service;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin
public class CatalogController {

    @Reference
    Catalog1Service catalog1Service;

    @Reference
    Catalog2Service catalog2Service;

    @Reference
    Catalog3Service catalog3Service;

    @RequestMapping("getCatalog1")
    public List<PmsBaseCatalog1> getCatalog1() {

        List<PmsBaseCatalog1> Catalog1List = catalog1Service.getCatalog1();

        return Catalog1List;
    }

    @RequestMapping("getCatalog2")
    public List<PmsBaseCatalog2> getCatalog2(String catalog1Id) {

        List<PmsBaseCatalog2> Catalog2List = catalog2Service.getCatalog2(catalog1Id);

        return Catalog2List;
    }

    @RequestMapping("getCatalog3")
    public List<PmsBaseCatalog3> getCatalog3(String catalog2Id) {

        List<PmsBaseCatalog3> Catalog3List = catalog3Service.getCatalog3(catalog2Id);

        return Catalog3List;
    }
}

package com.atguigu.gmall.item.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.gmall.bean.PmsProductSaleAttr;
import com.atguigu.gmall.bean.PmsSkuInfo;
import com.atguigu.gmall.bean.PmsSkuSaleAttrValue;
import com.atguigu.gmall.service.PmsProductInfoService;
import com.atguigu.gmall.service.PmsSkuInfoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Controller
public class ItemController {

    @Reference
    PmsSkuInfoService pmsSkuInfoService;

    @Reference
    PmsProductInfoService pmsProductInfoService;

    @RequestMapping("{skuId}.html")
    public String item(@PathVariable String skuId, ModelMap map) {
        PmsSkuInfo pmsSkuInfo = pmsSkuInfoService.getSkuById(skuId);
        //sku对象
        map.put("skuInfo", pmsSkuInfo);

        List<PmsProductSaleAttr> pmsProductSaleAttrs = pmsProductInfoService.spuSaleAttrListCheckBySku(pmsSkuInfo.getProductId(), pmsSkuInfo.getId());
//        List<PmsProductSaleAttr> pmsProductSaleAttrs = pmsProductInfoService.spuSaleAttrListCheckBySku(pmsSkuInfo.getProductId());
        //销售属性列表
        map.put("spuSaleAttrListCheckBySku", pmsProductSaleAttrs);

        //查询当前sku的spu的其他sku的集合hash表
        HashMap<String, String> skuSaleAttrHash = new HashMap<>();
        List<PmsSkuInfo> pmsSkuInfos =  pmsProductInfoService.getSkuSaleAttrValueListBySpu(pmsSkuInfo.getProductId());
        System.out.println(pmsSkuInfos);
        for (PmsSkuInfo skuInfo : pmsSkuInfos) {
            String k = "";
            String v = skuInfo.getId();
            List<PmsSkuSaleAttrValue> skuSaleAttrValueList = skuInfo.getSkuSaleAttrValueList();
            for (PmsSkuSaleAttrValue pmsSkuSaleAttrValue : skuSaleAttrValueList) {

                k += pmsSkuSaleAttrValue.getSaleAttrValueId() + "|";

            }
            skuSaleAttrHash.put(k, v);
        }

        //把map变成json串
        String valuesSkuJson = com.alibaba.fastjson.JSON.toJSONString(skuSaleAttrHash);

        map.put("valuesSkuJson", valuesSkuJson);
        
        return "item";
    }

    @RequestMapping("shangpinxiangqing")
    public String shangpinxiangqing() {

        return "shangpinxiangqing";
    }

    @RequestMapping("index")
    public String index(ModelMap modelMap) {
        modelMap.put("hello", "hello Thymeleaf!!!!");
        ArrayList<String> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            list.add("第" + i + "条数据！！！");
        }
        modelMap.put("list", list);
        modelMap.put("checked", 0);
        return "index";
    }
}

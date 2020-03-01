package com.atguigu.gmall.manager.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.gmall.bean.PmsSkuInfo;
import com.atguigu.gmall.service.PmsSkuInfoService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@CrossOrigin
public class SkuController {

    @Reference
    PmsSkuInfoService pmsSkuInfoService;

    @RequestMapping("saveSkuInfo")
    public String  saveSkuInfo(@RequestBody PmsSkuInfo pmsSkuInfo) {

        // 将spuId封装给productId
        pmsSkuInfo.setProductId(pmsSkuInfo.getSpuId());

        // 处理默认图片
        String skuDefaultImg = pmsSkuInfo.getSkuDefaultImg();
        if(StringUtils.isBlank(skuDefaultImg)){
            pmsSkuInfo.setSkuDefaultImg(pmsSkuInfo.getSkuImageList().get(0).getImgUrl());
        }


        pmsSkuInfoService.saveSkuInfo(pmsSkuInfo);


        pmsSkuInfoService.saveSkuInfo(pmsSkuInfo);

        return "success";
    }

}

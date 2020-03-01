package com.atguigu.gmall.manager.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.gmall.bean.PmsProductImage;
import com.atguigu.gmall.bean.PmsProductInfo;
import com.atguigu.gmall.bean.PmsProductSaleAttr;
import com.atguigu.gmall.service.PmsProductInfoService;
import com.atguigu.gmall.utils.PmsUploadUtil;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@CrossOrigin
public class SpuController {


    @Reference
    PmsProductInfoService pmsProductInfoService;

    @RequestMapping("spuList")
    public List<PmsProductInfo> spuList(String catalog3Id) {
        List<PmsProductInfo> pmsProductInfoList = pmsProductInfoService.spuList(catalog3Id);
        return pmsProductInfoList;
    }

    @RequestMapping("saveSpuInfo")
    public String saveSpuInfo(@RequestBody PmsProductInfo pmsProductInfo) {
        pmsProductInfoService.saveSpuInfo(pmsProductInfo);
        return "success";
    }

    @RequestMapping("fileUpload")
    public String fileUpload(@RequestParam("file")MultipartFile multipartFile) {

        String imgUrl = PmsUploadUtil.uploadImage(multipartFile);
        System.out.println(imgUrl);
        return imgUrl;
    }

    @RequestMapping("spuSaleAttrList")
    public List<PmsProductSaleAttr> spuSaleAttrList(String spuId) {
        List<PmsProductSaleAttr> pmsProductSaleAttrs = pmsProductInfoService.spuSaleAttrList(spuId);
        return pmsProductSaleAttrs;
    }

    @RequestMapping("spuImageList")
    public List<PmsProductImage> spuImageList(String spuId) {

        List<PmsProductImage> pmsProductImageList = pmsProductInfoService.spuImageList(spuId);
        return pmsProductImageList;
    }
}

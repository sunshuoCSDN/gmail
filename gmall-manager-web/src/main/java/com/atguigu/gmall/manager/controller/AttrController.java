package com.atguigu.gmall.manager.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.gmall.bean.PmsBaseAttrInfo;
import com.atguigu.gmall.bean.PmsBaseAttrValue;
import com.atguigu.gmall.bean.PmsBaseSaleAttr;
import com.atguigu.gmall.service.PmsBaseAttrInfoService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
public class AttrController {

    @Reference
    PmsBaseAttrInfoService pmsBaseAttrInfoService;


    @RequestMapping("attrInfoList")
    public List<PmsBaseAttrInfo> attrInfoList(String catalog3Id) {
        List<PmsBaseAttrInfo> attrInfoList = pmsBaseAttrInfoService.getattrInfoList(catalog3Id);
        return attrInfoList;
    }

    @RequestMapping("saveAttrInfo")
    public String saveAttrInfo(@RequestBody PmsBaseAttrInfo pmsBaseAttrInfo) {
        pmsBaseAttrInfoService.saveAttrInfo(pmsBaseAttrInfo);

        return "success";
    }

    @RequestMapping("getAttrValueList")
    public List<PmsBaseAttrValue> getAttrValueList(String attrId) {


        return pmsBaseAttrInfoService.getAttrValueList(attrId);
    }

    @RequestMapping("baseSaleAttrList")
    public List<PmsBaseSaleAttr> baseSaleAttrList() {
        List<PmsBaseSaleAttr> pmsBaseSaleAttrList = pmsBaseAttrInfoService.baseSaleAttrList();
        return pmsBaseSaleAttrList;
    }


}

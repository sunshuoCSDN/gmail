package com.atguigu.gmall.seaech.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.gmall.annotations.LoginRequired;
import com.atguigu.gmall.bean.*;
import com.atguigu.gmall.service.PmsBaseAttrInfoService;
import com.atguigu.gmall.service.SearchService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.*;

@Controller
public class SearchController {

    @Reference
    SearchService searchService;

    @Reference
    PmsBaseAttrInfoService pmsBaseAttrInfoService;

    @RequestMapping("list.html")
    public String list(PmsSearchParam pmsSearchParam, ModelMap modelMap) {// 三级分类id、关键字、

        // 调用搜索服务，返回搜索结果
        List<PmsSearchSkuInfo> pmsSearchSkuInfos = searchService.list(pmsSearchParam);
        modelMap.put("skuLsInfoList", pmsSearchSkuInfos);

        //抽取检索结果所包含的平台属性集合
        Set<String> setId = new HashSet<>();
        for (PmsSearchSkuInfo pmsSearchSkuInfo : pmsSearchSkuInfos) {
            List<PmsSkuAttrValue> skuAttrValueList = pmsSearchSkuInfo.getSkuAttrValueList();
            for (PmsSkuAttrValue pmsSkuAttrValue : skuAttrValueList) {
                String valueId = pmsSkuAttrValue.getValueId();
                setId.add(valueId);
            }
        }

        //根据valueId讲属性列表查询出来
        List<PmsBaseAttrInfo> pmsBaseAttrInfos = pmsBaseAttrInfoService.getPmsBaseAttrInfos(setId);
        modelMap.put("attrList", pmsBaseAttrInfos);

        //对平台属性进一步处理，去掉当前valueId所在的属性组
        String[] delValueIds = pmsSearchParam.getValueId();

        if (delValueIds != null) {
            List<PmsSearchCrumb> pmsSearchCrumbs = new ArrayList<>();
            for (String delValueId : delValueIds) {
                Iterator<PmsBaseAttrInfo> iterator = pmsBaseAttrInfos.iterator();
                PmsSearchCrumb pmsSearchCrumb = new PmsSearchCrumb();
                //生成面包屑的参数

                pmsSearchCrumb.setValueId(delValueId);
                pmsSearchCrumb.setUrlParam(getUrlParamForCrumb(pmsSearchParam, delValueId));
                while (iterator.hasNext()) {
                    PmsBaseAttrInfo pmsBaseAttrInfo = iterator.next();
                    List<PmsBaseAttrValue> attrValueList = pmsBaseAttrInfo.getAttrValueList();

                    for (PmsBaseAttrValue pmsBaseAttrValue : attrValueList) {
                        String valueId = pmsBaseAttrValue.getId();

                        if (delValueId.equals(valueId)) {
                            pmsSearchCrumb.setValueName(pmsBaseAttrValue.getValueName());
                            //删除该属性值所在的属性组
                            iterator.remove();
                        }

                    }

                }
                pmsSearchCrumbs.add(pmsSearchCrumb);

            }
            modelMap.put("attrValueSelectedList", pmsSearchCrumbs);
        }

        //url
        String urlParam = StringgetUrlParam(pmsSearchParam);
        modelMap.put("urlParam", urlParam);

        String keyword = pmsSearchParam.getKeyword();
        if (StringUtils.isNotBlank(keyword)) {
            modelMap.put("keyword", keyword);
        }

        //面包屑
    /*    List<PmsSearchCrumb> pmsSearchCrumbs = new ArrayList<>();
        if(delValueIds != null) {
            //如果valueId不等于空，说明当前请求中包含属性的参数，每一个参数都会生成一个面包屑
            for (String delValueId : delValueIds) {
                PmsSearchCrumb pmsSearchCrumb = new PmsSearchCrumb();
                //生成面包屑的参数
                pmsSearchCrumb.setValueName(delValueId);
                pmsSearchCrumb.setValueId(delValueId);
                pmsSearchCrumb.setUrlParam(getUrlParamForCrumb(pmsSearchParam, delValueId));
                pmsSearchCrumbs.add(pmsSearchCrumb);
            }
        }
        System.out.println(pmsSearchCrumbs);
        modelMap.put("attrValueSelectedList", pmsSearchCrumbs);*/

        return "list";
    }

    private String getUrlParamForCrumb(PmsSearchParam pmsSearchParam, String delValueId) {
        String catalog3Id = pmsSearchParam.getCatalog3Id();
        String keyword = pmsSearchParam.getKeyword();
        String[] skuAttrValue = pmsSearchParam.getValueId();

        String urlParam = "";

        if (StringUtils.isNotBlank(catalog3Id)) {
            if (StringUtils.isNotBlank(urlParam)) {
                urlParam = urlParam + "&";
            }
            urlParam = urlParam + "catalog3Id=" + catalog3Id;
        }
        if (StringUtils.isNotBlank(keyword)) {
            if (StringUtils.isNotBlank(urlParam)) {
                urlParam = urlParam + "&";
            }
            urlParam = urlParam + "keyword=" + keyword;
        }

        if (skuAttrValue != null) {
            for (String pmsSkuAttrValue : skuAttrValue) {
                if (!pmsSkuAttrValue.equals(delValueId)) {
                    urlParam = urlParam + "&valueId=" + pmsSkuAttrValue;
                }
            }
        }
        return urlParam;
    }

    private String StringgetUrlParam(PmsSearchParam pmsSearchParam) {
        String catalog3Id = pmsSearchParam.getCatalog3Id();
        String keyword = pmsSearchParam.getKeyword();
        String[] skuAttrValue = pmsSearchParam.getValueId();

        String urlParam = "";

        if (StringUtils.isNotBlank(catalog3Id)) {
            if (StringUtils.isNotBlank(urlParam)) {
                urlParam = urlParam + "&";
            }
            urlParam = urlParam + "catalog3Id=" + catalog3Id;
        }
        if (StringUtils.isNotBlank(keyword)) {
            if (StringUtils.isNotBlank(urlParam)) {
                urlParam = urlParam + "&";
            }
            urlParam = urlParam + "keyword=" + keyword;
        }

        if (skuAttrValue != null) {
            for (String pmsSkuAttrValue : skuAttrValue) {
                urlParam = urlParam + "&valueId=" + pmsSkuAttrValue;
            }
        }
        return urlParam;
    }

    @RequestMapping("index")
    @LoginRequired(loginSuccess = false)
    public String index() {
        return "index";
    }
}

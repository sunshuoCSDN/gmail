package com.atguigu.gmall.manager.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.gmall.bean.*;
import com.atguigu.gmall.manager.mapper.PmsProductImageMapper;
import com.atguigu.gmall.manager.mapper.PmsProductInfoMapper;
import com.atguigu.gmall.manager.mapper.PmsProductSaleAttrMapper;
import com.atguigu.gmall.manager.mapper.PmsProductSaleAttrValueMapper;
import com.atguigu.gmall.service.PmsProductInfoService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service
public class PmsProductInfoServiceImpl implements PmsProductInfoService {

    @Autowired
    PmsProductInfoMapper pmsProductInfoMapper;

    @Autowired
    PmsProductSaleAttrMapper pmsProductSaleAttrMapper;

    @Autowired
    PmsProductSaleAttrValueMapper pmsProductSaleAttrValueMapper;

    @Autowired
    PmsProductImageMapper pmsProductImageMapper;



    @Override
    public List<PmsProductInfo> spuList(String catalog3Id) {
        PmsProductInfo pmsProductInfo = new PmsProductInfo();
        pmsProductInfo.setCatalog3Id(catalog3Id);
        List<PmsProductInfo> pmsProductInfoList = pmsProductInfoMapper.select(pmsProductInfo);
        return pmsProductInfoList;
    }


    @Override
    public String saveSpuInfo(PmsProductInfo pmsProductInfo) {
        pmsProductInfoMapper.insertSelective(pmsProductInfo);
        List<PmsProductSaleAttr> pmsProductSaleAttrList = pmsProductInfo.getSpuSaleAttrList();
        for (PmsProductSaleAttr pmsProductSaleAttr : pmsProductSaleAttrList) {
            pmsProductSaleAttr.setProductId(pmsProductInfo.getId());
            List<PmsProductSaleAttrValue> pmsProductSaleAttrValueList = pmsProductSaleAttr.getSpuSaleAttrValueList();
            pmsProductSaleAttrMapper.insertSelective(pmsProductSaleAttr);
            for (PmsProductSaleAttrValue pmsProductSaleAttrValue : pmsProductSaleAttrValueList) {
                pmsProductSaleAttrValue.setProductId(pmsProductInfo.getId());
                pmsProductSaleAttrValueMapper.insert(pmsProductSaleAttrValue);
            }

        }

        List<PmsProductImage> spuImageList = pmsProductInfo.getSpuImageList();
        for (PmsProductImage pmsProductImage : spuImageList) {
            pmsProductImage.setProductId(pmsProductInfo.getId());
            pmsProductImageMapper.insert(pmsProductImage);
        }
        return "success";
    }

    @Override
    public List<PmsProductImage> spuImageList(String spuId) {
        PmsProductImage pmsProductImage = new PmsProductImage();
        pmsProductImage.setProductId(spuId);
        List<PmsProductImage> pmsProductImageList = pmsProductImageMapper.select(pmsProductImage);
        return pmsProductImageList;
    }

    @Override
    public List<PmsProductSaleAttr> spuSaleAttrList(String spuId) {
        PmsProductSaleAttr pmsProductSaleAttr = new PmsProductSaleAttr();
        pmsProductSaleAttr.setProductId(spuId);
        List<PmsProductSaleAttr> pmsProductSaleAttrList = pmsProductSaleAttrMapper.select(pmsProductSaleAttr);
        System.out.println(pmsProductSaleAttrList + "***");
        for (PmsProductSaleAttr productSaleAttr : pmsProductSaleAttrList) {
            PmsProductSaleAttrValue pmsProductSaleAttrValue = new PmsProductSaleAttrValue();
            pmsProductSaleAttrValue.setProductId(spuId);
            pmsProductSaleAttrValue.setSaleAttrId(productSaleAttr.getSaleAttrId());
            List<PmsProductSaleAttrValue> productSaleAttrValueList = pmsProductSaleAttrValueMapper.select(pmsProductSaleAttrValue);
            productSaleAttr.setSpuSaleAttrValueList(productSaleAttrValueList);
        }

        return pmsProductSaleAttrList;
    }

    @Override
    public List<PmsProductSaleAttr> spuSaleAttrListCheckBySku(String produceId) {
        PmsProductSaleAttr pmsProductSaleAttr = new PmsProductSaleAttr();
        pmsProductSaleAttr.setProductId(produceId);
        List<PmsProductSaleAttr> productSaleAttrList = pmsProductSaleAttrMapper.select(pmsProductSaleAttr);

        for (PmsProductSaleAttr productSaleAttr : productSaleAttrList) {
            String saleAttrId = productSaleAttr.getSaleAttrId();

            PmsProductSaleAttrValue pmsProductSaleAttrValue = new PmsProductSaleAttrValue();
            pmsProductSaleAttrValue.setSaleAttrId(saleAttrId);
            pmsProductSaleAttrValue.setProductId(produceId);
            List<PmsProductSaleAttrValue> productSaleAttrValues = pmsProductSaleAttrValueMapper.select(pmsProductSaleAttrValue);

            productSaleAttr.setSpuSaleAttrValueList(productSaleAttrValues);
        }

        return productSaleAttrList;
    }

    @Override
    public List<PmsProductSaleAttr> spuSaleAttrListCheckBySku(String productId, String skuId) {
        List<PmsProductSaleAttr> productSaleAttrList = pmsProductSaleAttrMapper.spuSaleAttrListCheckBySku(productId, skuId);
        return productSaleAttrList;
    }

    @Override
    public List<PmsSkuInfo> getSkuSaleAttrValueListBySpu(String productId) {
        List<PmsSkuInfo> pmsSkuInfos = pmsProductInfoMapper.selectSkuSaleAttrValueListBySpu(productId);
        return pmsSkuInfos;
    }


}

package com.atguigu.gmall.service;

import com.atguigu.gmall.bean.PmsSkuInfo;

import java.math.BigDecimal;
import java.util.List;

public interface PmsSkuInfoService {
    String saveSkuInfo(PmsSkuInfo pmsSkuInfo);

    PmsSkuInfo getSkuById(String skuId);

    List<PmsSkuInfo> getAllSku();

    boolean checkPrice(String productSkuId, BigDecimal price);
}

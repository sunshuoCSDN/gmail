package com.atguigu.gmall.manager.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.atguigu.gmall.bean.PmsSkuAttrValue;
import com.atguigu.gmall.bean.PmsSkuImage;
import com.atguigu.gmall.bean.PmsSkuInfo;
import com.atguigu.gmall.bean.PmsSkuSaleAttrValue;
import com.atguigu.gmall.manager.mapper.PmsSkuImageMapper;
import com.atguigu.gmall.manager.mapper.PmsSkuAttrValueMapper;
import com.atguigu.gmall.manager.mapper.PmsSkuInfoMapper;
import com.atguigu.gmall.manager.mapper.PmsSkuSaleAttrValueMapper;
import com.atguigu.gmall.service.PmsSkuInfoService;
import com.atguigu.gmall.util.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
public class PmsSkuInfoServiceImpl implements PmsSkuInfoService {

    @Autowired
    PmsSkuInfoMapper pmsSkuInfoMapper;

    @Autowired
    PmsSkuImageMapper pmsSkuImageMapper;

    @Autowired
    PmsSkuAttrValueMapper pmsSkuAttrValueMapper;

    @Autowired
    PmsSkuSaleAttrValueMapper pmsSkuSaleAttrValueMapper;
    
    @Autowired
    RedisUtil redisUtil;

    @Override
    public String saveSkuInfo(PmsSkuInfo pmsSkuInfo) {
        // 插入skuInfo
        int i = pmsSkuInfoMapper.insertSelective(pmsSkuInfo);
        String skuId = pmsSkuInfo.getId();

        // 插入平台属性关联
        List<PmsSkuAttrValue> skuAttrValueList = pmsSkuInfo.getSkuAttrValueList();
        for (PmsSkuAttrValue pmsSkuAttrValue : skuAttrValueList) {
            pmsSkuAttrValue.setSkuId(skuId);
            pmsSkuAttrValueMapper.insertSelective(pmsSkuAttrValue);
        }

        // 插入销售属性关联
        List<PmsSkuSaleAttrValue> skuSaleAttrValueList = pmsSkuInfo.getSkuSaleAttrValueList();
        for (PmsSkuSaleAttrValue pmsSkuSaleAttrValue : skuSaleAttrValueList) {
            pmsSkuSaleAttrValue.setSkuId(skuId);
            pmsSkuSaleAttrValueMapper.insertSelective(pmsSkuSaleAttrValue);
        }

        // 插入图片信息
        List<PmsSkuImage> skuImageList = pmsSkuInfo.getSkuImageList();
        for (PmsSkuImage pmsSkuImage : skuImageList) {
            pmsSkuImage.setSkuId(skuId);
            pmsSkuImageMapper.insertSelective(pmsSkuImage);
        }
        return "success";
    }

    public PmsSkuInfo getSkuByIdFromDB(String skuId) {
        //skuinfo信息
        PmsSkuInfo pmsSkuInfo = new PmsSkuInfo();
        pmsSkuInfo.setId(skuId);
        PmsSkuInfo skuInfo = pmsSkuInfoMapper.selectOne(pmsSkuInfo);

        //图片
        PmsSkuImage pmsSkuImage = new PmsSkuImage();
        pmsSkuImage.setSkuId(skuId);
        List<PmsSkuImage> skuImages = pmsSkuImageMapper.select(pmsSkuImage);
        skuInfo.setSkuImageList(skuImages);
        return skuInfo;
    }

    @Override
    public PmsSkuInfo getSkuById (String skuId) {
        PmsSkuInfo pmsSkuInfo = new PmsSkuInfo();

        //链接缓存
        Jedis jedis = redisUtil.getJedis();

        //查询缓存
        String skuKey = "sku:" + skuId + ":info";
        String skuJson = jedis.get(skuKey);
        if(StringUtils.isNotBlank(skuJson)) {//if(skuJson!=null&&!skuJson.equals(""))
            pmsSkuInfo = JSON.parseObject(skuJson, PmsSkuInfo.class);
        } else {
            // 如果缓存中没有，查询mysql

            String token = UUID.randomUUID().toString();
            // 设置分布式锁
            String result = jedis.set("sku:" + skuId + ":lock", token, "nx", "ex", 10);

            if(StringUtils.isNotBlank(result) && result.equals("OK")) {
                // 设置成功，有权在10秒的过期时间内访问数据库
                pmsSkuInfo = getSkuByIdFromDB(skuId);

                System.out.println(pmsSkuInfo+"--------------------->");
                if(pmsSkuInfo != null) {
                    // mysql查询结果存入redis
                    jedis.set(skuKey, JSON.toJSONString(pmsSkuInfo));
                } else {
                    System.out.println("ccccc");
                    //数据库中不存在该sku
                    //为防止缓存穿透，将空或空字符串设置给redis
                    jedis.setex(skuKey, 60*3, JSON.toJSONString(""));
                }

                String lockToken = jedis.get("sku:" + skuId + ":lock");
                if(StringUtils.isNotBlank(token) && lockToken.equals(token)) {
                    jedis.del("sku:" + skuId + ":lock");
                }
            } else {
                // 设置失败，自旋（该线程在睡眠几秒后，重新尝试访问本方法）
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return getSkuById(skuId);
            }


        }
        jedis.close();

        return pmsSkuInfo;
    }

    @Override
    public List<PmsSkuInfo> getAllSku() {
        List<PmsSkuInfo> pmsSkuInfos = pmsSkuInfoMapper.selectAll();
        for (PmsSkuInfo pmsSkuInfo : pmsSkuInfos) {
            String SkuId = pmsSkuInfo.getId();

            PmsSkuAttrValue pmsSkuAttrValue = new PmsSkuAttrValue();
            pmsSkuAttrValue.setSkuId(SkuId);
            List<PmsSkuAttrValue> pmsSkuAttrValueList = pmsSkuAttrValueMapper.select(pmsSkuAttrValue);
            pmsSkuInfo.setSkuAttrValueList(pmsSkuAttrValueList);
        }
        return pmsSkuInfos;
    }

    @Override
    public boolean checkPrice(String productSkuId, BigDecimal price) {
        boolean b = false;
        PmsSkuInfo pmsSkuInfo = new PmsSkuInfo();
        pmsSkuInfo.setId(productSkuId);
        PmsSkuInfo skuInfo = pmsSkuInfoMapper.selectOne(pmsSkuInfo);
        if(skuInfo.getPrice().compareTo(price)==0) {
            b = true;
        }
        return b;
    }
}

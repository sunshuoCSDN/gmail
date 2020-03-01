package com.atguigu.gmall.manager;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.gmall.bean.PmsBaseAttrInfo;
import com.atguigu.gmall.bean.PmsSkuInfo;
import com.atguigu.gmall.service.PmsBaseAttrInfoService;
import com.atguigu.gmall.service.PmsSkuInfoService;
import com.atguigu.gmall.util.RedisUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import redis.clients.jedis.Jedis;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GmallManageServiceApplicationTests {

    @Autowired
    RedisUtil redisUtil;

    @Autowired
    PmsSkuInfoService pmsSkuInfoService;

    @Reference
    PmsBaseAttrInfoService pmsBaseAttrInfoService;

    @Test
    public void contextLoads() {
        Jedis jedis = redisUtil.getJedis();
        System.out.println("--------------------------->");
        System.out.println(jedis.set("f", "123")+"_________________________>");
    }

    @Test
    public void PmsSkuInfo() {
        List<PmsSkuInfo> allSku = pmsSkuInfoService.getAllSku();
        System.out.println("----------->"+allSku);
    }

    @Test
    public void joinTest() {
        Set<String> strings = new HashSet<>();
        strings.add("1");
        strings.add("2");
        strings.add("3");
        strings.add("4");
        String join = StringUtils.join(strings, ",");
        System.out.println(join);
    }

    @Test
    public void pmsBaseAttrInfos() {
        Set<String> strings = new HashSet<>();
        strings.add("44");
        strings.add("88");
        strings.add("66");
        strings.add("67");
        strings.add("46");
        List<PmsBaseAttrInfo> pmsBaseAttrInfos = pmsBaseAttrInfoService.getPmsBaseAttrInfos(strings);
        System.out.println(pmsBaseAttrInfos);
    }

}

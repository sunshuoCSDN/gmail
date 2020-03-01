package com.atguigu.gmall.search;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.gmall.bean.PmsSearchSkuInfo;
import com.atguigu.gmall.bean.PmsSkuInfo;
import com.atguigu.gmall.service.PmsSkuInfoService;
import io.searchbox.client.JestClient;
import io.searchbox.core.Index;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GmallSearchServiceApplicationTests {

    @Reference
    PmsSkuInfoService pmsSkuInfoService;

    @Autowired
    JestClient jestClient;

    @Test
    public void contextLoads() throws IOException {
        List<PmsSkuInfo> pmsSkuInfos = new ArrayList<>();
        //查询mysql数据
        pmsSkuInfos = pmsSkuInfoService.getAllSku();
        System.out.println(pmsSkuInfos);
        //转化为es的数据结构
        List<PmsSearchSkuInfo> pmsSearchSkuInfos = new ArrayList<>();
        for (PmsSkuInfo pmsSkuInfo : pmsSkuInfos) {
            PmsSearchSkuInfo pmsSearchSkuInfo = new PmsSearchSkuInfo();

            BeanUtils.copyProperties(pmsSkuInfo, pmsSearchSkuInfo);

            pmsSearchSkuInfo.setId(Long.parseLong(pmsSkuInfo.getId()));

            pmsSearchSkuInfos.add(pmsSearchSkuInfo);
        }
        System.out.println(pmsSearchSkuInfos);

        //导入es
        for (PmsSearchSkuInfo pmsSearchSkuInfo : pmsSearchSkuInfos) {
            //第一个参数数据 第二个参数库名 第三个参数表明 第四个参数主键
            Index put = new Index.Builder(pmsSearchSkuInfo).index("gmall0105").type("PmsSkuInfo").id(pmsSearchSkuInfo.getId()+"").build();
            jestClient.execute(put);
        }

    }

}

package com.atguigu.gmall.manager.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.gmall.bean.PmsBaseAttrInfo;
import com.atguigu.gmall.bean.PmsBaseAttrValue;
import com.atguigu.gmall.bean.PmsBaseSaleAttr;
import com.atguigu.gmall.manager.mapper.PmsBaseAttrInfoMapper;
import com.atguigu.gmall.manager.mapper.PmsBaseAttrValueMapper;
import com.atguigu.gmall.manager.mapper.PmsBaseSaleAttrMapper;
import com.atguigu.gmall.service.PmsBaseAttrInfoService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.entity.Example;

import java.util.List;
import java.util.Set;

@Service
public class PmsBaseAttrInfoServiceImpl implements PmsBaseAttrInfoService {

    @Autowired
    PmsBaseAttrInfoMapper pmsBaseAttrInfoMapper;

    @Autowired
    PmsBaseAttrValueMapper pmsBaseAttrValueMapper;

    @Autowired
    PmsBaseSaleAttrMapper pmsBaseSaleAttrMapper;

    @Override
    public List<PmsBaseAttrInfo> getattrInfoList(String catalog3Id) {
        PmsBaseAttrInfo pmsBaseAttrInfo = new PmsBaseAttrInfo();
        pmsBaseAttrInfo.setCatalog3Id(catalog3Id);
        List<PmsBaseAttrInfo> attrInfoList = pmsBaseAttrInfoMapper.select(pmsBaseAttrInfo);
        PmsBaseAttrValue pmsBaseAttrValue = new PmsBaseAttrValue();
        for (PmsBaseAttrInfo baseAttrInfo : attrInfoList) {
            pmsBaseAttrValue.setAttrId(baseAttrInfo.getId());
            List<PmsBaseAttrValue> baseAttrValues = pmsBaseAttrValueMapper.select(pmsBaseAttrValue);
            baseAttrInfo.setAttrValueList(baseAttrValues);
        }

        return attrInfoList;
    }

//    PmsBaseAttrInfo{id='45', attrName='是否有root', catalog3Id='61', isEnabled='null', attrValueList=
//    PmsBaseAttrValue{id='104', valueName='有root', attrId='45', isEnabled='null', urlParam='null'},
//    PmsBaseAttrValue{id='105', valueName='无root', attrId='45', isEnabled='null', urlParam='null'},
//    PmsBaseAttrValue{id='null', valueName='自行配置', attrId='null', isEnabled='null', urlParam='null'}]}
    @Override
    public String saveAttrInfo(PmsBaseAttrInfo pmsBaseAttrInfo) {
        String attrInfoId = pmsBaseAttrInfo.getId();
        List<PmsBaseAttrValue> attrValueList = pmsBaseAttrInfo.getAttrValueList();
        if(StringUtils.isBlank(attrInfoId)) {
            //id为空 插入

            pmsBaseAttrInfoMapper.insert(pmsBaseAttrInfo);
            for (PmsBaseAttrValue pmsBaseAttrValue : attrValueList) {
                pmsBaseAttrValue.setAttrId(attrInfoId);
                pmsBaseAttrValueMapper.insert(pmsBaseAttrValue);
            }

        } else {
            //id不为空 修改

            //修改商品属性
            Example example = new Example(PmsBaseAttrInfo.class);
            example.createCriteria().andEqualTo("id", attrInfoId);
            pmsBaseAttrInfoMapper.updateByExampleSelective(pmsBaseAttrInfo, example);

            //删除商品属性
            PmsBaseAttrValue pmsBaseAttrValueDel = new PmsBaseAttrValue();
            pmsBaseAttrValueDel.setAttrId(attrInfoId);
            pmsBaseAttrValueMapper.delete(pmsBaseAttrValueDel);

            //添加商品属性
            for (PmsBaseAttrValue pmsBaseAttrValue : attrValueList) {
                pmsBaseAttrValue.setAttrId(attrInfoId);
                pmsBaseAttrValueMapper.insert(pmsBaseAttrValue);
            }
        }
        return "success";
    }

    @Override
    public List<PmsBaseAttrValue> getAttrValueList(String attrId) {
        PmsBaseAttrValue pmsBaseAttrValue = new PmsBaseAttrValue();
        pmsBaseAttrValue.setAttrId(attrId);

        return pmsBaseAttrValueMapper.select(pmsBaseAttrValue);
    }

    @Override
    public List<PmsBaseSaleAttr> baseSaleAttrList() {
        return pmsBaseSaleAttrMapper.selectAll();
    }

    @Override
    public List<PmsBaseAttrInfo> getPmsBaseAttrInfos(Set<String> setId) {

        String valueIdStr = StringUtils.join(setId, ",");
        List<PmsBaseAttrInfo> pmsBaseAttrInfos = pmsBaseAttrInfoMapper.selectAttrByValueId(valueIdStr);
        System.out.println(pmsBaseAttrInfos);
        return pmsBaseAttrInfos;
    }
}

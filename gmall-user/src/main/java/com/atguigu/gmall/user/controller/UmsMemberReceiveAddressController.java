package com.atguigu.gmall.user.controller;


import com.atguigu.gmall.bean.UmsMemberReceiveAddress;
import com.atguigu.gmall.service.UmsMemberReceiveAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class UmsMemberReceiveAddressController {

    @Autowired
    UmsMemberReceiveAddressService umsMemberReceiveAddressService;

    @RequestMapping("getAllUmsMemberReceiveAddress")
    public List<UmsMemberReceiveAddress> getAllUmsMemberReceiveAddress() {
        List<UmsMemberReceiveAddress> allUmsMemberReceiveAddress = umsMemberReceiveAddressService.getAllUmsMemberReceiveAddress();
        return allUmsMemberReceiveAddress;
    }

    @RequestMapping("insertUmsMemberReceiveAddressService")
    public Map<String, Object> insertUmsMemberReceiveAddressService(UmsMemberReceiveAddress umsMemberReceiveAddress) {
        HashMap<String, Object> map = new HashMap<>();
        int i = umsMemberReceiveAddressService.insertUmsMemberReceiveAddress(umsMemberReceiveAddress);
        if(i > 0) {
            map.put("message", true);
        } else {
            map.put("message", false);
        }
        return map;
    }

    @RequestMapping("removerUmsMemberReceiveAddress")
    public Map<String, Object> removerUmsMemberReceiveAddress(int id) {
        HashMap<String, Object> map = new HashMap<>();
        int i = umsMemberReceiveAddressService.removerUmsMemberReceiveAddress(id);
        if(i > 0) {
            map.put("message", true);
        } else {
            map.put("message", false);
        }
        return map;
    }

    @RequestMapping("updateUmsMemberReceiveAddress")
    public Map<String, Object> updateUmsMemberReceiveAddress(UmsMemberReceiveAddress umsMemberReceiveAddress) {
        HashMap<String, Object> map = new HashMap<>();
        int i = umsMemberReceiveAddressService.updateUmsMemberReceiveAddress(umsMemberReceiveAddress);
        if(i > 0) {
            map.put("message", true);
        } else {
            map.put("message", false);
        }
        return map;
    }
}

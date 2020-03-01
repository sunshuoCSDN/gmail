package com.atguigu.gmall.user.service.impl;


import com.atguigu.gmall.bean.UmsMemberReceiveAddress;
import com.atguigu.gmall.service.UmsMemberReceiveAddressService;
import com.atguigu.gmall.user.mapper.UmsMemberReceiveAddressMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UmsMemberReceiveAddressServiceImpl implements UmsMemberReceiveAddressService {

    @Autowired
    UmsMemberReceiveAddressMapper umsMemberReceiveAddressMapper;

    @Override
    public List<UmsMemberReceiveAddress> getAllUmsMemberReceiveAddress() {
        List<UmsMemberReceiveAddress> umsMemberReceiveAddresses = umsMemberReceiveAddressMapper.selectAll();
        return umsMemberReceiveAddresses;
    }

    @Override
    public int insertUmsMemberReceiveAddress(UmsMemberReceiveAddress umsMemberReceiveAddress) {
        int i = umsMemberReceiveAddressMapper.insert(umsMemberReceiveAddress);
        return i;
    }

    @Override
    public int removerUmsMemberReceiveAddress(int id) {
        UmsMemberReceiveAddress umsMemberReceiveAddress = new UmsMemberReceiveAddress();
        umsMemberReceiveAddress.setId(String.valueOf(id));
        int i = umsMemberReceiveAddressMapper.delete(umsMemberReceiveAddress);
        return i;
    }

    @Override
    public int updateUmsMemberReceiveAddress(UmsMemberReceiveAddress umsMemberReceiveAddress) {
        int i = umsMemberReceiveAddressMapper.updateByPrimaryKeySelective(umsMemberReceiveAddress);
        return i;
    }
}

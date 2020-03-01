package com.atguigu.gmall.service;


import com.atguigu.gmall.bean.UmsMemberReceiveAddress;

import java.util.List;

public interface UmsMemberReceiveAddressService {
    List<UmsMemberReceiveAddress> getAllUmsMemberReceiveAddress();

    int insertUmsMemberReceiveAddress(UmsMemberReceiveAddress umsMemberReceiveAddress);

    int removerUmsMemberReceiveAddress(int id);

    int updateUmsMemberReceiveAddress(UmsMemberReceiveAddress umsMemberReceiveAddress);
}

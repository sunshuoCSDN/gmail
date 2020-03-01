package com.atguigu.gmall.service;



import com.atguigu.gmall.bean.UmsMember;
import com.atguigu.gmall.bean.UmsMemberReceiveAddress;

import java.util.List;

public interface UserService {
    List<UmsMember> getAllUser();


    int addUmsMember(UmsMember umsMember);

    int removerUserById(int id);

    int updateUser(UmsMember umsMember);

    UmsMember login(UmsMember umsMember);

    void addUserToken(String token, String id);

    UmsMember checkOauthUser(UmsMember umsCheck);

    void addOauthUser(UmsMember umsMember);

    List<UmsMemberReceiveAddress> getReceiveAddressByMemberId(String memberId);

    UmsMemberReceiveAddress getReceiveAddressById(String receiveAddressId);
}

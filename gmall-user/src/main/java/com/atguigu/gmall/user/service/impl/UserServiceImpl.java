package com.atguigu.gmall.user.service.impl;


import com.atguigu.gmall.bean.UmsMember;
import com.atguigu.gmall.bean.UmsMemberReceiveAddress;
import com.atguigu.gmall.service.UserService;
import com.atguigu.gmall.user.mapper.UmsMemberReceiveAddressMapper;
import com.atguigu.gmall.user.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserMapper userMapper;

    @Autowired
    UmsMemberReceiveAddressMapper umsMemberReceiveAddressMapper;

    @Override
    public List<UmsMember> getAllUser() {

        List<UmsMember> umsMemberList = userMapper.selectAll();//userMapper.selectAllUser();

        return umsMemberList;
    }


    @Override
    public int addUmsMember(UmsMember umsMember) {
        int i = userMapper.insert(umsMember);
        return i;
    }

    @Override
    public int removerUserById(int id) {
        UmsMember umsMember = new UmsMember();
        umsMember.setId(String.valueOf(id));
        int i = userMapper.deleteByPrimaryKey(umsMember);
        return i;
    }

    @Override
    public int updateUser(UmsMember umsMember) {
        int i = userMapper.updateByPrimaryKeySelective(umsMember);
        return i;
    }

    @Override
    public UmsMember login(UmsMember umsMember) {
        return null;
    }
}

package com.atguigu.gmall.user.service.impl;


import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.atguigu.gmall.bean.UmsMember;
import com.atguigu.gmall.bean.UmsMemberReceiveAddress;
import com.atguigu.gmall.user.mapper.UmsMemberReceiveAddressMapper;
import com.atguigu.gmall.user.mapper.UserMapper;
import com.atguigu.gmall.service.UserService;

import com.atguigu.gmall.util.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;


import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserMapper userMapper;

    @Autowired
    UmsMemberReceiveAddressMapper umsMemberReceiveAddressMapper;

    @Autowired
    RedisUtil redisUtil;


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
        Jedis jedis = null;
        try {
            jedis = redisUtil.getJedis();
            if(jedis != null) {
                //k user:userName:password
                String umsMenberStr = jedis.get("user:" + umsMember.getUsername() + ":" + umsMember.getPassword());
                if (StringUtils.isNoneBlank(umsMenberStr)) { //密码正确
                    UmsMember umsMemberFormCache = JSON.parseObject(umsMenberStr, UmsMember.class);
                    return umsMemberFormCache;
                }
            }
            UmsMember umsMemberFormDB = loginFromDB(umsMember);
            if(umsMemberFormDB != null) {
                //放入缓存中
                jedis.setex("user:" + umsMemberFormDB.getUsername() + ":" + umsMemberFormDB.getPassword(), 60*60*2, JSON.toJSONString(umsMemberFormDB));
            }
            return umsMemberFormDB;
        } finally {
            jedis.close();
        }

    }

    @Override
    public void addUserToken(String token, String id) {
        Jedis jedis = redisUtil.getJedis();
        jedis.setex("user:"+id+":token", 60*60*2,token);
        jedis.close();
    }

    @Override
    public UmsMember checkOauthUser(UmsMember umsCheck) {
        UmsMember umsMember = userMapper.selectOne(umsCheck);
        return umsMember;
    }

    @Override
    public void addOauthUser(UmsMember umsMember) {
        userMapper.insertSelective(umsMember);
    }

    @Override
    public List<UmsMemberReceiveAddress> getReceiveAddressByMemberId(String memberId) {

        // 封装的参数对象
        UmsMemberReceiveAddress umsMemberReceiveAddress = new UmsMemberReceiveAddress();
        umsMemberReceiveAddress.setMemberId(memberId);
        List<UmsMemberReceiveAddress> umsMemberReceiveAddresses = umsMemberReceiveAddressMapper.select(umsMemberReceiveAddress);

        return umsMemberReceiveAddresses;
    }

    @Override
    public UmsMemberReceiveAddress getReceiveAddressById(String receiveAddressId) {
        UmsMemberReceiveAddress umsMemberReceiveAddress = new UmsMemberReceiveAddress();
        umsMemberReceiveAddress.setId(receiveAddressId);
        UmsMemberReceiveAddress umsMemberReceiveAddress1 = umsMemberReceiveAddressMapper.selectOne(umsMemberReceiveAddress);
        return umsMemberReceiveAddress1;
    }

    private UmsMember loginFromDB(UmsMember umsMember) {
        List<UmsMember> umsMembers = userMapper.select(umsMember);
        if(umsMembers!= null) {
            return umsMembers.get(0);
        }
        return null;
    }
}

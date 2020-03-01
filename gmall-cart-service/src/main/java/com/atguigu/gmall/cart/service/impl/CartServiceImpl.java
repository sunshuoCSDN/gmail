package com.atguigu.gmall.cart.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.atguigu.gmall.bean.OmsCartItem;
import com.atguigu.gmall.cart.mapper.OmsCartItemMapper;
import com.atguigu.gmall.service.CartService;
import com.atguigu.gmall.util.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class CartServiceImpl implements CartService {

    @Autowired
    OmsCartItemMapper omsCartItemMapper;

    @Autowired
    RedisUtil redisUtil;

    @Override
    public OmsCartItem ifCartExistByUser(String memberId, String skuId) {
        OmsCartItem omsCartItem = new OmsCartItem();
        omsCartItem.setMemberId(memberId);
        omsCartItem.setProductSkuId(skuId);
        omsCartItem = omsCartItemMapper.selectOne(omsCartItem);
        return omsCartItem;
    }

    @Override
    public void addCart(OmsCartItem omsCartItem) {
        if(StringUtils.isNoneBlank(omsCartItem.getMemberId()))
            omsCartItemMapper.insertSelective(omsCartItem);
    }

    @Override
    public void updateCart(OmsCartItem omsCartItemFromDb) {
        Example example = new Example(OmsCartItem.class);
        example.createCriteria().andEqualTo("id", omsCartItemFromDb.getId());
        omsCartItemMapper.updateByExample(omsCartItemFromDb, example);
    }

    @Override
    public void flushCartCache(String memberId) {
        OmsCartItem omsCartItem = new OmsCartItem();
        omsCartItem.setMemberId(memberId);
        List<OmsCartItem> omsCartItems = omsCartItemMapper.select(omsCartItem);

        //链接缓存
        Jedis jedis = redisUtil.getJedis();
        Map<String, String> map = new HashMap<>();
        for (OmsCartItem cartItem : omsCartItems) {
            cartItem.setTotalPrice(cartItem.getPrice().multiply(cartItem.getQuantity()));
            map.put(cartItem.getProductSkuId(), JSON.toJSONString(cartItem));
        }
        jedis.del("user:"+memberId+":cart");
        //key user:用户ID:cart
        jedis.hmset("user:"+memberId+":cart", map);
        //同步到redis缓存中
        String cartListCacher = jedis.set("cartListCacher", JSON.toJSONString(omsCartItems));
        jedis.close();
    }

    @Override
    public List<OmsCartItem> cartList(String memberId) {
        //链接缓存
        Jedis jedis = redisUtil.getJedis();
        List<OmsCartItem> omsCartItems = new ArrayList<>();
        List<String> hvals = jedis.hvals("user:" + memberId + ":cart");
        for (String hval : hvals) {
            OmsCartItem omsCartItem = JSON.parseObject(hval, OmsCartItem.class);
            omsCartItems.add(omsCartItem);
        }
        return omsCartItems;
    }

    @Override
    public void checkCart(OmsCartItem omsCartItem) {
        Example e = new Example(OmsCartItem.class);
        e.createCriteria().andEqualTo("memberId", omsCartItem.getMemberId()).andEqualTo("productSkuId", omsCartItem.getProductSkuId());
        omsCartItemMapper.updateByExampleSelective(omsCartItem, e);

        //缓存同步
        flushCartCache(omsCartItem.getMemberId());
    }

    @Override
    public void delCart() {

    }
}

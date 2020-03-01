package com.atguigu.gmall.util;

import com.alibaba.dubbo.common.json.JSON;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisUtil {

    private  JedisPool jedisPool;

    public void initPool(String host,int port ,int database){
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(200);
        poolConfig.setMaxIdle(30);
        poolConfig.setBlockWhenExhausted(true);
        poolConfig.setMaxWaitMillis(10*1000);
        poolConfig.setTestOnBorrow(true);
        jedisPool=new JedisPool(poolConfig,"49.232.169.105",6379,20*1000);
    }

    public Jedis getJedis(){
        Jedis jedis = jedisPool.getResource();
        return jedis;
    }

    public static void main(String[] args) {
        RedisUtil redisUtil = new RedisUtil();
        Jedis jedis = redisUtil.getJedis();
        String datas1 = jedis.get("datas1");

        jedis.close();
    }

}

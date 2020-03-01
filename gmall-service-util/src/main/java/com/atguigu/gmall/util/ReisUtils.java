package com.atguigu.gmall.util;

import com.alibaba.dubbo.common.json.JSON;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class ReisUtils {

    private final static String host = "49.232.169.105";

    private final static Integer port = 6379;

    private static JedisPool pool;

    static {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxIdle(5);
        pool = new JedisPool(jedisPoolConfig, host, port);
    }

    public static Jedis getJedis() {
        return pool.getResource();
    }

    /**
     * 获取分布式锁
     * @param lockkey
     * @param requestId
     * @param timeout
     * @return
     */
    public static Boolean getLok(String lockkey, String requestId, int timeout) {
        Jedis jedis = getJedis();
        String result = jedis.set(lockkey, requestId, "nx", "ex", timeout);
        if(result == "OK") {
            return true;
        }
        return false;
    }

    /**
     * 获取分布式锁
     * @param lockkey
     * @param requestId
     * @param timeout
     * @return
     */
    public static synchronized Boolean getLok2(String lockkey,  String requestId, int timeout) {
        Jedis jedis = getJedis();
        Long result = jedis.setnx(lockkey, requestId);
        if(result == 1) {
            //设置有效期
            jedis.expire(lockkey, timeout);
            return true;
        }
        return false;
    }

    /**
     * 释放锁
     * @param lockKey
     * @param requestId
     */
    public static void releaseLock(String lockKey, String requestId) {
        Jedis jedis = getJedis();
        if(requestId.equals(jedis.get(lockKey))){
            Long del = jedis.del(lockKey);
            System.out.println(del);
        }
    }


    public static void main(String[] args) {
        Jedis jedis = new Jedis(host, port);
        String datas2 = jedis.get("datas2");
        System.out.println(datas2);
        jedis.close();
//        System.out.println(getLok2("h2","bbvc", 17));
//        poolConnect();
//        releaseLock("h2", "bbb");

    }

    //redis单实例连接
    public static void singleConnect() {
        //创建一个Jedis的连接
        Jedis jedis = new Jedis(host, port);
        String h2 = jedis.get("h2");
        System.out.println(h2);
        //关闭连接
        jedis.close();
    }

    public static void poolConnect() {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxIdle(5);
        JedisPool pool = new JedisPool(jedisPoolConfig, host, port);
        Jedis jedis = pool.getResource();
        System.out.println(jedis.get("h2"));
        jedis.close();
        pool.close();
    }


}

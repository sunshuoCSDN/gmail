package com.atguigu.gmall.utils;

import java.util.HashMap;

public class TestJWT {
    public static void main(String[] args) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("memberId", "1");
        map.put("userId", "zhangsan");
        String encode = JwtUtil.encode("gmall", map, "127.0.0.1");
        System.err.println(encode);
    }
}

package com.atguigu.gmall.passport.controller;

import com.alibaba.fastjson.JSON;
import com.atguigu.gmall.util.HttpclientUtil;

import java.util.Map;

public class TestOAuth2 {

    public static String getCode() {
        //授权获取code
        String code = HttpclientUtil.doGet("https://api.weibo.com/oauth2/authorize?client_id=1128883322&response_type=code&redirect_uri=http://passport.gmall.com:8080/vlogin");
//         code = 6a714507a1fe5302eb131889fef5de1d

        // 在第一步和第二部返回回调地址之间,有一个用户操作授权的过程

        // 2 返回授权码到回调地址

//        通过回调地址获取授权码
        String s2 = "http://passport.gmail.com:8085/vlogin?code=6a714507a1fe5302eb131889fef5de1d";

        return "null";
    }

    public static String getAccessToken() {
        //        用授权码交换access_token
        String s3 = "https://api.weibo.com/oauth2/access_token?client_id=1128883322&client_secret=453155fc08c1841a908e9635baecb46e&grant_type=authorization_code&redirect_uri=http://passport.gmall.com:8080/vlogin&code=6a714507a1fe5302eb131889fef5de1d";
        String s = HttpclientUtil.doPost(s3, null);
        Map<String, String> map = JSON.parseObject(s, Map.class);
        //{"access_token":"2.00fBxwDIWHg5OB65666d06b30aFsn6","remind_in":"157679999","expires_in":157679999,"uid":"7387441587","isRealName":"true"}
        return map.get("access_token");
    }

    public static Map<String,String> getUserInfo() {
        //用access_token 换取用户信息
        String s4 = "https://api.weibo.com/2/users/show.json?access_token=2.00fBxwDIWHg5OB65666d06b30aFsn6&uid=7387441587";
        String user = HttpclientUtil.doGet(s4);
        Map<String, String> userMap = JSON.parseObject(user, Map.class);
        System.out.println(userMap);
        return userMap;
    }

    public static void main(String[] args) {
//      App Key  1128883322
//      App Secret  453155fc08c1841a908e9635baecb46e
        //授权获取code
//        String code = HttpclientUtil.doGet("https://api.weibo.com/oauth2/authorize?client_id=1128883322&response_type=code&redirect_uri=http://passport.gmall.com:8080/vlogin");
        // code = 6a714507a1fe5302eb131889fef5de1d

//        通过回调地址获取授权码
        String s2 = "http://passport.gmall.com:8080/vlogin?code=6a714507a1fe5302eb131889fef5de1d";

//        用授权码交换access_token
        String s3 = "https://api.weibo.com/oauth2/access_token?client_id=1128883322&client_secret=453155fc08c1841a908e9635baecb46e&grant_type=authorization_code&redirect_uri=http://passport.gmall.com:8080/vlogin&code=6a714507a1fe5302eb131889fef5de1d";
//        String s = HttpclientUtil.doPost(s3, null);
//        System.out.println(s);
        //{"access_token":"2.00fBxwDIWHg5OB65666d06b30aFsn6","remind_in":"157679999","expires_in":157679999,"uid":"7387441587","isRealName":"true"}

        //用access_token 换取用户信息
        String s4 = "https://api.weibo.com/2/users/show.json?access_token=2.00fBxwDIWHg5OB65666d06b30aFsn6&uid=7387441587";
        String user = HttpclientUtil.doGet(s4);
        Map<String, String> userMap = JSON.parseObject(user, Map.class);
        System.out.println(userMap);
    }

}

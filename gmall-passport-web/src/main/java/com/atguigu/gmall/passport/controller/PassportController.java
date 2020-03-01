package com.atguigu.gmall.passport.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.atguigu.gmall.bean.UmsMember;
import com.atguigu.gmall.service.UserService;
import com.atguigu.gmall.util.HttpclientUtil;
import com.atguigu.gmall.utils.JwtUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Controller
public class PassportController {

    @Reference
    UserService userService;

    @RequestMapping("vlogin")
    @ResponseBody
    public String vlogin(String code, HttpServletRequest request){

        //用code换取access_token
        String s3 = "https://api.weibo.com/oauth2/access_token?";
        Map<String,String> paramMap = new HashMap<>();
        paramMap.put("client_id","1128883322");
        paramMap.put("client_secret","453155fc08c1841a908e9635baecb46e");
        paramMap.put("grant_type","authorization_code");
        paramMap.put("redirect_uri","http://passport.gmail.com:8085/vlogin");
        paramMap.put("code",code);// 授权有效期内可以使用，没新生成一次授权码，说明用户对第三方数据进行重启授权，之前的access_token和授权码全部过期
        String access_token_json = HttpclientUtil.doPost(s3, paramMap);
        Map<String,String> map = JSON.parseObject(access_token_json, Map.class);
        String access_token = map.get("access_token");
        String uid = map.get("uid");


        //用access_token 换取用户信息
        String userUrl = "https://api.weibo.com/2/users/show.json?access_token="+access_token+"&uid="+uid;
        String userJson = HttpclientUtil.doGet(userUrl);
        Map<String, String> userMap = JSON.parseObject(userJson, Map.class);

        //用户数据保存数据库
        UmsMember umsMember = new UmsMember();
        umsMember.setSourceType("2");
        umsMember.setAccessCode(code);
        umsMember.setAccessToken(access_token);
        umsMember.setSourceUid((String)userMap.get("idstr"));
        umsMember.setCity((String)userMap.get("location"));
        umsMember.setNickname((String)userMap.get("screen_name"));
        String g = "0";
        String gender = (String)userMap.get("gender");
        if(gender.equals("m")){
            g = "1";
        }
        umsMember.setGender(g);

        UmsMember umsCheck = new UmsMember();
        umsCheck.setSourceUid(umsMember.getSourceUid());
        UmsMember umsMemberCheck = userService.checkOauthUser(umsCheck);
        if(umsMemberCheck==null) {
            userService.addOauthUser(umsMember);
        }else{
            umsMember = umsMemberCheck;
        }

        // 生成jwt的token，并且重定向到首页，携带该token
        String token = null;
        String memberId = umsMember.getId();
        String nickname = umsMember.getNickname();
        Map<String,Object> userInfo = new HashMap<>();
        userInfo.put("memberId",memberId);
        userInfo.put("nickname",nickname);


        String ip = request.getHeader("x-forwarded-for");// 通过nginx转发的客户端ip
        if(StringUtils.isBlank(ip)){
            ip = request.getRemoteAddr();// 从request中获取ip
            if(StringUtils.isBlank(ip)){
                ip = "127.0.0.1";
            }
        }

        // 按照设计的算法对参数进行加密后，生成token
        token = JwtUtil.encode("2019gmall0105", userInfo, ip);

        // 将token存入redis一份
//        userService.addUserToken(token,memberId);


        return "redirect:http://search.gmail.com:8083/index?token="+token;
    }

    @RequestMapping("verify")
    @ResponseBody
    public String verify(String token, String currentIp, HttpServletRequest request){
        HashMap<String, String> map = new HashMap<>();
        // 通过jwt校验token真假
        Map<String, Object> decode = JwtUtil.decode(token, "gmall", currentIp);
        if(decode != null) {
            map.put("status", "success");
            map.put("memberId", (String) decode.get("memberId"));
            map.put("nickName", (String) decode.get("nickName"));
        } else {
            map.put("status", "fail");
        }
        return JSON.toJSONString(map);
    }


    @RequestMapping("login")
    @ResponseBody
    public String login(UmsMember umsMember, HttpServletRequest request){

        // 调用用户服务验证用户名和密码
        String token = "";
        UmsMember umsMemberLogin = userService.login(umsMember);
        if(umsMemberLogin != null) {
            //登录成功

            //用jwt制作token
            Map<String, Object> map = new HashMap<>();
            map.put("memberId", umsMemberLogin.getId());
            map.put("nickName", umsMemberLogin.getNickname());
            String ip = request.getHeader("x-forwarded-for");// 通过nginx转发的客户端ip
            if(StringUtils.isBlank(ip)){
                ip = request.getRemoteAddr();// 从request中获取ip
                if(StringUtils.isBlank(ip)){
                    ip = "127.0.0.1";
                }
            }
            token = JwtUtil.encode("gmall", map, ip);

            //讲token存入reid，并设置过期时间
            userService.addUserToken(token, umsMemberLogin.getId());

        } else {
            //登录失败
            token = "fail";
        }

        return token;
    }

    @RequestMapping("index")
    public String index(String ReturnUrl, ModelMap map){

        map.put("ReturnUrl",ReturnUrl);
        return "index";
    }
}

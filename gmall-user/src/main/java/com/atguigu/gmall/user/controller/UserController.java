package com.atguigu.gmall.user.controller;

import com.atguigu.gmall.bean.UmsMember;
import com.atguigu.gmall.bean.UmsMemberReceiveAddress;
import com.atguigu.gmall.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class UserController {

    @Autowired
    UserService userService;


    @RequestMapping("getAllUser")
    @ResponseBody
    public List<UmsMember> getAllUser(){
        List<UmsMember> umsMembers = userService.getAllUser();
        return umsMembers;
    }

    @RequestMapping("addUser")
    @ResponseBody
    public Map<String, Object> addUser(UmsMember umsMember) {
        HashMap<String, Object> map = new HashMap<>();
        int i = userService.addUmsMember(umsMember);
        if(i > 0) {
            map.put("message", true);
        } else {
            map.put("message", false);
        }
        return map;
    }

    @RequestMapping("removerUserById")
    @ResponseBody
    public Map<String, Object> removerUserById(int id) {
        HashMap<String, Object> map = new HashMap<>();
        int i = userService.removerUserById(id);
        if(i > 0) {
            map.put("message", true);
        } else {
            map.put("message", false);
        }
        return map;
    }

    @RequestMapping("updateUser")
    @ResponseBody
    public Map<String, Object> updateUser(UmsMember umsMember) {
        HashMap<String, Object> map = new HashMap<>();
        int i = userService.updateUser(umsMember);
        if(i > 0) {
            map.put("message", true);
        } else {
            map.put("message", false);
        }
        return map;
    }

    @RequestMapping("index")
    @ResponseBody
    public String index(){
        return "hello user";
    }



}

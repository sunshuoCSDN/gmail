package com.atguigu.gmall.cart.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.atguigu.gmall.annotations.LoginRequired;
import com.atguigu.gmall.bean.OmsCartItem;
import com.atguigu.gmall.bean.PmsSkuInfo;
import com.atguigu.gmall.service.CartService;
import com.atguigu.gmall.service.PmsSkuInfoService;
import com.atguigu.gmall.utils.CookieUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class CartController {

    @Reference
    PmsSkuInfoService pmsSkuInfoService;

    @Reference
    CartService cartService;



    @LoginRequired(loginSuccess = false)
    @RequestMapping("addToCart")
    public String addToCart(String skuId, int quantity, HttpServletRequest request, HttpServletResponse response) {

        //调用商品服务查询商品信息
        PmsSkuInfo skuInfo = pmsSkuInfoService.getSkuById(skuId);


        //讲商品信息封装成购物车信息
        OmsCartItem omsCartItem = new OmsCartItem();
        omsCartItem.setCreateDate(new Date());
        omsCartItem.setDeleteStatus(0);
        omsCartItem.setModifyDate(new Date());
        omsCartItem.setPrice(skuInfo.getPrice());
        omsCartItem.setProductAttr("");
        omsCartItem.setProductBrand("");
        omsCartItem.setProductCategoryId(skuInfo.getCatalog3Id());
        omsCartItem.setProductId(skuInfo.getProductId());
        omsCartItem.setProductName(skuInfo.getSkuName());
        omsCartItem.setProductPic(skuInfo.getSkuDefaultImg());
        omsCartItem.setProductSkuCode("11111111111");
        omsCartItem.setProductSkuId(skuId);
        omsCartItem.setQuantity(new BigDecimal(quantity));
        omsCartItem.setIsChecked("1");

        //判断用户是否登录
        String memberId = (String)request.getAttribute("memberId");
        String nickname = (String)request.getAttribute("nickname");
//        String memberId = "1";//1

        if(StringUtils.isBlank(memberId)) {
            //用户没有登陆 cookie
            List<OmsCartItem> omsCartItems = new ArrayList<>();
            //cookie里原有的购物车数据
            String cartListCookie = CookieUtil.getCookieValue(request, "cartListCookie", true);
            if(StringUtils.isBlank(cartListCookie)) {
                //cookie中没数据
                omsCartItems.add(omsCartItem);
            } else  {
                //cookie不为空
                omsCartItems = JSON.parseArray(cartListCookie, OmsCartItem.class);
                //判断添加的购物车数据在cookie中是否存在
                Boolean exist = if_cache_exist(omsCartItems, omsCartItem);

                if(exist) {
                    //之前添加过，更新购物车添加数量
                    for (OmsCartItem cartItem : omsCartItems) {
                        if(cartItem.getProductSkuId().equals(omsCartItem.getProductSkuId())) {
                            cartItem.setQuantity(cartItem.getQuantity().add(omsCartItem.getQuantity()));
                            cartItem.setPrice(cartItem.getPrice().add(omsCartItem.getPrice()));
                        }
                    }
                } else {
                    //之前没有添加，更新购物车
                    omsCartItems.add(omsCartItem);
                }
            }


            CookieUtil.setCookie(request, response, "cartListCookie", JSON.toJSONString(omsCartItems), 60*60*72, true);

        } else {
            //用户已登录 DB + cache
            // 从db中查出购物车数据
            OmsCartItem omsCartItemFromDb = cartService.ifCartExistByUser(memberId,skuId);

            if(omsCartItemFromDb==null){
                // 该用户没有添加过当前商品
                omsCartItem.setMemberId(memberId);
                omsCartItem.setMemberNickname(nickname);
                omsCartItem.setQuantity(new BigDecimal(quantity));
                cartService.addCart(omsCartItem);

            }else{
                // 该用户添加过当前商品
                omsCartItemFromDb.setQuantity(omsCartItemFromDb.getQuantity().add(omsCartItem.getQuantity()));
                cartService.updateCart(omsCartItemFromDb);
            }

            // 同步缓存
            cartService.flushCartCache(memberId);
        }



        return "redirect:/success.html";
    }

    private Boolean if_cache_exist(List<OmsCartItem> omsCartItems, OmsCartItem omsCartItem) {
        Boolean b = false;
        for (OmsCartItem cartItem : omsCartItems) {
            String productSkuId = cartItem.getProductSkuId();
            if(productSkuId.equals(omsCartItem.getProductSkuId())) {
                b = true;
            }
        }
        return b;
    }

    @RequestMapping("cartList")
    @LoginRequired(loginSuccess = false)
    public String cartList(HttpServletRequest request, HttpServletResponse response, HttpSession session, ModelMap modelMap) {
        List<OmsCartItem> omsCartItems = new ArrayList<>();
//        String memberId = "0";
        String memberId = (String)request.getAttribute("memberId");
        String nickname = (String)request.getAttribute("nickname");
        if(StringUtils.isNoneBlank(memberId)) {
            //用户已登陆， 查询DB
            omsCartItems = cartService.cartList(memberId);
        } else {
            //用户没有登录， 查询cookie
            String cartListCookie = CookieUtil.getCookieValue(request, "cartListCookie", true);
            if(StringUtils.isNoneBlank(cartListCookie)) {
                omsCartItems = JSON.parseArray(cartListCookie, OmsCartItem.class);
            }
        }
        for (OmsCartItem omsCartItem : omsCartItems) {
            omsCartItem.setTotalPrice(omsCartItem.getQuantity().multiply(omsCartItem.getPrice()));
        }

        modelMap.put("cartList", omsCartItems);

        BigDecimal totalMoney = getTotalMoney(omsCartItems);
        modelMap.put("totalMoney", totalMoney);
        return "cartList";
    }

    @RequestMapping("checkCart")
    @LoginRequired(loginSuccess = false)
    public String checkCart (OmsCartItem omsCartItem, HttpServletRequest request, HttpServletResponse response, HttpSession session, ModelMap modelMap) {
//        String memberId = "1";
        String memberId = (String)request.getAttribute("memberId");
        String nickname = (String)request.getAttribute("nickname");
        omsCartItem.setMemberId(memberId);
        cartService.checkCart(omsCartItem);
        List<OmsCartItem> omsCartItems = cartService.cartList("1");
        for (OmsCartItem cartItem : omsCartItems) {
            cartItem.setTotalPrice(cartItem.getPrice().multiply(cartItem.getQuantity()));
        }
        modelMap.put("cartList", omsCartItems);

        BigDecimal totalMoney = getTotalMoney(omsCartItems);
        modelMap.put("totalMoney", totalMoney);
        return "cartListInner";
    }

    private BigDecimal getTotalMoney(List<OmsCartItem> omsCartItems) {
        BigDecimal bigDecimal = new BigDecimal("0");

        for (OmsCartItem omsCartItem : omsCartItems) {
            if(omsCartItem.getIsChecked().equals("1")) {
                bigDecimal = bigDecimal.add(omsCartItem.getTotalPrice());
            }
        }
        return bigDecimal;
    }
}

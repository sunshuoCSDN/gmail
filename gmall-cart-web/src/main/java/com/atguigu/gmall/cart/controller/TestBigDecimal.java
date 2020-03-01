package com.atguigu.gmall.cart.controller;

import java.math.BigDecimal;

public class TestBigDecimal {

    public static void main(String[] args) {
        //初始化
        BigDecimal b1 = new BigDecimal(0.01f);
        BigDecimal b2 = new BigDecimal(0.01d);
        BigDecimal b3 = new BigDecimal("0.01");

        System.out.println(b1);
        System.out.println(b2);
        System.out.println(b3);

        //比较
        int i = b1.compareTo(b2);//1 0 -1
        System.out.println(i);


        //运算
            //加
        BigDecimal add = b1.add(b2);
        System.out.println(add);
            //减
        BigDecimal subtract = b2.subtract(b1);
        System.out.println(subtract);
            //乘
        BigDecimal multiply = b1.multiply(b2);
        System.out.println(multiply);


        //约数
    }
}

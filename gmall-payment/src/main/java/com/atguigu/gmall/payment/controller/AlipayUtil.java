package com.atguigu.gmall.payment.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
 
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeRefundModel;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.response.AlipayTradeRefundResponse;
 
public class AlipayUtil {
	
    private static final Logger LOGGER = LoggerFactory.getLogger(AlipayUtil.class);   
    private static String APP_ID = "2018020102122556";
	
    //支付宝退款请求的网关
    private static String requestUrl = "https://openapi.alipay.com/gateway.do";
    //用户自己生成的私钥
    private static String APP_PRIVATE_KEY = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCdQeknhM2rhiGAH6V0ljxn3rAWIdzduTEQuteTfwjnZtvMhQPuuN1b/88D5yMuaZhZNFeUdWb+SmtP9DAzAWWgnT13T0YhJcxP6txm7JBRrjadCRt+LOFxPiPQk5t9fH7yXjw9i4uMDsNJeTncrVZ/AZYrk0ESC9anJR8XeuBc3HE8T4fqlKKl35jlumIWrPbPNQhKGXaGcOnpiaXO9qYYUSP/tnrjNYXHOso0yBs4YTl+LLX2TJ12p3n/oX6HnL4zQgtN5k4QasHP7CIig1ngcVQGfWsMm4djI9KXNXvGLQPfMQEmyb71mM5OCdl1MtAc6OaIAymhSv2hOLNIuyodAgMBAAECggEAe05/P5mGm4QlKI2n8u8KlneqovASe1kG/BNFjkYB+VBR8OAr4TfbepPvAyRuFap+5xN/yMz14VcBJkRWtufVhEdHNxJV7w/wUIncIGhGEYYFFMVbZWhTrbQH6TiUp6TC9dCmc6vD1CKPRkFj+YGBXT0lPy3LzBa0TYNyCbszyhthrgkpuFYbB0R93IPvvBh5NJFXQytwNb2oVopC9AQWviqnZUZcT0eJ087dQ1WLPa6blBD8DP1PUq0Ldr6pgKfObFxIj8+87DlJznRfdEsbqZlS7jagdw5tLr71WJpctIGPqKpgvajfePP/lj3eY82BKQB+aTw0zmAiB05Yes4LgQKBgQDq3EiQR8J1MEN2rpiLt1WvDYYvKVUgOY7Od//fRPgaMBstbe4TzGBpR8E+z267bHAWLaWtHkfX6muFHn1x68ozEUWk/nZq0smWnuPdcy4E7Itbk36W2FF/rOZB7j5ddlC9byrxDSNgcf9/FA/CU+i5KVQpLYfsk2dvwomvu0aFVQKBgQCraXpxzMmsBx4127LsZDO5bxfxb6nqzyK4NPe0VaGiRg8oaCWczcLz1J5iRqC9QeEwsSt4XU1sYBMTcsFpA0apZpm3prH2HJRx/isNENesaHcihF0mMd0WxU3xyRvWSDeZV5A1Zy1ZEJ+p17DGwb2j+yo2uBrDNXBgBWEzXwiRqQKBgBdXFvsHtqKQzlOQHGbeLGy+KlSrheMy9Sc9s7cLkqB/oWPNZfifugEceW71jGqh5y29EZb3yGoDyPWsxwi4Rxr2H3a7Nyd8lT4bwkdyt+MTYvIR4WW6T7chhqyMsbP2GyYIUzsrdBWUnrCRXNOSJTGpksyY0sZHC+OGcMp/EQ4VAoGBAIISSVL/pm1+/UK7U1ukcced8JpKNLM0uVD1CJ50eHHOHgR4e0owrWYfioxisejLjBlJ6AWvL2g0w2T3qKKKVN2JOM4ulU5/w3l4+KwygqaWowizTogEQJPd5ta52ADTzjTzSD/t6nByd+YHAWLhc4lyt0bMj6pf68VBb8/upm75AoGAGAYz79IVHp9eppykufjNcWu6okkG8tZnzuyaWKW/CuKKBWMaTk0vcyQlfJfxIBccoQrBuYyXBdcpPuZ/ys2C25pNrkACuhIKNgnMc0floJoYEfJzetw/3cIimWu4NJzVQOaojaGA58oo2+fub43Xn25Jq4rvSVe3oLdb5xWkw5Q=\n";
    //支付宝公钥
    private static String ALIPAY_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAhkZi6W0wn/prX+NIIF9ATb5Z8ReKK4hFYtBrweDfGHD1mNW7YIZY4G5hE7S2Sry8eFXlFgSlBWlJ4fVnDaK9MkVThpwE2H65ooVlK/wLuyPqovIVpMt/utva5Ayuzv7eQOWK45FdLDNDlK8QLoBko6SS+YbnWnf7a+mrf4NAS4UFClpfe8Byqe8XIraO2Cg4Ko5Y5schX39rOAH8GlLdgqQRYVQ2dCnkIQ+L+I4Cy9Mvw3rIkTwt3MBU+AqREXY4r5Bn6cmmX/9MAJbFqrofGiUAqG+qbjTcZAzgNPfuiD0zXgt/YYjMQMzck75BOmwnYOam2ajODUSQn8Xybsa7wQIDAQAB\n";
    
    //编码级别
    private static String CHARSET = "UTF-8";
    
    public static String refundOrder(){
  	System.out.println("开始调用支付宝加密******************************************************");
  	//实例化客户端
    	AlipayClient alipayClient = new DefaultAlipayClient(requestUrl, APP_ID, APP_PRIVATE_KEY, "json", CHARSET, ALIPAY_PUBLIC_KEY, "RSA2");
    	//SDK已经封装掉了公共参数，这里只需要传入业务参数。以下方法为sdk的model入参方式(model和biz_content同时存在的情况下取biz_content)。
    	AlipayTradeRefundModel refundModel = new AlipayTradeRefundModel();
    							//2020021222001430981422
    	refundModel.setTradeNo("2020021722001430981424463252");
    	refundModel.setRefundAmount("1");
    	refundModel.setRefundReason("商品退款");
    	//实例化具体API对应的request类,类名称和接口名称对应,当前调用接口名称：alipay.trade.app.pay
    	AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
    	request.setBizModel(refundModel);
    	try{
    		AlipayTradeRefundResponse response = alipayClient.execute(request);
    		System.out.println(response.getMsg()+"\n");
    		System.out.println(response.getBody());
    	}catch(Exception e){
    		e.printStackTrace();
    		LOGGER.error("支付宝退款错误！",e.getMessage());    		
    	}    
    	return "";    	
  	}
  
  	public static void main(String[] arg){

  		String res=refundOrder();
		System.out.println(res);
  	}
}

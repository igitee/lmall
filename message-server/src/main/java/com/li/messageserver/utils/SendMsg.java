package com.li.messageserver.utils;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;

import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author lihaodong
 * @Description
 * @Date 11:57 2019/1/16
 * @Param
 * @return
 **/
@Slf4j
public class SendMsg {


    /**
     * 这里填写你在平台里的ACOUNT_SID
     */
    private static final String ACCOUNT_SID = "2be56dd4aa6e4564b9690abb2d0a3f89";
    /**
     * 这里填写你在平台里的AUTH_TOKEN
     */
    private static final String AUTH_TOKEN = "cc8f4a3bf3664665ade85818fa3b2516";
    /**
     * 请求地址是固定的不用改
     */
    private static final String BASE_URL = "https://api.miaodiyun.com/20150822/industrySMS/sendSMS";

    /**
     * 测试短信发送（平台：秒嘀科技）
     *
     * @param to 接收信息手机号
     * @return
     */
    public static Map<String, Object> sendSMSG(String to, String smsContent, int code) {

        Map<String, Object> resMap = new HashMap<>();
        /**
         * 设置时间戳
         */
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        Date date = new Date();
        String timeStamp = format.format(date);

        /**
         * 各个数据经过MD5加密之后返回sig
         */
        String sig = MD5(ACCOUNT_SID, AUTH_TOKEN, timeStamp);

        /**
         * 组装参数
         */
        NetUtils netUtils = NetUtils.getInstance();
        Map<String, String> map = new HashMap<>();
        map.put("accountSid", ACCOUNT_SID);
        map.put("smsContent", smsContent);
        map.put("to", to);
        map.put("timestamp", timeStamp);
        map.put("sig", sig);
        String res = netUtils.postDataSynToNet(BASE_URL, map);
        JSONObject jsonObject = JSONObject.parseObject(res);
        resMap.put("respCode", jsonObject.get("respCode"));
        resMap.put("securityCode", code);
        return resMap;
    }


    /**
     * MD5算法 动态参数
     *
     * @param args
     * @return
     */
    public static String MD5(String... args) {
        StringBuffer result = new StringBuffer();
        if (args == null || args.length == 0) {
            return "";
        } else {
            StringBuffer str = new StringBuffer();
            for (String string : args) {
                str.append(string);
            }
            try {
                MessageDigest digest = MessageDigest.getInstance("MD5");
                byte[] bytes = digest.digest(str.toString().getBytes());
                for (byte b : bytes) {
                    String hex = Integer.toHexString(b & 0xff);
                    if (hex.length() == 1) {
                        result.append("0" + hex);
                    } else {
                        result.append(hex);
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result.toString();
    }

}

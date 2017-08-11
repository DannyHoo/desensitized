package com.danny.log.desensitized.entity;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author miaoxuehui@lxfintech.com
 * @Title: LimitFrequencyKeyEnum
 * @Copyright: Copyright (c) 2016
 * @Description: 限制频次key枚举定义<br>
 * @Company: lxfintech.com
 * @Created on 17/6/23下午3:09
 */
public enum LimitFrequencyKeyEnum implements ILimitKey{
    SMSCODE_MOBILE_DAY_LIMIT("smscode_mobile_{mobile}_#yyyyMMdd#", "手机号发送短信验证码每天不超过N次",
            "smscode_mobile_{}_#yyyyMMdd#",1,TimeUnit.DAYS,100),

    OCR_USER_DAY_LIMIT("ocr_user_{userBizId}_#yyyyMMdd#","ocr认证每个用户每天不超过N次",
            "ocr_user_{}_#yyyyMMdd#",1,TimeUnit.DAYS,20),

    FACE_USER_DAY_LIMIT("face_user_{userBizId}_#yyyyMMdd#","人脸识别每个用户每天不超过N次",
            "face_user_{}_#yyyyMMdd#",1,TimeUnit.DAYS,20),

    CARRIER_AUTH_USER_DAY_LIMIT("carrierAuth_user_{userBizId}_#yyyyMMdd#","运营商授权每个用户每天不超过N次",
            "carrierAuth_user_{}_#yyyyMMdd#",1,TimeUnit.DAYS,20),

    SOCIAL_SECURITY_AUTH_USER_DAY_LIMIT("socialSecurity_user_{userBizId}_#yyyyMMdd#","社保授权每个用户每天不超过N次",
            "socialSecurity_user_{}_#yyyyMMdd#",1,TimeUnit.DAYS,20),

    HOUSING_FUND_AUTH_USER_DAY_LIMIT("housingFund_user_{userBizId}_#yyyyMMdd#","公积金授权每个用户每天不超过N次",
            "housingFund_user_{}_#yyyyMMdd#",1,TimeUnit.DAYS,20),

    JINGDONG_AUTH_USER_DAY_LIMIT("jingDong_user_{userBizId}_#yyyyMMdd#","京东授权每个用户每天不超过N次",
            "jingDong_user_{}_#yyyyMMdd#",1,TimeUnit.DAYS,20),

    CREDITCARD_AUTH_USER_DAY_LIMIT("creditCard_user_{userBizId}_#yyyyMMdd#","信用卡授权每个用户每天不超过N次",
            "creditCard_user_{}_#yyyyMMdd#",1,TimeUnit.DAYS,20),


    LOGIN_MOBILE_ERROR_DAY_LIMIT("login_mobile_error_{mobile}_#yyyyMMdd#", "手机号登陆密码错误每天不超过N次",
                                    "login_mobile_error_{}_#yyyyMMdd#",1,TimeUnit.DAYS,10),
    ;

    private String key;
    private String name;
    private String expression;
    private long times;
    private TimeUnit timeUnit;
    private long limitValue;

    private LimitFrequencyKeyEnum(
            String key,String name, String expression,
            long times, TimeUnit timeUnit, long limitValue) {
        this.key = key;
        this.name = name;
        this.expression = expression;
        this.times = times;
        this.timeUnit = timeUnit;
        this.limitValue = limitValue;
    }

    public String getLimitFrequencyKey(Object... objects) {
        StringBuilder key = new StringBuilder();
        //加上limit_前缀
        key.append("limit_").append(getReplaceExpression());
//        return OwnStringUtils.getMessageFormat(key.toString(), objects);
        return "";
    }

    /**
     * 获取替换动态参数后的表达式
     * @return
     */
    private String getReplaceExpression() {
        Map<String, String> parameterMap = defineDynamceParameterMap();
        String expression = this.expression;
        for(String key: parameterMap.keySet()) {
            expression = expression.replaceAll("#"+key+"#", parameterMap.get(key));
        }
        return expression;
    }

    /**
     * 定义动态参数值
     * @return
     */
    private Map<String, String> defineDynamceParameterMap() {
        Map<String, String> parameterMap = new HashMap<>();
        parameterMap.put("yyyyMMdd", "20170721");

        return parameterMap;
    }

    public String getKey() {
        return key;
    }

    public String getName() {
        return name;
    }

    public String getExpression() {
        return expression;
    }

    public long getTimes() {
        return times;
    }

    public TimeUnit getTimeUnit() {
        return timeUnit;
    }

    public long getLimitValue() {
        return limitValue;
    }
}

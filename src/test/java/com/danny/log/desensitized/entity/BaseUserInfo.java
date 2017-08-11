package com.danny.log.desensitized.entity;

import com.danny.log.desensitized.annotation.Desensitized;
import com.danny.log.desensitized.enums.SensitiveTypeEnum;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author huyuyang@lxfintech.com
 * @Title: BaseUserInfo
 * @Copyright: Copyright (c) 2016
 * @Description:
 * @Company: lxjr.com
 * @Created on 2017-06-07 15:19:23
 */
public class BaseUserInfo extends BaseEntity{
    private List<Map> mapList;
    private List<String> strList;
    private Map<String,UserTypeEnum> map;
    private List<UserService> userServiceList;
    private ILimitKey iLimitKey;

    private UserTypeEnum userType;

    private static final long serialVersionUID = -3387516993124229938L;

    private UserService userService;

    @Desensitized(type = SensitiveTypeEnum.CHINESE_NAME)
    private String realName;

    @Desensitized(type = SensitiveTypeEnum.ID_CARD)
    private String idCardNo;

    @Desensitized(type = SensitiveTypeEnum.MOBILE_PHONE)
    private String mobileNo;

    private String account;

    @Desensitized(type = SensitiveTypeEnum.PASSWORD, isEffictiveMethod = "isEffictiveMethod")
    private String password;

    @Desensitized(type = SensitiveTypeEnum.BANK_CARD)
    private String bankCardNo;

    @Desensitized(type = SensitiveTypeEnum.EMAIL)
    private String email;

    private String extend1;
    private Integer extend2;
    private BigDecimal extend3;
    private Date extend4;

    public UserService getUserService() {
        return userService;
    }

    public BaseUserInfo setUserService(UserService userService) {
        this.userService = userService;
        return this;
    }

    public UserTypeEnum getUserType() {
        return userType;
    }

    public BaseUserInfo setUserType(UserTypeEnum userType) {
        this.userType = userType;
        return this;
    }

    public String getRealName() {
        return realName;
    }

    public BaseUserInfo setRealName(String realName) {
        this.realName = realName;
        return this;
    }

    public String getIdCardNo() {
        return idCardNo;
    }

    public BaseUserInfo setIdCardNo(String idCardNo) {
        this.idCardNo = idCardNo;
        return this;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public BaseUserInfo setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
        return this;
    }

    public String getAccount() {
        return account;
    }

    public BaseUserInfo setAccount(String account) {
        this.account = account;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public BaseUserInfo setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getBankCardNo() {
        return bankCardNo;
    }

    public BaseUserInfo setBankCardNo(String bankCardNo) {
        this.bankCardNo = bankCardNo;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public BaseUserInfo setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getExtend1() {
        return extend1;
    }

    public BaseUserInfo setExtend1(String extend1) {
        this.extend1 = extend1;
        return this;
    }

    public Integer getExtend2() {
        return extend2;
    }

    public BaseUserInfo setExtend2(Integer extend2) {
        this.extend2 = extend2;
        return this;
    }

    public BigDecimal getExtend3() {
        return extend3;
    }

    public BaseUserInfo setExtend3(BigDecimal extend3) {
        this.extend3 = extend3;
        return this;
    }

    public Date getExtend4() {
        return extend4;
    }

    public BaseUserInfo setExtend4(Date extend4) {
        this.extend4 = extend4;
        return this;
    }


    public List<String> getStrList() {
        return strList;
    }

    public BaseUserInfo setStrList(List<String> strList) {
        this.strList = strList;
        return this;
    }

    public Map<String, UserTypeEnum> getMap() {
        return map;
    }

    public BaseUserInfo setMap(Map<String, UserTypeEnum> map) {
        this.map = map;
        return this;
    }
    public ILimitKey getiLimitKey() {
        return iLimitKey;
    }

    public BaseUserInfo setiLimitKey(ILimitKey iLimitKey) {
        this.iLimitKey = iLimitKey;
        return this;
    }

    public List<UserService> getUserServiceList() {
        return userServiceList;
    }

    public BaseUserInfo setUserServiceList(List<UserService> userServiceList) {
        this.userServiceList = userServiceList;
        return this;
    }

    public List<Map> getMapList() {
        return mapList;
    }

    public BaseUserInfo setMapList(List<Map> mapList) {
        this.mapList = mapList;
        return this;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    /**
     * 如果账号等于"dannyhoo1234561"时，密码字段脱敏生效
     *
     * @return
     */
    public boolean isEffictiveMethod() {
        boolean isEffictive = "dannyhoo123456".equals(getAccount());
        return isEffictive;
    }
}

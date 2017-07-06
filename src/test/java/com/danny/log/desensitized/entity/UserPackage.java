package com.danny.log.desensitized.entity;

/**
 * @author huyuyang@lxfintech.com
 * @Title: Package
 * @Copyright: Copyright (c) 2016
 * @Description:
 * @Company: lxjr.com
 * @Created on 2017-06-22 14:54:21
 */
public class UserPackage {
    private BaseUserInfo baseUserInfo;
    private boolean flag;

    public BaseUserInfo getBaseUserInfo() {
        return baseUserInfo;
    }

    public UserPackage setBaseUserInfo(BaseUserInfo baseUserInfo) {
        this.baseUserInfo = baseUserInfo;
        return this;
    }

    public boolean isFlag() {
        return flag;
    }

    public UserPackage setFlag(boolean flag) {
        this.flag = flag;
        return this;
    }
}

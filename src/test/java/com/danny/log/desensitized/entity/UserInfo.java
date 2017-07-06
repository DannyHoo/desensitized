package com.danny.log.desensitized.entity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.concurrent.locks.Lock;

/**
 * @author huyuyang@lxfintech.com
 * @Title: UserInfo
 * @Copyright: Copyright (c) 2016
 * @Description:
 * @Company: lxjr.com
 * @Created on 2017-07-06 21:13:35
 */
public class UserInfo extends BaseUserInfo {
    private final static String field="field";
    private Integer field1;
    private BigDecimal field2;
    private Date field3;
    private String field4;
    private Lock field5;

    public static String getField() {
        return field;
    }

    public Integer getField1() {
        return field1;
    }

    public UserInfo setField1(Integer field1) {
        this.field1 = field1;
        return this;
    }

    public BigDecimal getField2() {
        return field2;
    }

    public UserInfo setField2(BigDecimal field2) {
        this.field2 = field2;
        return this;
    }

    public Date getField3() {
        return field3;
    }

    public UserInfo setField3(Date field3) {
        this.field3 = field3;
        return this;
    }

    public String getField4() {
        return field4;
    }

    public UserInfo setField4(String field4) {
        this.field4 = field4;
        return this;
    }

    public Lock getField5() {
        return field5;
    }

    public UserInfo setField5(Lock field5) {
        this.field5 = field5;
        return this;
    }
}

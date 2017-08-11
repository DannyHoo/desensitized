package com.danny.log.desensitized.entity;

import java.util.concurrent.TimeUnit;

/**
 * @author miaoxuehui@lxfintech.com
 * @Title: ILimitKey
 * @Copyright: Copyright (c) 2016
 * @Description: 限制key的接口<br>
 * @Company: lxfintech.com
 * @Created on 17/6/23下午3:07
 */
public interface ILimitKey {

    /**
     * 时间值
     * @return
     */
    public long getTimes();

    /**
     * 时间单位
     * @return
     */
    public TimeUnit getTimeUnit();

    /**
     * 限制值
     * @return
     */
    public long getLimitValue();

    /**
     * 限制key
     * @param objects
     * @return
     */
    public String getLimitFrequencyKey(Object... objects);
}

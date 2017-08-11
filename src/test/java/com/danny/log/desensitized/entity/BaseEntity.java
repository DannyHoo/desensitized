package com.danny.log.desensitized.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * @author huyuyang@lxfintech.com
 * @Title: BaseEntity
 * @Copyright: Copyright (c) 2016
 * @Description:
 * @Company: lxjr.com
 * @Created on 2017-07-06 13:38:13
 */
public class BaseEntity /*implements Serializable*/{
    private static final long serialVersionUID = -3387516993124229938L;

    private Long id;
    private Date createTime;
    private Date updateTime;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Long getId() {
        return id;
    }

    public BaseEntity setId(Long id) {
        this.id = id;
        return this;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public BaseEntity setCreateTime(Date createTime) {
        this.createTime = createTime;
        return this;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public BaseEntity setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
        return this;
    }
}

package com.linln.modules.system.domain;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by HSY on 2019/2/10.
 */
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class BaseStringEntity implements Serializable {
    private static final long serialVersionUID = -250118731239275742L;
    @CreatedDate
    @Column(name="createDateTime",columnDefinition="DATETIME COMMENT '创建时间'")
    private Date createDateTime=new Date();
    @Column(name="createUser",columnDefinition="VARCHAR(50) COMMENT '创建人'")
    private String createUser;
    @Column(name="modifyUser",columnDefinition="VARCHAR(50) COMMENT '修改人'")
    private String modifyUser;
    @LastModifiedDate
    @Column(name="modifyDateTime",columnDefinition="DATETIME COMMENT '修改时间'")
    private Date modifyDateTime=new Date();

    public Date getCreateDateTime() {
        return createDateTime;
    }

    public void setCreateDateTime(Date createDateTime) {
        this.createDateTime = createDateTime;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public String getModifyUser() {
        return modifyUser;
    }

    public void setModifyUser(String modifyUser) {
        this.modifyUser = modifyUser;
    }

    public Date getModifyDateTime() {
        return modifyDateTime;
    }

    public void setModifyDateTime(Date modifyDateTime) {
        this.modifyDateTime = modifyDateTime;
    }
}

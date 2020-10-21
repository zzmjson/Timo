package com.linln.modules.system.domain;

import com.linln.common.utils.StatusUtil;
import lombok.Data;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Data
@Entity
@Table(name = "sys_consulting")
@org.hibernate.annotations.Table(appliesTo = "sys_consulting", comment = "")
public class Consulting extends BaseStringEntity {

    @Id
    @Column(name = "id", columnDefinition = "VARCHAR(50) comment '咨询表'")
    private String id;
    @Column(name = "name", columnDefinition = "VARCHAR(50) COMMENT '类型名称'")
    private String name;
    @Column(name = "state", columnDefinition = "int COMMENT '状态'")
    private int state;
}
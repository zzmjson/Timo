package com.linln.modules.system.domain;

import lombok.Data;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Data
@Entity
@Table(name = "sys_case_set")
@EntityListeners(AuditingEntityListener.class)
public class CaseSet extends BaseStringEntity {

    @Id
    @Column(name="id",columnDefinition="VARCHAR(50) COMMENT '个案设置管理'")
    private String id;

    @Column(name="type",columnDefinition="VARCHAR(50) COMMENT '类型名称'")
    private String type;

    @Column(name="pid",columnDefinition="VARCHAR(50) COMMENT '类型id'")
    private String pid;

    @Column(name="content",columnDefinition="VARCHAR(50) COMMENT '字典内容'")
    private String content="";



}

package com.linln.modules.system.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Date;
import java.sql.Time;

//
@Data
@Entity
@Table(name = "sys_group")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer"})
@org.hibernate.annotations.Table(appliesTo = "sys_group", comment = "")
public class Group extends BaseStringEntity {

    @Id
    @Column(name = "id", columnDefinition = "VARCHAR(50) comment '因子分组表'")
    private String id;
    @Column(name = "scale_id", columnDefinition = "VARCHAR(50) COMMENT '所属量表Id'")
    private String scaleId;
    @Column(name = "is_stat", columnDefinition = "int COMMENT '是否参与统计'")
    private int isStat;
    @Column(name = "serial", columnDefinition = "VARCHAR(50) COMMENT '序号'")
    private int serial;
    @Column(name = "stat_type", columnDefinition = "VARCHAR(50) COMMENT '统计方式'")
    private String statType;
    @Column(name = "info", columnDefinition = "VARCHAR(50) COMMENT '因子分组说明'")
    private String info;

}
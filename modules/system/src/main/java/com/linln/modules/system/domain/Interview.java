package com.linln.modules.system.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;


@Data
@Entity
@Table(name = "sys_interview")
@EntityListeners(AuditingEntityListener.class)
public class Interview extends BaseStringEntity {

    @Id
    @Column(name="id",columnDefinition="VARCHAR(50) COMMENT '访谈记录表'")
    private String id;

    @Column(name="consult_Id",columnDefinition="VARCHAR(50) COMMENT '咨询师id'")
    private String consultId;

    @Column(name="consult_name",columnDefinition="VARCHAR(50) COMMENT '咨询师名称'")
    private String consultName;

    @Column(name="demander_Id",columnDefinition="VARCHAR(50) COMMENT '访谈用户Id'")
    private String demanderId;

    @Column(name="demander_name",columnDefinition="VARCHAR(50) COMMENT '访谈用户名称'")
    private String demanderName;

//    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name="demander_Time",columnDefinition="DATETIME COMMENT '访谈时间'")
    private Date demanderTime;

    @Column(name="demander_site",columnDefinition="VARCHAR(50) COMMENT '咨询地点'")
    private String demanderSite;

    @Column(name="demander_mode",columnDefinition="VARCHAR(50) COMMENT '咨询方式'")
    private String demanderMode;

    @Column(name="report_id",columnDefinition="VARCHAR(50) COMMENT '个案报告信息Id'")
    private String reportId;



}

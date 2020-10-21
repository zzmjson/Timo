//package com.linln.modules.system.domain;
//
//import lombok.Data;
//import org.springframework.data.jpa.domain.support.AuditingEntityListener;
//
//import javax.persistence.*;
//
//@Data
//@Entity
//@Table(name = "sys_case_template")
//@EntityListeners(AuditingEntityListener.class)
//public class CaseTemplate extends BaseStringEntity {
//
//
//    @Id
//    @Column(name="id",columnDefinition="VARCHAR(50) COMMENT '个案模板表'")
//    private String id;
//
//    @Column(name="template_type",columnDefinition="VARCHAR(50) COMMENT '模板类型：1、访谈记录模板，2、报告模板'")
//    private String templateType="";
//
//    @Lob
//    @Column(name="fixed",columnDefinition="text COMMENT '固定信息'")
//    private String fixed="";
//
//    @Lob
//    @Column(name="title",columnDefinition="text COMMENT '标题（只用于报告模块）'")
//    private String title ="";
//
//    @Lob
//    @Column(name="little_title",columnDefinition="text COMMENT '小标题（只用于报告模块）'")
//    private String littleTitle ="";
//
//
//
//
//
//
//}

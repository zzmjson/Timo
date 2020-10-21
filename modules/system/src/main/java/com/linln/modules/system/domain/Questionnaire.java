package com.linln.modules.system.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Data
@Entity
@Table(name = "sys_ques")
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(value = { "hibernateLazyInitializer"})
@org.hibernate.annotations.Table(appliesTo = "sys_ques", comment = "")
public class Questionnaire extends BaseStringEntity {

    @Id
    @Column(name="id",columnDefinition="VARCHAR(50) COMMENT '问卷调查表'")
    private String id;

    @Lob
    @Column(name="title",columnDefinition="VARCHAR(500) COMMENT '问卷标题'")
    private String title;

    @Lob
    @Column(name="message",columnDefinition="text COMMENT '问卷说明'")
    private String message;

    @Column(name="viewing",columnDefinition="VARCHAR(50) COMMENT '可看结果 ：1、允许、2、不允许'")
    private String viewing="1";


    @Column(name="state",columnDefinition="VARCHAR(50) COMMENT '问卷状态:1、发布，2、 不发布'")
    private String state="1";

    @Column(name="number",columnDefinition="VARCHAR(50) COMMENT '已收集份数'")
    private String number="0";

    @Lob
    @Column(name="per_list",columnDefinition="LONGTEXT COMMENT '权限列表'")
    private String perList;





}

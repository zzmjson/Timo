package com.linln.modules.system.domain;

import lombok.Data;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Data
@Entity
@Table(name = "sys_con_toc")
@EntityListeners(AuditingEntityListener.class)
@org.hibernate.annotations.Table(appliesTo = "sys_con_toc", comment = "")
public class ContentTopic extends BaseStringEntity {

    @Id
    @Column(name="id",columnDefinition="VARCHAR(50) COMMENT '内容题目表（模板）'")
    private String id;

    @Column(name="no",columnDefinition="VARCHAR(50) COMMENT '题目编号'")
    private String no;

    @Column(name="type",columnDefinition="VARCHAR(50) COMMENT '提目类型 ：1、单选题、2、多选题、3、'")
    private String type="1";

    @Column(name="superior",columnDefinition="VARCHAR(50) COMMENT '上级类型 ：1、量表、2、问卷调查、3、'")
    private String superior="1";


    @Column(name="scale_id",columnDefinition="VARCHAR(50) COMMENT '量表id/问卷调查表id'")
    private String scaleId;

    @Column(name="title",columnDefinition="VARCHAR(50) COMMENT '量表标题/问卷调查标题'")
    private String title;

    @Lob
    @Column(name="dateil",columnDefinition="text COMMENT '题目详情'")
    private String dateil;

    @Column(name="answer",columnDefinition="varchar(255) COMMENT '答案'")
    private String answer;

    @Column(name="score",columnDefinition="varchar(255) COMMENT '分数'")
    private String score;

    @Column(name="warn_scope",columnDefinition="varchar(255) COMMENT '分数预警范围'")
    private String warnScope;

    @Column(name="group_id",columnDefinition="varchar(50) COMMENT '因子分组id'")
    private String groupId;

    @Column(name="serial",columnDefinition="varchar(50) COMMENT '因子分组序号'")
    private String serial;

    @Column(name="info",columnDefinition="varchar(50) COMMENT '因子分组标题'")
    private String info;


}

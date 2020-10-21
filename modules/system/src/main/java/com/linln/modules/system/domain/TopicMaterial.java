package com.linln.modules.system.domain;

import lombok.Data;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Data
@Entity
@Table(name = "sys_toc_mel")
@EntityListeners(AuditingEntityListener.class)
@org.hibernate.annotations.Table(appliesTo = "sys_toc_mel", comment = "")
public class TopicMaterial extends BaseStringEntity {

    @Id
    @Column(name="id",columnDefinition="VARCHAR(50) COMMENT '题目素材表(题目选项，模板表)'")
    private String id;


    @Column(name="topic_id",columnDefinition="VARCHAR(50) COMMENT '题目id'")
    private String appraisalTopicId;


    @Column(name="no",columnDefinition="VARCHAR(50) COMMENT '题目编号'")
    private String no;

    @Column(name="scale_id",columnDefinition="VARCHAR(50) COMMENT '量表id'")
    private String scaleId;

    @Lob
    @Column(name="content",columnDefinition="text COMMENT '内容'")
    private String content;

    @Lob
    @Column(name="score",columnDefinition="double(12,2) COMMENT '分数'")
    private String score;

    @Column(name="group_id",columnDefinition="varchar(50) COMMENT '因子分组id'")
    private String groupId;

    @Column(name="serial",columnDefinition="varchar(50) COMMENT '因子分组序号'")
    private String serial;

    @Column(name="info",columnDefinition="varchar(50) COMMENT '因子分组标题'")
    private String info;




}

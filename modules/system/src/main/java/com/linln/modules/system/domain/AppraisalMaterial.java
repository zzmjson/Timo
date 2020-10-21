package com.linln.modules.system.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Data
@Entity
@Table(name = "sys_aal_material")
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(value = { "hibernateLazyInitializer"})
@org.hibernate.annotations.Table(appliesTo = "sys_aal_material", comment = "")
public class AppraisalMaterial extends BaseStringEntity {
    public AppraisalMaterial(String id, String appraisalTopicId, String no, String scaleId, String content, String score, String userId, String result) {
        this.id = id;
        this.appraisalTopicId = appraisalTopicId;
        this.no = no;
        this.scaleId = scaleId;
        this.content = content;
        this.score = score;
        this.userId = userId;
        this.result = result;
    }


    public AppraisalMaterial( ) {

    }

    @Id
    @Column(name="id",columnDefinition="VARCHAR(50) COMMENT '题目素材表(题目选项，用户答题记录表)'")
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


    @Column(name="user_Id",columnDefinition="VARCHAR(50) COMMENT '答题用户id'")
    private String userId;

    @Column(name="result",columnDefinition="VARCHAR(50) COMMENT '是否选中：1是，0否'")
    private String result;

    @Column(name="group_id",columnDefinition="varchar(50) COMMENT '因子分组id'")
    private String groupId;

    @Column(name="serial",columnDefinition="varchar(50) COMMENT '因子分组序号'")
    private String serial;

    @Column(name="info",columnDefinition="varchar(50) COMMENT '因子分组标题'")
    private String info;



}

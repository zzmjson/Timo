package com.linln.modules.system.domain;

import lombok.Data;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Data
@Entity
@Table(name = "sys_appraisal")
@EntityListeners(AuditingEntityListener.class)
@org.hibernate.annotations.Table(appliesTo = "sys_appraisal", comment = "")
public class AppraisalTopic extends BaseStringEntity {


    public AppraisalTopic(String id, String no, String type, String superior, String scaleId, String title, String dateil, String answer, String score, String warnScope, String userId,String upmqId) {
        this.id = id;
        this.no = no;
        this.type = type;
        this.superior = superior;
        this.scaleId = scaleId;
        this.title = title;
        this.dateil = dateil;
        this.answer = answer;
        this.score = score;
        this.warnScope = warnScope;
        this.userId = userId;
        this.upmqId = upmqId;
    }

    public AppraisalTopic() {

    }

    @Id
    @Column(name="id",columnDefinition="VARCHAR(50) COMMENT '用户测评题目表（用户答题记录）'")
    private String id;

    @Column(name="no",columnDefinition="VARCHAR(50) COMMENT '题目编号'")
    private String no;

    @Column(name="type",columnDefinition="VARCHAR(50) COMMENT '提目类型 ：1、单选题、2、多选题、3、'")
    private String type="1";



    @Column(name="superior",columnDefinition="VARCHAR(50) COMMENT '上级类型 ：1、量表、2、问卷调查、3、'")
    private String superior="1";


    @Column(name="scale_id",columnDefinition="VARCHAR(50) COMMENT '量表id'")
    private String scaleId;

    @Column(name="title",columnDefinition="VARCHAR(50) COMMENT '量表标题'")
    private String title;


    @Lob
    @Column(name="dateil",columnDefinition="text COMMENT '题目详情'")
    private String dateil;


    @Lob
    @Column(name="answer",columnDefinition="text COMMENT '答案'")
    private String answer;

    @Column(name="score",columnDefinition="varchar(255) COMMENT '分数'")
    private String score;

    @Column(name="warn_scope",columnDefinition="varchar(255) COMMENT '分数预警范围'")
    private String warnScope;


    @Column(name="user_Id",columnDefinition="VARCHAR(50) COMMENT '答题用户id'")
    private String userId;


    @Column(name="upmq_Id",columnDefinition="VARCHAR(50) COMMENT '答题用户测评表id'")
    private String upmqId;

    @Column(name="group_id",columnDefinition="varchar(50) COMMENT '因子分组id'")
    private String groupId;

    @Column(name="serial",columnDefinition="varchar(50) COMMENT '因子分组序号'")
    private String serial;

    @Column(name="info",columnDefinition="varchar(50) COMMENT '因子分组标题'")
    private String info;

}

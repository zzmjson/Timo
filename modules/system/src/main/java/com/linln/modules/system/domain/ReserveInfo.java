package com.linln.modules.system.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.linln.common.utils.StatusUtil;
import lombok.Data;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Time;

//
@Data
@Entity
@Table(name = "sys_reserve_info")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer"})
@org.hibernate.annotations.Table(appliesTo = "sys_reserve_info", comment = "")
public class ReserveInfo extends BaseStringEntity {

    @Id
    @Column(name = "id", columnDefinition = "VARCHAR(50) comment '预约信息表'")
    private String id;
    @Column(name = "counselor_id", columnDefinition = "VARCHAR(50) COMMENT '咨询师id'")
    private String counselorId;
    @Column(name = "counselor_name", columnDefinition = "VARCHAR(50) COMMENT '咨询师姓名'")
    private String counselorName;
    @Column(name = "site", columnDefinition = "VARCHAR(50) COMMENT '咨询地点'")
    private String site;
    @Column(name = "res_date", columnDefinition = "Date COMMENT '预约日期'")
    private Date resDate;
    @Column(name = "res_time", columnDefinition = "Time COMMENT '预约具体时段'")
    private Time resTime;
    @Column(name = "res_type", columnDefinition = "VARCHAR(50) COMMENT '咨询类型'")
    private String resType;
    @Column(name = "manner", columnDefinition = "VARCHAR(50) COMMENT '咨询方式'")
    private String manner;
    @Column(name = "state", columnDefinition = "int COMMENT '当前状态 1 已完成 2 爽约 3 已取消 4 已过期  '")
    private int state=1;
    @Column(name = "user_id", columnDefinition = "VARCHAR(50) COMMENT '咨询用户Id'")
    private String userId;
    @Column(name = "nike_name", columnDefinition = "VARCHAR(50) COMMENT '咨询用户姓名'")
    private String nikeName;
    @Column(name = "feedback_rating", columnDefinition = "VARCHAR(50) COMMENT '反馈评级'")
    private String feedbackRating;
    @Column(name = "feedback", columnDefinition = "VARCHAR(500) COMMENT '反馈评价'")
    private String feedback;
    @Column(name = "emotion", columnDefinition = "VARCHAR(500) COMMENT '情绪'")
    private String emotion;
    @Column(name = "persional_state", columnDefinition = "VARCHAR(500) COMMENT '个人近况总结'")
    private String persionalState;
    @Column(name = "family_state", columnDefinition = "VARCHAR(500) COMMENT '家庭近况'")
    private String familyState;
    @Column(name = "problem", columnDefinition = "VARCHAR(500) COMMENT '咨询问题'")
    private String problem;
    @Column(name = "actuality", columnDefinition = "VARCHAR(500) COMMENT '现状'")
    private String actuality;
    @Column(name = "ishave", columnDefinition = "int COMMENT '是否添加量表 1 已添加 0 未添加'")
    private int ishave=0;
}
package com.linln.modules.system.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "sys_user_pmq")
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(value = { "hibernateLazyInitializer"})
@org.hibernate.annotations.Table(appliesTo = "sys_user_pmq", comment = "")
public class UserPmq extends BaseStringEntity {

    @Id
    @Column(name="id",columnDefinition="VARCHAR(50) COMMENT '用户测评表'")
    private String id;

    @Column(name="user_id",columnDefinition="VARCHAR(50) COMMENT '用户id'")
    private String userId;

    @Column(name="asses_id",columnDefinition="VARCHAR(50) COMMENT '测评计划表id'")
    private String assesId;

    @Column(name="user_name",columnDefinition="VARCHAR(50) COMMENT '用户名称'")
    private String userName;

    @Column(name="scale_id",columnDefinition="VARCHAR(50) COMMENT '量表id'")
    private String scaleId;

    @Lob
    @Column(name="scoring",columnDefinition="text COMMENT '评分标准'")
    private String scoring;

    @Lob
    @Column(name="consequence",columnDefinition="text COMMENT '测评结果'")
    private String consequence;

    @Lob
    @Column(name="suggest",columnDefinition="text COMMENT '辅导建议'")
    private String suggest;

    @Lob
    @Column(name="comment",columnDefinition="text COMMENT '咨询师评语'")
    private String comment;


    @Column(name="scale_title",columnDefinition="text COMMENT '量表标题'")
    private String scaleTitle;

    @Column(name="scale_alias",columnDefinition="text COMMENT '量表别名'")
    private String scaleAlias;

    @Column(name="only_no",columnDefinition="varchar(255) COMMENT '唯一编号'")
    private String onlyNo;

    @Column(name="scale_type_id",columnDefinition="varchar(255) COMMENT '量表类型id'")
    private String scaleTypeId;

    @Column(name="scale_type_name",columnDefinition="varchar(255) COMMENT '量表类别名称'")
    private String scaleTypeName;

    @Column(name="counselor_id",columnDefinition="varchar(255) COMMENT '咨询师id'")
    private String counselorId;

    @Column(name="counselor_name",columnDefinition="varchar(255) COMMENT '咨询师名称'")
    private String counselorName;

    @Column(name="sum_socre",columnDefinition="int COMMENT '总分'")
    private String sumScore;

    @Column(name="is_warn",columnDefinition="int COMMENT '是否预警(0 预警 1 不预警)'")
    private Integer isWarn=1;

    @Column(name="calculate_Time",columnDefinition="DATETIME COMMENT '计算总分时间'")
    private Date calculateTime;

    @Column(name="submit_Time",columnDefinition="DATETIME COMMENT '提交答案时间'")
    private Date submitTime;

    @Column(name="answer_required",columnDefinition="varchar(255) COMMENT '答题时长'")
    private String answerRequired;

    @Column(name="get_Number",columnDefinition="int COMMENT '是否计算分数(0 否 1 是)'")
    private Integer getNumber=1;

    @Lob
    @Column(name="group_score",columnDefinition="text COMMENT '因子得分详情'")
    private String groupScore;



}

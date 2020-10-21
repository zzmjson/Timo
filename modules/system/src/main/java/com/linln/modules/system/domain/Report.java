package com.linln.modules.system.domain;

import lombok.Data;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.sql.Date;

@Data
@Entity
@Table(name = "sys_report")
@EntityListeners(AuditingEntityListener.class)
public class Report extends BaseStringEntity {

    @Id
    @Column(name="id",columnDefinition="VARCHAR(50) COMMENT '个人报告表'")
    private String id;

    @Column(name="visitor_id",columnDefinition="VARCHAR(50) COMMENT '来访者id'")
    private String visitorId;

    @Column(name="visitor_name",columnDefinition="VARCHAR(50) COMMENT '来访者名称'")
    private String visitorName;

    @Column(name="consulting_type",columnDefinition="VARCHAR(50) COMMENT '咨询类型'")
    private String consultingType;

    @Column(name="sex",columnDefinition="VARCHAR(50) COMMENT '性别'")
    private String sex="男";

    @Column(name="case_id",columnDefinition="VARCHAR(50) COMMENT '个案来源id'")
    private String caseId;

    @Column(name="case_title",columnDefinition="VARCHAR(50) COMMENT '个案来源标题'")
    private String caseTitle;

    @Column(name="age",columnDefinition="VARCHAR(50) COMMENT '年龄'")
    private String age;

    @Column(name="picture",columnDefinition="VARCHAR(255) COMMENT '头像'")
    private String picture;

    @Column(name="rank_id",columnDefinition="VARCHAR(50) COMMENT '处理级别id'")
    private String rankId;

    @Column(name="rank_content",columnDefinition="VARCHAR(50) COMMENT '处理级别 '")
    private String rankContent;

    @Column(name="only",columnDefinition="VARCHAR(50) COMMENT '个案编号'")
    private String only;

    @Column(name="state",columnDefinition="VARCHAR(50) COMMENT '个人结案状态状态：是否结案，1 未结案 2 结案'")
    private String state="1";

    @Column(name="number",columnDefinition="int COMMENT '访谈次数'")
    private Integer number=0;

    @Column(name="reply_id",columnDefinition="VARCHAR(50) COMMENT '回访人id'")
    private String replyId;

    @Column(name="reply_name",columnDefinition="VARCHAR(50) COMMENT '回访人名称'")
    private String replyName;

    @Column(name="reply_Type",columnDefinition="VARCHAR(50) COMMENT '回访方式'")
    private String replyType;

    @Column(name="reply_Time",columnDefinition="DATE COMMENT '回访时间'")
    private Date replyTime;

    @Lob
    @Column(name="reply_state",columnDefinition="Text COMMENT '回访个案状态'")
    private String replyState;

    @Lob
    @Column(name="note",columnDefinition="TEXT COMMENT '回访备注'")
    private String note;

    @Column(name="rese_id",columnDefinition="VARCHAR(50) COMMENT '预约信息id'")
    private String reseId;

}

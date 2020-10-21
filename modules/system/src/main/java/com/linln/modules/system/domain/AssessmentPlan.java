package com.linln.modules.system.domain;

import lombok.Data;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.sql.Date;

@Data
@Entity
@Table(name = "sys_asse_plan")
@EntityListeners(AuditingEntityListener.class)
@org.hibernate.annotations.Table(appliesTo = "sys_asse_plan", comment = "")
public class AssessmentPlan extends BaseStringEntity{
    @Id
    @Column(name="id",columnDefinition="VARCHAR(50) COMMENT '测评计划表'")
    private String id;

    @Column(name="title",columnDefinition="VARCHAR(50) COMMENT '计划标题'")
    private String title;

    @Column(name="scale_number",columnDefinition="int COMMENT '量表数'")
    private int scaleNumber;

    @Column(name="star_time",columnDefinition="Date COMMENT '开始时间'")
    private Date starTime;

    @Column(name="end_time",columnDefinition="Date COMMENT '结束时间'")
    private Date endTime;

    @Column(name="state",columnDefinition="int COMMENT '计划状态 1 已启用(生效) 2 已启用(过期) 3 已停用'")
    private int state;

    @Lob
    @Column(name="per_list",columnDefinition="LONGTEXT COMMENT '权限列表'")
    private String perList;

}

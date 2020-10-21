package com.linln.modules.system.domain;

import lombok.Data;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Time;

@Data
@Entity
@Table(name = "sys_rese_timesolt")
@EntityListeners(AuditingEntityListener.class)
@org.hibernate.annotations.Table(appliesTo = "sys_rese_timesolt", comment = "")
public class ReseTimeSolt extends BaseStringEntity {
    @Id
    @Column(name="id",columnDefinition="VARCHAR(50) COMMENT '预约时段表'")
    private String id;
    @Column(name="counselor_id",columnDefinition="VARCHAR(50) COMMENT '咨询师id'")
    private String counselorId;
    @Column(name="counselor_name",columnDefinition="VARCHAR(50) COMMENT '咨询师名称'")
    private String counselorName;
    @Column(name="star_date",columnDefinition="Date COMMENT '开始生效时间'")
    private Date starDate;
    @Column(name="end_date",columnDefinition="Date COMMENT '结束预约时间'")
    private Date endDate;
    @Column(name="star_time",columnDefinition="Time COMMENT '预约时刻'")
    private Time starTime;
    @Column(name="time_slot",columnDefinition="VARCHAR(50) COMMENT '持续时长'")
    private String timeSlot;
    @Column(name="bwfore",columnDefinition="VARCHAR(50) COMMENT '提前截止提交'")
    private String before;
    @Column(name="action",columnDefinition="VARCHAR(50) COMMENT '开始允许提交'")
    private String action;
    @Column(name="rese_count",columnDefinition="int COMMENT '可预约数'")
    private String reseCount;
    @Column(name="site",columnDefinition="VARCHAR(50) COMMENT '咨询地点'")
    private String site;
    @Column(name="consult_way ",columnDefinition="VARCHAR(50) COMMENT '咨询方式'")
    private String consultWay ;
    @Column(name="state",columnDefinition="int COMMENT '状态 1 已启用 2 已停用 3 已失效'")
    private String state;






}

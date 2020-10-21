package com.linln.modules.system.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Data
@Entity
@Table(name = "sys_scale")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer"})
@EntityListeners(AuditingEntityListener.class)
public class Scale extends BaseStringEntity {


    @Id
    @Column(name="id",columnDefinition="VARCHAR(50) COMMENT '量表'")
    private String id;

    @Column(name="title",columnDefinition="VARCHAR(50) COMMENT '量表标题'")
    private String title;

    @Column(name="alias",columnDefinition="VARCHAR(50) COMMENT '量表别名'")
    private String alias;
    @Lob
    @Column(name="instruction",columnDefinition="text COMMENT '测评指导语'")
    private String instruction="";

    @Lob
    @Column(name="info",columnDefinition="text COMMENT '量表说明'")
    private String info="";

    @Column(name="only_no",columnDefinition="varchar(255) COMMENT '唯一编号'")
    private String onlyNo="";

    @Column(name="scale_type_id",columnDefinition="varchar(255) COMMENT '量表类型id'")
    private String scaleTypeId="";

    @Column(name="scale_type_name",columnDefinition="varchar(255) COMMENT '量表类别名称'")
    private String scaleTypeName="";

    @Column(name="user_id",columnDefinition="varchar(255) COMMENT '咨询师id'")
    private String UserId="";

    @Column(name="user_nickname",columnDefinition="varchar(255) COMMENT '咨询师名称'")
    private String UserNickname="";

    @Column(name="warn_scope",columnDefinition="varchar(255) COMMENT '总分预警范围'")
    private String warnScope="";

    @Column(name="duration_time",columnDefinition="varchar(255) COMMENT '测量表完成时长(0为不限时)'")
    private String durationTime="0";

    @Column(name="state",columnDefinition="int COMMENT '量表状态 0 未发布 1已发布'")
    private int state=1;
    @Column(name="is_group",columnDefinition="int COMMENT '量表是否有因子分组 0 没有 1有'")
    private int isGroup;




}

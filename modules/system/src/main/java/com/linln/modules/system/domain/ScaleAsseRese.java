package com.linln.modules.system.domain;

import lombok.Data;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.sql.Date;

@Data
@Entity
@Table(name = "sys_scale_asse_rese")
@EntityListeners(AuditingEntityListener.class)
@org.hibernate.annotations.Table(appliesTo = "sys_scale_asse_rese", comment = "")
public class ScaleAsseRese extends BaseStringEntity {

    @Id
    @Column(name="id",columnDefinition="VARCHAR(50) COMMENT '量表测评预约表'")
    private String id;

    @Column(name="visitor ",columnDefinition="VARCHAR(50) COMMENT '预约主体'")
    private String visitor ;

    @Column(name="counselor_name ",columnDefinition="VARCHAR(50) COMMENT '咨询师'")
    private String counselorName ;

    @Column(name="coun_id ",columnDefinition="VARCHAR(50) COMMENT '咨询师id'")
    private String counId ;

    @Column(name="site ",columnDefinition="VARCHAR(50) COMMENT '咨询地点'")
    private String site ;

    @Column(name="res_date ",columnDefinition="Date COMMENT '预约日期'")
    private Date resDate ;

    @Column(name="res_time ",columnDefinition="VARCHAR(50) COMMENT '预约时间'")
    private String resTime ;

    @Column(name="state ",columnDefinition="int COMMENT '状态 1 已预约 2 已完成 3 已过期'")
    private int state=1 ;
    


}

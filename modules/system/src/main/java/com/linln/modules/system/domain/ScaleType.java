package com.linln.modules.system.domain;

import lombok.Data;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Data
@Entity
@Table(name = "sys_scale_type")
@EntityListeners(AuditingEntityListener.class)
public class ScaleType extends BaseStringEntity {

    @Id
    @Column(name="id",columnDefinition="VARCHAR(50) COMMENT '量表类别'")
    private String id;

    @Column(name="name",columnDefinition="VARCHAR(50) COMMENT '类别名称'")
    private String name;
    @Lob
    @Column(name="note",columnDefinition="VARCHAR(500) COMMENT '备注'")
    private String note;

}

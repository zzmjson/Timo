package com.linln.modules.system.domain;

import lombok.Data;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Data
@Entity
@Table(name = "sys_asse_scale")
@EntityListeners(AuditingEntityListener.class)
@org.hibernate.annotations.Table(appliesTo = "sys_asse_scale", comment = "")
public class AsseScale extends BaseStringEntity {
    @Id
    @Column(name="id",columnDefinition="VARCHAR(50) COMMENT '测评计划与测评量表对映关系表'")
    private String id;
    @Column(name="asse_id",columnDefinition="VARCHAR(50) COMMENT '测评计划id'")
    private String asseId;
    @Column(name="scale_id",columnDefinition="VARCHAR(50) COMMENT '测评量表id'")
    private String scaleId;
}

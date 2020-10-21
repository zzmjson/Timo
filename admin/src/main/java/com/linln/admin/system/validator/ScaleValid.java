package com.linln.admin.system.validator;



import com.linln.modules.system.service.ScaleService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

/**
 * @author 小懒虫
 * @date 2018/8/14
 */
@Data
public class ScaleValid implements Serializable {

    @Autowired
    ScaleService scaleService;

    @NotEmpty(message = "量表标题不能为空")
    private String title;

    @NotEmpty(message = "量表别名不能为空")
    private String alias;

    @NotEmpty(message = "唯一编号不能为空")
    private String onlyNo;


}

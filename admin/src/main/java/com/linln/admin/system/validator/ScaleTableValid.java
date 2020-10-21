package com.linln.admin.system.validator;

import com.linln.modules.system.domain.Dept;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * @author 小懒虫
 * @date 2018/8/14
 */
@Data
public class ScaleTableValid implements Serializable {
    @NotEmpty(message = "类型名称不能为空")
    private String name;

}

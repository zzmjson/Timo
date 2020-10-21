package com.linln.admin.system.validator;



import com.linln.modules.system.service.ScaleService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

/**
 * @author 小懒虫
 * @date 2018/8/14
 */
@Data
public class ContentTopicValidator implements Serializable {



    @NotEmpty(message = "题目内容不能为空")
    private String dateil;


    @NotEmpty(message = "选项内容不能为空")
    private String content;


}

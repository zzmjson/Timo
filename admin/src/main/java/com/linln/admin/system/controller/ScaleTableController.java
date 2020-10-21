package com.linln.admin.system.controller;


import com.alibaba.fastjson.JSON;
import com.linln.admin.system.validator.ScaleTableValid;
import com.linln.admin.system.validator.UserValid;
import com.linln.common.utils.RandomUtil;
import com.linln.common.utils.ResultVoUtil;
import com.linln.common.vo.ResultVo;
import com.linln.component.actionLog.action.UserAction;
import com.linln.component.actionLog.annotation.ActionLog;
import com.linln.component.actionLog.annotation.EntityParam;
import com.linln.modules.system.domain.ScaleType;

import com.linln.modules.system.service.ScaleTableService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.UUID;


/**
 * @author 小懒虫
 * @date 2018/8/14
 */
@Controller
@RequestMapping("/system/scaleTable")
@RequiresPermissions("system:scaleTable:index")
public class ScaleTableController {


    @Autowired
    ScaleTableService scaleTableService;

    /**
     * 列表页面
     */
    @GetMapping("/index")
//    @RequiresPermissions("system:scaleTable:index")
    public String index(@RequestParam(value = "searchText",defaultValue = "") String searchText ,
                        @RequestParam(value = "page",defaultValue = "1") Integer page,
                        @RequestParam(value = "size",defaultValue = "10") Integer size,
                        Model model,  HttpServletRequest request) {
        PageRequest req = PageRequest.of(page-1, size);
        Page<ScaleType> list = scaleTableService.fetchScaleTypeBySearch(searchText,req);
        // 封装数据
        model.addAttribute("list", list.getContent());
        model.addAttribute("page", list);
        return "/system/scaleTable/index";
    }

    /**
     * 跳转到添加页面
     */
    @GetMapping("/add")
//    @RequiresPermissions("system:scaleTable:add")
    public String toAdd() {
        return "/system/scaleTable/add";
    }


    /**
     * 保存添加/修改的数据
     * @param valid
     * @param scaleType
     * @return
     */
    @PostMapping("/save")
//    @RequiresPermissions({"system:scaleTable:add", "system:scaleTable:edit"})
    @ResponseBody
    @ActionLog(key=UserAction.USER_SAVE, action = UserAction.class)
    public ResultVo save(@Validated ScaleTableValid valid, @EntityParam ScaleType scaleType) throws Exception {

        if(scaleType.getId()==null){   //添加
            Long id= RandomUtil.getId();
            scaleType.setId(id+"");
        }else{    //编辑
            scaleType.setModifyDateTime(new Date());
        }
        scaleTableService.save(scaleType);
        return ResultVoUtil.SAVE_SUCCESS;
    }
    /**
     * 跳转到编辑页面
     */
    @GetMapping("/edit/{id}")
//    @RequiresPermissions("system:scaleTable:edit")
    public String toEdit(@PathVariable("id") String stId, Model model) {
        ScaleType scaleType1=scaleTableService.fetchOne(stId);
        model.addAttribute("scaleTable", scaleType1);
        return "/system/scaleTable/add";
    }






}

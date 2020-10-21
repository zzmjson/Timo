package com.linln.admin.system.controller;



import com.alibaba.fastjson.JSON;
import com.linln.admin.system.detailsRealize.SummaryRealize;
import com.linln.admin.system.validator.ScaleValid;
import com.linln.common.utils.RandomUtil;
import com.linln.common.utils.ResultVoUtil;
import com.linln.common.vo.ResultVo;
import com.linln.component.actionLog.action.UserAction;
import com.linln.component.actionLog.annotation.ActionLog;
import com.linln.component.actionLog.annotation.EntityParam;
import com.linln.component.shiro.ShiroUtil;
import com.linln.modules.system.domain.*;
//import com.linln.modules.system.service.ScaleService;
import com.linln.modules.system.service.ScaleService;
import com.linln.modules.system.service.ScaleTableService;
import com.linln.modules.system.service.SummaryService;
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
import java.util.List;
import java.util.Map;
import java.util.TreeMap;


/**
 * @author 小懒虫
 * @date 2018/8/14
 */
@Controller
@RequestMapping("/system/scale")
@RequiresPermissions("system:scale:index")
public class ScaleController {


    @Autowired
    ScaleService scaleService;
    @Autowired
    ScaleTableService scaleTableService;
    @Autowired
    SummaryRealize summaryRealize;
    @Autowired
    SummaryService summaryService;

    /**
     * 列表页面
     */
    @GetMapping("/index")
//    @RequiresPermissions("system:scale:index")
    public String index(@RequestParam(value = "searchText",defaultValue = "") String searchText ,
                        @RequestParam(value = "page",defaultValue = "1") Integer page,
                        @RequestParam(value = "size",defaultValue = "10") Integer size,
                        Model model,  HttpServletRequest request) {
        PageRequest req = PageRequest.of(page-1, size);
        Page<Scale> list = scaleService.fetchScaleBySearch(searchText,req);
        // 封装数据
        model.addAttribute("list", list.getContent());
        model.addAttribute("page", list);
        return "/system/scale/index";
    }


    /**
     * 跳转到添加页面
     */
    @GetMapping("/add")
//    @RequiresPermissions("system:scale:add")
    public String toAdd() {
        return "/system/scale/add";
    }


    /**
     * 保存添加/修改的数据
     * @param valid
     * @param scale
     * @return
     */
    @PostMapping("/save")
//    @RequiresPermissions({"system:scale:add", "system:scale:edit"})
    @ResponseBody
    @ActionLog(key=UserAction.USER_SAVE, action = UserAction.class)
    public ResultVo save(@Validated ScaleValid valid, @EntityParam Scale scale,
                         @RequestParam(value = "grade",defaultValue = "") List<String> grades,
                         @RequestParam(value = "contentSummary",defaultValue = "") List<String> contentSummarys,
                         @RequestParam(value = "suggest",defaultValue = "") List<String> suggests
                         ) throws Exception {

        System.out.println(JSON.toJSONString(suggests));
        User user= ShiroUtil.getSubject();
        for (int i=0;i<grades.size();i++){
            if(contentSummarys.get(i).equals("")){
                return  ResultVoUtil.error("结论内容不能为空");
            }
            String a=grades.get(i).substring(0,grades.get(i).indexOf("-"));
            String aa=grades.get(i).substring(grades.get(i).indexOf("-" )+1);
            if(aa.equals(a)|| Double.parseDouble(aa)<Double.parseDouble(a)){
                return ResultVoUtil.error("分数范围有误");
            }
        }
        if(scale.getId()==null){   //添加
            Long id= RandomUtil.getId();
            scale.setId(id+"");
            scale.setCreateDateTime(new Date());
            scale.setCreateUser(user.getId()+"");
        }else{    //编辑
            Scale scale1=scaleService.getOne(scale.getId());
            scale.setModifyDateTime(new Date());
            scale.setModifyUser(user.getId()+"");
            scale.setCreateUser(scale1.getCreateUser());
            scale.setCreateDateTime(scale1.getCreateDateTime());
        }
        Scale scaleonlyNo= scaleService.findByOnlyNoId(scale.getOnlyNo());
      if(scaleonlyNo!=null  && !scaleonlyNo.getId().equals(scale.getId())){
          return ResultVoUtil.error("唯一编号已使用");
      }

        summaryRealize.saveSummary(grades,contentSummarys,suggests,scale.getId(),user.getId()+"");  //添加结论
        scaleService.save(scale);
        return ResultVoUtil.SAVE_SUCCESS;
    }



    /**
     * 获取排序部门列表
     */
    @GetMapping("/scaleTypeList")
//    @RequiresPermissions({"system:scale:add", "system:scale:edit"})
    @ResponseBody
    public Map<String, String> sortList(){
        List<ScaleType> levelDept = scaleTableService.getBayAll();
        Map<String, String> sortMap = new TreeMap<>();
        for (int i = 1; i <= levelDept.size(); i++) {
            sortMap.put(levelDept.get(i-1).getId(), levelDept.get(i - 1).getName());
        }
        return sortMap;
    }

    /**
     * 跳转到编辑页面
     */
    @GetMapping("/edit/{id}")
//    @RequiresPermissions("system:scale:edit")
    public String toEdit(@PathVariable("id") Scale scale, Model model) {
        List<Summary>  summaries=summaryService.findByScaleIdGradeOrderByAsc(scale.getId());

        model.addAttribute("summaries", summaries);
        model.addAttribute("scale", scale);
        return "/system/scale/add";
    }






}

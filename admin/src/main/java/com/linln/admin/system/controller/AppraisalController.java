package com.linln.admin.system.controller;


import com.alibaba.fastjson.JSON;
import com.linln.admin.system.detailsRealize.SummaryRealize;
import com.linln.admin.system.detailsRealize.TopicMaterialRealize;
import com.linln.admin.system.validator.ScaleValid;
import com.linln.common.utils.MsgUtil;
import com.linln.common.utils.RandomUtil;
import com.linln.common.utils.ResultVoUtil;
import com.linln.common.vo.ResultVo;
import com.linln.component.actionLog.action.UserAction;
import com.linln.component.actionLog.annotation.ActionLog;
import com.linln.component.actionLog.annotation.EntityParam;
import com.linln.component.shiro.ShiroUtil;
import com.linln.modules.system.domain.*;
import com.linln.modules.system.service.AppraisalService;
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
import java.util.*;

//import com.linln.modules.system.service.ScaleService;


/**
 * @author 小懒虫
 * @date 2018/8/14
 */
@Controller
@RequestMapping("/system/appraisal")
@RequiresPermissions("system:appraisal:index")
public class AppraisalController {

    @Autowired
    AppraisalService appraisalService;
    @Autowired
    TopicMaterialRealize topicMaterialRealize;
    @Autowired
    ScaleService scaleService;


    /**
     * 列表页面
     */
    @GetMapping("/index")
    public String index(@RequestParam(value = "searchText",defaultValue = "") String searchText ,
                        @RequestParam(value = "page",defaultValue = "1") Integer page,
                        @RequestParam(value = "size",defaultValue = "10") Integer size,
                        Model model,  HttpServletRequest request) {
        PageRequest req = PageRequest.of(page-1, size);
        Page<UserPmq> list = appraisalService.fetchAppraisalBySearch(searchText,req);
//         封装数据
        model.addAttribute("list", list.getContent());
        model.addAttribute("page", list);
        return "/system/appraisal/index";
    }

    /**
     * 跳转到用户测评记录页面
     * @param upId
     * @param model
     * @return
     */
    @RequestMapping(value = "/record/{upId}")
//    @RequiresPermissions("system:appraisal:record")
    public String record(@PathVariable("upId") String upId,Model model){
        UserPmq userPmq=appraisalService.fetchOne(upId);
        List<List<Map<String,Object>>>  getAllSale= appraisalService.getAllSale(userPmq.getScaleId(),"1",userPmq.getId());
        model.addAttribute("list",getAllSale);
        model.addAttribute("userPmq",userPmq);
        return "/system/appraisal/record";
    }




    @RequestMapping("/report")
//    @RequiresPermissions("system:appraisal:repost")
    public String report(){
        return "/system/appraisal/report";
    }
//
//    @ResponseBody
//    @GetMapping("/getTopic")   //获取用户答题信息
//    @RequiresPermissions("system:appraisal:getTopic")
//    public ResultVo  getTopic(@RequestParam(value = "uspId")String uspId) {
//        UserPmq userPmq=appraisalService.fetchOne(uspId);
//         List<List<Map<String,Object>>>  getAllSale= appraisalService.getAllSale(userPmq.getScaleId());
//        return ResultVoUtil.success(getAllSale);
//    }
    @RequestMapping("/settle/{param}")  //重新计算测评分数
//    @RequiresPermissions("system:appraisal:settle")
    @ResponseBody
    public ResultVo  settle(
            @PathVariable("param") String param,
            @RequestParam(value = "ids", required = true) List<String> ids
    ) throws Exception {
        User user= ShiroUtil.getSubject();
        UserPmq userPmq=appraisalService.fetchOne(ids.get(0));
        topicMaterialRealize.correct(userPmq.getScaleId(),userPmq.getUserId(),userPmq.getSubmitTime(),user.getId()+"",userPmq.getAssesId());
        return ResultVoUtil.success(MsgUtil.OPTSUCCESS);
    }



    //获取用户测评档案报告
    @RequestMapping("/getReport/{uspId}")
//    @RequiresPermissions("system:appraisal:getReport")
    public String  getReport(@PathVariable(value = "uspId")String uspId,Model model ) throws Exception {
       Map<String ,Object> map=appraisalService.getUserPmqBys(uspId);
        model.addAttribute("map",map);
        return "/system/appraisal/report";
    }



    //咨询师评语修改
    @RequestMapping("/evaluate")
//    @RequiresPermissions("system:appraisal:evaluate")
    @ResponseBody
    public ResultVo getReport(
            @RequestParam(value = "id")String id,
            @RequestParam(value = "comment")String comment) throws Exception {
        UserPmq userPmq=appraisalService.fetchOne(id);
        User user= ShiroUtil.getSubject();  //当前咨询师
        userPmq.setCounselorId(user.getId()+"");
        userPmq.setCounselorName(user.getNickname());
        userPmq.setComment(comment);
        userPmq.setModifyUser(user.getId()+"");
        userPmq.setModifyDateTime(new Date());
        appraisalService.save(userPmq);
        return ResultVoUtil.success(MsgUtil.ADDSUCCESS);
    }

    @RequestMapping("/scoreDetail/{id}")
    public String scoreDetail(@PathVariable("id") UserPmq userPmq,Model model){
//        System.out.println(userPmq);
        String[] arr = userPmq.getGroupScore().split(";");
        List head =new ArrayList();
        List content = new ArrayList();
        for (String s : arr) {
            String [] temp = s.split(",");
            head.add(temp[0]);
            content.add(temp[1]);
        }
        model.addAttribute("userPmq",userPmq);
        model.addAttribute("head",head);
        model.addAttribute("content",content);
        return "/system/appraisal/scoreDetail";
    }

    public static void main(String[] args) {
        String str = "求和_SUM,4.0;平均_AVG,2.5;";
        String[] arr = str.split(";");
        List head =new ArrayList();
        List content = new ArrayList();
        for (String s : arr) {
            String [] temp = s.split(",");
            head.add(temp[0]);
            content.add(temp[1]);
        }
        for (Object o : head) {
            System.out.print(o);
            System.out.print("---");
        }
        System.out.println();
        System.out.println("------------------");
        for (Object o : content) {
            System.out.print(o);
            System.out.print("---");
        }
    }






}

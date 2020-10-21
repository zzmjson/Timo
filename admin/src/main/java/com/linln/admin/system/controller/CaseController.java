package com.linln.admin.system.controller;

import com.linln.common.utils.Iputil;
import com.linln.common.utils.RandomUtil;
import com.linln.common.utils.RedisUtil;
import com.linln.common.utils.ResultVoUtil;
import com.linln.common.vo.ResultVo;
import com.linln.component.shiro.ShiroUtil;
import com.linln.component.thymeleaf.utility.DictUtil;
import com.linln.modules.system.domain.*;
import com.linln.modules.system.service.*;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.persistence.Id;
import javax.servlet.http.HttpServletRequest;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/system/case")
//@RequiresPermissions("system:case:index")
public class CaseController {

    @Autowired
    private UserService userService;
    @Autowired
    private CaseService caseService;
    @Autowired
    private ReportService reportService;
    @Autowired
    private ReserveInfoService reserveInfoService;
    @Autowired
    private InterviewService interviewService;
    @Autowired
    private InterviewCoetentService interviewCoetentService;
    @Autowired
    private ReportContentService reportContentService;
    @Autowired
    private RoleService roleService;

    /**
     * 跳转到个案设置首页
     * @return
     */
    @RequestMapping("/index")
    @RequiresPermissions("system:case:index")
    public String index(){
        return "/system/case/index";
    }

    /**
     * 跳转到访谈模版页面
     * @return
     */
    @RequestMapping("/talkTemplate")
    @RequiresPermissions("system:case:talkTemplate")
    public String caseTemplate(){
        return "/system/case/talkTemplate";
    }

    /**
     * 跳转到访谈模版页面
     * @return
     */
    @RequestMapping("/reportTemplate")
    @RequiresPermissions("system:case:reportTemplate")
    public String reportTemplate(){
        return "/system/case/reportTemplate";
    }

    /**
     * 跳转到个案统管页面
     * @return
     */
    @RequestMapping("/manage")
    @RequiresPermissions("system:case:manage")
    public String manage(@RequestParam(value = "only",defaultValue = "") String only ,
                         @RequestParam(value = "visitorName",defaultValue = "") String visitorName ,
                         @RequestParam(value = "caseId",defaultValue = "0") String caseId ,
                         @RequestParam(value = "rankId",defaultValue = "0") String rankId ,
                         @RequestParam(value = "page",defaultValue = "1") Integer page,
                         @RequestParam(value = "size",defaultValue = "10") Integer size,
                         HttpServletRequest request,Model model){
        PageRequest req = PageRequest.of(page-1, size);
        //获取当前登录咨询师信息
//        String ip = Iputil.getIpAddr(request);
//        String userInfo = "Heart:user"+ip;
//        User user = (User) redisUtil.get(userInfo);
        User user = ShiroUtil.getSubject();
        List<String> roles = roleService.findRoleTitleByUserId(user.getId());
        Page<Report> repsotsByCreateUser = null;
        boolean isAdmin = false;
        for (String role : roles){
            if ("系统管理员".equals(role)){
                isAdmin=true;
            }
        }
        if (isAdmin){
            repsotsByCreateUser = reportService.findAllBySearch(only, visitorName, caseId, rankId, req);
        }else {
             repsotsByCreateUser = reportService.findRepsotsByCreateUser(user.getId().toString(),only,visitorName,caseId,rankId,req);
        }
        model.addAttribute("list",repsotsByCreateUser.getContent());
        model.addAttribute("page",repsotsByCreateUser);
        //获取级别信息
        List<Map<String, String>> level = caseService.findLevel();
        //获取来源信息
        List<Map<String, String>> sourse = caseService.findSourse();
        model.addAttribute("sourse",sourse);
        model.addAttribute("level",level);
        return "/system/case/manage";
    }

    /**
     * 跳转到个案信息详情页面
     * @return
     */
    @RequestMapping("/caseDetail/{id}")
//    @RequiresPermissions("system:case:caseDetail")
    public String caseDetail(@PathVariable("id") String id,Model model){
        Report one = reportService.getOne(id);
        List<ReserveInfo> userIdAndCounId = reserveInfoService.findUserIdAndCounId(one.getVisitorId(), one.getCreateUser());
        //封装咨询方式
        Map<String, String> consult_way = DictUtil.value("CONSULT_WAY");
        List consultWay = new ArrayList();
        for (int i=1;i<=consult_way.size();i++){
            consultWay.add(consult_way.get(i+""));
        }

        //封装预约地址
        Map<String, String> site = DictUtil.value("SITE");
        List list = new ArrayList();
        for (int i=1;i<=site.size();i++){
            list.add(site.get(i+""));
        }

        List<Interview> byReportId = interviewService.findByReportId(one.getId());
        //查询访谈类容
        Map<String,InterviewContent> map = new HashMap<>();
        int index = 0;
        if (byReportId.size()>0){

            for (Interview interview : byReportId){
                List<InterviewContent>  listByInterviewId = interviewCoetentService.findListByInterviewId(interview.getId());
                int finalIndex = index;
                listByInterviewId.forEach(x->{
                    map.put(x.getTypeTitle()+ finalIndex,x);
                });
                index++;
            }
        }
        System.out.println(map);
        model.addAttribute("interviewContent",map);
        model.addAttribute("intviews",byReportId);
        model.addAttribute("sites",list);
        model.addAttribute("ways",consultWay);
        model.addAttribute("report",one);
        model.addAttribute("list",userIdAndCounId);
        return "/system/case/caseDetail";
    }

    /**
     * 跳转到添加个案信息页面
     * @return
     */
    @RequestMapping("/add")
//    @RequiresPermissions("system:case:add")
    public String AddCase(Model model,HttpServletRequest request){
        //获取当前登录咨询师信息
        User user = ShiroUtil.getSubject();
        List<String> roles = roleService.findRoleTitleByUserId(user.getId());
        boolean isAdmin = false;
        List<ReserveInfo> reserveInfos=null;
       for (String role : roles){
           if ("系统管理员".equals(role)){
               isAdmin=true;
           }
       }
       if (isAdmin){
            reserveInfos = reserveInfoService.findReserAll();
       }else {
            reserveInfos = reserveInfoService.findreserByCounselorId(user.getId().toString());
       }
        model.addAttribute("list",reserveInfos);
        List<Map<String, String>> users = userService.findUsers();
        List<Map<String, String>> sourse = caseService.findSourse();
        List<Map<String, String>> level = caseService.findLevel();
        //封装咨询类型
        Map<String, String> consult_type = DictUtil.value("CONSULT_TYPE");
        List cons = new ArrayList();
        for (int i=1;i<=consult_type.size();i++){
            cons.add(consult_type.get(i+""));
        }
        model.addAttribute("level",level);
        model.addAttribute("sourse",sourse);
        model.addAttribute("users",users);
        model.addAttribute("type",cons);
        return "/system/case/add";
    }

    @ResponseBody
    @RequestMapping("/saveCase")
//    @RequiresPermissions("system:case:saveCase")
    public ResultVo savaSace(Report report, HttpServletRequest request,@RequestParam("reserInfoId") String reserInfoId) throws Exception{
        report.setId(RandomUtil.getId().toString());
        if (reserInfoId==null || reserInfoId.isEmpty()){
            User user = userService.getById(Long.parseLong(report.getVisitorId()));
            report.setSex(user.getSex()==1?"男":"女");
            report.setAge(user.getAge());
            report.setPicture(user.getPicture());
            report.setVisitorName(user.getNickname());
            report.setOnly(RandomUtil.getCaseNo());
            //个案来源
            CaseSet source = caseService.findById(report.getCaseId());
            //个案级别
            CaseSet level = caseService.findById(report.getRankId());
            report.setCaseTitle(source.getContent());
            report.setRankContent(level.getContent());
            //获取当前登录咨询师信息
//            String ip = Iputil.getIpAddr(request);
//            String userInfo = "Heart:user"+ip;
//            User user1 = (User) redisUtil.get(userInfo);
            User user1 = ShiroUtil.getSubject();
            report.setCreateUser(user1.getId().toString());
            report.setReplyName(user1.getNickname());
            report.setReplyId(user1.getId().toString());
        }else {
            ReserveInfo one = reserveInfoService.getOne(reserInfoId);
            User user = userService.getById(Long.parseLong(one.getUserId()));
            report.setSex(user.getSex()==1?"男":"女");
            report.setAge(user.getAge());
            report.setPicture(user.getPicture());
            report.setVisitorName(user.getNickname());
            report.setVisitorId(one.getUserId());
            report.setOnly(RandomUtil.getCaseNo());
            report.setCreateUser(one.getCounselorId());
            report.setReplyId(one.getCounselorId());
            report.setReplyName(one.getCounselorName());
            report.setCaseTitle("心理测评");
            report.setCaseId("159041696905400333");
            report.setRankContent("一般");
            report.setRankId("159039999024200204");
            report.setConsultingType(one.getResType());
            report.setReseId(one.getId());
            reserveInfoService.updataState(2,one.getId());
        }
        //添加报告内容
        ReportContent pone = new ReportContent(RandomUtil.getId().toString(),"个案信息","0",null,null,report.getId());
        ReportContent cone = new ReportContent(RandomUtil.getId().toString(),"个案信息",pone.getId(),"个案自述",null,report.getId());
        ReportContent ctwo = new ReportContent(RandomUtil.getId().toString(),"个案信息",pone.getId(),"来访原因",null,report.getId());

        ReportContent ptwo = new ReportContent(RandomUtil.getId().toString(),"家庭关系,人际关系及个人成长经历","0",null,null,report.getId());
        ReportContent cthree = new ReportContent(RandomUtil.getId().toString(),"家庭关系,人际关系及个人成长经历",ptwo.getId(),"人际关系",null,report.getId());
        ReportContent cfour = new ReportContent(RandomUtil.getId().toString(),"家庭关系,人际关系及个人成长经历",ptwo.getId(),"家庭关系",null,report.getId());
        ReportContent cfive = new ReportContent(RandomUtil.getId().toString(),"家庭关系,人际关系及个人成长经历",ptwo.getId(),"个人成长经历",null,report.getId());

        ReportContent pthree = new ReportContent(RandomUtil.getId().toString(),"个人的情绪、个性特征、兴趣爱好、自我认识评价及常用的应对方式","0",null,null,report.getId());
        ReportContent csix = new ReportContent(RandomUtil.getId().toString(),"个人的情绪、个性特征、兴趣爱好、自我认识评价及常用的应对方式",pthree.getId(),"个人的情绪",null,report.getId());
        ReportContent cseven = new ReportContent(RandomUtil.getId().toString(),"个人的情绪、个性特征、兴趣爱好、自我认识评价及常用的应对方式",pthree.getId(),"个性特征",null,report.getId());
        ReportContent ceight = new ReportContent(RandomUtil.getId().toString(),"个人的情绪、个性特征、兴趣爱好、自我认识评价及常用的应对方式",pthree.getId(),"兴趣爱好",null,report.getId());
        ReportContent cnine = new ReportContent(RandomUtil.getId().toString(),"个人的情绪、个性特征、兴趣爱好、自我认识评价及常用的应对方式",pthree.getId(),"自我认识评价",null,report.getId());
        ReportContent cten = new ReportContent(RandomUtil.getId().toString(),"个人的情绪、个性特征、兴趣爱好、自我认识评价及常用的应对方式",pthree.getId(),"常用应对方式",null,report.getId());

        ReportContent pfour = new ReportContent(RandomUtil.getId().toString(),"既往病史，家族病史","0",null,null,report.getId());
        ReportContent celeven = new ReportContent(RandomUtil.getId().toString(),"既往病史，家族病史",pfour.getId(),"既往病史",null,report.getId());
        ReportContent ctwelve = new ReportContent(RandomUtil.getId().toString(),"既往病史，家族病史",pfour.getId(),"家族病史",null,report.getId());

        ReportContent pfive = new ReportContent(RandomUtil.getId().toString(),"心理测试结果","0",null,null,report.getId());
        ReportContent cthirteen = new ReportContent(RandomUtil.getId().toString(),"心理测试结果",pfive.getId(),"心理测试结果",null,report.getId());

        ReportContent psix = new ReportContent(RandomUtil.getId().toString(),"咨询师的一般印象","0",null,null,report.getId());
        ReportContent cfoueteen = new ReportContent(RandomUtil.getId().toString(),"咨询师的一般印象",psix.getId(),"咨询师的一般印象",null,report.getId());

        ReportContent pseven = new ReportContent(RandomUtil.getId().toString(),"诊断评价与意见","0",null,null,report.getId());
        ReportContent cfiveteen = new ReportContent(RandomUtil.getId().toString(),"诊断评价与意见",pseven.getId(),"诊断评价与意见",null,report.getId());

        ReportContent peight = new ReportContent(RandomUtil.getId().toString(),"咨询各阶段效果分析","0",null,null,report.getId());
        ReportContent csixteen = new ReportContent(RandomUtil.getId().toString(),"咨询各阶段效果分析",peight.getId(),"咨询各阶段效果分析",null,report.getId());

        ReportContent pnine = new ReportContent(RandomUtil.getId().toString(),"咨询师分析总结","0",null,null,report.getId());
        ReportContent cseventeen = new ReportContent(RandomUtil.getId().toString(),"咨询师分析总结",pnine.getId(),"咨询师分析总结",null,report.getId());
        reportService.save(report);
        reportContentService.save(pone);
        reportContentService.save(ptwo);
        reportContentService.save(pthree);
        reportContentService.save(pfour);
        reportContentService.save(pfive);
        reportContentService.save(psix);
        reportContentService.save(pseven);
        reportContentService.save(peight);
        reportContentService.save(pnine);
        reportContentService.save(cone);
        reportContentService.save(ctwo);
        reportContentService.save(cthree);
        reportContentService.save(cfour);
        reportContentService.save(cfive);
        reportContentService.save(csix);
        reportContentService.save(cseven);
        reportContentService.save(ceight);
        reportContentService.save(cnine);
        reportContentService.save(cten);
        reportContentService.save(celeven);
        reportContentService.save(ctwelve);
        reportContentService.save(cthirteen);
        reportContentService.save(cfoueteen);
        reportContentService.save(cfiveteen);
        reportContentService.save(csixteen);
        reportContentService.save(cseventeen);
        return ResultVoUtil.SAVE_SUCCESS;
    }

    /**
     * 更新个案状态
     * @param id
     * @return
     */
    @RequestMapping("/updateState")
    @ResponseBody
    public ResultVo setReportState(@RequestParam(value = "id") String id){
        reportService.updateState(id);
        Report report = reportService.getOne(id);
        reserveInfoService.updataState(3,report.getReseId());
        return ResultVoUtil.SAVE_SUCCESS;
    }

    /**
     * 保存访谈记录
     * @param interview
     * @param reserId
     * @return
     * @throws Exception
     */
    @RequestMapping("/saveInterview")
    @ResponseBody
    public ResultVo saveInterview(Interview interview,@RequestParam("reserId") String reserId) throws Exception {
//        System.out.println(interview);
        interview.setId(RandomUtil.getId().toString());
        InterviewContent one = new InterviewContent(RandomUtil.getId().toString(),interview.getId(),"基本信息",null);
        InterviewContent two = new InterviewContent(RandomUtil.getId().toString(),interview.getId(),"咨询过程",null);
        InterviewContent three = new InterviewContent(RandomUtil.getId().toString(),interview.getId(),"个案自述",null);
        InterviewContent four = new InterviewContent(RandomUtil.getId().toString(),interview.getId(),"咨询评估",null);
        interviewCoetentService.save(one);
        interviewCoetentService.save(two);
        interviewCoetentService.save(three);
        interviewCoetentService.save(four);
        interviewService.save(interview);
        if (reserId!=null && !reserId.isEmpty()){
            reserveInfoService.updataIshave(reserId);
            reserveInfoService.updataState(2,reserId);
        }
        Report one1 = reportService.getOne(interview.getReportId());
        one1.setNumber(one1.getNumber()+1);
        reportService.save(one1);
        return ResultVoUtil.SAVE_SUCCESS;
    }

    /**
     * 保存访谈内容
     * @param
     * @return
     */
    @RequestMapping("/saveInterviewContent")
    @ResponseBody
    public ResultVo saveInterviewContent(@RequestParam("id") String id,
                                         @RequestParam("content") String content){
        InterviewContent interviewContent = interviewCoetentService.getOne(id);
        interviewContent.setContent(content);
        interviewCoetentService.save(interviewContent);
        return ResultVoUtil.success("保存成功");
    }

    /**
     * 保存回访信息
     * @param id
     * @param replyType
     * @param replyTime
     * @param replyState
     * @param note
     * @return
     */
    @ResponseBody
    @RequestMapping("/saveReply")
    public ResultVo saveReply(@RequestParam("id") String id,
                              @RequestParam("replyType") String replyType,
                              @RequestParam("replyTime") Date replyTime,
                              @RequestParam("replyState") String replyState,
                              @RequestParam("note") String note){
        Report one = reportService.getOne(id);
        one.setReplyType(replyType);
        one.setReplyState(replyState);
        one.setNote(note);
        one.setReplyTime(replyTime);
        reportService.save(one);
        return ResultVoUtil.SAVE_SUCCESS;
    }

    /**
     * 跳转到个人报告页面
     * @param id
     * @param model
     * @return
     */
    @RequestMapping("/caseReport/{id}")
    public String caseReport(@PathVariable("id") String id, Model model){
        Report report = reportService.getOne(id);
        Map<String,ReportContent> map = new HashMap<>();
        List<ReportContent> reportContentList = reportContentService.findByReportId(id);
        reportContentList.forEach(x->{
            if (x.getTinyTitle()!=null){
                map.put(x.getTinyTitle(),x);
            }
        });
        model.addAttribute("reportContents",map);
        model.addAttribute("report",report);
        return "/system/case/caseReport";
    }

    /**
     * 保存个人报个内容信息
     * @param id
     * @param content
     * @return
     */
    @RequestMapping("/saveReportContent")
    @ResponseBody
    public ResultVo saveReportContent(@RequestParam("id") String id,@RequestParam("content") String content){
        ReportContent reportContent = reportContentService.getOne(id);
        reportContent.setContent(content);
        reportContentService.save(reportContent);
        return ResultVoUtil.SAVE_SUCCESS;
    }

    @ResponseBody
    @RequestMapping("/updateLevel")
    public ResultVo updateLevel(@RequestParam("id") String id,@RequestParam("rankId") String rankId){
        CaseSet caseSet = caseService.findById(rankId);
        Report report = reportService.getOne(id);
        report.setRankId(rankId);
        report.setRankContent(caseSet.getContent());
        reportService.save(report);
        return ResultVoUtil.SAVE_SUCCESS;
    }

    @ResponseBody
    @RequestMapping("deleteInterview")
    public ResultVo deleteInterview(@RequestParam("id") String id,
                                    @RequestParam("reportId") String reportId){
        interviewService.deleteById(id);
        Report one = reportService.getOne(reportId);
        one.setNumber(one.getNumber()-1);
        reportService.save(one);
        return ResultVoUtil.success("删除成功");
    }
}

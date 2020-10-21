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
import org.apache.xmlbeans.impl.xb.xsdschema.Public;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Controller
@RequestMapping("/system/consultRese")
public class ConsultResrController {

    @Autowired
    private UserService userService;
    @Autowired
    private ReseTimeSoltService reseTimeSoltService;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private ReserveInfoService reserveInfoService;
    @Autowired
    private DeptService deptService;
    @Autowired
    private ScaleAsseReseService scaleAsseReseService;

    /**
     * 跳转到访谈预约记录页面
     * @return
     */
    @RequestMapping("/talkReseRec")
    @RequiresPermissions("system:consultRese:talkReseRec")
    public String talkReseRec(@RequestParam(value = "searchText",defaultValue = "") String searchText,
                              @RequestParam(value = "state",defaultValue = "0") int state,
                              @RequestParam(value = "page",defaultValue = "1") Integer page,
                              @RequestParam(value = "size",defaultValue = "10") Integer size,
                              Model model,  HttpServletRequest request){
//        System.out.println(searchText);
//        System.out.println(state);
        //获取当前登录咨询师信息
        User user = ShiroUtil.getSubject();
        Set<Role> roles = user.getRoles();
        boolean isAdmin = false;
        List<ReserveInfo> reserveInfos=null;
        for (Role role : roles){
            if ("系统管理员".equals(role.getTitle()) || "数据管理员".equals(role.getTitle()) || "主管".equals(role.getTitle())){
                isAdmin=true;
            }
        }
        PageRequest req = PageRequest.of(page-1, size);
        if (isAdmin){
            Page<ReserveInfo> allBySearch = reserveInfoService.findAllBySearch(searchText, state, req);
            model.addAttribute("list",allBySearch.getContent());
            model.addAttribute("page",allBySearch);
        }else {
            Page<ReserveInfo> byUserIdAndSearch = reserveInfoService.findByUserIdAndSearch(searchText, state, user.getId().toString(), req);
            model.addAttribute("list",byUserIdAndSearch.getContent());
            model.addAttribute("page",byUserIdAndSearch);
        }
        return "/system/consultRese/talkReseRec";
    }

    /**
     * 跳转到访谈咨询详情页面
     * @return
     */
    @RequestMapping("/talkReseDetail/{id}")
//    @RequiresPermissions("system:consultRese:talkReseDetail")
    public String talkReseDetail(@PathVariable("id") String id,Model model){
//        System.out.println(id);
        ReserveInfo one = reserveInfoService.getOne(id);
        model.addAttribute("rese",one);
        return "/system/consultRese/talkReseDetail";
    }

    @RequestMapping("/setTimeSlot")
//    @RequiresPermissions("system:consultRese:setTimeSlot")
    public String setTimeSlot(HttpServletRequest request,Model model){
//        String ip = Iputil.getIpAddr(request);
//        String userInfo = "Heart:user"+ip;
//        User user = (User) redisUtil.get(userInfo);
        User user = ShiroUtil.getSubject();
        List<ReseTimeSolt> list = reseTimeSoltService.findReseByuser(user.getId().toString());
        model.addAttribute("list",list);
        return "/system/consultRese/setTimeSlot";
    }
    /**
     * 跳转到添加时段规则
     */
    @RequestMapping("/addTimeSlot")
//    @RequiresPermissions("system:consultRese:addTimeSlot")
    public String addTimeSlot(Model model){
        model.addAttribute("counselor",userService.findCounselors());
        //封装预约地址
        Map<String, String> site = DictUtil.value("SITE");
        List list = new ArrayList();
        for (int i=1;i<=site.size();i++){
            list.add(site.get(i+""));
        }
        //封装咨询方式
        Map<String, String> consult_way = DictUtil.value("CONSULT_WAY");
        List consultWay = new ArrayList();
        for (int i=1;i<=consult_way.size();i++){
            consultWay.add(consult_way.get(i+""));
        }
        model.addAttribute("consultWay",consultWay);
        model.addAttribute("sites",list);
        return "/system/consultRese/addTimeSlot";
    }


    /**
     * 跳转到量表测评预约
     * @return
     */
    @RequestMapping("/scaleApprRese")
    @RequiresPermissions("system:consultRese:scaleApprRese")
    public String scaleApprRese( @RequestParam(value = "page",defaultValue = "1") Integer page,
                                 @RequestParam(value = "size",defaultValue = "10") Integer size,
                                 Model model, HttpServletRequest request){
        PageRequest req = PageRequest.of(page-1, size);
        Page<ScaleAsseRese> scaleAsseReses = scaleAsseReseService.findScaleAsseReses(req);
        model.addAttribute("list",scaleAsseReses.getContent());
        model.addAttribute("page",scaleAsseReses);
        return "/system/consultRese/scaleApprRese";
    }

    /**
     * 跳转到添加量表预约页面
     * @return
     */
    @RequestMapping("/addScaleRese")
//    @RequiresPermissions("system:consultRese:addScaleRese")
    public String addScaleRese(Model model){
        //分封装预约人列表
        List<Map<String, String>> users = userService.findUsers();
        model.addAttribute("users",users);
        //封装部门列表
        List<Dept> deptAll = deptService.findDeptAll();
        model.addAttribute("depts",deptAll);
        //封装咨询师列表
        List<Map<String, String>> counselors = userService.findCounselors();
        model.addAttribute("counselor",counselors);
        //封装预约地址
        Map<String, String> site = DictUtil.value("SITE");
        List list = new ArrayList();
        for (int i=1;i<=site.size();i++){
            list.add(site.get(i+""));
        }
        model.addAttribute("sites",list);
        return "/system/consultRese/addScaleRese";
    }

    /**
     * 保存预约时段信息
     * @param request
     * @param reseTimeSolt
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("/saveTimeSolt")
    public ResultVo save(HttpServletRequest request, ReseTimeSolt reseTimeSolt) throws Exception {
//        String ip = Iputil.getIpAddr(request);
//        String userInfo = "Heart:user"+ip;
//        User user = (User) redisUtil.get(userInfo);
        User user = ShiroUtil.getSubject();
        reseTimeSolt.setId(RandomUtil.getId()+"");
        reseTimeSolt.setCounselorId(user.getId().toString());
        reseTimeSolt.setCounselorName(user.getNickname());
        reseTimeSoltService.save(reseTimeSolt);
        return ResultVoUtil.SAVE_SUCCESS;
    }

    /**
     * 保存量表测评预约
     * @param scaleAsseRese
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("/saveScaleAsseRese")
    public ResultVo saveScaleAsseRese(ScaleAsseRese scaleAsseRese ,HttpServletRequest request) throws  Exception{
//        System.out.println(scaleAsseRese.toString());
//        String ip = Iputil.getIpAddr(request);
//        String userInfo = "Heart:user"+ip;
//        User user = (User) redisUtil.get(userInfo);
        User user = ShiroUtil.getSubject();
        scaleAsseRese.setCreateUser(user.getNickname());
        scaleAsseRese.setId(RandomUtil.getId()+"");
        scaleAsseRese.setCounselorName(userService.getById(Long.parseLong(scaleAsseRese.getCounId())).getNickname());
        scaleAsseReseService.save(scaleAsseRese);
        return ResultVoUtil.SAVE_SUCCESS;
    }
    @RequestMapping("/scaleReseDetail/{id}")
    public String scaleReseDetail(@PathVariable("id") String id,Model model){
        ScaleAsseRese one = scaleAsseReseService.getOne(id);
        model.addAttribute("item",one);
        return "/system/consultRese/scaleReseDetail";
    }
}

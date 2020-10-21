package com.linln.admin.system.controller;


import com.linln.admin.system.validator.AssessmentPlanValid;
import com.linln.admin.system.validator.ScaleTableValid;
import com.linln.common.utils.Iputil;
import com.linln.common.utils.RandomUtil;
import com.linln.common.utils.RedisUtil;
import com.linln.common.utils.ResultVoUtil;
import com.linln.common.vo.ResultVo;
import com.linln.component.actionLog.action.UserAction;
import com.linln.component.actionLog.annotation.ActionLog;
import com.linln.component.actionLog.annotation.EntityParam;
import com.linln.component.shiro.ShiroUtil;
import com.linln.modules.system.domain.*;
import com.linln.modules.system.service.*;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.persistence.Id;
import javax.servlet.http.HttpServletRequest;
import java.sql.Array;
import java.sql.Date;
import java.util.*;


/**
 * @author 小懒虫
 * @date 2018/8/14
 */
@Controller
@RequestMapping("/system/assessmentPlan")
@RequiresPermissions("system:assessmentPlan:index")
public class assmessmentPlanController {


    @Autowired
    private AssessmentPlanService assessmentPlanService;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private ScaleService scaleService;

    @Autowired
    private AsseScaleService asseScaleService;

    @Autowired
    private UserService userService;

    @Autowired
    private DeptService deptService;

    @Autowired
    private AppraisalService appraisalService;

    /**
     * 列表页面
     */
    @GetMapping("/index")
    public String index(@RequestParam(value = "searchText",defaultValue = "") String searchText ,
                        @RequestParam(value = "state",defaultValue = "0") int state,
                        @RequestParam(value = "page",defaultValue = "1") Integer page,
                        @RequestParam(value = "size",defaultValue = "10") Integer size,
                        Model model,  HttpServletRequest request) {
        PageRequest req = PageRequest.of(page-1, size);
        Page<AssessmentPlan> list = assessmentPlanService.fetchAssessmentPlanBySearch(searchText,state,req);
        // 封装数据
        model.addAttribute("list", list.getContent());
        model.addAttribute("page", list);
        return "/system/assessmentPlan/index";
    }

    /**
     * 跳转到添加页面
     */
    @GetMapping("/add")
//    @RequiresPermissions("system:assessmentPlan:add")
    public String toAdd() {
        return "/system/assessmentPlan/add";
    }


    /**
     * 保存添加/修改的数据
     * @param valid
     * @param assessmentPlan
     * @return
     */
    @PostMapping("/save")
//    @RequiresPermissions({"system:assessmentPlan:add", "system:assessmentPlan:edit"})
    @ResponseBody
    @ActionLog(key=UserAction.USER_SAVE, action = UserAction.class)
    public ResultVo save(@Validated AssessmentPlanValid valid, @EntityParam AssessmentPlan assessmentPlan,HttpServletRequest request) throws Exception {
//        String ip = Iputil.getIpAddr(request);
//        String userInfo = "user"+ip;
//        User user = (User) redisUtil.get(userInfo);
        User user = ShiroUtil.getSubject();
        //添加
        if (assessmentPlan.getId()==null){
            assessmentPlan.setId(RandomUtil.getId()+"");
            assessmentPlan.setCreateDateTime(new Date(System.currentTimeMillis()));
            assessmentPlan.setState(3);
            assessmentPlan.setCreateUser(user.getNickname());
            assessmentPlanService.save(assessmentPlan);
        }else { //修改
            AssessmentPlan ass = assessmentPlanService.findOneById(assessmentPlan.getId());
            ass.setStarTime(assessmentPlan.getStarTime());
            ass.setEndTime(assessmentPlan.getEndTime());
            ass.setTitle(assessmentPlan.getTitle());
            ass.setModifyDateTime(new Date(System.currentTimeMillis()));
            ass.setModifyUser(user.getNickname());
            int status = ass.getState();
            if (status==1 || status==2){
                Date date = assessmentPlan.getEndTime();
                long time = date.getTime();
                long nowTime = System.currentTimeMillis();
                int days = (int) ((time - nowTime) / (1000 * 60 * 60 * 24));
                if (days>=0){
                    status=1;
                }else {
                    status=2;
                }
            }
            ass.setState(status);
            assessmentPlanService.save(ass);
        }

        return ResultVoUtil.SAVE_SUCCESS;
    }
    /**
     * 跳转到编辑页面
     */
    @GetMapping("/edit/{id}")
//    @RequiresPermissions("system:scaleTable:edit")
    public String toEdit(@PathVariable("id") String id, Model model) {
//        ScaleType scaleType1=scaleTableService.fetchOne(stId);
//        model.addAttribute("scaleTable", scaleType1);
        AssessmentPlan assessmentPlan = assessmentPlanService.findOneById(id);
        model.addAttribute("item",assessmentPlan);
        return "/system/assessmentPlan/edit";
    }
    @ResponseBody
    @PostMapping("/updateState")
//    @RequiresPermissions("system:assessmentPlan:save")
    public ResultVo updateState(@RequestParam(value = "id",defaultValue = "0") String id){
        assessmentPlanService.updateState(id);
        return ResultVoUtil.SAVE_SUCCESS;
    }

    /**
     * 跳转到详情页面
     * @param id
     * @param model
     * @return
     */
    @GetMapping("/detail/{id}")
    public String detail(@PathVariable("id") String id , Model model){
        AssessmentPlan assessmentPlan = assessmentPlanService.findOneById(id);
        model.addAttribute("item",assessmentPlan);
        model.addAttribute("scales",scaleService.findScaleByAsseId(id));
        model.addAttribute("assId",id);
        String str = assessmentPlan.getPerList();
        List list = new ArrayList();
        if (str!=null){
            String [] strs = str.split(",");
            for(int i = 0;i<strs.length;i++){
                Map<String,Object> maps = new HashMap<String,Object>();
                Map<String,String> map = new HashMap<String,String>();
                String [] poprt=strs[i].split(":");
                map.put("type",poprt[0]);
                String [] dept = poprt[1].split("/");
                String [] user = poprt[2].split("/");
                map.put("deptName",dept[0]);
                map.put("deptId",dept[1]);
                map.put("username",user[0]);
                map.put("nickname",user[1]);
                maps.put("map",map);
                list.add(maps);
            }
        }

        model.addAttribute("permission",list);
        return "/system/assessmentPlan/detail";
    }

    /**
     * 删除测评计划下的量表
     * @param assid
     * @param scaleId
     * @param model
     * @return
     */
    @ResponseBody
    @RequestMapping("/deleteScale")
    public ResultVo deleteScale(@RequestParam("assId") String assid ,
                                @RequestParam("scaleId") String scaleId ,
                                Model model){
        String [] str  = scaleId.split(",");
        AssessmentPlan ass = assessmentPlanService.findOneById(assid);
        for(int i=0;i<str.length;i++){
            if (!assessmentPlanService.deleteScale(assid,str[i])){
                return ResultVoUtil.error("失败，请重新操作");
            }
            ass.setScaleNumber(ass.getScaleNumber()-1);
            assessmentPlanService.save(ass);
        }
        return ResultVoUtil.success( "成功");

    }

    /**
     * 跳转到添加量表页面
     * @param assId
     * @param page
     * @param size
     * @param model
     * @param request
     * @return
     */
    @RequestMapping("/detail/addScale")
    public String addScale(@RequestParam(value = "assId",defaultValue = "") String assId,
                           @RequestParam(value = "page",defaultValue = "1") Integer page,
                           @RequestParam(value = "size",defaultValue = "10") Integer size,
                           Model model,  HttpServletRequest request){
        PageRequest req = PageRequest.of(page-1, size);
        Page<List<Map<String,String>>> list = scaleService.findScales(assId,req);
//        List<Scale> sca = scaleService.findScaleByAsseId(assId);
//        List<String> str =new ArrayList<>();
//        sca.forEach(x->str.add(x.getId()));
        // 封装数据
//        System.out.println(list.getContent().toString());
        model.addAttribute("list", list.getContent());
        model.addAttribute("page", list);
        model.addAttribute("assId",assId);
//        model.addAttribute("check",str);
//        System.out.println("assId:"+assId);
        return "/system/assessmentPlan/addScale";
    }

    /**
     * 添加量表到测评计划中
     * @param assId
     * @param str
     * @param request
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("/addScaleToAss")
    public ResultVo addScale(@RequestParam(value = "assId",defaultValue = "") String assId,
                             @RequestParam(value = "str",defaultValue = "") String str,
                             HttpServletRequest request) throws Exception {
//        System.out.println(assId);
//        String ip = Iputil.getIpAddr(request);
//        String userInfo = "user"+ip;
//        User user = (User) redisUtil.get(userInfo);
        User user = ShiroUtil.getSubject();
        String[] list = str.split(",");
        List<String> s = Arrays.asList(list);
        AssessmentPlan assessmentPlan = assessmentPlanService.findOneById(assId);
        for (String x : s) {
            AsseScale asseScale = new AsseScale();
            asseScale.setAsseId(assId);
            asseScale.setScaleId(x);
            long id = RandomUtil.getId();
            asseScale.setId(id+"");
            asseScale.setCreateDateTime(new java.util.Date());
            asseScale.setCreateUser(user.getId()+"");
            asseScaleService.save(asseScale);
            assessmentPlan.setScaleNumber(assessmentPlan.getScaleNumber()+1);
            assessmentPlanService.save(assessmentPlan);
        }
        return ResultVoUtil.SAVE_SUCCESS;
    }

    /**
     * 删除查看权限列表
     * @param assId
     * @param type
     * @param deptName
     * @param nickname
     * @return
     */
    @ResponseBody
    @RequestMapping("/deletePerList")
    public ResultVo deletePerList(@RequestParam("assId") String assId,
                                  @RequestParam("type") String type,
                                  @RequestParam(value = "deptName",defaultValue = "不为空") String deptName,
                                  @RequestParam(value = "nickname",defaultValue = "不为空") String nickname){
        AssessmentPlan assessmentPlan = assessmentPlanService.findOneById(assId);
        String str = assessmentPlan.getPerList();
        String[] split = str.split(",");
        String s="";
        for(int i=0;i<split.length;i++){
            if (split[i].indexOf(deptName)!=-1){
                s = str.replaceAll(split[i]+",", "");
                break;
            }
            if (split[i].indexOf(nickname)!=-1){
                s = str.replaceAll(split[i]+",", "");
                break;
            }
        }
        if ("".equals(s)){
            s=null;
        }
        assessmentPlan.setPerList(s);
        assessmentPlanService.save(assessmentPlan);
        return ResultVoUtil.success("删除成功！");
    }

    /**
     * 跳转到添加查看权限页面
     * @param assId
     * @param model
     * @return
     */
    @RequestMapping("/detail/addPerList")
    public String addPerList(@RequestParam(value = "assId",defaultValue = "") String assId,
                             Model model){
        List<Map<String, String>> users = userService.findUsers();
        List<Dept> deptAll = deptService.findDeptAll();
        AssessmentPlan assessmentPlan = assessmentPlanService.findOneById(assId);
        String perList = assessmentPlan.getPerList();
        if (perList!=null){
            String [] str = perList.split(",");
            Iterator<Map<String, String>> iterator = users.iterator();
            while (iterator.hasNext()){
                if (perList.indexOf(iterator.next().get("nickname"))!=-1){
                    iterator.remove();
                }
            }
            Iterator<Dept> iterator1 = deptAll.iterator();
            while (iterator1.hasNext()){
                if (perList.indexOf(iterator1.next().getTitle())!=-1){
                    iterator1.remove();
                }
            }
        }
        model.addAttribute("assId",assId);
        model.addAttribute("users",users);
        model.addAttribute("depts",deptAll);
        return "/system/assessmentPlan/addPerList";
    }

    @RequestMapping("/detail/savePerList")
    @ResponseBody
    public ResultVo savePerList(@RequestParam(value = "userList",defaultValue = " ") String users,
                                @RequestParam(value = "deptList",defaultValue = " ") String depts,
                                @RequestParam(value = "assId",defaultValue = "") String assId){
        AssessmentPlan assessmentPlan = assessmentPlanService.findOneById(assId);
        String perList="";
        if (assessmentPlan.getPerList()!=null){
            perList = assessmentPlan.getPerList();
        }
        int temp = 0;
        if (!users.equals(" ")){
            String [] str = users.split(",");
            for (int i=0;i<str.length;i++){
                temp = Integer.valueOf(str[i]);
                User user = userService.getById((long) temp);
                String s = "2:"+" "+"/"+" "+":"+user.getUsername()+"/"+user.getNickname()+",";
                perList +=s;
            }
            assessmentPlan.setPerList(perList);
        }
        if (!depts.equals(" ")){
            String [] dept = depts.split(",");
            for (int i=0;i<dept.length;i++){
                temp = Integer.valueOf(dept[i]);
                Dept dept1 = deptService.getById((long)temp);
                String s = "1:"+dept1.getTitle()+"/"+dept1.getId()+":"+" "+"/"+" "+",";
                perList +=s;
            }
            assessmentPlan.setPerList(perList);
        }
        assessmentPlanService.save(assessmentPlan);
        return ResultVoUtil.SAVE_SUCCESS;
    }
    @GetMapping("/collent/{id}")
    public String collent(@PathVariable("id") String id , Model model){
        List<Map<String, String>> byAssId = appraisalService.findByAssId(id);
        System.out.println(byAssId);
        model.addAttribute("list",byAssId);
        return "/system/assessmentPlan/collent";
    }
    /**
     * 列表页面
     */
    @GetMapping("/collent/detail/{userId}/{assesId}")
    public String index(@PathVariable("userId") String userId,
                        @PathVariable("assesId") String assesId,
                        @RequestParam(value = "page",defaultValue = "1") Integer page,
                        @RequestParam(value = "size",defaultValue = "10") Integer size,
                        Model model,  HttpServletRequest request) {
        PageRequest req = PageRequest.of(page-1, size);
        Page<UserPmq> list = appraisalService.findPageByUserIdAndAssesId(userId,assesId,req);
//         封装数据
        model.addAttribute("list", list.getContent());
        model.addAttribute("page", list);
        return "/system/appraisal/index";
    }

    /**
     * 删除计划测评计划
     * @param id
     * @return
     */
    @ResponseBody
    @RequestMapping("/update")
    public ResultVo delete(@RequestParam("id") String id){
        AssessmentPlan assessmentPlan = assessmentPlanService.findOneById(id);
        if (assessmentPlan.getScaleNumber()>=1){
            return ResultVoUtil.error("计划有关联数据存在不能删除！");
        }else {
            assessmentPlan.setState(4);
            assessmentPlanService.save(assessmentPlan);
            return ResultVoUtil.success("删除成功");
        }

    }



    public static void main(String[] args) {
        String str = "1:研发部门/1: / ,1:测试部门/2: / ,2: / :18201201144/大飞,";
        System.out.println(str);
        String [] split = str.split(",");
        for(int i=0;i<split.length;i++){
            if (split[i].indexOf("研发部门")!=-1){
                System.out.println(split[i]);
                String s = str.replaceAll(split[i]+",", "");
                System.out.println(s);
            }
        }
    }

}

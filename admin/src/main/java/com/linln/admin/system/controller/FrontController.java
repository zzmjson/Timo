package com.linln.admin.system.controller;

import com.linln.common.enums.ResultEnum;
import com.linln.common.exception.ResultException;
import com.linln.common.utils.Iputil;
import com.linln.common.utils.RandomUtil;
import com.linln.common.utils.RedisUtil;
import com.linln.common.utils.ResultVoUtil;
import com.linln.common.vo.ResultVo;
import com.linln.component.shiro.ShiroUtil;
import com.linln.component.thymeleaf.utility.DictUtil;
import com.linln.modules.system.domain.*;
import com.linln.modules.system.repository.ReseTimeSoltRepository;
import com.linln.modules.system.service.*;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.sql.Date;
import java.util.*;

@Controller
@RequestMapping("/front")
@RequiresPermissions({"system:role:user","system/front/index"})
public class FrontController {
    @Autowired
    private DeptService deptService;
    @Autowired
    private AssessmentPlanService assessmentPlanService;
    @Autowired
    private ScaleService scaleService;
    @Autowired
    private UserService userService;
    @Autowired
    private TemplateService templateService;
    @Autowired
    private ReseTimeSoltService ReseTimeSoltService;
    @Autowired
    private ReserveInfoService reserveInfoService;
    @Autowired
    private AppraisalService appraisalService;
    @Autowired
    private QuesStatisService quesStatisService;
    @Autowired
    private RoleService roleService;

    @ResponseBody
    @RequestMapping("/getuser")
    public ResultVo getUser(HttpServletRequest request){
//        String ip = Iputil.getIpAddr(request);
//        String userInfo = "Heart:user"+ip;
//        User user = (User) redisUtil.get(userInfo);
        // 判断是否拥有后台页面权限
        User user = ShiroUtil.getSubject();
        Set<Role> roles1 = user.getRoles();
        Set<String> perms = new HashSet<>();
        boolean isFront = false;
        boolean isAdmin = false;
        if (roles1.size()>0){
            for(Role role:roles1){
                Set<Menu> menus = role.getMenus();
                for (Menu menu :menus){
                    perms.add(menu.getPerms());
                }
            }
            for(String str :perms){
                if ("index".equals(str)){
                    isAdmin = true;
                }
                if ("system/front/index".equals(str)){
                    isFront = true;
                }
            }
        }

        List<String> role= roleService.findRoleTitleByUserId(user.getId());
        Map<String,Object> map = new HashMap<>();
        map.put("isAdmin",isAdmin);
        map.put("isFront",isFront);
        map.put("role",role.get(0));
        map.put("user",user);
        return ResultVoUtil.success(map);
    }
    /**
     * 查询前台首页用户数据
     * @param request
     * @return
     */
    @ResponseBody
    @GetMapping("/index")
    public ResultVo index (HttpServletRequest request){
//        String ip = Iputil.getIpAddr(request);
        Map<String,Object> map = new HashMap<>();
//        String userInfo = "Heart:user"+ip;
//        User user = (User) redisUtil.get(userInfo);
        User user = ShiroUtil.getSubject();
        int reseNum = reserveInfoService.findCountByUser(user.getId().toString());
        int apprNum = appraisalService.findCountByUser(user.getId().toString());
        int quesNum  = quesStatisService.findCountByUserId(user.getId().toString());
        map.put("reseNum",reseNum);
        map.put("apprNum",apprNum);
        map.put("querNum",quesNum);
        return ResultVoUtil.success("",map);
    }

    /**
     *前台获取测评计划列表
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping("/findAssessment")
    public ResultVo findAssessment(HttpServletRequest request){
//        String ip = Iputil.getIpAddr(request);
//        String userInfo = "Heart:user"+ip;
//        User user = (User) redisUtil.get(userInfo);
        User user = ShiroUtil.getSubject();
        String nickname =user.getUsername() +"/"+user.getNickname();
        Dept dept = user.getDept();
        String deptName = dept.getTitle()+"/"+dept.getId();
//        System.out.println(nickname);
//        System.out.println(deptName);
        List<AssessmentPlan> list = assessmentPlanService.findAssessmentByDeptOrUser(nickname, deptName);
//        System.out.println(list);
        return ResultVoUtil.success("",list);
    }

    /**
     * 前台根据测评计划获取测评量表列表
     * @param assId
     * @return
     */
    @ResponseBody
    @RequestMapping("/getScales")
    public ResultVo getScalesByAss(@RequestParam("assId") String assId){
        Long userId= ShiroUtil.getSubject().getId();
        List<Map<String, String>> scales = scaleService.findListByAss(assId,userId);
        return ResultVoUtil.success("",scales);
    }

    @ResponseBody
    @GetMapping("/getAsseTemplate")
    public ResultVo getAsseTemplate(){
        Map<String, String> asseTemp = templateService.getAsseTemp();
        return ResultVoUtil.success(asseTemp);
    }

    /**
     * 获取预约咨询的的数据
     * @return
     */
    @ResponseBody
    @RequestMapping("/findReses")
    public ResultVo getReseByDate(){
//        List<ReseTimeSolt> reseByDate = reseTimeSoltRepository.findReseByDate(today);
        Map<String,Object> map = new HashMap<>();
        //封装咨询师列表
        List<Map<String, String>> counselors = userService.findCounselors();
        map.put("counselors",counselors);
        //封装预约地址
        Map<String, String> site = DictUtil.value("SITE");
        List list = new ArrayList();
        for (int i=1;i<=site.size();i++){
            list.add(site.get(i+""));
        }
        map.put("sites",list);
        //封装咨询方式
        Map<String, String> consult_way = DictUtil.value("CONSULT_WAY");
        List consultWay = new ArrayList();
        for (int i=1;i<=consult_way.size();i++){
            consultWay.add(consult_way.get(i+""));
        }
        map.put("ways",consultWay);
        //封装咨询类型
        Map<String, String> consult_type = DictUtil.value("CONSULT_TYPE");
        List cons = new ArrayList();
        for (int i=1;i<=consult_type.size();i++){
            cons.add(consult_type.get(i+""));
        }
        map.put("consultType",cons);
//        DictUtil.keyValue()
        return ResultVoUtil.success(map);
    }

    /**
     * 保存前台请求保存用户预约信息
     * @param reserveInfo
     * @param request
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("/saveRese")
    public ResultVo saveRese(ReserveInfo reserveInfo,HttpServletRequest request)throws Exception{
//        String ip = Iputil.getIpAddr(request);
//        String userInfo = "Heart:user"+ip;
//        User user = (User) redisUtil.get(userInfo);
        User user = ShiroUtil.getSubject();
        reserveInfo.setId(RandomUtil.getId()+"");
        reserveInfo.setCreateUser(user.getUsername());
        reserveInfo.setModifyUser(user.getUsername());
        reserveInfo.setEmotion("");
        long l = Long.parseLong(reserveInfo.getCounselorId());
        reserveInfo.setCounselorName(userService.getById(l).getNickname());
        reserveInfo.setUserId(user.getId().toString());
        reserveInfo.setNikeName(user.getNickname());
        reserveInfoService.save(reserveInfo);
        return ResultVoUtil.SAVE_SUCCESS;
    }

    /**
     * 前台分页查询用户预约信息
     * @param request
     * @param page
     * @param size
     * @return
     */
    @RequestMapping("/getReserves")
    @ResponseBody
    public ResultVo getReserveInfoByUser(HttpServletRequest request,
                                         @RequestParam(value = "page",defaultValue = "1") int page,
                                         @RequestParam(value = "size",defaultValue = "10") int size){
        PageRequest req = PageRequest.of(page-1, size);
//        String ip = Iputil.getIpAddr(request);
//        String userInfo = "Heart:user"+ip;
//        User user = (User) redisUtil.get(userInfo);
        User user = ShiroUtil.getSubject();
        Page<ReserveInfo> listByUserId = reserveInfoService.getListByUserId(user.getId().toString(), req);
        return ResultVoUtil.success(listByUserId);
    }

    @RequestMapping("/getReseDetail")
    @ResponseBody
    public ResultVo getReseDetail(@RequestParam(value = "reseId") String reseId){
        ReserveInfo one = reserveInfoService.getOne(reseId);
        return ResultVoUtil.success(one);
    }

    /**
     * 前台获取用户测评记录列表
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping("/getAsseRec")
    public ResultVo getAsseRec(HttpServletRequest request){
//        String ip = Iputil.getIpAddr(request);
//        String userInfo = "Heart:user"+ip;
//        User user = (User) redisUtil.get(userInfo);
        User user = ShiroUtil.getSubject();
        List<UserPmq> userPmqs = appraisalService.fetchUserPmqByUser(user.getId().toString());
        return ResultVoUtil.success(userPmqs);
    }

    /**
     * 根据用户测评档案id查询答题信息
     * @param rid
     * @return
     */
    @RequestMapping("/getAsseRecord")
    @ResponseBody
    public ResultVo getAsseReport(@RequestParam("rid") String rid){
        UserPmq userPmq=appraisalService.fetchOne(rid);
        List<List<Map<String,Object>>>  getAllSale= appraisalService.getAllSale(userPmq.getScaleId(),"1",userPmq.getId());
        Scale one = scaleService.getOne(userPmq.getScaleId());
        User user = ShiroUtil.getSubject();
        Map<String,Object> hashmap = new HashMap<>();
        hashmap.put("list",getAllSale);
        hashmap.put("info",user);
        hashmap.put("scale",one);
        return ResultVoUtil.success(hashmap);
    }

    /**
     * 根据测评档案id查询测评报告信息
     * @param id
     * @return
     */
    @RequestMapping("/getAsseReport")
    @ResponseBody
    public ResultVo getAssesReport(@RequestParam("id") String id){
//        System.out.println(id);
        UserPmq userPmq = appraisalService.fetchOne(id);
        System.out.println(userPmq);
        User user = userService.getById(Long.valueOf(userPmq.getUserId()));
        Scale one = scaleService.getOne(userPmq.getScaleId());
        Map<String,Object> map = new HashMap<>();
        map.put("user",user);
        map.put("userPmq",userPmq);
        map.put("scale",one);
        return ResultVoUtil.success(map);
    }

    /**
     * 保存前台修改用户基本信息
     * @param user
     * @return
     */
    @RequestMapping(value = "/saveUserBasicInfo",method = RequestMethod.POST)
    @ResponseBody
    public ResultVo saveUserBasicInfo(User user){
        User subject = ShiroUtil.getSubject();
        subject.setRealName(user.getRealName());
        if (user.getBirthDate()!=null){
            subject.setBirthDate(user.getBirthDate());
        }
        subject.setInfo(user.getInfo());
        subject.setSex(user.getSex());
        System.out.println(subject);
        userService.save(subject);
        return ResultVoUtil.SAVE_SUCCESS;
    }
    /**
     * 保存前台修改用户基本信息
     * @param user
     * @return
     */
    @RequestMapping(value = "/saveUserOtherInfo",method = RequestMethod.POST)
    @ResponseBody
    public ResultVo saveUserOtherInfo(User user){
        User subject = ShiroUtil.getSubject();
        subject.setSerialNo(user.getSerialNo());
        subject.setNation(user.getNation());
        subject.setEducation(user.getEducation());
        subject.setAddress(user.getAddress());
        userService.save(subject);
        return ResultVoUtil.SAVE_SUCCESS;
    }

    /**
     * 前台修改密码
     * @param password
     * @param newPassword
     * @return
     */
    @PostMapping("/changPassword")
    @ResponseBody
    public ResultVo editPassword(@RequestParam("password") String password,@RequestParam("newPassword") String newPassword){
        //判断原来密码是否正确
        User user = ShiroUtil.getSubject();
        String oldPwd = ShiroUtil.encrypt(password, user.getSalt());
        if (password.isEmpty() || "".equals(password.trim()) || !oldPwd.equals(user.getPassword())) {
            return ResultVoUtil.error("原密码输入错误!");
        }
        String salt = ShiroUtil.getRandomSalt();
        String encrypt = ShiroUtil.encrypt(newPassword, salt);
        user.setPassword(encrypt);
        user.setSalt(salt);
        // 保存数据
        userService.save(user);
        return ResultVoUtil.success("修改成功");
    }

}

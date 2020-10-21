package com.linln.admin.system.controller;

import com.linln.common.config.properties.ProjectProperties;
import com.linln.common.data.URL;
import com.linln.common.enums.ResultEnum;
import com.linln.common.exception.ResultException;
import com.linln.common.utils.*;
import com.linln.common.vo.ResultVo;
import com.linln.component.actionLog.action.UserAction;
import com.linln.component.actionLog.annotation.ActionLog;
import com.linln.component.shiro.ShiroUtil;
import com.linln.modules.system.domain.Menu;
import com.linln.modules.system.domain.Role;
import com.linln.modules.system.domain.User;
import com.linln.modules.system.service.RoleService;
import com.linln.modules.system.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * @author 小懒虫
 * @date 2018/8/14
 */
@Controller
public class LoginController implements ErrorController {

    @Autowired
    private RoleService roleService;
    @Autowired
    RedisUtil redisUtil;
    @Autowired
    UserService userService;

    /**
     * 跳转到登录页面
     */
    @GetMapping("/login")
    public String toLogin(Model model) {
        ProjectProperties properties = SpringContextUtil.getBean(ProjectProperties.class);
        model.addAttribute("isCaptcha", properties.isCaptchaOpen());
        return "/login";
    }

    /**
     * 实现登录
     */
    @PostMapping("/login")
    @ResponseBody
    @ActionLog(key = UserAction.USER_LOGIN, action = UserAction.class)
    public Object login(String username, String password, String captcha, String rememberMe,HttpServletRequest request) {
        // 判断账号密码是否为空
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
            throw new ResultException(ResultEnum.USER_NAME_PWD_NULL);
        }
        // 判断验证码是否正确
        ProjectProperties properties = SpringContextUtil.getBean(ProjectProperties.class);
        if (properties.isCaptchaOpen()) {
            Session session = SecurityUtils.getSubject().getSession();
            String sessionCaptcha = (String) session.getAttribute("captcha");
            if (StringUtils.isEmpty(captcha) || StringUtils.isEmpty(sessionCaptcha)
                    || !captcha.toUpperCase().equals(sessionCaptcha.toUpperCase())) {
                throw new ResultException(ResultEnum.USER_CAPTCHA_ERROR);
            }
            session.removeAttribute("captcha");
        }
        // 1.获取Subject主体对象
        Subject subject = SecurityUtils.getSubject();
        // 2.封装用户数据
        UsernamePasswordToken token = new UsernamePasswordToken(username, password);
        // 3.执行登录，进入自定义Realm类中
        try {
            // 判断是否自动登录
            if (rememberMe != null) {
                token.setRememberMe(true);
            } else {
                token.setRememberMe(false);
            }
            subject.login(token);

            // 判断是否拥有前台页面权限
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
                    if ("system/front/index".equals(str)){
                        isFront = true;
                    }
                    //判断是否拥有后台页面权限
                    if ("index".equals(str)){
                        isAdmin = true;
                    }
                }
            }


            String ip = Iputil.getIpAddr(request);
            ArrayList list = (ArrayList) roleService.findDeptList(user.getId());
            ip ="Heart:"+ip;
            redisUtil.lSet(ip,list);
            if (isFront){
                return ResultVoUtil.success("登录成功", new URL("/front/index.html"));
            }else if (isAdmin){
                return ResultVoUtil.success("登录成功", new URL("/"));
            }else {
                SecurityUtils.getSubject().logout();
                return ResultVoUtil.error("您没有权限访问网站，请联系管理员！");
            }
/*
            if (roleService.existsUserOk(user.getId())) {
//                redisUtil.set(user.getId().toString(),user);
//                String ip = Iputil.getIpAddr(request);
//                ArrayList list = (ArrayList) roleService.findDeptList(user.getId());
//                String userInfo = "Heart:user"+ip;
//                ip ="Heart:"+ip;
//                user.setDeptmId(user.getDept().getId());
//                user.setRemark(roleService.findRoleTitleByUserId(user.getId()));
//                redisUtil.set(userInfo,user);
//                redisUtil.lSet(ip,list);
                return ResultVoUtil.success("登录成功", new URL("/front/index.html"));
//                return  ResultVoUtil.success("登录成功","redirect:/index");
//                return "/front/index.html";
            } else {
                SecurityUtils.getSubject().logout();
                return ResultVoUtil.success("登录成功", new URL("/front/index.html"));
//                return ResultVoUtil.error("您不是后台管理员！");
            }
            */
        } catch (LockedAccountException e) {
            return ResultVoUtil.error("该账号已被冻结");
        } catch (AuthenticationException e) {
            return ResultVoUtil.error("用户名或密码错误");
        }
    }

    /**
     * 验证码图片
     */
    @GetMapping("/captcha")
    public void captcha(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //设置响应头信息，通知浏览器不要缓存
        response.setHeader("Expires", "-1");
        response.setHeader("Cache-Control", "no-cache");
        response.setHeader("Pragma", "-1");
        response.setContentType("image/jpeg");

        // 获取验证码
        String code = CaptchaUtil.getRandomCode();
        // 将验证码输入到session中，用来验证
        request.getSession().setAttribute("captcha", code);
        // 输出到web页面
        ImageIO.write(CaptchaUtil.genCaptcha(code), "jpg", response.getOutputStream());
    }

    /**
     * 退出登录
     */
    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        String ip = Iputil.getIpAddr(request);
        redisUtil.del("Heart:"+ip);
//        redisUtil.del("Heart:user"+ip);
        SecurityUtils.getSubject().logout();
        return "redirect:/login";
    }

    /**
     * 权限不足页面
     */
    @GetMapping("/noAuth")
    public String noAuth() {
        return "/system/main/noAuth";
    }

    /**
     * 自定义错误页面
     */
    @Override
    public String getErrorPath() {
        return "/error";
    }

    /**
     * 处理错误页面
     */
    @RequestMapping("/error")
    public String handleError(Model model, HttpServletRequest request) {
        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        String errorMsg = "好像出错了呢！";
        if (statusCode == 404) {
            errorMsg = "页面找不到了！好像是去火星了~";
        }
        model.addAttribute("statusCode", statusCode);
        model.addAttribute("msg", errorMsg);
        return "/system/main/error";
    }
}

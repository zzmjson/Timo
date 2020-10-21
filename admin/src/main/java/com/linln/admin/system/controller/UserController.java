package com.linln.admin.system.controller;

import com.alibaba.fastjson.JSON;
import com.linln.admin.system.validator.UserValid;
import com.linln.common.constant.AdminConst;
import com.linln.common.enums.ResultEnum;
import com.linln.common.enums.StatusEnum;
import com.linln.common.exception.ResultException;
import com.linln.common.utils.*;
import com.linln.common.vo.ResultVo;
import com.linln.component.actionLog.action.StatusAction;
import com.linln.component.actionLog.action.UserAction;
import com.linln.component.actionLog.annotation.ActionLog;
import com.linln.component.actionLog.annotation.EntityParam;
import com.linln.component.excel.ExcelUtil;
import com.linln.component.fileUpload.config.properties.UploadProjectProperties;
import com.linln.component.shiro.ShiroUtil;
import com.linln.modules.system.domain.Dept;
import com.linln.modules.system.domain.Role;
import com.linln.modules.system.domain.User;
import com.linln.modules.system.repository.UserRepository;
import com.linln.modules.system.service.DeptService;
import com.linln.modules.system.service.RoleService;
import com.linln.modules.system.service.UserService;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.filechooser.FileSystemView;
import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author 小懒虫
 * @date 2018/8/14
 */
@Controller
@RequestMapping("/system/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    DeptService deptService;

    /**
     * 列表页面
     */
    @GetMapping("/index")
    @RequiresPermissions("system:user:index")
    public String index(@RequestParam(value = "status",defaultValue = "-1") Integer status ,
                        @RequestParam(value = "dept",defaultValue = "0") Long dept ,
                        @RequestParam(value = "username",defaultValue = "") String username,
                        @RequestParam(value = "nickname",defaultValue = "") String nickname,
                        @RequestParam(value = "page",defaultValue = "1") Integer page,
                        @RequestParam(value = "size",defaultValue = "10") Integer size,
                        Model model, User user, HttpServletRequest request) {
        PageRequest req = PageRequest.of(page-1, size);
        String ip = Iputil.getIpAddr(request);
        List arr = redisUtil.lGet("Heart:"+ip,0,-1);
        arr.forEach(x->Integer.valueOf(x.toString()));
        Object[] arrays = arr.toArray();
        // 获取用户列表
        Page<User> list = userService.findUserList(status,dept,username,nickname,arrays,req);
        // 封装数据
        model.addAttribute("list", list.getContent());
        model.addAttribute("page", list);
        model.addAttribute("dept", user.getDept());
        return "/system/user/index";
    }
    /**
     * 跳转到添加页面
     */
    @GetMapping("/add")
    @RequiresPermissions("system:user:add")
    public String toAdd() {
        return "/system/user/add";
    }

    /**
     * 跳转到编辑页面
     */
    @GetMapping("/edit/{id}")
    @RequiresPermissions("system:user:edit")
    public String toEdit(@PathVariable("id") User user, Model model) {
        model.addAttribute("user", user);
        return "/system/user/add";
    }

    /**
     * 保存添加/修改的数据
     * @param valid 验证对象
     * @param user 实体对象
     */
    @PostMapping("/save")
    @RequiresPermissions({"system:user:add", "system:user:edit"})
    @ResponseBody
    @ActionLog(key = UserAction.USER_SAVE, action = UserAction.class)
    public ResultVo save(@Validated UserValid valid, @EntityParam User user) {
//        if (!"admin".equals(user.getUsername())){
//            user.setUsername(user.getPhone());
//        }
//        if (user.getId()!=null & user.getId()==1){
//            return ResultVoUtil.success("管理员的信息禁止修改！");
//        }
//        user.setUsername(user.getPhone());
        if (user.getId()!=null){
            if (user.getId()==1){
                return ResultVoUtil.success("管理员的信息禁止修改！");
            }
        }
        user.setUsername(user.getPhone());
        // 验证数据是否合格
        if (user.getId() == null) {

            // 判断密码是否为空
            if (user.getPassword().isEmpty() || "".equals(user.getPassword().trim())) {
                throw new ResultException(ResultEnum.USER_PWD_NULL);
            }

            // 判断两次密码是否一致
            if (!user.getPassword().equals(valid.getConfirm())) {
                throw new ResultException(ResultEnum.USER_INEQUALITY);
            }

            // 对密码进行加密
            String salt = ShiroUtil.getRandomSalt();
            String encrypt = ShiroUtil.encrypt(user.getPassword(), salt);
            user.setPassword(encrypt);
            user.setSalt(salt);

            //设置默认权限
            HashSet<Role> roles = new HashSet<>();
            Role role = roleService.getById((long) 5);
            roles.add(role);
//            roles.add()
            user.setRoles(roles);
            //设置默认头像
            user.setPicture("/upload/picture/images/logo.png");
        }

        // 判断用户名是否重复
        if (userService.repeatByUsername(user)) {
            throw new ResultException(ResultEnum.USER_EXIST);
        }

        // 复制保留无需修改的数据
        if (user.getId() != null) {
            // 不允许操作超级管理员数据
            if (user.getId().equals(AdminConst.ADMIN_ID) &&
                    !ShiroUtil.getSubject().getId().equals(AdminConst.ADMIN_ID)) {
                throw new ResultException(ResultEnum.NO_ADMIN_AUTH);
            }
            User beUser = userService.getById(user.getId());
            String[] fields = {"password", "salt", "picture", "roles"};
            EntityBeanUtil.copyProperties(beUser, user, fields);
        }

        // 保存数据
        userService.save(user);
        return ResultVoUtil.SAVE_SUCCESS;
    }

    /**
     * 跳转到详细页面
     */
    @GetMapping("/detail/{id}")
    @RequiresPermissions("system:user:detail")
    public String toDetail(@PathVariable("id") User user, Model model) {
        model.addAttribute("user", user);
        return "/system/user/detail";
    }

    /**
     * 跳转到修改密码页面
     */
    @GetMapping("/pwd")
    @RequiresPermissions("system:user:pwd")
    public String toEditPassword(Model model, @RequestParam(value = "ids", required = false) List<Long> ids) {
        model.addAttribute("idList", ids);
        return "/system/user/pwd";
    }

    /**
     * 修改密码
     */
    @PostMapping("/pwd")
    @RequiresPermissions("system:user:pwd")
    @ResponseBody
    @ActionLog(key = UserAction.EDIT_PWD, action = UserAction.class)
    public ResultVo editPassword(String password, String confirm,
                                 @RequestParam(value = "ids", required = false) List<Long> ids,
                                 @RequestParam(value = "ids", required = false) List<User> users) {

        // 判断密码是否为空
        if (password.isEmpty() || "".equals(password.trim())) {
            throw new ResultException(ResultEnum.USER_PWD_NULL);
        }

        // 判断两次密码是否一致
        if (!password.equals(confirm)) {
            throw new ResultException(ResultEnum.USER_INEQUALITY);
        }

        // 不允许操作超级管理员数据
        if (ids.contains(AdminConst.ADMIN_ID) &&
                !ShiroUtil.getSubject().getId().equals(AdminConst.ADMIN_ID)) {
            throw new ResultException(ResultEnum.NO_ADMIN_AUTH);
        }

        // 修改密码，对密码进行加密
        users.forEach(user -> {
            String salt = ShiroUtil.getRandomSalt();
            String encrypt = ShiroUtil.encrypt(password, salt);
            user.setPassword(encrypt);
            user.setSalt(salt);
        });

        // 保存数据
        userService.save(users);
        return ResultVoUtil.success("修改成功");
    }

    /**
     * 跳转到角色分配页面
     */
    @GetMapping("/role")
    @RequiresPermissions("system:user:role")
    public String toRole(@RequestParam(value = "ids") User user, Model model) {
        // 获取指定用户角色列表
        Set<Role> authRoles = user.getRoles();
        // 获取全部角色列表
        Sort sort = new Sort(Sort.Direction.ASC, "createDate");
        List<Role> list = roleService.getListBySortOk(sort);

        model.addAttribute("id", user.getId());
        model.addAttribute("list", list);
        model.addAttribute("authRoles", authRoles);
        return "/system/user/role";
    }

    /**
     * 保存角色分配信息
     */
    @PostMapping("/role")
    @RequiresPermissions("system:user:role")
    @ResponseBody
    @ActionLog(key = UserAction.EDIT_ROLE, action = UserAction.class)
    public ResultVo auth(
            @RequestParam(value = "id", required = true) User user,
            @RequestParam(value = "roleId", required = false) HashSet<Role> roles) {

        // 不允许操作超级管理员数据
        if (user.getId().equals(AdminConst.ADMIN_ID) &&
                !ShiroUtil.getSubject().getId().equals(AdminConst.ADMIN_ID)) {
            throw new ResultException(ResultEnum.NO_ADMIN_AUTH);
        }

        // 更新用户角色
        user.setRoles(roles);

        // 保存数据
        userService.save(user);
        return ResultVoUtil.SAVE_SUCCESS;
    }

    /**
     * 获取用户头像
     */
    @GetMapping("/picture")
    public void picture(String p, HttpServletResponse response) throws IOException {
        String defaultPath = "/images/user-picture.jpg";
        if (!(StringUtils.isEmpty(p) || p.equals(defaultPath))) {
            UploadProjectProperties properties = SpringContextUtil.getBean(UploadProjectProperties.class);
            String fuPath = properties.getFilePath();
            String spPath = properties.getStaticPath().replace("*", "");
            File file = new File(fuPath + p.replace(spPath, ""));
            if (file.exists()) {
                FileCopyUtils.copy(new FileInputStream(file), response.getOutputStream());
                return;
            }
        }
        Resource resource = new ClassPathResource("static" + defaultPath);
        FileCopyUtils.copy(resource.getInputStream(), response.getOutputStream());
    }

    /**
     * 导出用户数据
     */
    @GetMapping("/export")
    @RequiresPermissions("system:user:export")
    @ResponseBody
    public void exportExcel() {
        UserRepository userRepository = SpringContextUtil.getBean(UserRepository.class);
//        User u = new User();
//        List<User> list = new ArrayList<>();
//        list.add(u);
        ExcelUtil.exportExcel(User.class, userRepository.findAll());
//        ExcelUtil.exportExcel(User.class, list);
    }

    /**
     * 设置一条或者多条数据的状态
     */
    @RequestMapping("/status/{param}")
    @RequiresPermissions("system:user:status")
    @ResponseBody
    @ActionLog(name = "用户状态", action = StatusAction.class)
    public ResultVo updateStatus(
            @PathVariable("param") String param,
            @RequestParam(value = "ids", required = false) List<Long> ids) {

        // 不能修改超级管理员状态
        if (ids.contains(AdminConst.ADMIN_ID)) {
            throw new ResultException(ResultEnum.NO_ADMIN_STATUS);
        }

        // 更新状态
        StatusEnum statusEnum = StatusUtil.getStatusEnum(param);
        if (userService.updateStatus(statusEnum, ids)) {
            return ResultVoUtil.success(statusEnum.getMessage() + "成功");
        } else {
            return ResultVoUtil.error(statusEnum.getMessage() + "失败，请重新操作");
        }
    }
    @RequestMapping("/import")
    public String imports(){
        return  "/system/user/import";
    }

    @RequestMapping("/downloadUserTemplate")
    public ResultVo downloadFile(HttpServletResponse response) throws UnsupportedEncodingException {
//        String ph=userService.exportUserToExcel(response,searchText,userType,acceptance,(page-1)*size,size);
        String ph=userService.exportUserTemplateToExcel(response);
        String rootPath =ph;//这里是我在配置文件里面配置的根路径，各位可以更换成自己的路径之后再使用（例如：D：/test）
        String FullPath = rootPath ;//将文件的统一储存路径和文件名拼接得到文件全路径
        File packetFile = new File(FullPath);
        String fileName = packetFile.getName(); //下载的文件名
        File file = new File(FullPath);
        // 如果文件名存在，则进行下载
        if (file.exists()) {
            // 配置文件下载
            response.setHeader("content-type", "application/octet-stream");
            response.setContentType("application/octet-stream");
            // 下载文件能正常显示中文
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
            // 实现文件下载
            byte[] buffer = new byte[1024];
            FileInputStream fis = null;
            BufferedInputStream bis = null;
            try {
                fis = new FileInputStream(file);
                bis = new BufferedInputStream(fis);
                OutputStream os = response.getOutputStream();
                int i = bis.read(buffer);
                while (i != -1) {
                    os.write(buffer, 0, i);
                    i = bis.read(buffer);
                }
                System.out.println("Download the song successfully!");
            } catch (Exception e) {
                System.out.println("Download the song failed!");
            } finally {
                if (bis != null) {
                    try {
                        bis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (fis != null) {
                    try {
                        fis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        } else {//对应文件不存在
            try {
                //设置响应的数据类型是html文本，并且告知浏览器，使用UTF-8 来编码。
                response.setContentType("text/html;charset=UTF-8");

                //String这个类里面， getBytes()方法使用的码表，是UTF-8，  跟tomcat的默认码表没关系。 tomcat iso-8859-1
                String csn = Charset.defaultCharset().name();

                System.out.println("默认的String里面的getBytes方法使用的码表是： " + csn);

                //1. 指定浏览器看这份数据使用的码表
                response.setHeader("Content-Type", "text/html;charset=UTF-8");
                OutputStream os = response.getOutputStream();

                os.write("对应文件不存在".getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
//                return R.error("-1","对应文件不存在");
        }
        return null;
    }

    @ResponseBody
    @RequestMapping("importUser")
    public ResultVo importUuser(@RequestParam("file") MultipartFile file) throws Exception {
        List<String> list = new ArrayList();
        List<String> messages = new ArrayList<>();
        try {
            FileSystemView fsv = FileSystemView.getFileSystemView();
            File com=fsv.getHomeDirectory();
            File myFile = new File(com.getPath()+ UUID.randomUUID()+file.getOriginalFilename());
            file.transferTo(myFile);
            FileInputStream fs = new FileInputStream(myFile);
            // 1、 加载Excel文件对象
            XSSFWorkbook hssfWorkbook = new XSSFWorkbook(fs);
            // 2、 读取一个sheet
            XSSFSheet sheet = hssfWorkbook.getSheetAt(0);//获取第一个sheet对象
            int coloumNum=sheet.getRow(0).getPhysicalNumberOfCells();

            for (Row row : sheet) {

                // 第一行表头跳过
                if (row.getRowNum() == 0) {
                    // 第一行 跳过
                    continue;
                }
                for (int i=0;i<coloumNum;i++){
                    if (row.getCell(i)!=null){
                        row.getCell(i).setCellType(Cell.CELL_TYPE_STRING);
                    }
                }
                //生成一个用户对象
                User user = new User();
                //获取手机号码
                String phone = row.getCell(1).getStringCellValue();
                //验证手机号码是否正确
                if (isMobile(phone)){
                    //设置用户名
                    user.setPhone(phone);
                    //设置手机号
                    user.setUsername(phone);
                }else {
                    //将错误的信息条数记录下来，返回给前台用户
                    list.add(row.getCell(0).getStringCellValue());
                    messages.add("第"+row.getCell(0).getStringCellValue()+"条数据，手机号码不正确,");
                    //结束本次循环
                    continue;
                }
                //判断手机号是否注册
//                User byPhone = userService.findByPhone(phone);
                Boolean aBoolean = userService.repeatByUsername(user);
                if (aBoolean){
                    //将错误的信息条数记录下来，返回给前台用户
                    list.add(row.getCell(0).getStringCellValue());
                    messages.add("第"+row.getCell(0).getStringCellValue()+"条数据，手机号码已注册,");
                    continue;
                }
                //获取密码
                String password = row.getCell(2).getStringCellValue();
                //设置密码并加密
                String salt = ShiroUtil.getRandomSalt();
                String encrypt = ShiroUtil.encrypt(password, salt);
                user.setPassword(encrypt);
                //设置密码盐
                user.setSalt(salt);
                //获取并设置用户昵称
                user.setNickname(row.getCell(3).getStringCellValue());
                user.setRealName(row.getCell(3).getStringCellValue());
                //获取并设置组织部门
                String title = row.getCell(4).getStringCellValue();
                Dept dept = deptService.findByTitle(title);
                if (dept==null){
                    list.add(row.getCell(0).getStringCellValue());
                    messages.add("第"+row.getCell(0).getStringCellValue()+"条数据，部门信息填写错误");
                    continue;
                }else {
                    user.setDept(dept);
                }

//                获取性别
                if (row.getCell(5)!=null){
                    String sex = row.getCell(5).getStringCellValue();
                    //设置性别
                    if ("男".equals(sex)){
                        user.setSex((byte) 1);
                    }else if ("女".equals(sex)){
                        user.setSex((byte) 2);
                    }else {
                        list.add(row.getCell(0).getStringCellValue());
                        messages.add("第"+row.getCell(0).getStringCellValue()+"条数据，性别信息填写错误,");
                        continue;
                    }
                }else {
                    user.setSex((byte) 1);
                }
                //获取唯一编号
                if (row.getCell(6)!=null) {
                    String only = row.getCell(6).getStringCellValue();
                    user.setSerialNo(only);
                }
                //设置用户默认角色
                Role byId = roleService.getById((long) 5);
                Set<Role> roles = new HashSet<>();
                roles.add(byId);
                user.setRoles(roles);
                //获取并设置真实姓名
//                user.setRealName(row.getCell(5).getStringCellValue());
                //获取名设置身份证号码
                /*
                String idCard = row.getCell(6).getStringCellValue();
                System.out.println(idCard);
                System.out.println(idCard.length());
                if (idCard.length()==18){
                    user.setIdNumber(idCard);
                }else {
                    list.add(row.getCell(0).getStringCellValue());
                    messages.add("第"+row.getCell(0).getStringCellValue()+"条数据，身份证填写有误,");
                    continue;
                }

                 */
                //获取并设置电子邮箱
//                String email = row.getCell(5).getStringCellValue();
//                if (isEmail(email)){
//                    user.setEmail(email);
//                }else {
//                    list.add(row.getCell(0).getStringCellValue());
//                    messages.add("第"+row.getCell(0).getStringCellValue()+"条数据，邮箱填写有误,");
//                    continue;
//                }
                //获取并设置编号
//                user.setSerialNo(row.getCell(6).getStringCellValue());
                //获取名设置民族
//                user.setNation(row.getCell(7).getStringCellValue());
                //获取并设置学历
//                user.setEducation(row.getCell(8).getStringCellValue());
                //设置默认头像
                user.setPicture("/upload/picture/images/logo.png");
//                user.setDept(deptService.getById((long) 9));
                userService.save(user);
            }
            //删除文件
            myFile.delete();
        }catch (Exception e){
            throw new Exception(e);
        }
        String str= "";
        String message = "";
        for(String x:list){
            str = str+x+',';
        }
        for(String x:messages){
            message +=x;
        }
        if (list.size()>0) {
            return ResultVoUtil.error("第" + str + "条数据保存失败！请检查数据是否有误！" + message);
        }else {
            return ResultVoUtil.success("上传成功！");
        }
    }
    public static boolean isMobile(String mobiles) {
        Pattern p = Pattern.compile("^1(3|5|7|8|4)\\d{9}");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }
    public static boolean isEmail(String mobiles) {
        Pattern p = Pattern.compile("\\w*+[@]\\w+[.]\\w*");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }

}

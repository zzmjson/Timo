package com.linln.modules.system.service.impl;

import com.linln.common.data.PageSort;
import com.linln.common.enums.StatusEnum;
import com.linln.common.utils.ExcelData;
import com.linln.common.utils.ExcelUtils;
import com.linln.common.utils.RedisUtil;
import com.linln.modules.system.domain.Dept;
import com.linln.modules.system.domain.User;
import com.linln.modules.system.repository.UserRepository;
import com.linln.modules.system.service.DeptService;
import com.linln.modules.system.service.UserService;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.*;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author 小懒虫
 * @date 2018/8/14
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DeptService deptService;
    @Autowired
    private RedisUtil redisUtil;

    /**
     * 根据用户名查询用户数据
     *
     * @param username 用户名
     * @return 用户数据
     */
    @Override
    public User getByName(String username) {
        return userRepository.findByUsername(username);
    }

    /**
     * 用户名是否存在
     *
     * @param user 用户对象
     * @return 用户数据
     */
    @Override
    public Boolean repeatByUsername(User user) {
        Long id = user.getId() != null ? user.getId() : Long.MIN_VALUE;
        return userRepository.findByUsernameAndIdNot(user.getUsername(), id) != null;
    }

    /**
     * 根据用户ID获取用户信息
     *
     * @param id 用户ID
     */
    @Override
    public User getById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public User findByPhone(String phone) {
//        return userRepository.findByPhone(phone);
        return null;
    }

    /**
     * 获取分页列表数据
     *
     * @param user 实体对象
     * @return 返回分页数据
     */
    @Override
    @Transactional
    public Page<User> getPageList(User user) {
        // 创建分页对象
        PageRequest page = PageSort.pageRequest(Sort.Direction.ASC);

        // 使用Specification复杂查询
        return userRepository.findAll(new Specification<User>() {

            @Override
            public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> preList = new ArrayList<>();
                if (user.getId() != null) {
                    preList.add(cb.equal(root.get("id").as(Long.class), user.getId()));
                }
                if (user.getUsername() != null) {
                    preList.add(cb.equal(root.get("username").as(String.class), user.getUsername()));
                }
                if (user.getNickname() != null) {
                    preList.add(cb.like(root.get("nickname").as(String.class), "%" + user.getNickname() + "%"));
                }
                if (user.getDept() != null) {
                    // 联级查询部门

                    Dept dept = user.getDept();
                    List<Long> deptIn = new ArrayList<>();
                    deptIn.add(dept.getId());
                    List<Dept> deptList = deptService.getListByPidLikeOk(dept.getId());
                    deptList.forEach(item -> deptIn.add(item.getId()));

                    Join<User, Dept> join = root.join("dept", JoinType.INNER);
                    CriteriaBuilder.In<Long> in = cb.in(join.get("id").as(Long.class));
                    deptIn.forEach(in::value);
                    preList.add(in);
                }

                // 数据状态
                if (user.getStatus() != null) {
                    preList.add(cb.equal(root.get("status").as(Byte.class), user.getStatus()));
                }

                Predicate[] pres = new Predicate[preList.size()];
//                Predicate restriction = query.where(preList.toArray(pres)).getRestriction();
                return query.where(preList.toArray(pres)).getRestriction();
            }

        }, page);
    }

    /**
     * 保存用户
     *
     * @param user 用户实体类
     */
    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    /**
     * 保存用户列表
     *
     * @param userList 用户实体类
     */
    @Override
    @Transactional
    public List<User> save(List<User> userList) {
        return userRepository.saveAll(userList);
    }

    /**
     * 状态(启用，冻结，删除)/批量状态处理
     */
    @Override
    @Transactional
    public Boolean updateStatus(StatusEnum statusEnum, List<Long> ids) {
        // 联级删除与角色之间的关联
        if (statusEnum == StatusEnum.DELETE) {
            return userRepository.deleteByIdIn(ids) > 0;
        }

        return userRepository.updateStatus(statusEnum.getCode(), ids) > 0;
    }

    @Override
    public Page<User> findUserList(int status, long dept_id, String username, String nickname, Object[] arr, Pageable req) {
        return userRepository.findUserList(status, dept_id, username, nickname,arr,req);
    }

    /**
     * 查询用户列表
     * @return
     */
    @Override
    public List<Map<String, String>> findUsers() {
        return userRepository.findUsers();
    }

    @Override
    public void setUserToRedis(String ip, List depts, String userInfo, User user) {
        redisUtil.set(userInfo,user,1800);
        redisUtil.lSet(ip,depts,1800);
    }

    @Override
    public List<Map<String, String>> findCounselors() {
        return userRepository.findCounselors();
    }


    /**
     * 筛选用户数据
     * @param list
     * @param ip
     * @return
     */
    private List<User> filterDate(List<User> list, String ip) {
        List list1 = redisUtil.lGet("Heart:"+ip, 0, -1);
        List<User> result = new ArrayList<User>();
        int x = 0;
        int y = 0;
        for (int i = 0; i < list.size(); i++) {
            for (int j = 0; j < list1.size(); j++) {
                if (list.get(i).getDept().getId() == (long) Integer.valueOf(list1.get(j).toString())) {
                    result.add(list.get(i));
                }
                y++;
            }
            x++;
        }
        return result;
    }

    @Override
    public String exportUserTemplateToExcel(HttpServletResponse response) {
        int rowIndex = 0;
        ExcelData data = new ExcelData();
        data.setName("用户数据");
        List<String> titles = new ArrayList();
        titles.add("序号");
        titles.add("手机号");
        titles.add("密码");
        titles.add("用户名");
        titles.add("组织部门");
        titles.add("性别(非必填)");
        titles.add("工号(非必填)");
//        titles.add("真实姓名");
//        titles.add("身份证号码");
//        titles.add("电子邮箱");
//        titles.add("民族");
//        titles.add("学历");

        data.setTitles(titles);
        List<List<Object>> rows = new ArrayList();
        data.setRows(rows);
        String path= ExcelUtils.FILE_PATHD + "/用户导入模版"+new Date().getTime() +".xls";
        try{
            rowIndex = ExcelUtils.generateExcel(data, path);
//                ExcelUtils.exportExcel(response,"用户信息",data);
        }catch (Exception e){
            e.printStackTrace();
        }
//        System.out.println(Integer.valueOf(rowIndex));
//        System.out.println(path);
//        System.out.println(data.getName());
        return path;
    }

}
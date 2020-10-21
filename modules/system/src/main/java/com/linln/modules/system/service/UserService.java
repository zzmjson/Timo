package com.linln.modules.system.service;

import com.linln.common.enums.StatusEnum;
import com.linln.modules.system.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * @author 小懒虫
 * @date 2018/8/14
 */
public interface UserService {

    /**
     * 根据手机号查询用户
     * @param phone 用户手机号
     * @return 用户数据
     */
    public User findByPhone(String phone);

    /**
     * 获取分页列表数据
     * @param user 实体对象
     * @return 返回分页数据
     */
    Page<User> getPageList(User user);

    /**
     * 保存用户
     * @param user 用户实体类
     * @return 用户信息
     */
    User save(User user);

    /**
     * 保存用户列表
     * @param userList 用户实体类
     * @return 用户列表
     */
    List<User> save(List<User> userList);

    /**
     * 根据用户名查询用户数据
     * @param username 用户名
     * @return 用户数据
     */
    User getByName(String username);

    /**
     * 用户名是否重复
     * @param user 用户对象
     * @return 用户数据
     */
    Boolean repeatByUsername(User user);

    /**
     * 根据用户ID查询用户数据
     * @param id 用户ID
     * @return 用户信息
     */
    User getById(Long id);

    /**
     * 状态(启用，冻结，删除)/批量状态处理
     * @param statusEnum 数据状态
     * @param idList 数据ID列表
     * @return 操作结果
     */
    @Transactional
    Boolean updateStatus(StatusEnum statusEnum, List<Long> idList);

    /**
     * 根据多条件查询用户列表
     * @param status
     * @param dept_id
     * @param username
     * @param nickname
     * @return
     */
    public Page<User> findUserList(int status, long dept_id, String username, String nickname, Object[] arr , Pageable req);

    /**
     * 查询用户列表
     * @return
     */
    public List<Map<String,String>> findUsers();

    /**
     * 存储用户信息跟用户到redis中
     * @param ip
     * @param depts
     * @param userInfo
     * @param user
     */
    public void setUserToRedis(String ip,List depts,String userInfo,User user);

    /**
     * 查询咨询师列表
     * @return
     */
    public List<Map<String,String>> findCounselors();

    //导出用户模版到Excel
    String exportUserTemplateToExcel(HttpServletResponse response);

}

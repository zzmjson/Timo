package com.linln.modules.system.repository;

import com.linln.modules.system.domain.Dept;
import com.linln.modules.system.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;

/**
 * @author 小懒虫
 * @date 2018/8/14
 */
public interface UserRepository extends BaseRepository<User, Long>, JpaSpecificationExecutor<User> {
    /**
     * 根据手机号查询用户
     * @param phone 用户手机号
     * @return 用户数据
     */
//    public User findByPhone(String phone);

    /**
     * 根据用户名查询用户数据
     * @param username 用户名
     * @return 用户数据
     */
    public User findByUsername(String username);

    /**
     * 根据用户名查询用户数据,且排查指定ID的用户
     * @param username 用户名
     * @param id 排除的用户ID
     * @return 用户数据
     */
    public User findByUsernameAndIdNot(String username, Long id);

    /**
     * 查找多个相应部门的用户列表
     * @param dept 部门对象
     * @return 用户列表
     */
    public List<User> findByDept(Dept dept);

    /**
     * 删除多条数据
     * @param ids ID列表
     * @return 影响行数
     */
    public Integer deleteByIdIn(List<Long> ids);

    /**
     * 多条件查询用户列表
     * @param status
     * @param dept_id
     * @param username
     * @param nickname
     * @return
     */
    @Query(value = "SELECT * FROM sys_user WHERE IF(?1='-1',`status`=1,`status`=?1) " +
            "AND IF(?2='0',1=1,dept_id=?2) " +
            "AND IF(?3='',1=1,username LIKE %?3%) " +
            "AND IF(?4='',1=1,nickname LIKE %?4%)" +
            "AND dept_id in (?5)",nativeQuery = true)
    public Page<User> findUserList(int status, long dept_id, String username, String nickname, Object[] arr, Pageable req);

    @Query(value = "SELECT u.id,u.nickname FROM sys_user AS u WHERE STATUS=1",nativeQuery = true)
    public List<Map<String,String>> findUsers();

    @Query(value = "SELECT su.id,su.nickname FROM sys_user su JOIN sys_user_role sur ON su.id=sur.user_Id JOIN sys_role sr ON sr.id=sur.role_id WHERE sr.id=4 AND su.status=1",nativeQuery = true)
    public List<Map<String,String>> findCounselors();
}

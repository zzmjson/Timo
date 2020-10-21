package com.linln.modules.system.repository;

import com.linln.modules.system.domain.ReseTimeSolt;
import com.linln.modules.system.domain.ReserveInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author 小懒虫
 * @date 2018/8/14extends JpaRepository<Scale, Integer>
 */
public interface ReserveInfoRepository extends JpaRepository<ReserveInfo,String> {

    /**
     * 分页查询用户预约列表
     * @param searchText
     * @param state
     * @param req
     * @return
     */
    @Query(value = "SELECT * FROM sys_reserve_info WHERE if( ''=?1,1=1,nike_name LIKE %?1%) and if(?2=0,1=1,state=?2) order by res_date desc",
            countQuery ="SELECT * FROM sys_reserve_info WHERE if(?1='',1=1,nike_name LIKE %?1%) and if(?2=0,1=1,state=?2) order by res_date desc",nativeQuery = true)
    public Page<ReserveInfo> findAllBySearch(String searchText, int state, Pageable req);

    /**
     * 多条件查询咨询师名下的预约记录
     * @param searchText
     * @param state
     * @param id
     * @param req
     * @return
     */
    @Query(value = "SELECT * FROM sys_reserve_info WHERE if( ''=?1,1=1,nike_name LIKE %?1%) and if(?2=0,1=1,state=?2) and counselor_id=?3 order by res_date desc",
            countQuery ="SELECT * FROM sys_reserve_info WHERE if(?1='',1=1,nike_name LIKE %?1%) and if(?2=0,1=1,state=?2) and counselor_id=?3 order by res_date desc",nativeQuery = true)
    public Page<ReserveInfo> findByUserIdAndSearch(String searchText, int state,String id,Pageable req);

    /**
     * 根据用户id分页查询用户预约列表
     * @param userId
     * @param req
     * @return
     */
    @Query(value = "SELECT * FROM sys_reserve_info WHERE user_id=?1 order by res_date desc",nativeQuery = true)
    public Page<ReserveInfo> getListByUserId(String userId,Pageable req);

    /**
     * 根据用户id查询用户预约记录
     * @param userId
     * @return
     */
    @Query(value = "SELECT COUNT(id) FROM sys_reserve_info WHERE user_id=?1",nativeQuery = true)
    int findCountByUser(String userId);


    /**
     * 根据咨询师id查询用户预约列表
     * @param id
     * @return
     */
    @Query(value = "SELECT * FROM sys_reserve_info WHERE counselor_id=?1 AND state=1 order by res_date desc",nativeQuery = true)
    List<ReserveInfo> findByCounselorId(String id);

    /**
     * 查询所有预约未确认信息
     * @return
     */
    @Query(value = "SELECT * FROM sys_reserve_info WHERE  state=1 order by res_date desc",nativeQuery = true)
    List<ReserveInfo> findReserAll();

    @Modifying
    @Transactional
    @Query(value = "update sys_reserve_info set state=?1 where id=?2",nativeQuery = true)
    void updataState(int state,String id);

    @Query(value = "SELECT * FROM sys_reserve_info WHERE user_id=?1 AND counselor_id=?2 AND ishave=0",nativeQuery = true)
    List<ReserveInfo> findUserIdAndCounId(String userId,String counselorId);

    @Modifying
    @Transactional
    @Query(value = "update sys_reserve_info set ishave=1 where id=?1",nativeQuery = true)
    void updataIshave(String id);
}

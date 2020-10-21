package com.linln.modules.system.service;

import com.linln.modules.system.domain.ReseTimeSolt;
import com.linln.modules.system.domain.ReserveInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * @author 小懒虫
 * @date 2018/8/14
 */
public interface ReserveInfoService {

    /**
     * 保存/修改用户预约信息
     * @param reserveInfo
     * @return
     */
    ReserveInfo save(ReserveInfo reserveInfo);

    /**
     * 根据id查询用户预约记录详情
     * @param id
     * @return
     */
    ReserveInfo getOne(String id);

    /**
     * 多条件分页查询用户预约列表
     * @param searchText
     * @param state
     * @param req
     * @return
     */
    public Page<ReserveInfo> findAllBySearch(String searchText, int state, Pageable req);

    /**
     * 根据用户id查询用户预约列表
     * @param userId
     * @param req
     * @return
     */
    public Page<ReserveInfo> getListByUserId(String userId,Pageable req);

    /**
     * 根据用户id查询预约记录数
     * @param userId
     * @return
     */
    int findCountByUser(String userId);

    /**
     * 根据咨询师id查询器名下预约列表
     * @param id
     * @return
     */
    List<ReserveInfo> findreserByCounselorId(String id);

    /**
     * 更新预约状态
     * @param state
     * @param id
     */
    void updataState(int state,String id);

    /**
     * 根据咨询师id以及来访者id查询可添加访谈记录的预约信息
     * @param userId
     * @param counselorId
     * @return
     */
    List<ReserveInfo> findUserIdAndCounId(String userId,String counselorId);

    /**
     * 修改添加访谈列表状态
     * @param id
     */
    void updataIshave(String id);

    /**
     * 查询所有预约信息
     * @return
     */
    List<ReserveInfo> findReserAll();

    /**
     * 多条件查询咨询师名下的预约记录
     * @param searchText
     * @param state
     * @param id
     * @param req
     * @return
     */
    public Page<ReserveInfo> findByUserIdAndSearch(String searchText, int state,String id,Pageable req);
}

package lyzzcw.work.speedkill.order.infrastructure.mapper;


import lyzzcw.work.speedkill.order.domain.entity.Order;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface OrderMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Order record);

    int insertSelective(Order record);

    Order selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Order record);

    int updateByPrimaryKey(Order record);

    /**
     * 保存订单
     */
    int saveOrder(Order order);

    /**
     * 根据用户id获取订单列表
     */
    List<Order> getOrderByUserId(@Param("userId") Long userId);

    /**
     * 根据活动id获取订单列表
     */
    List<Order> getOrderByActivityId(@Param("activityId") Long activityId);
}
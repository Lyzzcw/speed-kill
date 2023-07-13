package lyzzcw.work.speedkill.goods.domain.repository;


import lyzzcw.work.speedkill.goods.domain.entity.Goods;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author lzy
 * @version 1.0
 * Date: 2023/7/8 9:12
 * Description: No Description
 */
public interface GoodsRepository {
    /**
     * 保存商品信息
     */
    int saveGoods(Goods Goods);

    /**
     * 根据id获取商品详细信息
     */
    Goods getGoodsId(@Param("id") Long id);

    /**
     * 根据活动id获取商品列表
     */
    List<Goods> getGoodsByActivityId(@Param("activityId") Long activityId);

    /**
     * 修改商品状态
     */
    int updateStatus(@Param("status") Integer status, @Param("id") Long id);

    /**
     * 扣减库存
     */
    int updateAvailableStock(@Param("count") Integer count, @Param("id") Long id);

    /**
     * db尝试扣减库存
     */
    boolean updateDbAvailableStock(@Param("count") Integer count, @Param("id") Long id);

    /**
     * 获取当前可用库存
     */
    Integer getAvailableStockById(@Param("id") Long id);

}

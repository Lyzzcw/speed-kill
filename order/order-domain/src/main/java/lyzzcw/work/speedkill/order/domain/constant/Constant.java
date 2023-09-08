package lyzzcw.work.speedkill.order.domain.constant;

/**
 * @author lzy
 * @version 1.0
 * Date: 2023/7/9 0:06
 * Description: No Description
 */
public interface Constant {

    //活动列表 -> cache key prefix
    String ACTIVITY_CACHE_KEY_PREFIX = "ACTIVITY_CACHE_KEY_";

    //商品列表 -> cache key prefix
    String GOODS_CACHE_KEY_PREFIX = "GOODS_CACHE_KEY_";

    //商品详情 -> cache key prefix
    String GOODS_DETAIL_CACHE_KEY_PREFIX = "GOODS_DETAIL_CACHE_KEY_";

    //商品库存 -> cache key prefix
    String GOODS_STOCK_CACHE_KEY_PREFIX = "GOODS_STOCK_CACHE_KEY_";

    //本地订单事务 -> cache key prefix
    String LOCAL_TRANSACTION_ORDER_KEY_PREFIX = "LOCAL_TRANSACTION_ORDER_CACHE_KEY_";

}

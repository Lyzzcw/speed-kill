package lyzzcw.work.speedkill.stock.domain.constant;

/**
 * @author lzy
 * @version 1.0
 * Date: 2023/7/9 0:06
 * Description: No Description
 */
public interface Constant {
    /**
     * 商品库存的Key
     */
    String GOODS_ITEM_STOCK_KEY_PREFIX = "item:stock:";

    /**
     * 商品库存编排
     */
    String GOODS_STOCK_BUCKETS_SUSPEND_KEY = "goods:buckets:suspend:";

    /**
     * 库存校对
     */
    String GOODS_STOCK_BUCKETS_ALIGN_KEY = "goods:buckets:align:";

    /**
     * 分桶库存
     */
    String GOODS_BUCKET_AVAILABLE_STOCKS_KEY = "goods:buckets:available:";

    /**
     * 分桶编排
     */
    String GOODS_BUCKET_ARRANGEMENT_KEY = "goods:buckets:arrangement:";

    /**
     * 商品库存分桶数量
     */
    String GOODS_BUCKETS_QUANTITY_KEY = "goods:buckets:quantity:";

    /**
     * 获取Key
     */
    static String getKey(String prefix, String key){
        return prefix.concat(key);
    }

}

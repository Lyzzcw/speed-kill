package lyzzcw.work.speedkill.order.application.place.order.impl;

import com.alibaba.fastjson.JSONObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lyzzcw.work.component.common.http.exception.BaseException;
import lyzzcw.work.component.redis.cache.redis.RedisCache;
import lyzzcw.work.component.redis.lock.DistributedLock;
import lyzzcw.work.component.redis.lock.factory.DistributedLockFactory;
import lyzzcw.work.speedkill.dubbo.interfaces.goods.GoodsDubboService;
import lyzzcw.work.speedkill.goods.domain.entity.Goods;
import lyzzcw.work.speedkill.order.application.place.order.PlaceOrderService;
import lyzzcw.work.speedkill.order.domain.constant.Constant;
import lyzzcw.work.speedkill.order.domain.constant.HttpCode;
import lyzzcw.work.speedkill.order.domain.dto.OrderDTO;
import lyzzcw.work.speedkill.order.domain.entity.Order;
import lyzzcw.work.speedkill.order.domain.repository.OrderRepository;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @author lzy
 * @version 1.0
 * Date: 2023/7/10 23:33
 * Description: No Description
 */
@Component
@ConditionalOnProperty(name = "place.order.type", havingValue = "lock")
@RequiredArgsConstructor
@Slf4j
public class PlaceOrderByLockService implements PlaceOrderService {
    @DubboReference(version = "1.0.0")
    private GoodsDubboService goodsService;

    final RedisCache redisCache;
    final OrderRepository orderRepository;
    final DistributedLockFactory distributedLockFactory;

    @Override
    public Order placeOrder(OrderDTO orderDTO, Goods goods) {
        //redis 是否减库存标识，catch 回滚用
        boolean decrementStock = false;

        this.checkGoods(orderDTO,goods);

        String key = Constant.GOODS_STOCK_CACHE_KEY_PREFIX.concat(String.valueOf(goods.getId()));
        DistributedLock lock = distributedLockFactory.getDistributedLock(key);


        try {
            //未获取到分布式锁
            if (!lock.tryLock(2, 5, TimeUnit.SECONDS)){
                throw new BaseException(HttpCode.RETRY_LATER.getMesaage());
            }
            // 查询库存信息
            Integer stock = redisCache.getCacheObject(key);
            //库存不足
            if (stock < orderDTO.getQuantity()){
                throw new BaseException(HttpCode.STOCK_LT_ZERO.getMesaage());
            }
            //扣减库存
            redisCache.decrement(key, orderDTO.getQuantity());
            //正常执行了扣减缓存中库存的操作
            decrementStock = true;

            Order order = this.buildOrder(orderDTO,goods);
            //插入 订单
            orderRepository.saveOrder(order);
            //更新库存(这里更新库存可以用异步，反正是用redis控制)
            goodsService.updateAvailableStock(order.getQuantity(),order.getGoodsId());
            return order;
        }catch (Exception e){
            //将内存中的库存增加回去
            if (decrementStock){
                redisCache.increment(key,orderDTO.getQuantity());
                log.info("transaction decrementStock");
            }
            if (e instanceof InterruptedException){
                log.error("PlaceOrderLockService|下单分布式锁被中断|参数:{}|异常信息:{}", JSONObject.toJSONString(orderDTO), e.getMessage());
            }else{
                log.error("PlaceOrderLockService|分布式锁下单失败|参数:{}|异常信息:{}", JSONObject.toJSONString(orderDTO), e.getMessage());
            }
            throw new BaseException(e.getMessage());
        }finally {
            if (lock.isLocked() && lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }

    }


    @Override
    public Long initGoodsStock(String goodsId,int stock){
        String key = Constant.GOODS_STOCK_CACHE_KEY_PREFIX.concat(goodsId);
        redisCache.setCacheObject(key,stock);
        return -1L;
    }

}

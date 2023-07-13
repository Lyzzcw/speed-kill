package lyzzcw.work.speedkill.order.application.place.order.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lyzzcw.work.component.common.HttpUtils.exception.BaseException;
import lyzzcw.work.component.common.IDUtils.SnowflakeIdWorker;
import lyzzcw.work.speedkill.dubbo.interfaces.goods.GoodsDubboService;
import lyzzcw.work.speedkill.goods.domain.entity.Goods;
import lyzzcw.work.speedkill.order.application.place.order.PlaceOrderService;
import lyzzcw.work.speedkill.order.domain.constant.Constant;
import lyzzcw.work.speedkill.order.domain.constant.HttpCode;
import lyzzcw.work.speedkill.order.domain.convert.OrderConverter;
import lyzzcw.work.speedkill.order.domain.dto.OrderDTO;
import lyzzcw.work.speedkill.order.domain.entity.Order;
import lyzzcw.work.speedkill.order.domain.enums.OrderStatus;
import lyzzcw.work.speedkill.order.domain.repository.OrderRepository;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author lzy
 * @version 1.0
 * Date: 2023/7/10 23:33
 * Description: redis multi事务/watch
 * <p>
 * WATCH 机制：使用 WATCH 监视一个或多个 key ,
 * 跟踪 key 的 value 修改情况，如果有key 的 value 值在事务 EXEC 执行之前被修改了，
 * 整个事务被取消。EXEC 返回提示信息，表示事务已经失败。
 * <p>
 * WATCH 机制使的事务 EXEC 变的有条件，事务只有在被 WATCH 的 key 没有修改的前提下才能执行。
 * 不满足条件，事务被取消。使用 WATCH 监视了一个带过期时间的键，那么即使这个键过期了，事务仍然可以正常执行.
 * 大多数情况下，不同的客户端会访问不同的键，相互同时竞争同一 key 的情况一般都很少，watch 能很好解决数据冲突的问题。
 * <p>
 * 取消watch机制#
 * ①WATCH 命令可以被调用多次。对键的监视从 WATCH 执行之后开始生效，直到调用 EXEC 为止。不管事务是否成功执行，对所有键的监视都会被取消。
 * ②当客户端断开连接时，该客户端对键的监视也会被取消。
 * ③UNWATCH 命令可以手动取消对所有键的监视
 */
@Component
@ConditionalOnProperty(name = "place.order.type", havingValue = "watch")
@RequiredArgsConstructor
@Slf4j
public class PlaceOrderByWatchService implements PlaceOrderService {
    @DubboReference(version = "1.0.0")
    private GoodsDubboService goodsService;

    final OrderRepository orderRepository;
    final RedisTemplate redisTemplate;

    @Override
    public Order placeOrder(OrderDTO orderDTO, Goods goods) {

        this.checkGoods(orderDTO, goods);

        String key = Constant.GOODS_STOCK_CACHE_KEY_PREFIX.concat(String.valueOf(goods.getId()));


        //用jedis 这么写是没问题的，但是在redisTemplate中会有ERR EXEC without MULTI异常
        //源码得知enableTransactionSupport 属性的默认值是 false，
        //导致了每一个 RedisConnection 都是重新获取的。
        //所以，我们执行的 MULTI 和 EXEC 这两个命令不在同一个 Connection 中。
        //redisTemplate.watch(key);
        //redisTemplate.multi();
        //redisTemplate.exec();

        //Spring整合Redis事务的正确实现
        //使用 SessionCallBack 接口，从而保证所有的命令都是通过同一个 Redis 的连接进行操作。
        Order order = (Order)redisTemplate.execute(new SessionCallback<Order>() {
            public Order execute(RedisOperations operations) {
                try {
                    redisTemplate.watch(key);
                    // 查询库存信息
                    Integer stock = (Integer) redisTemplate.opsForValue().get(key);
                    //库存不足
                    if (stock < orderDTO.getQuantity()) {
                        throw new BaseException(HttpCode.STOCK_LT_ZERO.getMesaage());
                    }
                    redisTemplate.multi();//开启事务
                    //扣减库存
                    redisTemplate.opsForValue().decrement(key, orderDTO.getQuantity());
                    //提交事务，如果返回nil则说明执行失败
                    List list = redisTemplate.exec();
                    if (list != null && list.size() > 0) {
                        //进来发现东西还有，秒杀成功
                        Order order = OrderConverter.INSTANCE.orderDTO2Order(orderDTO);
                        order.setId(SnowflakeIdWorker.generateId());
                        order.setGoodsName(goods.getGoodsName());
                        order.setActivityPrice(goods.getActivityPrice());
                        BigDecimal orderPrice = goods.getActivityPrice().multiply(BigDecimal.valueOf(order.getQuantity()));
                        order.setOrderPrice(orderPrice);
                        order.setStatus(OrderStatus.CREATED.getCode());
                        order.setCreateTime(LocalDateTime.now());
                        //插入 订单
                        orderRepository.saveOrder(order);
                        //更新库存(这里更新库存可以用异步，反正是用redis控制)
                        goodsService.updateAvailableStock(order.getQuantity(), order.getGoodsId());
                        return order;
                    } else {
                        //执行结果不是OK，说明被修改了，被别人抢了
                        throw new IllegalStateException(HttpCode.RETRY_LATER.getMesaage());
                    }
                }catch (Exception e) {
                    log.error("redis watch error", e);
                }finally {
                    redisTemplate.unwatch();
                }
                return null;
            }
        });
        return order;
    }


    @Override
    public Long initGoodsStock(String goodsId, int stock) {
        String key = Constant.GOODS_STOCK_CACHE_KEY_PREFIX.concat(goodsId);
        redisTemplate.opsForValue().set(key, stock);
        return -1L;
    }

}

package lyzzcw.work.speedkill.order.application.place.order.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lyzzcw.work.component.common.HttpUtils.exception.BaseException;
import lyzzcw.work.speedkill.dubbo.interfaces.goods.GoodsDubboService;
import lyzzcw.work.speedkill.goods.domain.entity.Goods;
import lyzzcw.work.speedkill.order.application.place.order.PlaceOrderService;
import lyzzcw.work.speedkill.order.domain.constant.HttpCode;
import lyzzcw.work.speedkill.order.domain.dto.OrderDTO;
import lyzzcw.work.speedkill.order.domain.entity.Order;
import lyzzcw.work.speedkill.order.domain.repository.OrderRepository;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 * @author lzy
 * @version 1.0
 * Date: 2023/7/10 14:30
 * Description: No Description
 */
@Component
@ConditionalOnProperty(name = "place.order.type", havingValue = "db")
@RequiredArgsConstructor
@Slf4j
public class PlaceOrderByDBService implements PlaceOrderService {
    @DubboReference(version = "1.0.0")
    private GoodsDubboService goodsService;

    final OrderRepository orderRepository;

    @Override
    public Order placeOrder(OrderDTO orderDTO, Goods goods) {

        this.checkGoods(orderDTO,goods);
        //扣减库存不成功，则库存不足
        if (!goodsService.updateDbAvailableStock(orderDTO.getQuantity(), orderDTO.getGoodsId())){
            log.info("Insufficient inventory,order:{} ----> goods:{}",orderDTO,goods);
            throw new BaseException(HttpCode.STOCK_LT_ZERO.getMesaage());
        }
        log.info("Sufficient inventory,order:{} ----> goods:{}",orderDTO,goods);
        Order order = this.buildOrder(orderDTO,goods);
        //保存订单
        orderRepository.saveOrder(order);
        return order;
    }

    @Override
    public Long initGoodsStock(String goods,int stock) {
        return null;
    }

}

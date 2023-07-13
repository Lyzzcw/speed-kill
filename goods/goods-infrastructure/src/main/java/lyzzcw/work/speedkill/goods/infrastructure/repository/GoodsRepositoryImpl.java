package lyzzcw.work.speedkill.goods.infrastructure.repository;

import lombok.RequiredArgsConstructor;
import lyzzcw.work.speedkill.goods.domain.repository.GoodsRepository;
import lyzzcw.work.speedkill.goods.domain.entity.Goods;
import lyzzcw.work.speedkill.goods.infrastructure.mapper.GoodsMapper;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author lzy
 * @version 1.0
 * Date: 2023/7/8 9:13
 * Description: No Description
 */
@Component
@RequiredArgsConstructor
public class GoodsRepositoryImpl implements GoodsRepository {

    final GoodsMapper goodsMapper;

    @Override
    public int saveGoods(Goods goods) {
        return goodsMapper.saveGoods(goods);
    }

    @Override
    public Goods getGoodsId(Long id) {
        return goodsMapper.getGoodsId(id);
    }

    @Override
    public List<Goods> getGoodsByActivityId(Long activityId) {
        return goodsMapper.getGoodsByActivityId(activityId);
    }

    @Override
    public int updateStatus(Integer status, Long id) {
        return goodsMapper.updateStatus(status,id);
    }

    @Override
    public int updateAvailableStock(Integer count, Long id) {
        return goodsMapper.updateAvailableStock(count,id);
    }

    @Override
    public boolean updateDbAvailableStock(Integer count, Long id) {
        return goodsMapper.updateDbAvailableStock(count,id) > 0;
    }

    @Override
    public Integer getAvailableStockById(Long id) {
        return goodsMapper.getAvailableStockById(id);
    }
}

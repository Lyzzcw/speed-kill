package lyzzcw.work.speedkill.stock.application.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lyzzcw.work.component.common.http.exception.BaseException;
import lyzzcw.work.component.redis.lock.DistributedLock;
import lyzzcw.work.component.redis.lock.factory.DistributedLockFactory;
import lyzzcw.work.speedkill.stock.application.service.StockBucketArrangementService;
import lyzzcw.work.speedkill.stock.domain.constant.Constant;
import lyzzcw.work.speedkill.stock.domain.constant.HttpCode;
import lyzzcw.work.speedkill.stock.domain.model.dto.StockBucketDTO;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author lzy
 * @version 1.0
 * Date: 2023/9/1 13:51
 * Description: No Description
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class StockBucketArrangementServiceImpl implements StockBucketArrangementService {

    final DistributedLockFactory distributedLockFactory;
    final DataSourceTransactionManager dataSourceTransactionManager;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void arrangeStockBuckets(Long goodsId, Integer stock, Integer bucketsQuantity, Integer assignmentMode) {
        log.info("arrangeBuckets|准备库存分桶|{},{},{}", goodsId, stock, bucketsQuantity);
        if (goodsId == null || stock == null || stock <= 0 || bucketsQuantity == null || bucketsQuantity <= 0){
            throw new BaseException(HttpCode.PARAMS_INVALID.getMessage());
        }
        try {
            DistributedLock lock = distributedLockFactory.getDistributedLock(Constant.getKey(Constant.GOODS_STOCK_BUCKETS_SUSPEND_KEY, String.valueOf(goodsId)));
            boolean isLock = lock.tryLock();
            if (!isLock){
                log.info("arrangeStockBuckets|库存分桶时获取锁失败|{}", goodsId);
                return;
            }

        }catch (Exception e) {

        }finally {

        }

    }

    @Override
    public StockBucketDTO getStockBucketDTO(Long goodsId, Long version) {
        return null;
    }
}

package lyzzcw.work.speedkill.goods.application.listener.consumer;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;
import lyzzcw.work.component.redis.cache.business.L2CacheService;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.List;

/**
 * @author lzy
 * @version 1.0
 * Date: 2023/7/10 9:27
 * Description: No Description
 */
@Component
@Slf4j
@ConditionalOnProperty(name = "rocketmq.enabled", havingValue = "true")
public class UpdateCacheConsumer {

    private DefaultMQPushConsumer consumer;

    @Value("${rocketmq.name-server.address}")
    private String nameServerAddr;

    @Value("${rocketmq.group.updateCache}")
    private String group;

    @Value("${rocketmq.topic.updateCache}")
    private String topic;

    @Autowired
    private L2CacheService L2CacheService;

    @PostConstruct
    public void init() throws MQClientException {
        log.info("update cache consumer initialized");
        //1.创建消费者consumer,制定消费者组名
        this.consumer = new DefaultMQPushConsumer(this.group);
        //2.指定NameServer地址
        this.consumer.setNamesrvAddr(this.nameServerAddr);
        //3.订阅主题Topic和Tage
        this.consumer.subscribe(this.topic,"*");
        //设置消费模式 负载均衡|广播模式
        this.consumer.setMessageModel(MessageModel.CLUSTERING);
        //4.设置回调函数，处理消息
        this.consumer.registerMessageListener(new MessageListenerConcurrently() {
            //接收消息内容
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> list, ConsumeConcurrentlyContext consumeConcurrentlyContext) {
                try {
                    list.forEach(messageExt -> {
                       JSONObject json =  JSON.parseObject(new String(messageExt.getBody()));
                        L2CacheService.updateCacheData(
                               json.get("ID"), json.get("R"),json.getString("keyPrefix"));
                       log.info("update cache success : {}", json );
                    });
                    return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                }catch (Exception e){
                    log.error("update cache error",e);
                    return ConsumeConcurrentlyStatus.RECONSUME_LATER;
                }
            }
        });
        //5.启动消费者
        consumer.start();
    }

    @PreDestroy
    public void destroy() {
        log.info("update cache consumer destroyed");
        this.consumer.shutdown();
    }
}

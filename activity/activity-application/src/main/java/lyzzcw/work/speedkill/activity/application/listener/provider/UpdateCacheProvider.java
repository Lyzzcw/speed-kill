package lyzzcw.work.speedkill.activity.application.listener.provider;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.nio.charset.StandardCharsets;

/**
 * @author lzy
 * @version 1.0
 * Date: 2023/7/10 9:13
 * Description: 更新缓存事件发布
 */
@Component
@Slf4j
@ConditionalOnProperty(name = "rocketmq.enabled", havingValue = "true")
public class UpdateCacheProvider {

    private DefaultMQProducer producer;

    @Value("${rocketmq.name-server.address}")
    private String nameServerAddr;

    @Value("${rocketmq.group.updateCache}")
    private String group;

    @Value("${rocketmq.topic.updateCache}")
    private String topic;


    @PostConstruct
    public void init() throws MQClientException {
        log.info("update cache provider initialized");
        //1.创建消息生产者producer,并制定生产者组名
        this.producer = new DefaultMQProducer(this.group);
        //2.指定NameServer地址
        this.producer.setNamesrvAddr(this.nameServerAddr);
        this.producer.setSendMsgTimeout(1000*10);
        //3.启动producer
        this.producer.start();
    }

    @PreDestroy
    public void destroy() {
        log.info("update cache provider destroyed");
        this.producer.shutdown();
    }

    public <ID,R> SendStatus send(ID id,R r,String keyPrefix) throws Exception {
        JSONObject json = new JSONObject();
        json.put("ID",id);
        json.put("R",r);
        json.put("keyPrefix",keyPrefix);
        Message msg = new Message(this.topic, "*",
                json.toJSONString().getBytes(StandardCharsets.UTF_8));
        SendResult result = this.producer.send(msg);
        log.info("update cache provider send result: {}",JSON.toJSONString(result));
        return result.getSendStatus();
    }
}

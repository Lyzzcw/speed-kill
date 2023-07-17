package lyzzcw.work.speedkill.order.application.listener.provider;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.TransactionMQProducer;
import org.apache.rocketmq.client.producer.TransactionSendResult;
import org.apache.rocketmq.common.message.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author lzy
 * @version 1.0
 * Date: 2023/7/17 9:26
 * Description: No Description
 */
@Component
@Slf4j
@ConditionalOnProperty(name = "rocketmq.enabled", havingValue = "true")
public class TransactionOrderProvider {
    private String producerGroup = "order_trans_group";
    private String topic = "order_trans_topic";
    private TransactionMQProducer producer;
    @Value("${rocketmq.name-server.address}")
    private String nameServerAddr;


    //执行任务的线程池
    ThreadPoolExecutor executor = new ThreadPoolExecutor(5, 10, 60,
            TimeUnit.SECONDS, new ArrayBlockingQueue<>(50));

    @Autowired
    private TransactionOrderListener transactionOrderListener;
    @PostConstruct
    public void init(){
        producer = new TransactionMQProducer(producerGroup);
        producer.setNamesrvAddr(nameServerAddr);
        producer.setSendMsgTimeout(Integer.MAX_VALUE);
        producer.setExecutorService(executor);
        producer.setTransactionListener(transactionOrderListener);
        this.start();
    }
    private void start(){
        try {
            this.producer.start();
            log.info("start order mq producer success");
        } catch (MQClientException e) {
            e.printStackTrace();
        }
    }

    //事务消息发送
    public TransactionSendResult send(String data) throws MQClientException {
        Message message = new Message(this.topic,"*",data.getBytes(StandardCharsets.UTF_8));
        return this.producer.sendMessageInTransaction(message, null);
    }
}

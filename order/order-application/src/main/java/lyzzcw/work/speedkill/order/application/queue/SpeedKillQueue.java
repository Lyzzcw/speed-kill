package lyzzcw.work.speedkill.order.application.queue;

import lyzzcw.work.speedkill.order.domain.dto.OrderDTO;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author lzy
 * @version 1.0
 * Date: 2023/7/24 16:25
 * Description:
 * 利用阻塞队类，也可以解决高并发问题。
 * 其思想就是把接收到的请求按顺序存放到队列中，消费者线程逐一从队列里取数据进行处理，看下具体代码。
 * 阻塞队列：这里使用静态内部类的方式来实现单例模式，在并发条件下不会出现问题。
 * 这里需要特别注意的是，在代码中不能通过throw抛异常，否则消费线程会终止，而且由于进队和出队存在时间间隙，会导致商品少卖
 * 也可以基于Disruptor高性能队列来实现，Disruptor开发的系统单线程能支撑每秒600万订单。
 */
public class SpeedKillQueue{
    // 队列大小
    static final int QUEUE_MAX_SIZE = 100;

    // 用于多线程间下单的队列
    static BlockingQueue<OrderDTO> blockingQueue = new LinkedBlockingQueue<>(QUEUE_MAX_SIZE);

    // 使用静态内部类，实现单例模式
    private SpeedKillQueue(){};

    private static class SingletonHolder{
        // 静态初始化器，由JVM来保证线程安全
        private  static SpeedKillQueue queue = new SpeedKillQueue();
    }

    /**
     * 单例队列
     * @return
     */
    public static SpeedKillQueue getSkillQueue(){
        return SingletonHolder.queue;
    }

    /**
     * 生产入队
     * @param kill
     * @throws InterruptedException
     * add(e) 队列未满时，返回true；队列满则抛出IllegalStateException(“Queue full”)异常——AbstractQueue
     * put(e) 队列未满时，直接插入没有返回值；队列满时会阻塞等待，一直等到队列未满时再插入。
     * offer(e) 队列未满时，返回true；队列满时返回false。非阻塞立即返回。
     * offer(e, time, unit) 设定等待的时间，如果在指定时间内还不能往队列中插入数据则返回false，插入成功返回true。
     */
    public  Boolean produce(OrderDTO kill) {
        return blockingQueue.offer(kill);
    }
    /**
     * 消费出队
     * poll() 获取并移除队首元素，在指定的时间内去轮询队列看有没有首元素有则返回，否者超时后返回null
     * take() 与带超时时间的poll类似不同在于take时候如果当前队列空了它会一直等待其他线程调用notEmpty.signal()才会被唤醒
     */
    public OrderDTO consume() throws InterruptedException {
        return blockingQueue.take();
    }

    /**
     * 获取队列大小
     * @return
     */
    public int size() {
        return blockingQueue.size();
    }
}

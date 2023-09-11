/**
 * Copyright 2022-9999 the original author or authors.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package lyzzcw.work.speedkill.ratelimiter.queue;



import lyzzcw.work.component.common.http.exception.BaseException;

import java.util.concurrent.Callable;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author lzy
 * @version 1.0.0
 * @description 封装的线程池并发数限流器
 */
public class QueueRateLimiter {
    //线程池对象
    private ThreadPoolExecutor executor;
    //超时时间
    private long timeout;
    //超时时间单位
    private TimeUnit timeoutUnit;

    public QueueRateLimiter() {
    }

    public QueueRateLimiter(ThreadPoolExecutor executor, long timeout, TimeUnit timeoutUnit) {
        this.executor = executor;
        this.timeout = timeout;
        this.timeoutUnit = timeoutUnit;
    }

    public ThreadPoolExecutor getExecutor() {
        return executor;
    }

    public void setExecutor(ThreadPoolExecutor executor) {
        this.executor = executor;
    }

    public long getTimeout() {
        return timeout;
    }

    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }

    public TimeUnit getTimeoutUnit() {
        return timeoutUnit;
    }

    public void setTimeoutUnit(TimeUnit timeoutUnit) {
        this.timeoutUnit = timeoutUnit;
    }

    public void execute(Runnable command){
        executor.execute(command);
    }

    public <T> T submit(Callable<T> task){
        try {
            return executor.submit(task).get();
        } catch (Exception e) {
            throw new BaseException("服务器异常");
        }
    }

    public <T> T submitWithTimeout(Callable<T> task){
        try {
            return executor.submit(task).get(timeout, timeoutUnit);
        } catch (Exception e) {
            throw new BaseException("服务器异常");
        }
    }

    public void shutdown(){
        if (executor != null){
            executor.shutdown();
        }
    }
}

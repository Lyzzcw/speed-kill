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

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author lzy
 * @version 1.0.0
 * @description 并发数限流拦截器
 */
@Aspect
@Component
@ConditionalOnProperty(prefix = "rate.limit.local.concurrent", name = "enabled", havingValue = "true")
@Slf4j
public class QueueRateLimiterInterceptor implements EnvironmentAware, DisposableBean {
    private static final Map<String, QueueRateLimiter> BH_CONCURRENT_RATE_LIMITER_MAP = new ConcurrentHashMap<>();
    private Environment environment;

    @Value("${rate.limit.local.concurrent.default.corePoolSize:3}")
    private int defaultCorePoolSize;

    @Value("${rate.limit.local.concurrent.default.maximumPoolSize:5}")
    private int defaultMaximumPoolSize;

    @Value("${rate.limit.local.concurrent.default.queueCapacity:10}")
    private int defaultQueueCapacity;

    @Value("${rate.limit.local.concurrent.default.keepAliveTime:30}")
    private long defaultKeepAliveTime;

    @Value("${rate.limit.local.concurrent.default.timeout:1}")
    private long defaultTimeOut;

    @Pointcut("@annotation(QueueLimiter)")
    public void pointCut(QueueLimiter QueueLimiter){

    }

    @Around(value = "pointCut(QueueLimiter)")
    public Object around(ProceedingJoinPoint pjp, QueueLimiter QueueLimiter) throws Throwable {
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        String className = pjp.getTarget().getClass().getSimpleName();
        String methodName = signature.getName();
        String rateLimitName = environment.resolvePlaceholders(QueueLimiter.name());
        if (StrUtil.isEmpty(rateLimitName) || rateLimitName.contains("${")) {
            rateLimitName = className + "-" + methodName;
        }
        QueueRateLimiter rateLimiter = this.getRateLimiter(rateLimitName, QueueLimiter);
        Object[] args = pjp.getArgs();
        //基于自定义线程池实现并发数限流
        return rateLimiter.submit(() -> {
            try {
                return pjp.proceed(args);
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * 获取QueueRateLimiter对象
     */
    private QueueRateLimiter getRateLimiter(String rateLimitName, QueueLimiter QueueLimiter) {
        //先从Map缓存中获取
        QueueRateLimiter bhRateLimiter = BH_CONCURRENT_RATE_LIMITER_MAP.get(rateLimitName);
        //如果获取的bhRateLimiter为空，则创建bhRateLimiter，注意并发，创建的时候需要加锁
        if (bhRateLimiter == null){
            final String finalRateLimitName = rateLimitName.intern();
            synchronized (finalRateLimitName){
                //double check
                bhRateLimiter = BH_CONCURRENT_RATE_LIMITER_MAP.get(rateLimitName);
                //获取的bhRateLimiter再次为空
                if (bhRateLimiter == null){
                    int corePoolSize = QueueLimiter.corePoolSize() <= 0 ? defaultCorePoolSize : QueueLimiter.corePoolSize();
                    int maximumPoolSize = QueueLimiter.maximumPoolSize() <= 0 ? defaultMaximumPoolSize : QueueLimiter.maximumPoolSize();
                    int queueCapacity = QueueLimiter.queueCapacity() <= 0 ? defaultQueueCapacity : QueueLimiter.queueCapacity();

                    long keepAliveTime = QueueLimiter.keepAliveTime() <= 0 ? defaultKeepAliveTime : QueueLimiter.keepAliveTime();
                    TimeUnit keepAliveTimeUnit = QueueLimiter.keepAliveTimeUnit();

                    long timeout = QueueLimiter.timeout() <= 0 ? defaultTimeOut : QueueLimiter.timeout();
                    TimeUnit timeoutUnit = QueueLimiter.timeoutUnit();

                    ThreadPoolExecutor executor = new ThreadPoolExecutor(corePoolSize,
                            maximumPoolSize,
                            keepAliveTime,
                            keepAliveTimeUnit,
                            new ArrayBlockingQueue<>(queueCapacity),
                            (r) -> new Thread(r, "rate-limiter-threadPool-".concat(rateLimitName).concat("-"))
                            , new QueueRateLimiterPolicy());

                    bhRateLimiter = new QueueRateLimiter(executor, timeout, timeoutUnit);
                    BH_CONCURRENT_RATE_LIMITER_MAP.putIfAbsent(rateLimitName, bhRateLimiter);
                }
            }
        }
        return bhRateLimiter;
    }


    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    @Override
    public void destroy() throws Exception {
        //关闭线程池
        log.info("destroy|关闭线程池");
        Map<String, QueueRateLimiter> map = BH_CONCURRENT_RATE_LIMITER_MAP;
        if (map.size() > 0){
            for (Map.Entry<String, QueueRateLimiter> entry : map.entrySet()){
                QueueRateLimiter rateLimiter = entry.getValue();
                rateLimiter.shutdown();
            }
            map.clear();
            BH_CONCURRENT_RATE_LIMITER_MAP.clear();
        }
    }
}

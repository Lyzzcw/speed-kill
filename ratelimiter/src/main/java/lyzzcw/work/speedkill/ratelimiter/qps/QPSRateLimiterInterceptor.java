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
package lyzzcw.work.speedkill.ratelimiter.qps;

import cn.hutool.core.util.StrUtil;
import com.google.common.util.concurrent.RateLimiter;
import lombok.extern.slf4j.Slf4j;
import lyzzcw.work.component.common.HttpUtils.exception.BaseException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * @author lzy
 * @version 1.0.0
 * @description 限流切面
 */
@Aspect
@Component
@ConditionalOnProperty(prefix = "rate.limit.local.qps", name = "enabled", havingValue = "true")
@Slf4j
public class QPSRateLimiterInterceptor implements EnvironmentAware {
    private static final Map<String, QPSRateLimiter> QPS_RATE_LIMITER_MAP = new ConcurrentHashMap<>();
    private Environment environment;

    @Value("${rate.limit.local.qps.default.permitsPerSecond:1000}")
    private double defaultPermitsPerSecond;

    @Value("${rate.limit.local.qps.default.timeout:1}")
    private long defaultTimeout;

    @Pointcut("@annotation(QPSLimiter)")
    public void pointCut(QPSLimiter QPSLimiter){

    }

    @Around(value = "pointCut(QPSLimiter)")
    public Object around(ProceedingJoinPoint pjp, QPSLimiter QPSLimiter) throws Throwable {
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        String className = pjp.getTarget().getClass().getSimpleName();
        String methodName = signature.getName();
        String rateLimitName = environment.resolvePlaceholders(QPSLimiter.name());
        if (StrUtil.isEmpty(rateLimitName) || rateLimitName.contains("${")) {
            rateLimitName = className + "-" + methodName;
        }
        QPSRateLimiter rateLimiter = this.getRateLimiter(rateLimitName, QPSLimiter);
        boolean success = rateLimiter.tryAcquire();
        Object[] args = pjp.getArgs();
        if (success){
            return pjp.proceed(args);
        }
        log.error("around|访问接口过于频繁|{}|{}", className, methodName);
        throw new BaseException("操作过于频繁，稍后再试");
    }

    /**
     * 获取QPSRateLimiter对象
     */
    private QPSRateLimiter getRateLimiter(String rateLimitName, QPSLimiter QPSLimiter) {
        //先从Map缓存中获取
        QPSRateLimiter QPSRateLimiter = QPS_RATE_LIMITER_MAP.get(rateLimitName);
        //如果获取的QPSRateLimiter为空，则创建QPSRateLimiter，注意并发，创建的时候需要加锁
        if (QPSRateLimiter == null){
            final String finalRateLimitName = rateLimitName.intern();
            synchronized (finalRateLimitName){
                //double check
                QPSRateLimiter = QPS_RATE_LIMITER_MAP.get(rateLimitName);
                //获取的QPSRateLimiter再次为空
                if (QPSRateLimiter == null){
                    double permitsPerSecond = QPSLimiter.permitsPerSecond() <= 0 ? defaultPermitsPerSecond : QPSLimiter.permitsPerSecond();
                    long timeout = QPSLimiter.timeout() <= 0 ? defaultTimeout : QPSLimiter.timeout();
                    TimeUnit timeUnit = QPSLimiter.timeUnit();
                    QPSRateLimiter = new QPSRateLimiter(RateLimiter.create(permitsPerSecond), timeout, timeUnit);
                    QPS_RATE_LIMITER_MAP.putIfAbsent(rateLimitName, QPSRateLimiter);
                }
            }
        }
        return QPSRateLimiter;
    }


    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }
}

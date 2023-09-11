package lyzzcw.work.speedkill.gateway.filterfactory;

import com.alibaba.fastjson2.JSON;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.util.concurrent.RateLimiter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lyzzcw.work.component.common.http.entity.Result;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @author lzy
 * @version 1.0
 * Date: 2023/9/7 14:47
 * Description: 自定义限流器
 * 配置参数固定为类名前缀-CustomRequestRateLimit
 * 类名后缀固定为-GatewayFilterFactory
 */
@Component
@Slf4j
public class CustomRequestRateLimitGatewayFilterFactory extends
        AbstractGatewayFilterFactory<CustomRequestRateLimitGatewayFilterFactory.Config> {

    //构造函数，加载Config
    public CustomRequestRateLimitGatewayFilterFactory() {
        //固定写法
        super(CustomRequestRateLimitGatewayFilterFactory.Config.class);
        log.info("Loaded GatewayFilterFactory [RateLimit]");
    }

    private static final Cache<String, RateLimiter> RATE_LIMITER_CACHE = CacheBuilder
            .newBuilder()
            .maximumSize(1000)
            .expireAfterAccess(1, TimeUnit.HOURS)
            .build();

    @Override
    public GatewayFilter apply(Config config) {
        return new GatewayFilter() {
            @SneakyThrows
            @Override
            public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
                String remoteAddr = Objects.requireNonNull(exchange.getRequest().getRemoteAddress()).getAddress().getHostAddress();
                RateLimiter rateLimiter = RATE_LIMITER_CACHE.get(remoteAddr, () ->
                        RateLimiter.create(Double.parseDouble(config.getPermitsPerSecond())));
                if (rateLimiter.tryAcquire()) {
                    return chain.filter(exchange);
                }
                log.warn("Could not acquire rate limit for " + remoteAddr);
                ServerHttpResponse response = exchange.getResponse();
                response.setStatusCode(HttpStatus.TOO_MANY_REQUESTS);
                response.getHeaders().add("Content-Type", "application/json;charset=UTF-8");
                Result<String> responseMessage = Result.fail(
                        "Could not acquire rate limit for "+remoteAddr,"Guava-接口被限流了",1001);
                String responseStr = JSON.toJSONString(responseMessage);
                DataBuffer dataBuffer = response.bufferFactory().wrap(responseStr.getBytes(StandardCharsets.UTF_8));
                return response.writeWith(Mono.just(dataBuffer));
            }
        };
    }

    //读取配置文件中的参数 赋值到 配置类中
    @Override
    public List<String> shortcutFieldOrder() {
        return Collections.singletonList("permitsPerSecond");
    }

    public static class Config {
        private String permitsPerSecond;

        public void setPermitsPerSecond(String permitsPerSecond){
            this.permitsPerSecond = permitsPerSecond;
        }

        public String getPermitsPerSecond(){
            return this.permitsPerSecond;
        }
    }
}

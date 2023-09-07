package lyzzcw.work.speedkill.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author lzy
 * @version 1.0
 * Date: 2023/9/6 13:53
 * Description: No Description
 */
@EnableDiscoveryClient
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class,
        RedisAutoConfiguration.class})
public class GatewayStarter {
    public static void main(String[] args) {
        System.setProperty("user.home", "/home/lzy/gateway");
        System.setProperty("csp.sentinel.app.type", "1");
        SpringApplication.run(GatewayStarter.class, args);
    }
}

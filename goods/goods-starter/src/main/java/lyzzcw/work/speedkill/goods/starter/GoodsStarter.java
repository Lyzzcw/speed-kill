package lyzzcw.work.speedkill.goods.starter;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author lzy
 * @version 1.0
 * Date: 2023/7/5 10:26
 * Description: No Description
 */
@SpringBootApplication
@EnableDubbo(scanBasePackages = "lyzzcw.work.speedkill.goods.application")
@EnableDiscoveryClient
public class GoodsStarter {
    public static void main(String[] args) {
        SpringApplication.run(GoodsStarter.class, args);
    }
}

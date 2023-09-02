package lyzzcw.work.speedkill.stock.starter;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
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
@EnableDubbo(scanBasePackages = "lyzzcw.work.speedkill.stock.application")
@EnableDiscoveryClient
@MapperScan(value = {"lyzzcw.work.speedkill.stock.infrastructure.mapper"})
public class StockStarter {
    public static void main(String[] args) {
        SpringApplication.run(StockStarter.class, args);
    }
}

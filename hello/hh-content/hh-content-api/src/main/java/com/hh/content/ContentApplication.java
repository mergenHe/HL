package com.hh.content;

import com.alibaba.nacos.api.config.annotation.NacosConfigListener;
import com.alibaba.nacos.api.config.annotation.NacosProperty;
import com.spring4all.swagger.EnableSwagger2Doc;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
@EnableSwagger2Doc
@SpringBootApplication

public class ContentApplication {
   public static void main(String[] args) {
      SpringApplication.run(ContentApplication.class, args);
   }
}

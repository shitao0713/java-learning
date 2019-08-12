package com.example.learn;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @description: web包main方法
 *
 * @author xiaoTaoShi
 * @date 2019/6/10 10:57
 * @version 1.0.0
 * @copyright (C) 2013 WonHigh Information Technology Co.,Ltd All Rights Reserved. 
 *
 * The software for the WonHigh technology development, without the company's written consent,
 * and any other individuals and organizations shall not be used, Copying, Modify or distribute 
 * the software.
 */

@EnableDubbo(scanBasePackages = { "com.example.learn" })
@SpringBootApplication
@ComponentScan(basePackages = { "com.example.learn.*" })
@MapperScan("com.example.learn.dao.mapper")
public class JavaLearnWebApplication {
	public static void main(String[] args) {
		SpringApplication.run(JavaLearnWebApplication.class, args);
	}
}

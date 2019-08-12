package com.example.learn.dao.mybatis;

import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Configuration;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @description:
 *
 * @author: xiaoTaoShi
 * @date: 2019/6/21 22:15
 * @version 1.0.0
 * @copyright (C) 2013 WonHigh Information Technology Co.,Ltd 
 *  All Rights Reserved. 
 *
 * The software for the WonHigh technology development, without the 
 * company's written consent, and any other individuals and 
 * organizations shall not be used, Copying, Modify or distribute 
 * the software.
 */

@Configuration
@AutoConfigureAfter(MybatisConfig.class)
@MapperScan("com.example.learn.dao.mapper")
public class MybatisMapperScannerConfig {
	
	public MapperScannerConfigurer mapperScannerConfigurer() {
		MapperScannerConfigurer mapperScannerConfigurer = new MapperScannerConfigurer();
		mapperScannerConfigurer.setSqlSessionFactoryBeanName("sqlSessionFactory");
		mapperScannerConfigurer.setBasePackage("com.example.learn.dao.mapper");
		return mapperScannerConfigurer;
	}
}

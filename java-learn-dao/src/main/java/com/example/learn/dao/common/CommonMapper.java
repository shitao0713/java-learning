package com.example.learn.dao.common;

import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

/**
 * @description 通用mapper 创建CommonMapper接口继承Mapper
 *   用 Mapper 是一个可以实现任意 MyBatis 通用方法的框架，项目提供了常规的增删改查操作以及 Example 相关的单表操作。
 *   @  https://github.com/abel533/Mapper
 *   通用 Mapper 是为了解决 MyBatis 使用中 90% 的基本操作.
 *
 *   还有一个使用工具: PageHelper则提供通用的分页查询功能，使用它们可以很方便的进行开发，可以节省开发人员大量的时间
 *   @  https://github.com/pagehelper/Mybatis-PageHelper
 *
 * @author 石 涛
 * @date 2019/6/21 16:24
 * @version 1.0.0
 * @copyright (C) 2013 WonHigh Information Technology Co.,Ltd 
 *  All Rights Reserved. 
 *
 * The software for the WonHigh technology development, without the 
 * company's written consent, and any other individuals and 
 * organizations shall not be used, Copying, Modify or distribute 
 * the software.
 */

public interface CommonMapper<T> extends Mapper<T>, MySqlMapper<T> {
}

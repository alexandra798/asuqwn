package com.forum;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class ForumAdminApplication
{
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );
    }
}




@SpringBootApplication // 通常这个注解已经包含了 @ComponentScan 等
//扫描 forum-common 模块中的 com.forum.mapper 包下的所有 Mapper 接口
@MapperScan("com.forum.mapper")
public class ForumAdminApplication {
    public static void main(String[] args) {
        SpringApplication.run(ForumAdminApplication.class, args);
    }
}
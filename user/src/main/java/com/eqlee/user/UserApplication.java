package com.eqlee.user;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import yq.handle.ApplicationAdviceHandle;
import yq.handle.GlobalResponseHandler;
import yq.jwt.contain.LocalUser;
import yq.utils.IdGenerator;

@SpringBootApplication
@EnableTransactionManagement
@EnableSwagger2
@MapperScan("com.eqlee.user.dao")
public class UserApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(UserApplication.class, args);
    }

    @Bean
    public GlobalResponseHandler getGlobalResponseHandler() {
        return new GlobalResponseHandler();
    }

    @Bean
    public ApplicationAdviceHandle getApplicationAdviceHandle() {
        return new ApplicationAdviceHandle();
    }


    @Bean
    public PaginationInterceptor paginationInterceptor() {
        return new PaginationInterceptor();
    }

    @Bean
    public IdGenerator getIdGenerator () {
        return new IdGenerator();
    }

    @Bean
    public LocalUser getLocalUser () {
        return new LocalUser();
    }

    /**
     * 打包
     * @param builder
     * @return
     */
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(UserApplication.class);
    }

}

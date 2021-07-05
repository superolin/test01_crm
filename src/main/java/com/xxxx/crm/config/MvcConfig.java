package com.xxxx.crm.config;

import com.xxxx.crm.interceptors.NoLoginInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


//添加拦截器⽣效的配置类
@Configuration
public class MvcConfig implements WebMvcConfigurer {

    @Bean
    public NoLoginInterceptor noLoginInterceptor(){
        return new NoLoginInterceptor();
    }

    /**
     * 添加拦截器
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry){
        registry.addInterceptor(noLoginInterceptor())
                //用于设置需要拦截的过滤规则
                .addPathPatterns("/**")
                //用于设置不需要拦截的过滤规则
                .excludePathPatterns("/index","/user/login","/css/**","/images/**","/js/**","/lib/**");
    }
}

package com.mb.caching.config;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.Method;
import java.util.Arrays;

@Configuration
@EnableCaching
public class EhCacheConfig {

    @Bean
    public KeyGenerator multiplyKeyGenerator(){
      return  new KeyGenerator() {
            @Override
            public Object generate(Object o, Method method, Object... params) {
                return method.getName() + "_" + Arrays.toString(params);
            }
        };
    }
}

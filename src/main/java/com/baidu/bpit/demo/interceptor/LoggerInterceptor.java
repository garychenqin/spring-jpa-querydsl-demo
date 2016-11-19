package com.baidu.bpit.demo.interceptor;

import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Created by chenshouqin on 2016-11-19 08:43.
 */
@Component
@Aspect
public class LoggerInterceptor {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    ThreadLocal<Long> startTime = new ThreadLocal<Long>();

    @Pointcut("execution(public * com.baidu.bpit.demo.controller..*.*(..))")
    public void webLogPointcut(){}

    @Before("webLogPointcut()")
    public void before() {
        long beginTime = System.currentTimeMillis();
        logger.info("start time : {}", beginTime);
        startTime.set(beginTime);
    }

    @AfterReturning(returning = "ret", pointcut = "webLogPointcut()")
    public void after(Object ret) {
        long endTime = System.currentTimeMillis();
        logger.info("end time : {}", endTime);
        logger.info("execute time : {}", endTime - startTime.get());
    }
}

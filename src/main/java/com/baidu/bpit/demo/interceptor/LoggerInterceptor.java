package com.baidu.bpit.demo.interceptor;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by chenshouqin on 2016-11-19 08:43.
 */
@Component
@Aspect
@Order(1)
public class LoggerInterceptor {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    ThreadLocal<Long> startTime = new ThreadLocal<Long>();

    @Pointcut("execution(public * com.baidu.bpit.demo.controller..*.*(..))")
    public void webLogPointcut() {
    }

    @Before("webLogPointcut()")
    public void before(JoinPoint joinPoint) {
        long beginTime = System.currentTimeMillis();
        startTime.set(beginTime);

        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();

        logger.info("URL : {}", request.getRequestURL().toString());
        logger.info("REQUEST-METHOD : {}", request.getMethod());
        logger.info("IP : {}", request.getRemoteAddr());
        logger.info("METHOD : {}", joinPoint.getSignature().getDeclaringTypeName() +
                "." + joinPoint.getSignature().getName());
    }

    @AfterReturning(returning = "ret", pointcut = "webLogPointcut()")
    public void after(Object ret) {
        long endTime = System.currentTimeMillis();
        logger.info("execute time : {}", endTime - startTime.get());
    }
}

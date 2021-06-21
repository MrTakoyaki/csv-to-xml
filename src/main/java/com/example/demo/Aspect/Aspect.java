package com.example.demo.Aspect;


import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;


@Component
@org.aspectj.lang.annotation.Aspect
public class Aspect {
    @Pointcut("execution(* com.example.demo.Service..*(..))")
    public void ServicePointcut() {
    }



    @Before("ServicePointcut()")
    public void before(JoinPoint joinPoint) {
        Logger logger = Logger.getLogger(joinPoint.getTarget().getClass().getName());
        System.out.println("=====Service方法開始=====");
        System.out.println("method name:" + getMethodName(joinPoint));
        logger.info("method name:" + getMethodName(joinPoint));
//        Arrays.stream(joinPoint.getArgs()).forEach(System.out::println);
        for(int i=0;i<joinPoint.getArgs().length;i++){
            System.out.println(joinPoint.getArgs()[i]);
            logger.info(joinPoint.getArgs()[i]);
        }
        System.out.println("=====Service方法結束=====");
    }

    private String getMethodName(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        return signature.getName();
    }
}

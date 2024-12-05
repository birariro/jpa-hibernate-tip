package com.example.jpatipsample.utils.support;

import com.example.jpatipsample.utils.QueryAssertions;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

import java.lang.reflect.Proxy;
import java.sql.Connection;

@Aspect
public class ConnectionProxyAspect {

    @Pointcut("execution(* javax.sql.DataSource.getConnection(..))")
    public void performancePointcut() {
    }

    @Around("performancePointcut()")
    public Object start(ProceedingJoinPoint point) throws Throwable {

        Connection connection = (Connection) point.proceed();

        QueryStorage queryStorage = QueryAssertions.getQueryStorage();
        Connection proxyConnection = (Connection) Proxy.newProxyInstance(
                getClass().getClassLoader(),
                new Class[]{Connection.class},
                (proxy, method, args) -> {

                    if (method.getName().equals("prepareStatement")) {
                        if (queryStorage != null) {
                            queryStorage.pushQueryString(String.valueOf(args[0]));
                        }
                    }
                    return method.invoke(connection, args);
                }
        );

        return proxyConnection;
    }

}
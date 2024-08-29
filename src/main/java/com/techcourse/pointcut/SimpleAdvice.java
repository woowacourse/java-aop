package com.techcourse.pointcut;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.jetbrains.annotations.NotNull;

public class SimpleAdvice implements MethodInterceptor {

    @Override
    public Object invoke(@NotNull final MethodInvocation invocation) throws Throwable {
        return String.format("Hello, %s!", invocation.proceed());
    }
}

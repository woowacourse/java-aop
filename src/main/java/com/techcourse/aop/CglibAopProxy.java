package com.techcourse.aop;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;

public class CglibAopProxy implements AopProxy {

    private Class<?> targetClass;
    private MethodInterceptor methodInterceptor;

    public void setTargetClass(final Class<?> targetClass) {
        this.targetClass = targetClass;
    }

    public void addAdvice(final MethodInterceptor methodInterceptor) {
        this.methodInterceptor = methodInterceptor;
    }

    @Override
    public Object getProxy() {
        final var enhancer = new Enhancer();
        enhancer.setSuperclass(targetClass);
        enhancer.setCallback(methodInterceptor);
        return enhancer.create();
    }
}

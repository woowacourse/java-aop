package com.techcourse.sample;

import org.springframework.aop.MethodBeforeAdvice;

import java.lang.reflect.Method;

public class HelloBeforeAdvice implements MethodBeforeAdvice {

    @Override
    public void before(final Method method, final Object[] args, final Object target) throws Throwable {
        System.out.println("메서드 호출 전에 실행할 부가기능을 정의한 메서드입니다.");
    }
}

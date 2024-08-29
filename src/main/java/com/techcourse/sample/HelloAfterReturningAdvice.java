package com.techcourse.sample;

import org.springframework.aop.AfterReturningAdvice;

import java.lang.reflect.Method;

public class HelloAfterReturningAdvice implements AfterReturningAdvice {

    @Override
    public void afterReturning(final Object returnValue, final Method method, final Object[] args, final Object target) throws Throwable {
        if (!(returnValue instanceof String)) {
            throw new IllegalArgumentException("리턴값의 타입이 문자열이 아닙니다.");
        }
        System.out.println("메서드 호출 후에 실행할 부가기능을 정의한 메서드입니다.");
    }
}

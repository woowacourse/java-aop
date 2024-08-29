package com.techcourse.sample;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * HelloAroundAdvice는 어드바이스입니다.
 * 🌟어드바이스(Advice)란 타깃(Target)의 조인포인트에 부가기능을 제공하는 객체입니다.
 * 인터셉터(Interceptor)는 어드바이스를 구현하는 가장 일반적인 방법입니다.
 * 스프링은 어드바이스를 🌟인터셉터(Interceptor) 인터페이스로 모델링했습니다.
 *
 * 어드바이스를 적용하려면 위치를 지정해야 합니다.
 * 원하는 위치를 지정하기 위해 조인포인트(JoinPoint)라는 개념을 사용합니다.
 * 🌟조인포인트(JoinPoint)는 어드바이스(Advice)가 적용될 수 있는 위치를 의미합니다.
 * 스프링은 조인포인트를 🌟인보케이션(Invocation) 인터페이스로 모델링했습니다.
 *
 * 스프링 AOP는 주로 메서드 호출 시점에 부가기능을 적용합니다.
 * MethodInterceptor는 이름처럼 메서드 호출할 때 부가기능(어드바이스)을 적용합니다.
 * MethodInterceptor는 메서드 호출 전후에 부가기능을 수행할 수 있어서 어라운드 어드바이스(Around advice)라 부릅니다.
 */
public class HelloAroundAdvice implements MethodInterceptor {

    /**
     * invoke(...) 메서드의 인자 MethodInvocation은 메서드 호출 시점을 추상화한 조인포인트입니다.
     * 스프링 AOP는 조인포인트를 단순화하여 메서드 호출만 지원합니다.
     * 대부분 조인포인트는 메서드 호출만으로 충분합니다.
     * 메서드 호출이 아닌 다른 조인포인트가 필요하다면 AspectJ를 사용하세요.
     */
    @Nullable
    @Override
    public Object invoke(@NotNull final MethodInvocation invocation) throws Throwable {
        final var before = "Hello, "; // 타깃의 메서드 호출 전(before)에 실행할 부가기능입니다.

        final Object result = invocation.proceed(); // 타깃의 메서드를 호출합니다.

        final var after = "!"; // 타깃의 메서드 호출 후(after)에 실행할 부가기능입니다.

        return String.format("%s%s%s", before, result, after); // 어라운드 어드바이스는 메서드의 반환값을 변경할 수 있습니다.
    }
}

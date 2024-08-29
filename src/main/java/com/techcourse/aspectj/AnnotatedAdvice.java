package com.techcourse.aspectj;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class AnnotatedAdvice {

    // SpringAOP 테스트의 AspectJ 포인트컷 표현식
    @Pointcut("execution(* getMessage*(..))")
    public void getMessage() {
    }

    // 스프링 빈을 대상으로 하는 AspectJ 포인트컷 표현식
    @Pointcut("bean(world)")
    public void inWorld() {
    }

    // SpringAOP 테스트의 HelloAroundAdvice 클래스를 AspectJ로 변경한 코드
    // @Around 외에도 @Before, @After, @AfterReturning, @AfterThrowing 등의 어드바이스 타입이 존재한다.
    @Around(value = "getMessage()")
    public Object helloAroundAdvice(ProceedingJoinPoint pjp) throws Throwable {
        final var before = "Hello, ";
        final Object result = pjp.proceed();
        final var after = "!";
        return String.format("%s%s%s", before, result, after);
    }

    // HelloBeforeAdvice 클래스를 AspectJ로 변경한 코드
    @Before(value = "getMessage()")
    public void helloBeforeAdvice(JoinPoint joinPoint) {
        System.out.println("메서드 호출 전에 실행할 부가기능을 정의한 메서드입니다.");
    }
}

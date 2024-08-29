package com.techcourse.pointcut;

import com.techcourse.sample.World;
import org.springframework.aop.ClassFilter;
import org.springframework.aop.support.StaticMethodMatcherPointcut;

import java.lang.reflect.Method;

public class SimpleStaticPointcut extends StaticMethodMatcherPointcut {
    @Override
    public boolean matches(final Method method, final Class<?> targetClass) {
        return "getMessage".equals(method.getName());
    }

    // 타깃 클래스가 World 클래스인 경우에만 적용
    @Override
    public ClassFilter getClassFilter() {
        return clazz -> clazz == World.class;
    }
}

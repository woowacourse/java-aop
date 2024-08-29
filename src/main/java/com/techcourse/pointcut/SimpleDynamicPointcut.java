package com.techcourse.pointcut;

import org.springframework.aop.ClassFilter;
import org.springframework.aop.support.DynamicMethodMatcherPointcut;

import java.lang.reflect.Method;

public class SimpleDynamicPointcut extends DynamicMethodMatcherPointcut {

    @Override
    public boolean matches(final Method method, final Class<?> targetClass) {
        // static check
        return "dynamicPointcut".equals(method.getName());
    }

    @Override
    public boolean matches(final Method method, final Class<?> targetClass, final Object... args) {
        // dynamic check
        int x = (Integer) args[0];
        return x != 100;
    }

    // 타깃 클래스가 SampleBean 클래스인 경우에만 적용
    @Override
    public ClassFilter getClassFilter() {
        return cls -> cls == SampleBean.class;
    }
}

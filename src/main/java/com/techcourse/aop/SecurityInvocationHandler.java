package com.techcourse.aop;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class SecurityInvocationHandler implements InvocationHandler {

    private static final Logger log = LoggerFactory.getLogger(SecurityInvocationHandler.class);

    private final Object target;
    private final SecurityManager securityManager;

    public SecurityInvocationHandler(final Object target) {
        this.target = target;
        this.securityManager = new SecurityManager();
    }

    @Override
    public Object invoke(final Object proxy, final Method method, final Object[] args) throws Throwable {
        final var user = securityManager.getLoggedOnUser();
        if (user == null) {
            log.info("인증되지 않은 사용자입니다.");
            throw new IllegalStateException();
        }

        if ("gugu".equals(user.name())) {
            log.info("gugu 사용자가 인증되었습니다.");
            return method.invoke(target, args);
        }

        log.info("{} 사용자는 메서드에 대한 접근 권한이 없습니다.", user.name());
        throw new IllegalStateException();
    }
}

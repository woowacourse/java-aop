package com.techcourse.aop;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

public class SecurityMethodInterceptor implements MethodInterceptor {

    private static final Logger log = LoggerFactory.getLogger(SecurityMethodInterceptor.class);

    private final SecurityManager securityManager;

    public SecurityMethodInterceptor() {
        this.securityManager = new SecurityManager();
    }

    @Override
    public Object intercept(final Object obj, final Method method, final Object[] args, final MethodProxy methodProxy) throws Throwable {
        final var user = securityManager.getLoggedOnUser();
        if (user == null) {
            log.info("인증되지 않은 사용자입니다.");
            throw new IllegalStateException();
        }

        if ("gugu".equals(user.name())) {
            log.info("gugu 사용자가 인증되었습니다.");
            return methodProxy.invokeSuper(obj, args);
        }

        log.info("{} 사용자는 메서드에 대한 접근 권한이 없습니다.", user.name());
        throw new IllegalStateException();
    }
}

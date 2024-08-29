package com.techcourse.aop;

public class SecurityManager {

    private static final ThreadLocal<User> threadLocal = new ThreadLocal<>();

    public void login(String name, String password) {
        threadLocal.set(new User(name, password));
    }

    public void logout() {
        threadLocal.remove();
    }

    public User getLoggedOnUser() {
        return threadLocal.get();
    }
}

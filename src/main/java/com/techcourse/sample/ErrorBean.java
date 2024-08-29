package com.techcourse.sample;

public class ErrorBean {

    public void exception() throws Exception {
        throw new Exception("Generic Exception");
    }

    public void illegalArgumentException() throws IllegalArgumentException {
        throw new IllegalArgumentException("IllegalArgument Exception");
    }
}

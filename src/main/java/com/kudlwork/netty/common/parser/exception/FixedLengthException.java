package com.kudlwork.netty.common.parser.exception;

public class FixedLengthException extends RuntimeException {

    public FixedLengthException(String message) {
        super(message);
    }

    public FixedLengthException(String message, Throwable e) {
        super(message, e);
    }
}

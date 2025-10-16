package me.duanbn.snailfish.core;

public class RegisterException extends RuntimeException {

    public RegisterException(String msg) {
        super(msg);
    }

    public RegisterException(Throwable t) {
        super(t);
    }

    public RegisterException(String msg, Throwable t) {
        super(msg, t);
    }

}

package me.duanbn.snailfish.core.concurrent;

/**
 * @author bingnan.dbn
 */
public class TaskException extends RuntimeException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public TaskException(String msg) {
        super(msg);
    }

    public TaskException(Throwable t) {
        super(t);
    }

    public TaskException(String msg, Throwable t) {
        super(msg, t);
    }

}

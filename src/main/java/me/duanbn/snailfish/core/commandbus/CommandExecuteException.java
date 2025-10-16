package me.duanbn.snailfish.core.commandbus;

import lombok.Data;
import lombok.EqualsAndHashCode;
import me.duanbn.snailfish.core.ErrorMessageI;

/**
 * 命令相关的异常.
 * 
 * @author bingnan.dbn
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CommandExecuteException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private String code;
    private String message;

    public CommandExecuteException(ErrorMessageI errMsg) {
        super(errMsg.getCode() + " - " + errMsg.getMessage());
        this.code = errMsg.getCode();
        this.message = errMsg.getMessage();
    }

    public CommandExecuteException(ErrorMessageI errMsg, Object... params) {
        super(errMsg.getCode() + " - " + String.format(errMsg.getMessage(), params));
        this.code = errMsg.getCode();
        this.message = String.format(errMsg.getMessage(), params);
    }

    public CommandExecuteException(Throwable t) {
        super(t);
    }

    public CommandExecuteException(String code, String message) {
        super(code + " - " + message);
        this.code = code;
        this.message = message;
    }

    public CommandExecuteException(String code, String message, Throwable t) {
        super(code + " - " + message, t);
        this.code = code;
        this.message = message;
    }

}

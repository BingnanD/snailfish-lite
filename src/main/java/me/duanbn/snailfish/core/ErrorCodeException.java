/**
 * 
 */
package me.duanbn.snailfish.core;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author bingnan.dbn
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ErrorCodeException extends Exception {

    public ErrorCodeException(ErrorMessageI errMsg) {
        super(errMsg.getCode() + " - " + errMsg.getMessage());
    }

    public ErrorCodeException(ErrorMessageI errMsg, Object... params) {
        super(errMsg.getCode() + " - " + String.format(errMsg.getMessage(), params));
    }

    public ErrorCodeException(String msg) {
        super(msg);
    }

    public ErrorCodeException(String msg, Throwable t) {
        super(msg, t);
    }

    public ErrorCodeException(Throwable t) {
        super(t);
    }

    public ErrorCodeException(String code, String message) {
        super(code + " - " + message);
    }

}

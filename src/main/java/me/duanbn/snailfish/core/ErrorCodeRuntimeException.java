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
public class ErrorCodeRuntimeException extends RuntimeException {

    public ErrorCodeRuntimeException(ErrorMessageI errMsg) {
        super(errMsg.getCode() + " - " + errMsg.getMessage());
    }

    public ErrorCodeRuntimeException(ErrorMessageI errMsg, Object... params) {
        super(errMsg.getCode() + " - " + String.format(errMsg.getMessage(), params));
    }

    public ErrorCodeRuntimeException(String msg) {
        super(msg);
    }

    public ErrorCodeRuntimeException(Throwable t) {
        super(t);
    }

    public ErrorCodeRuntimeException(String msg, Throwable t) {
        super(msg, t);
    }

    public ErrorCodeRuntimeException(String code, String message) {
        super(code + " - " + message);
    }

}

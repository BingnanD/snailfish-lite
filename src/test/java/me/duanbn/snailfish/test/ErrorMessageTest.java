/**
 * 
 */
package me.duanbn.snailfish.test;

import org.junit.jupiter.api.Test;

import lombok.extern.slf4j.Slf4j;
import me.duanbn.snailfish.core.ErrorCodeException;
import me.duanbn.snailfish.core.ErrorCodeRuntimeException;
import me.duanbn.snailfish.core.ErrorMessageI;

/**
 * @author bingnan.dbn
 *
 */
@Slf4j
public class ErrorMessageTest {

    public static enum ErrorCode implements ErrorMessageI {
        MSG1("code0001", "this is a error message"),
        MSG2("code0002", "this is a error with %s");

        private String code;
        private String msg;

        private ErrorCode(String code, String msg) {
            this.code = code;
            this.msg = msg;
        }

        @Override
        public String getCode() {
            return this.code;
        }

        @Override
        public String getMessage() {
            return this.msg;
        }
    }

    @Test
    public void test() {
        try {
            throw new ErrorCodeException(ErrorCode.MSG1);
        } catch (Exception e) {
            log.error("", e);
        }
    }

    @Test
    public void test1() {
        try {
            throw new ErrorCodeException(ErrorCode.MSG2, "abc");
        } catch (Exception e) {
            log.error("", e);
        }
    }

    @Test
    public void test3() {
        try {
            throw new ErrorCodeRuntimeException(ErrorCode.MSG1);
        } catch (Exception e) {
            log.error("", e);
        }
    }

    @Test
    public void test4() {
        try {
            throw new ErrorCodeRuntimeException(ErrorCode.MSG2, "abc");
        } catch (Exception e) {
            log.error("", e);
        }
    }

}

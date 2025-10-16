package me.duanbn.snailfish.api.dto;

import java.util.ArrayList;
import java.util.Collection;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * MultiResponse
 *
 * @author zhilin
 * @author bingnan.dbn
 */
@Data
@EqualsAndHashCode(callSuper = true)
@SuppressWarnings({ "unchecked" })
public class MultiResponse<T> extends Response {

    /** (de)serialization */
    private static final long serialVersionUID = 4683238591912052316L;

    private Collection<T> data;
    private long total;

    public MultiResponse() {
        this.setCode(CODE_SUCCESS);
        this.setSuccess(true);
    }

    public static <T> MultiResponse<T> of() {
        return of(new ArrayList<T>());
    }

    public static <T> MultiResponse<T> of(Collection<T> data) {
        MultiResponse<T> multiResponse = new MultiResponse<>();
        multiResponse.setCode("200");
        multiResponse.setSuccess(true);
        multiResponse.setData(data);
        return multiResponse;
    }

    public static <T> MultiResponse<T> valueOf(Response response) {
        return (MultiResponse<T>) response;
    }

}

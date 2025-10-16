package me.duanbn.snailfish.api.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Response with single record
 *
 * @author zhilin
 * @author bingnan.dbn
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SingleResponse<T> extends Response {

    /** (de)serialization */
    private static final long serialVersionUID = 5158018076182500789L;

    private T data;

    public SingleResponse() {
        this.setCode(CODE_SUCCESS);
        this.setSuccess(true);
    }

    public static <T> SingleResponse<T> of() {
        SingleResponse<T> singleResponse = new SingleResponse<>();
        singleResponse.setCode(CODE_SUCCESS);
        singleResponse.setSuccess(true);
        return singleResponse;
    }

    public static <T> SingleResponse<T> of(T data) {
        SingleResponse<T> singleResponse = new SingleResponse<>();
        singleResponse.setSuccess(true);
        singleResponse.setCode(CODE_SUCCESS);
        singleResponse.setData(data);
        return singleResponse;
    }

    @SuppressWarnings("unchecked")
    public static <T> SingleResponse<T> valueOf(Response response) {
        return (SingleResponse<T>) response;
    }

}

package me.duanbn.snailfish.api.dto;

import java.io.Serializable;

import lombok.Data;

/**
 * Response
 *
 * @author zhilin
 */
@Data
public class Response implements Serializable {

	/** (de)serialization */
	private static final long serialVersionUID = 3064481355711332664L;

	/** 成功 */
	public static final String CODE_SUCCESS = "200";
	/** 校验参数失败 */
	public static final String CODE_PARAM_FAILURE = "400";
	/** 权限验证失败 */
	public static final String CODE_AUTH_FAILURE = "401";
	/** 执行命令内部错误 */
	public static final String CODE_INTERNAL_ERROR = "500";

	private boolean isSuccess;
	private String code;
	private String message;

	public static Response buildSuccess() {
		Response response = new Response();
		response.setSuccess(true);
		return response;
	}

	public static Response buildFailure(String errorCode, String errorMessage) {
		Response response = new Response();
		response.setSuccess(false);
		response.setCode(errorCode);
		response.setMessage(errorMessage);
		return response;
	}

}

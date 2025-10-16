package me.duanbn.snailfish.util;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class HttpException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int statusCode;

	public HttpException(String msg) {
		super(msg);
	}

	public HttpException(Throwable t) {
		super(t);
	}

	public HttpException(String msg, Throwable t) {
		super(msg, t);
	}

}

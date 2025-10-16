package me.duanbn.snailfish.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.hibernate.validator.HibernateValidator;
import org.springframework.util.CollectionUtils;

import me.duanbn.snailfish.util.lang.StringUtil;

import lombok.Data;

/**
 * 
 * @author bingnan.dbn
 *
 */
@SuppressWarnings({ "rawtypes" })
public class Validation {

	public static void assertNotEmpty(String value, String message) {
		if (StringUtil.isBlank(value)) {
			throw new IllegalArgumentException(message);
		}
	}

	public static void assertNotEmpty(Collection value, String message) {
		if (CollectionUtils.isEmpty(value)) {
			throw new IllegalArgumentException(message);
		}
	}

	public static void assertNotNull(Object value, String message) {
		if (value == null) {
			throw new IllegalArgumentException(message);
		}
	}

	private static Validator validator = javax.validation.Validation.byProvider(HibernateValidator.class).configure()
			.failFast(false).buildValidatorFactory().getValidator();

	public static <T> ValidResult validateBean(T t, Class<?>... groups) {
		ValidResult result = new ValidResult();
		Set<ConstraintViolation<T>> violationSet = validator.validate(t, groups);
		boolean hasError = violationSet != null && violationSet.size() > 0;
		result.setHasErrors(hasError);
		if (hasError) {
			for (ConstraintViolation<T> violation : violationSet) {
				result.addError(violation.getPropertyPath().toString(), violation.getMessage());
			}
		}
		return result;
	}

	public static <T> ValidResult validateProperty(T obj, String propertyName) {
		ValidResult result = new ValidResult();
		Set<ConstraintViolation<T>> violationSet = validator.validateProperty(obj, propertyName);
		boolean hasError = violationSet != null && violationSet.size() > 0;
		result.setHasErrors(hasError);
		if (hasError) {
			for (ConstraintViolation<T> violation : violationSet) {
				result.addError(propertyName, violation.getMessage());
			}
		}
		return result;
	}

	/**
	 * 校验结果类
	 */
	@Data
	public static class ValidResult {

		/**
		 * 是否有错误
		 */
		private boolean hasErrors;

		/**
		 * 错误信息
		 */
		private List<ErrorMessage> errors;

		public ValidResult() {
			this.errors = new ArrayList<>();
		}

		public boolean hasErrors() {
			return hasErrors;
		}

		public void setHasErrors(boolean hasErrors) {
			this.hasErrors = hasErrors;
		}

		/**
		 * 获取所有验证信息
		 * 
		 * @return 集合形式
		 */
		public List<ErrorMessage> getAllErrors() {
			return errors;
		}

		/**
		 * 获取所有验证信息
		 * 
		 * @return 字符串形式
		 */
		public String getErrors() {
			StringBuilder sb = new StringBuilder();
			for (ErrorMessage error : errors) {
				sb.append(error.getMessage()).append(" ");
				// sb.append(error.getPropertyPath()).append(":").append(error.getMessage()).append("
				// ");
			}
			return sb.toString();
		}

		public void addError(String propertyName, String message) {
			this.errors.add(new ErrorMessage(propertyName, message));
		}
	}

	@Data
	public static class ErrorMessage {

		private String propertyPath;

		private String message;

		public ErrorMessage() {
		}

		public ErrorMessage(String propertyPath, String message) {
			this.propertyPath = propertyPath;
			this.message = message;
		}
	}

}

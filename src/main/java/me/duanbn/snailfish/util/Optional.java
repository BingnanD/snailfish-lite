package me.duanbn.snailfish.util;

import java.util.Collection;
import java.util.function.Consumer;

import org.springframework.util.CollectionUtils;

import me.duanbn.snailfish.util.lang.StringUtil;

import lombok.Setter;

/**
 * @author bingnan.dbn
 */
@SuppressWarnings({ "rawtypes" })
public class Optional<T> {

	@Setter
	private T value;
	@Setter
	private boolean isString;
	@Setter
	private boolean isCollection;

	public static <T> Optional<T> ofNullable(T value) {
		Optional<T> optional = new Optional<>();
		optional.setValue(value);
		optional.setString(false);
		return optional;
	}

	public static Optional<String> ofEmptyable(String value) {
		Optional<String> optional = new Optional<>();
		optional.setValue(value);
		optional.setString(true);
		return optional;
	}

	public static <E> Optional<Collection<E>> ofEmptyable(Collection<E> value) {
		Optional<Collection<E>> optional = new Optional<>();
		optional.setCollection(true);
		optional.setValue(value);
		return optional;
	}

	public Optional<T> ifPresent(Consumer<T> consumer) {
		if (this.isString) {
			if (StringUtil.isNotBlank((String) this.value)) {
				consumer.accept(this.value);
			}
		} else if (this.isCollection) {
			if (!CollectionUtils.isEmpty((Collection) this.value)) {
				consumer.accept(value);
			}
		} else {
			if (this.value != null) {
				consumer.accept(this.value);
			}
		}

		return this;
	}

	public void orElse(Consumer<T> consumer) {
		if (this.isString) {
			if (StringUtil.isBlank((String) this.value)) {
				consumer.accept(this.value);
			}
		} else if (this.isCollection) {
			if (CollectionUtils.isEmpty((Collection) this.value)) {
				consumer.accept(value);
			}
		} else {
			if (this.value == null) {
				consumer.accept(null);
			}
		}
	}

}

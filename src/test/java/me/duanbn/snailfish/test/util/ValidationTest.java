package me.duanbn.snailfish.test.util;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.junit.jupiter.api.Test;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import me.duanbn.snailfish.util.Validation;
import me.duanbn.snailfish.util.Validation.ValidResult;

@Slf4j
public class ValidationTest {

	@Test
	public void test() {
		Bean bean = new Bean();
		bean.setId(10L);
		bean.setS("aaaaaaaaaa");
		bean.setI(5);
		bean.setInnerBean(new InnerBean());
		ValidResult validResult = Validation.validateBean(bean);
		if (validResult.hasErrors()) {
			log.error("{}", validResult.getErrors());
		}
	}

	@Data
	public static class Bean {
		@NotNull
		private Long id;
		@NotNull
		@Length(max = 10)
		private String s;
		@Min(1)
		@Max(10)
		private Integer i;

		@NotNull
		@Valid
		private InnerBean innerBean;

	}

	public static class InnerBean {
		@NotNull()
		private String ss;
	}

}

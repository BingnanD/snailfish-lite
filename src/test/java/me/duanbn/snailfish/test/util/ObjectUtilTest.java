package me.duanbn.snailfish.test.util;

import java.lang.reflect.Field;

import org.junit.jupiter.api.Test;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import me.duanbn.snailfish.util.ObjectUtil;

@Slf4j
@SuppressWarnings({ "rawtypes" })
public class ObjectUtilTest {

	@Test
	public void testGetFields() {
		Field[] fields = ObjectUtil.getFields(A.class, true);
		showField(fields);

		fields = ObjectUtil.getFields(B.class, true);
		showField(fields);
	}

	@Test
	public void testGetInterfaces() {
		Class[] interfaces = ObjectUtil.getInterfaces(A.class, true);
		showInterface(interfaces);

		interfaces = ObjectUtil.getInterfaces(B.class, true);
		showInterface(interfaces);

		interfaces = ObjectUtil.getInterfaces(C.class, true);
		showInterface(interfaces);
	}

	private void showField(Field[] fields) {
		for (Field field : fields) {
			log.info("{}", field);
		}
	}

	private void showInterface(Class[] clazzes) {
		for (Class clazz : clazzes) {
			log.info("{}", clazz);
		}
	}

	public interface A1 {
	}

	public interface B1 {
	}

	@Data
	public static class A implements A1 {
		private Integer I;
		private String s;
	}

	@Data
	@EqualsAndHashCode(callSuper = true)
	public static class B extends A implements B1 {
		private Long L;
		private Double D;
	}

	public static class C {
	}

}

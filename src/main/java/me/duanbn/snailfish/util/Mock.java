package me.duanbn.snailfish.util;

import java.lang.reflect.Field;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.util.CollectionUtils;

import me.duanbn.snailfish.util.collection.Lists;
import me.duanbn.snailfish.util.collection.Maps;

public class Mock {

	private static final Map<Class<?>, List<Field>> fieldCache = Maps.newConcurrentMap();

	private static Boolean[] boolValues = new Boolean[] { true, false };

	public static <T> T mock(Class<T> clazz) {
		List<Field> fields = getFields(clazz, true);
		if (CollectionUtils.isEmpty(fields)) {
			return null;
		}

		T instance = null;
		try {
			instance = clazz.getDeclaredConstructor().newInstance();
		} catch (Exception e) {
			throw new IllegalArgumentException("create mock isntance err " + clazz.getName());
		}

		if (CollectionUtils.isEmpty(fields)) {
			return instance;
		}

		try {
			for (Field field : fields) {
				if (field.get(instance) != null || field.getType() == Map.class || field.getType() == List.class) {
					continue;
				}
				field.setAccessible(true);

				if (field.getType() == Byte.TYPE || field.getType() == Byte.class) {
					field.set(instance, (byte) 1);
				} else if (field.getType() == Boolean.TYPE || field.getType() == Boolean.class) {
					field.set(instance, boolValues[Random.nextInt().intValue() % boolValues.length]);
				} else if (field.getType() == Short.TYPE) {
					field.setShort(instance, Random.nextInt().shortValue());
				} else if (field.getType() == Short.class) {
					field.set(instance, Short.valueOf(Random.nextInt().shortValue()));
				} else if (field.getType() == Integer.TYPE) {
					field.setInt(instance, Random.nextInt());
				} else if (field.getType() == Integer.class) {
					field.set(instance, Random.nextInt());
				} else if (field.getType() == Long.TYPE) {
					field.setLong(instance, Random.nextLong());
				} else if (field.getType() == Long.class) {
					field.set(instance, Random.nextLong());
				} else if (field.getType() == Float.TYPE) {
					field.setFloat(instance, Random.nextInt().floatValue());
				} else if (field.getType() == Float.class) {
					field.set(instance, Float.valueOf(Random.nextInt().floatValue()));
				} else if (field.getType() == Double.TYPE) {
					field.setDouble(instance, Random.nextInt().doubleValue());
				} else if (field.getType() == Double.class) {
					field.set(instance, Double.valueOf(Random.nextInt().doubleValue()));
				} else if (field.getType() == byte[].class) {
					field.set(instance, new byte[] { 0, 1, 2 });
				} else if (field.getType() == Byte[].class) {
					field.set(instance, new Byte[] { 3, 4, 5 });
				} else if (field.getType() == Date.class) {
					field.set(instance, new Date());
				} else if (field.getType().isEnum()) {
					Object[] enumConstants = field.getType().getEnumConstants();
					Object object = enumConstants[Random.nextInt() % enumConstants.length];
					field.set(instance, object);
				} else if (field.getType() == String.class) {
					field.set(instance, Random.javaUUID());
				} else if (field.getType() == URL.class) {
					field.set(instance, new URL("http://www.test.com"));
				} else {
					field.set(instance, mock(field.getType()));
				}
			}
		} catch (Exception e) {
			throw new IllegalStateException(e);
		}

		return instance;
	}

	private static List<Field> getFields(Class<?> clazz, boolean includeSuperClass) {
		List<Field> fieldList = fieldCache.get(clazz);

		if (fieldList == null) {
			synchronized (Mock.class) {
				fieldList = Lists.newArrayList();
				loadFields(fieldList, clazz, includeSuperClass);
			}
		}

		return fieldList;
	}

	private static void loadFields(List<Field> fields, Class<?> clazz, boolean includeSuperClass) {
		Field[] declaredFields = clazz.getDeclaredFields();
		if (declaredFields == null) {
			return;
		}
		for (Field f : declaredFields) {
			f.setAccessible(true);
			fields.add(f);
		}

		if (includeSuperClass && clazz.getSuperclass() != null) {
			loadFields(fields, clazz.getSuperclass(), includeSuperClass);
		}

	}

}

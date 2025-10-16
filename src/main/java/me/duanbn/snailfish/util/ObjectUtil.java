package me.duanbn.snailfish.util;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import me.duanbn.snailfish.util.collection.Lists;
import me.duanbn.snailfish.util.collection.Maps;

public class ObjectUtil {

	private static Map<Class<?>, List<Field>> fieldCache = Maps.newHashMap();
	private static Map<Class<?>, List<Class<?>>> interfaceCache = Maps.newHashMap();

	public static <T> T create(Class<T> clazz) {
		try {
			return clazz.newInstance();
		} catch (Exception e) {
			throw new IllegalArgumentException(clazz + " can not be new with no param constructor");
		}
	}

	public static Field[] getFields(Class<?> clazz, boolean includeSuperClass) {

		List<Field> fields = fieldCache.get(clazz);

		if (fields == null) {
			synchronized (ObjectUtil.class) {
				if (fields == null) {
					fields = Lists.newArrayList();
					loadFields(fields, clazz, includeSuperClass);
				}
			}
		}

		return fields.toArray(new Field[fields.size()]);
	}

	private static void loadFields(List<Field> fieldList, Class<?> clazz, boolean includeSuperClass) {
		Field[] declaredFields = clazz.getDeclaredFields();
		for (Field field : declaredFields) {
			fieldList.add(field);
		}

		if (includeSuperClass && clazz.getSuperclass() != null) {
			loadFields(fieldList, clazz.getSuperclass(), includeSuperClass);
		}
	}

	@SuppressWarnings("rawtypes")
	public static Class[] getInterfaces(Class<?> clazz, boolean includeSuperClass) {

		List<Class<?>> interfaces = interfaceCache.get(clazz);

		if (interfaces == null) {
			synchronized (ObjectUtil.class) {
				if (interfaces == null) {
					interfaces = Lists.newArrayList();
					loadInterfaces(interfaces, clazz, includeSuperClass);
				}
			}
		}

		return interfaces.toArray(new Class[interfaces.size()]);
	}

	private static void loadInterfaces(List<Class<?>> interfaceList, Class<?> clazz, boolean includeSuperClass) {
		Class<?>[] interfaces = clazz.getInterfaces();
		for (Class<?> interfaceClazz : interfaces) {
			interfaceList.add(interfaceClazz);
		}

		if (includeSuperClass && clazz.getSuperclass() != null) {
			loadInterfaces(interfaceList, clazz.getSuperclass(), includeSuperClass);
		}
	}

}

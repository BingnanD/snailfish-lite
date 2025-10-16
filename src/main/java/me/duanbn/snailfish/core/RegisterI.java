package me.duanbn.snailfish.core;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

import me.duanbn.snailfish.util.collection.Lists;

/**
 * @author zhilin
 * @author bingnan.dbn
 */
public interface RegisterI {

	/**
	 * 执行注册操作.将某种资源注册到本地进程中.
	 * 
	 * @param clazz 表示某种资源的Class.
	 */
	void doRegistration(Class<?> clazz) throws Exception;

	/**
	 * 获取被注册对象的泛型信息
	 * 
	 * @param clazz      被注册对象
	 * @param paramIndex 第几个泛型参数，从0开始
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	default Class getRegisterObjectGenricType(Class<?> clazz, int paramIndex) {
		List<Type> actualTypeList = Lists.newArrayList();

		Type genericSuperclass = clazz.getGenericSuperclass();
		if (genericSuperclass != null && genericSuperclass instanceof ParameterizedType) {
			Type[] params = ((ParameterizedType) genericSuperclass).getActualTypeArguments();
			for (Type type : params) {
				actualTypeList.add(type);
			}
		}

		Type[] genericInterfaces = clazz.getGenericInterfaces();
		if (genericInterfaces != null && genericInterfaces.length > 0) {
			ParameterizedType genType = (ParameterizedType) genericInterfaces[0];
			Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
			for (Type type : params) {
				actualTypeList.add(type);
			}
		}

		return (Class) actualTypeList.get(paramIndex);
	}

}

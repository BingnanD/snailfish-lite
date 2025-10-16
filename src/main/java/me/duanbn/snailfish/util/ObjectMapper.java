package me.duanbn.snailfish.util;

import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.springframework.util.CollectionUtils;

import me.duanbn.snailfish.util.collection.Lists;
import me.duanbn.snailfish.util.collection.Maps;
import me.duanbn.snailfish.util.lang.StringUtil;

import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.converter.BidirectionalConverter;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import ma.glasnost.orika.metadata.ClassMapBuilder;

/**
 * 对象转换.
 * 
 * @author shanwei
 *
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class ObjectMapper {

	private static final Map<String, MapperFactory> cache = Maps.newConcurrentMap();
	private static final Map<String, ClassMapBuilder> cacheForClassMapBuilder = Maps
			.newConcurrentMap();

	private static final Lock lock = new ReentrantLock();

	private static String cacheKeyForMapperFactory(Class src, Class targetClass) {
		String key = "mapperkey-" + src.getName() + targetClass.getName();
		return key;
	}

	private static String cacheKeyForClassMapBuilder(Class srcClass, Class targetClass) {
		String key = "classMapKey-" + srcClass.getName() + targetClass.getName();
		return key;
	}

	/********************************************************************************
	 * map
	 ********************************************************************************/
	public static void map(Object src, Object target) {
		map3(src, target, null, false);
	}

	public static void map(Object src, Object target, List<MapField> mapFieldList) {
		map3(src, target, mapFieldList, false);
	}

	public static void map(Object src, Object target, boolean mapNulls) {
		map3(src, target, null, mapNulls);
	}

	public static void map(Object src, Object target, List<MapField> mapFieldList, boolean mapNulls) {
		map3(src, target, mapFieldList, mapNulls);
	}

	public static void map(Object src, Object target, BidirectionalConverter converter) {
		map0(src, target, converter, false);
	}

	public static void map(Object src, Object target, BidirectionalConverter converter, boolean mapNulls) {
		map0(src, target, converter, mapNulls);
	}

	public static <T> T map(Object src, Class<T> targetClass) {
		return map1(src, targetClass, null, false);
	}

	public static <T> T map(Object src, Class<T> targetClass, List<MapField> mapFieldList) {
		return map2(src, targetClass, mapFieldList, false);
	}

	public static <T> T map(Object src, Class<T> targetClass, boolean mapNulls) {
		return map1(src, targetClass, null, mapNulls);
	}

	public static <T> T map(Object src, Class<T> targetClass, List<MapField> mapFieldList, boolean mapNulls) {
		return map2(src, targetClass, mapFieldList, mapNulls);
	}

	public static <T> T map(Object src, Class<T> targetClass, BidirectionalConverter converter) {
		return map1(src, targetClass, converter, false);
	}

	public static <T> T map(Object src, Class<T> targetClass, BidirectionalConverter converter, boolean mapNulls) {
		return map1(src, targetClass, converter, mapNulls);
	}

	private static void map0(Object src, Object target, BidirectionalConverter converter, boolean mapNulls) {
		if (src == null || target == null) {
			return;
		}

		MapperFactory mapperFactory = getMapperFactoryWithConverter(src.getClass(), target.getClass(), converter,
				mapNulls);
		mapperFactory.getMapperFacade().map(src, target);
	}

	private static <T> T map1(Object src, Class<T> targetClass, BidirectionalConverter converter, boolean mapNulls) {
		if (src == null || targetClass == null) {
			return null;
		}

		MapperFactory mapperFactory = getMapperFactoryWithConverter(src.getClass(), targetClass, converter, mapNulls);

		T target = (T) mapperFactory.getMapperFacade().map(src, targetClass);
		return target;
	}

	private static <T> T map2(Object src, Class<T> targetClass, List<MapField> mapFieldList, boolean mapNulls) {
		if (src == null || targetClass == null) {
			return null;
		}

		MapperFactory mapperFactory = getMapperFactoryWithMapField(src.getClass(), targetClass, mapFieldList, mapNulls);
		T target = (T) mapperFactory.getMapperFacade().map(src, targetClass);
		return target;
	}

	private static void map3(Object src, Object target, List<MapField> mapFieldList, boolean mapNulls) {
		if (src == null || target == null) {
			return;
		}

		MapperFactory mapperFactory = getMapperFactoryWithMapField(src.getClass(), target.getClass(), mapFieldList,
				mapNulls);
		mapperFactory.getMapperFacade().map(src, target);
	}

	/********************************************************************************
	 * map list
	 ********************************************************************************/
	public static <T> List<T> mapAsList(Iterable src, Class<T> targetClass) {
		return mapAsList0(src, targetClass, null, false);
	}

	public static <T> List<T> mapAsList(Iterable src, Class<T> targetClass, boolean mapNulls) {
		return mapAsList1(src, targetClass, null, mapNulls);
	}

	public static <T> List<T> mapAsList(Iterable src, Class<T> targetClass, BidirectionalConverter converter) {
		return mapAsList0(src, targetClass, converter, false);
	}

	public static <T> List<T> mapAsList(Iterable src, Class<T> targetClass, BidirectionalConverter converter,
			boolean mapNulls) {
		return mapAsList0(src, targetClass, converter, mapNulls);
	}

	public static <T> List<T> mapAsList(Iterable src, Class<T> targetClass, List<MapField> mapFieldList) {
		return mapAsList1(src, targetClass, mapFieldList, false);
	}

	public static <T> List<T> mapAsList(Iterable src, Class<T> targetClass, List<MapField> mapFieldList,
			boolean mapNulls) {
		return mapAsList1(src, targetClass, mapFieldList, mapNulls);
	}

	private static <T> List<T> mapAsList0(Iterable src, Class<T> targetClass, BidirectionalConverter converter,
			boolean mapNulls) {
		if (src == null || !src.iterator().hasNext()) {
			return Lists.newArrayList();
		}

		Class srcClass = src.iterator().next().getClass();
		MapperFactory mapperFactory = getMapperFactoryWithConverter(srcClass, targetClass, converter, mapNulls);
		List<T> targetList = mapperFactory.getMapperFacade().mapAsList(src, targetClass);
		return targetList;
	}

	private static <T> List<T> mapAsList1(Iterable src, Class<T> targetClass, List<MapField> mapFieldList,
			boolean mapNulls) {
		if (src == null || !src.iterator().hasNext()) {
			return Lists.newArrayList();
		}

		Class srcClass = src.iterator().next().getClass();
		MapperFactory mapperFactory = getMapperFactoryWithMapField(srcClass, targetClass, mapFieldList, mapNulls);
		List<T> targetList = mapperFactory.getMapperFacade().mapAsList(src, targetClass);
		return targetList;
	}

	/********************************************************************************
	 * global config
	 ********************************************************************************/
	public static void registerConverer(Class srcClass, Class targetClass, BidirectionalConverter converter) {
		registerConverter(srcClass, targetClass, null, converter);
	}

	public static void registerConverter(Class srcClass, Class targetClass, String field,
			BidirectionalConverter converter) {
		MapperFactory mapperFactory = getMapperFactory(srcClass, targetClass);
		if (StringUtil.isNotBlank(field)) {
			mapperFactory.getConverterFactory().registerConverter(field, converter);
		} else {
			mapperFactory.getConverterFactory().registerConverter(converter);
		}
	}

	/********************************************************************************
	 * interal method
	 ********************************************************************************/
	private static <T> MapperFactory getMapperFactory(Class srcClass, Class<T> targetClass) {
		String cacheKey = cacheKeyForMapperFactory(srcClass, targetClass);

		MapperFactory mapperFactory = cache.get(cacheKey);
		if (mapperFactory == null) {
			try {
				lock.lock();
				mapperFactory = cache.get(cacheKey);
				if (mapperFactory == null) {
					mapperFactory = new DefaultMapperFactory.Builder().mapNulls(false).build();
					cache.put(cacheKey, mapperFactory);
				}
			} finally {
				lock.unlock();
			}
		}
		return mapperFactory;
	}

	private static <T> MapperFactory getMapperFactoryWithConverter(Class srcClass, Class<T> targetClass,
			BidirectionalConverter converter, boolean mapNulls) {

		MapperFactory mapperFactory = getMapperFactory(srcClass, targetClass);
		if (converter != null) {
			mapperFactory.getConverterFactory().registerConverter(converter);
		}

		String cacheKeyForClassMapBuilder = cacheKeyForClassMapBuilder(srcClass, targetClass);
		ClassMapBuilder<? extends Object, T> classMap = cacheForClassMapBuilder.get(cacheKeyForClassMapBuilder);
		if (classMap == null) {
			try {
				lock.lock();
				if (classMap == null) {
					classMap = mapperFactory.classMap(srcClass, targetClass);
					classMap.mapNulls(mapNulls);
					classMap.mapNullsInReverse(mapNulls);
					classMap.byDefault().register();
					cacheForClassMapBuilder.put(cacheKeyForClassMapBuilder, classMap);
				}
			} finally {
				lock.unlock();
			}
		}

		return mapperFactory;
	}

	private static <T> MapperFactory getMapperFactoryWithMapField(Class srcClass, Class<T> targetClass,
			List<MapField> mapFieldList, boolean mapNulls) {
		MapperFactory mapperFactory = getMapperFactory(srcClass, targetClass);
		ClassMapBuilder<? extends Object, T> classMap = mapperFactory.classMap(srcClass, targetClass);

		if (!CollectionUtils.isEmpty(mapFieldList)) {
			for (MapField mapField : mapFieldList) {
				if (mapField.converter != null) {
					mapperFactory.getConverterFactory().registerConverter(mapField.getConverterName(),
							mapField.converter);
					classMap.fieldMap(mapField.srcFieldName, mapField.targetFieldName)
							.converter(mapField.getConverterName()).add();
				} else {
					classMap.field(mapField.srcFieldName, mapField.targetFieldName);
				}
			}
		}

		classMap.mapNulls(mapNulls);
		classMap.mapNullsInReverse(mapNulls);
		classMap.byDefault().register();
		cache.put(cacheKeyForMapperFactory(srcClass, targetClass), mapperFactory);
		return mapperFactory;
	}

	/********************************************************************************
	 * map field
	 ********************************************************************************/
	public static class MapField {

		private String srcFieldName;
		private String targetFieldName;
		private BidirectionalConverter converter;

		public static MapField valueOf(String fieldName) {
			return new MapField(fieldName);
		}

		public static MapField valueOf(String srcFieldName, String targetFieldName) {
			return new MapField(srcFieldName, targetFieldName);
		}

		public static MapField valueOf(String fieldName, BidirectionalConverter converter) {
			return new MapField(fieldName, converter);
		}

		public static MapField valueOf(String srcFieldName, String targetFieldName, BidirectionalConverter converter) {
			return new MapField(srcFieldName, targetFieldName, converter);
		}

		private MapField(String fieldName) {
			this(fieldName, fieldName, null);
		}

		private MapField(String srcFieldName, String targetFieldName) {
			this(srcFieldName, targetFieldName, null);
		}

		private MapField(String fieldName, BidirectionalConverter converter) {
			this(fieldName, fieldName, converter);
		}

		private MapField(String srcFieldName, String targetFieldName, BidirectionalConverter converter) {
			this.srcFieldName = srcFieldName;
			this.targetFieldName = targetFieldName;
			this.converter = converter;
		}

		public String getConverterName() {
			return this.converter.getClass().getName();
		}

	}

}

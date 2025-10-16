package me.duanbn.snailfish.core.domain;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import lombok.AccessLevel;
import lombok.Setter;
import ma.glasnost.orika.converter.BidirectionalConverter;
import me.duanbn.snailfish.api.dto.DomainEDTO;
import me.duanbn.snailfish.api.dto.DomainVDTO;
import me.duanbn.snailfish.core.DataNode;
import me.duanbn.snailfish.core.domain.converter.DateConverter;
import me.duanbn.snailfish.core.domain.converter.IdConverter;
import me.duanbn.snailfish.core.domain.pattern.DomainPatternCallbackConsumer;
import me.duanbn.snailfish.core.domain.pattern.DomainPatternCallbackFunction;
import me.duanbn.snailfish.core.domain.pattern.DomainPatternI;
import me.duanbn.snailfish.core.domain.pattern.DomainPatternRegister;
import me.duanbn.snailfish.core.domain.pattern.DomainSPEC;
import me.duanbn.snailfish.util.ObjectMapper;
import me.duanbn.snailfish.util.ObjectUtil;
import me.duanbn.snailfish.util.collection.Lists;

/**
 * 领域模式总线. 领域服务，领域工厂，领域仓储.
 * 
 * @author bingnan.dbn
 *
 */
@SuppressWarnings({ "rawtypes", "unchecked", })
public class DomainBus {

	@Setter(AccessLevel.PRIVATE)
	protected static DomainObjectResgister domainObjectResgister;
	@Setter(AccessLevel.PRIVATE)
	protected static DomainPatternRegister domainPatternRegister;

	public static final IdConverter idConverter = new IdConverter();
	public static final DateConverter dateConverter = new DateConverter();

	static {
		DomainBus.registerConverter(DomainE.class, DomainEDTO.class, "id", idConverter);
		DomainBus.registerConverter(DomainV.class, DomainVDTO.class, "id", idConverter);
	}

	public static class DomainBusInjector {
		@Autowired
		private DomainObjectResgister domainObjectResgister;
		@Autowired
		private DomainPatternRegister domainPatternRegister;

		@PostConstruct
		public void postConstruct() {
			DomainBus.setDomainObjectResgister(domainObjectResgister);
			DomainBus.setDomainPatternRegister(domainPatternRegister);
		}
	}

	/********************************************************************************
	 * dispatch
	 ********************************************************************************/
	public static <T extends DomainPatternI, R> R dispatch(Class<T> clazz,
			DomainPatternCallbackFunction<T, R> function) {
		T findComponent = (T) domainPatternRegister.getDomainPattern(clazz);

		if (findComponent == null) {
			throw new DomainBusException("not found domain pattern object by " + clazz);
		}

		return function.apply(findComponent);
	}

	public static <T extends DomainPatternI, R> R dispatch(Class<T> clazz, String patternName,
			DomainPatternCallbackFunction<T, R> function) {
		T findComponent = (T) domainPatternRegister.getDomainPattern(clazz, patternName);

		if (findComponent == null) {
			throw new DomainBusException("not found domain pattern object by " + clazz);
		}

		return function.apply(findComponent);
	}

	public static <T extends DomainPatternI> void dispatchVoid(Class<T> clazz,
			DomainPatternCallbackConsumer<T> consumer) {
		T findComponent = (T) domainPatternRegister.getDomainPattern(clazz);

		if (findComponent == null) {
			throw new DomainBusException("not found domain pattern object by " + clazz);
		}

		consumer.accept(findComponent);
	}

	public static <T extends DomainPatternI> void dispatchVoid(Class<T> clazz, String patternName,
			DomainPatternCallbackConsumer<T> consumer) {
		T findComponent = (T) domainPatternRegister.getDomainPattern(clazz, patternName);

		if (findComponent == null) {
			throw new DomainBusException("not found domain pattern object by " + clazz);
		}

		consumer.accept(findComponent);
	}

	public static <T> T createSpec(Class<T> specClazz) {
		if (!DomainSPEC.class.isAssignableFrom(specClazz)) {
			throw new IllegalArgumentException(
					specClazz + " is not a DomainSpec, it is should extends from DomainSpec class");
		}
		return ObjectUtil.create(specClazz);
	}

	public static List<Class> getDomainEntityClasses() {
		return domainObjectResgister.getEntityClasses();
	}

	public static List<Class> getDomainValueObjectClasses() {
		return domainObjectResgister.getValueObjectClasses();
	}

	/********************************************************************************
	 * map to DataNode
	 ********************************************************************************/
	public static DataNode map(Object src) {
		DataNode dataNode = new DataNode();
		dataNode.append(src);
		return dataNode;
	}

	public static List<DataNode> mapAsList(List srcs) {
		List<DataNode> result = Lists.newArrayList();
		srcs.forEach(src -> {
			result.add(map(src));
		});
		return result;
	}

	/********************************************************************************
	 * map
	 ********************************************************************************/
	public static void map(Object src, Object target) {
		ObjectMapper.map(src, target);
	}

	public static void map(Object src, Object target, List<ObjectMapper.MapField> mapFieldList) {
		ObjectMapper.map(src, target, mapFieldList);
	}

	public static void map(Object src, Object target, boolean mapNulls) {
		ObjectMapper.map(src, target, mapNulls);
	}

	public static void map(Object src, Object target, List<ObjectMapper.MapField> mapFieldList, boolean mapNulls) {
		ObjectMapper.map(src, target, mapFieldList, mapNulls);
	}

	public static void map(Object src, Object target, BidirectionalConverter converter) {
		ObjectMapper.map(src, target, converter);
	}

	public static void map(Object src, Object target, BidirectionalConverter converter, boolean mapNulls) {
		ObjectMapper.map(src, target, converter, mapNulls);
	}

	public static <T> T map(Object src, Class<T> targetClass) {
		return ObjectMapper.map(src, targetClass);
	}

	public static <T> T map(Object src, Class<T> targetClass, List<ObjectMapper.MapField> mapFieldList) {
		return ObjectMapper.map(src, targetClass, mapFieldList);
	}

	public static <T> T map(Object src, Class<T> targetClass, boolean mapNulls) {
		return ObjectMapper.map(src, targetClass, mapNulls);
	}

	public static <T> T map(Object src, Class<T> targetClass, List<ObjectMapper.MapField> mapFieldList,
			boolean mapNulls) {
		return ObjectMapper.map(src, targetClass, mapFieldList, mapNulls);
	}

	public static <T> T map(Object src, Class<T> targetClass, BidirectionalConverter converter) {
		return ObjectMapper.map(src, targetClass, converter);
	}

	public static <T> T map(Object src, Class<T> targetClass, BidirectionalConverter converter, boolean mapNulls) {
		return ObjectMapper.map(src, targetClass, converter, mapNulls);
	}

	public static <T> List<T> mapAsList(Iterable src, Class<T> targetClass) {
		return ObjectMapper.mapAsList(src, targetClass);
	}

	public static <T> List<T> mapAsList(Iterable src, Class<T> targetClass, boolean mapNulls) {
		return ObjectMapper.mapAsList(src, targetClass, mapNulls);
	}

	public static <T> List<T> mapAsList(Iterable src, Class<T> targetClass, BidirectionalConverter converter) {
		return ObjectMapper.mapAsList(src, targetClass, converter);
	}

	public static <T> List<T> mapAsList(Iterable src, Class<T> targetClass, BidirectionalConverter converter,
			boolean mapNulls) {
		return ObjectMapper.mapAsList(src, targetClass, converter, mapNulls);
	}

	public static <T> List<T> mapAsList(Iterable src, Class<T> targetClass, List<ObjectMapper.MapField> mapFieldList) {
		return ObjectMapper.mapAsList(src, targetClass, mapFieldList);
	}

	public static <T> List<T> mapAsList(Iterable src, Class<T> targetClass, List<ObjectMapper.MapField> mapFieldList,
			boolean mapNulls) {
		return ObjectMapper.mapAsList(src, targetClass, mapFieldList, mapNulls);
	}

	public static void registerConverer(Class srcClass, Class targetClass, BidirectionalConverter converter) {
		ObjectMapper.registerConverer(srcClass, targetClass, converter);
	}

	public static void registerConverter(Class srcClass, Class targetClass, String field,
			BidirectionalConverter converter) {
		ObjectMapper.registerConverter(srcClass, targetClass, field, converter);
	}

}
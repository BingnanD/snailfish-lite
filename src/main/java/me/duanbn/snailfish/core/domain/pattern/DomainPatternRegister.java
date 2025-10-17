package me.duanbn.snailfish.core.domain.pattern;

import java.util.List;
import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import lombok.extern.slf4j.Slf4j;
import me.duanbn.snailfish.core.Bootstrap.BootstrapAttribute;
import me.duanbn.snailfish.core.RegisterI;
import me.duanbn.snailfish.core.domain.DomainBusException;
import me.duanbn.snailfish.core.domain.annotations.DomainFactory;
import me.duanbn.snailfish.core.domain.annotations.DomainRepository;
import me.duanbn.snailfish.core.domain.annotations.DomainService;
import me.duanbn.snailfish.util.ObjectUtil;
import me.duanbn.snailfish.util.collection.Lists;
import me.duanbn.snailfish.util.collection.Maps;
import me.duanbn.snailfish.util.lang.StringUtil;

/**
 * 
 * @author bingnan.dbn
 *
 */
@Slf4j
public class DomainPatternRegister implements RegisterI, ApplicationContextAware {

	private ApplicationContext appCtx;

	private Map<Class<?>, Map<String, DomainPatternI>> mapWithInter = Maps.newConcurrentMap();
	private Map<Class<?>, DomainPatternI> mapWithInstance = Maps.newConcurrentMap();

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.appCtx = applicationContext;
	}

	public DomainPatternI getDomainPattern(Class<?> clazz) {
		if (clazz.isInterface()) {
			Map<String, ? extends DomainPatternI> map2 = mapWithInter.get(clazz);
			if (map2.size() == 1) {
				return map2.values().iterator().next();
			}
			throw new DomainBusException(
					String.format("There are multiple implemented by given clazz: %s", clazz.getName()));
		} else {
			return this.mapWithInstance.get(clazz);
		}
	}

	public DomainPatternI getDomainPattern(Class<?> clazz, String value) {
		DomainPatternI domainPatternI = mapWithInter.get(clazz).get(value);
		if (domainPatternI == null) {
			throw new DomainBusException(
					String.format("not found implemented by given clazz: %s, name: %s", clazz.getName(), value));
		}
		return domainPatternI;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void doRegistration(Class<?> clazz) throws Exception {
		DomainPatternI domainPatternIns = (DomainPatternI) this.appCtx.getBean(clazz);
		this.mapWithInstance.put(clazz, domainPatternIns);

		BootstrapAttribute bootstrapAttr = this.appCtx.getBean(BootstrapAttribute.class);

		// handle domain factory
		if (DomainFactoryI.class.isAssignableFrom(clazz)) {
			DomainFactory annotation = clazz.getAnnotation(DomainFactory.class);
			if (annotation == null) {
				throw new DomainBusException(
						clazz.getSimpleName() + " implements DomainFactoryI should be annotationed by @DomainFactory");
			}

			if (bootstrapAttr.isEnableLog())
				log.info("register domain factory [{}] done", clazz.getSimpleName());
		}

		// handle domain repository
		if (DomainRepositoryI.class.isAssignableFrom(clazz)) {
			List<Class> domainPatternClasses = getDomainPatternInterfaces(clazz);

			for (Class class1 : domainPatternClasses) {
				if (this.mapWithInter.containsKey(class1)) {
					continue;
				}
				Map<String, DomainPatternI> map2 = this.appCtx.getBeansOfType(class1);
				Map<String, DomainPatternI> map3 = Maps.newHashMap();
				List<String> list = Lists.newArrayList();
				for (DomainPatternI domainPatternI : map2.values()) {
					DomainRepository annotation = domainPatternI.getClass().getAnnotation(DomainRepository.class);
					if (annotation == null) {
						throw new DomainBusException(clazz.getSimpleName()
								+ " implements DomainRepositoryI should be annotation by @DomainRepository");
					}

					String patternBeanName = StringUtil.isNotBlank(annotation.value()) ? annotation.value()
							: domainPatternI.getClass().getSimpleName();
					map3.put(patternBeanName, domainPatternI);
					list.add(domainPatternI.getClass().getSimpleName());
				}
				this.mapWithInter.put(class1, map3);

				if (bootstrapAttr.isEnableLog())
					log.info("register domain repository [{}] {} done", class1.getSimpleName(), list);
			}
		}

		// handle domain service.
		if (DomainServiceI.class.isAssignableFrom(clazz)) {
			List<Class> domainPatternClasses = getDomainPatternInterfaces(clazz);
			for (Class class1 : domainPatternClasses) {
				if (this.mapWithInter.containsKey(class1)) {
					continue;
				}
				Map<String, DomainPatternI> map2 = this.appCtx.getBeansOfType(class1);
				Map<String, DomainPatternI> map3 = Maps.newHashMap();
				List<String> list = Lists.newArrayList();
				for (DomainPatternI domainPatternI : map2.values()) {
					DomainService annotation = domainPatternI.getClass().getAnnotation(DomainService.class);
					if (annotation == null) {
						throw new DomainBusException(clazz.getSimpleName()
								+ " implements DomainServiceI should be annotationed by @DomainService");
					}

					String patternBeanName = StringUtil.isNotBlank(annotation.value()) ? annotation.value()
							: domainPatternI.getClass().getSimpleName();
					map3.put(patternBeanName, domainPatternI);
					list.add(domainPatternI.getClass().getSimpleName());
				}
				this.mapWithInter.put(class1, map3);

				if (bootstrapAttr.isEnableLog())
					log.info("register domain service [{}] {} done", class1.getSimpleName(), list);
			}
		}

	}

	@SuppressWarnings("rawtypes")
	private List<Class> getDomainPatternInterfaces(Class<?> domainPatternClazz) {
		List<Class> domainPatternList = Lists.newArrayList();

		Class[] interfaces = ObjectUtil.getInterfaces(domainPatternClazz, true);
		if (null == interfaces || 0 == interfaces.length) {
			throw new DomainBusException(
					String.format("There should be at leads one interface implemented by given clazz: %s",
							domainPatternClazz.getName()));
		}
		for (Class it : interfaces) {
			if (DomainRepositoryI.class.isAssignableFrom(it) || DomainServiceI.class.isAssignableFrom(it))
				domainPatternList.add(it);
		}

		return domainPatternList;
	}

}

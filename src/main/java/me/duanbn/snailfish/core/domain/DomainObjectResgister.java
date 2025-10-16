/**
 * 
 */
package me.duanbn.snailfish.core.domain;

import java.util.List;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import lombok.Getter;
import me.duanbn.snailfish.core.RegisterI;
import me.duanbn.snailfish.util.collection.Lists;

/**
 * @author bingnan.dbn
 *
 */
@SuppressWarnings({ "rawtypes", "unused" })
public class DomainObjectResgister implements RegisterI, ApplicationContextAware {

	private ApplicationContext appCtx;

	@Getter
	private List<Class> entityClasses = Lists.newArrayList();
	@Getter
	private List<Class> valueObjectClasses = Lists.newArrayList();

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.appCtx = applicationContext;
	}

	@Override
	public void doRegistration(Class<?> clazz) throws Exception {
		// handle domain entity.
		if (DomainE.class.isAssignableFrom(clazz)) {
			this.entityClasses.add(clazz);
		}

		// handle domain value object.
		if (DomainV.class.isAssignableFrom(clazz)) {
			this.valueObjectClasses.add(clazz);
		}
	}

}

package me.duanbn.snailfish.test.domain.factory;

import me.duanbn.snailfish.core.domain.annotations.DomainFactory;
import me.duanbn.snailfish.core.domain.pattern.DomainFactoryI;
import me.duanbn.snailfish.test.domain.beans.DomainEntityE;

@DomainFactory
public class DomainEntityFactory implements DomainFactoryI {

	public DomainEntityE create() {
		return new DomainEntityE();
	}

}

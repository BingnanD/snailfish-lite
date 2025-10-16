package me.duanbn.snailfish.test.domain.service;

import lombok.extern.slf4j.Slf4j;
import me.duanbn.snailfish.core.domain.annotations.DomainService;

@Slf4j
@DomainService("DomainEntityService2")
public class DomainEntityService2 implements DomainEntityServiceI {

	public void hello() {
		log.info("hello domain bus");
	}

}

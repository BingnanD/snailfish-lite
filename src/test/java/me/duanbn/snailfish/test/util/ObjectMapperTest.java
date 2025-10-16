package me.duanbn.snailfish.test.util;

import org.junit.jupiter.api.Test;

import com.alibaba.fastjson.JSON;

import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MappingContext;
import ma.glasnost.orika.converter.BidirectionalConverter;
import ma.glasnost.orika.metadata.Type;
import me.duanbn.snailfish.test.domain.beans.DomainEntityE;
import me.duanbn.snailfish.test.domain.beans.DomainEntityEDTO;
import me.duanbn.snailfish.test.domain.beans.DomainValueObjectVDTO;
import me.duanbn.snailfish.util.Mock;
import me.duanbn.snailfish.util.ObjectMapper;
import me.duanbn.snailfish.util.Random;

@Slf4j
public class ObjectMapperTest {

	@Test
	public void test() throws Exception {
		ObjectMapper.registerConverter(DomainEntityE.class, DomainEntityEDTO.class, "id", new IdConverter());
		ObjectMapper.registerConverter(DomainEntityE.class, DomainEntityEDTO.class, "entityId", new IdConverter());

		DomainEntityEDTO domainEntityDTO = Mock.mock(DomainEntityEDTO.class);
		domainEntityDTO.setUuid(null);
		domainEntityDTO.setEntityId(Random.snowFlakeId().toString());
		domainEntityDTO.setId(Random.snowFlakeId().toString());
		DomainEntityE domainEntityE = ObjectMapper.map(domainEntityDTO, DomainEntityE.class);
		log.info("{}", JSON.toJSONString(domainEntityE, true));
	}

	@Test
	public void testConverter() throws Exception {
		DomainValueObjectVDTO valueObject = Mock.mock(DomainValueObjectVDTO.class);
		valueObject.setId(Random.snowFlakeId().toString());

		DomainEntityEDTO domainEntityDTO = Mock.mock(DomainEntityEDTO.class);
		domainEntityDTO.setDomainEnum("A");
		domainEntityDTO.setEntityId(Random.snowFlakeId().toString());
		domainEntityDTO.setId(Random.snowFlakeId().toString());
		domainEntityDTO.setValueObject(valueObject);

		DomainEntityE domainEntityE = ObjectMapper.map(domainEntityDTO, DomainEntityE.class, new IdConverter());
		log.info("{}", JSON.toJSONString(domainEntityE, true));
	}

	private static class IdConverter extends BidirectionalConverter<Long, String> {
		@Override
		public String convertTo(Long source, Type<String> destinationType, MappingContext mappingContext) {
			return source.toString();
		}

		@Override
		public Long convertFrom(String source, Type<Long> destinationType, MappingContext mappingContext) {
			return Long.parseLong(source);
		}
	}

}

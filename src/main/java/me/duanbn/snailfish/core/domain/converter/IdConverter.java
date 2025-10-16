package me.duanbn.snailfish.core.domain.converter;

import ma.glasnost.orika.MappingContext;
import ma.glasnost.orika.converter.BidirectionalConverter;
import ma.glasnost.orika.metadata.Type;

public class IdConverter extends BidirectionalConverter<Long, String> {

    @Override
    public String convertTo(Long source, Type<String> destinationType, MappingContext mappingContext) {
        return source.toString();
    }

    @Override
    public Long convertFrom(String source, Type<Long> destinationType, MappingContext mappingContext) {
        return Long.parseLong(source);
    }

}

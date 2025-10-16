package me.duanbn.snailfish.core.domain.converter;

import ma.glasnost.orika.MappingContext;
import ma.glasnost.orika.converter.BidirectionalConverter;
import ma.glasnost.orika.metadata.Type;

import java.util.Date;

public class DateConverter extends BidirectionalConverter<Date, Long> {

    @Override
    public Long convertTo(Date source, Type<Long> destinationType, MappingContext mappingContext) {
        return source.getTime();
    }

    @Override
    public Date convertFrom(Long source, Type<Date> destinationType, MappingContext mappingContext) {
        return new Date(source);
    }

}

package io.microsamples.reactive.service.mappers;

import io.microsamples.reactive.service.protobuf.AltRecord;
import io.microsamples.reactive.service.protobuf.Record;
import org.mapstruct.CollectionMappingStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.factory.Mappers;

@Mapper(collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface RecordMapper {

    RecordMapper INSTANCE = Mappers.getMapper( RecordMapper.class );

    @Mapping(target = "allFields", ignore = true)
    AltRecord recordToAltRecord(Record car);
}

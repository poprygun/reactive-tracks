package io.microsamples.reactive.service.mappers;

import io.github.benas.randombeans.EnhancedRandomBuilder;
import io.github.benas.randombeans.api.EnhancedRandom;
import io.microsamples.reactive.service.protobuf.AltRecord;
import io.microsamples.reactive.service.protobuf.Record;
import org.junit.Ignore;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;


public class RecordMapperTest {

    private static EnhancedRandom enhancedRandom = EnhancedRandomBuilder.aNewEnhancedRandom();

    @Test
    @Ignore
    public void shouldConvertToAltRecord(){

        Record record = enhancedRandom.nextObject(Record.class);

        AltRecord altTrack = RecordMapper.INSTANCE.recordToAltRecord(record);

        assertThat(altTrack).isNotNull();
        assertThat(record).isEqualToComparingOnlyGivenFields(altTrack
        , "longitude", "latitude");
    }

}
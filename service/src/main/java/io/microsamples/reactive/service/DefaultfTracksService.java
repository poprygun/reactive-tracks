package io.microsamples.reactive.service;

import io.github.benas.randombeans.EnhancedRandomBuilder;
import io.github.benas.randombeans.FieldDefinitionBuilder;
import io.github.benas.randombeans.api.EnhancedRandom;
import io.microsamples.reactive.service.protobuf.AltRecord;
import io.microsamples.reactive.service.protobuf.Record;
import io.microsamples.reactive.service.protobuf.RecordsRequest;
import io.microsamples.reactive.service.protobuf.TracksService;
import io.microsamples.reactive.service.source.RecordIdRandomizer;
import io.microsamples.reactive.service.source.TrackPublisher;
import io.netty.buffer.ByteBuf;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Random;

@Component
@Slf4j
public class DefaultfTracksService implements TracksService {

    private TrackPublisher recordPublisher;
    private TrackPublisher altRecordPublisher;
    private static EnhancedRandom enhancedRandom;
    private RecordIdRandomizer randomizer = new RecordIdRandomizer();


    public DefaultfTracksService() {

        enhancedRandom = EnhancedRandomBuilder.aNewEnhancedRandomBuilder()
                .randomize(FieldDefinitionBuilder.field().named("id")
                        .ofType(String.class)
                        .inClass(Record.class).get(), randomizer)
                .build();

        initRecordPublisher();
        initAltRecordPublisher();
    }

    private void initRecordPublisher() {
        recordPublisher = new TrackPublisher<>(() -> {

            Record record = enhancedRandom.nextObject(Record.class);
            return record.toBuilder().setId(randomizer.getRandomValue()).build();

        }, () -> new Random().nextInt(3)
                , Duration.ofNanos(1000000));
    }

    private void initAltRecordPublisher() {
        altRecordPublisher = new TrackPublisher<>(() -> {

            AltRecord record = enhancedRandom.nextObject(AltRecord.class);
            return record.toBuilder().setId(randomizer.getRandomValue()).build();

        }, () -> new Random().nextInt(5)
                , Duration.ofSeconds(1));
    }

    @Override
    public Mono<Record> record(RecordsRequest message, ByteBuf metadata) {
        return Mono.from(recordPublisher.getPublisher().limitRequest(1));
    }

    @Override
    public Flux<Record> tracks(RecordsRequest message, ByteBuf metadata) {
        return recordPublisher.getPublisher().limitRequest(message.getMaxResults());
    }

    @Override
    public Flux<AltRecord> altTracks(RecordsRequest message, ByteBuf metadata) {
        return altRecordPublisher.getPublisher().limitRequest(message.getMaxResults());
    }
    //todo example of implementing delay
//  @Override
//  public Flux<Record> tracks(RecordsRequest message, ByteBuf metadata) {
//    Flux<Long> interval = Flux.interval(Duration.ofMillis(10));
//
////    Stream<Record> recordStream = randomStreamOf(message.getMaxResults(), Record.class);
////
////    Flux<Record> records = Flux.fromStream(recordStream);
//
//    RecordPublisher recordPublisher = new RecordPublisher();
//
//
//    return Flux.zip(interval, recordPublisher.getPublisher().limitRequest(message.getMaxResults())).map(Tuple2::getT2);
//  }
}

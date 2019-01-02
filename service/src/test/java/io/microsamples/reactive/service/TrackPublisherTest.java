package io.microsamples.reactive.service;

import io.github.benas.randombeans.EnhancedRandomBuilder;
import io.github.benas.randombeans.FieldDefinitionBuilder;
import io.github.benas.randombeans.api.EnhancedRandom;
import io.microsamples.reactive.service.protobuf.Record;
import io.microsamples.reactive.service.source.RecordIdRandomizer;
import io.microsamples.reactive.service.source.TrackPublisher;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.util.Random;


@Slf4j
public class TrackPublisherTest {
    private static EnhancedRandom enhancedRandom;

    private TrackPublisher<Record> trackPublisher;

    @Before
    public void initPublisher() {

        RecordIdRandomizer randomizer = new RecordIdRandomizer();
        enhancedRandom = EnhancedRandomBuilder.aNewEnhancedRandomBuilder()
                .randomize(FieldDefinitionBuilder.field().named("id")
                                .ofType(String.class)
                                .inClass(Record.class).get(), randomizer)
                .build();

        trackPublisher = new TrackPublisher<>(() -> {

            Record record = enhancedRandom.nextObject(Record.class);
            return record.toBuilder().setId(randomizer.getRandomValue()).build();

        }, () -> new Random().nextInt(3)
        , Duration.ofNanos(1000000));
    }


    @Test
    @Ignore
    public void shouldKeepEmitting() throws InterruptedException {

        trackPublisher.getPublisher()
                .subscribe(r -> log.info("--> {}", r));

        Thread.currentThread().join();
    }

    @Test
    public void shouldEmitSpecifiedNumberOfTracks() throws InterruptedException {
        int expectSome = 13;

        StepVerifier.create(trackPublisher.getPublisher()
//                .log()
        .limitRequest(expectSome))
                .expectSubscription()
                .expectNextCount(expectSome)
                .verifyComplete();
    }

}

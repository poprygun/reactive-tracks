package io.microsamples.reactive.service;

import io.microsamples.reactive.service.mappers.RecordMapper;
import io.microsamples.reactive.service.protobuf.AltRecord;
import io.microsamples.reactive.service.protobuf.Record;
import io.microsamples.reactive.service.protobuf.RecordsRequest;
import lombok.extern.slf4j.Slf4j;
import org.junit.Ignore;
import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@Slf4j
public class DefaultfTracksServiceTest {

    private DefaultfTracksService defaultfTracksService = new DefaultfTracksService();


    @Test
    @Ignore
    public void shouldAddAltitude2() throws InterruptedException {
        Flux<? extends AltRecord> altTracks = defaultfTracksService.altTracks(requestWith(13), null);

        Flux<? extends Record> tracks = defaultfTracksService.tracks(requestWith(13), null);



        tracks.filter(t -> t.getId().equalsIgnoreCase("f641bc00-f1c7-44e7-8949-7fc9feb2e4f7"))
                .subscribe(System.out::println);
//
//        Flux.zip(altTracks, tracks, (record, altRecord) -> altRecord.toString().concat( "--")
//        .concat(record.toString()))
//                .subscribe(System.out::println);
        Thread.currentThread().join();


    }

    @Test
    @Ignore
    public void shouldAddAltitude() throws InterruptedException {
        Flux<? extends Record> tracks = defaultfTracksService.tracks(requestWith(13), null);

        tracks
                .map(r -> RecordMapper.INSTANCE.recordToAltRecord(r))
        .subscribe(altRecord -> System.out.println(altRecord.getClass()));
        Thread.currentThread().join();


    }

    @Test
    public void shouldEmitOne() {
        Mono<Record> record
                = defaultfTracksService.record(requestWith(1), null);

        StepVerifier.create(record)
                .expectNextMatches(r -> r.getId() != null
                        && 0 != r.getLatitude()
                        && 0 != r.getLongitude()
                )
                .verifyComplete();

    }

    @Test
    public void shouldEmitMultiple() {
        final int noOfRecords = 13;
        StepVerifier.create(defaultfTracksService.tracks(requestWith(noOfRecords), null))
                .expectNextCount(noOfRecords)
                .expectComplete()
                .verify();
    }

    @Test
    public void recordsToBlocking() {
        Iterable<Record> records = defaultfTracksService.tracks(requestWith(13), null).toIterable();
        records.forEach(record -> log.info("Record from flux {}", record));
    }

    private RecordsRequest requestWith(int recordsRequested) {
        return RecordsRequest.newBuilder().setMaxResults(recordsRequested).build();
    }
}
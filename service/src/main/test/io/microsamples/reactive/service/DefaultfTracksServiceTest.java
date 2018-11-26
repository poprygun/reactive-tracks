package io.microsamples.reactive.service;

import io.microsamples.reactive.service.protobuf.Record;
import io.microsamples.reactive.service.protobuf.RecordsRequest;
import io.microsamples.reactive.service.protobuf.TracksServiceClient;
import io.netifi.proteus.spring.core.annotation.Group;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@Slf4j
@SpringBootTest
@RunWith(SpringRunner.class)
public class DefaultfTracksServiceTest {

    @Group("quickstart.services.helloservices")
    private TracksServiceClient defaultfTracksService;

    @Test
    public void record() {
        Mono<Record> record
                = defaultfTracksService.record(requestWith(1));
        log.info("Received response {}", record.block());
    }

    @Test
    public void records() {
        final int noOfRecords = 13;
        StepVerifier.create(defaultfTracksService.tracks(requestWith(noOfRecords)))
                .expectNextCount(noOfRecords)
                .expectComplete()
                .verify();
    }

    @Test
    public void recordsToBlocking() {
        Iterable<Record> records = defaultfTracksService.tracks(requestWith(13)).toIterable();
        records.forEach(record -> log.info("Record from flux {}", record));
    }

    private RecordsRequest requestWith(int recordsRequested) {
        return RecordsRequest.newBuilder().setMaxResults(recordsRequested).build();
    }
}
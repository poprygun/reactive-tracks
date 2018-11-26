package io.microsmples.reactive.client;

import io.microsamples.reactive.service.protobuf.Record;
import io.microsamples.reactive.service.protobuf.RecordsRequest;
import io.microsamples.reactive.service.protobuf.TracksServiceClient;
import io.netifi.proteus.spring.core.annotation.Group;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;


@Component
public class ClientRunner implements CommandLineRunner {
    private static final Logger logger = LogManager.getLogger(ClientRunner.class);

    @Group("quickstart.services.helloservices")
    private TracksServiceClient client;

    private int counter = 1;

    @Override
    public void run(String... args) throws Exception {
        RecordsRequest request = RecordsRequest.newBuilder().setMaxResults(213).build();

        client.tracks(request).subscribe(record -> processed(record));

        Thread.currentThread().join();
    }

    private void processed(Record record) {
        counter = counter + 1;
        logger.info("Processing record {}", counter);
    }

    private void runMono(RecordsRequest request) {
        logger.info("Sending request to Tracks Service {}", request);

        Mono<Record> record = client.record(request);

        logger.info("Received response {}", record.block());
    }
}

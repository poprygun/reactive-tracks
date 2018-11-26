package io.microsamples.reactive.service;

import io.github.benas.randombeans.EnhancedRandomBuilder;
import io.microsamples.reactive.service.protobuf.Record;
import io.microsamples.reactive.service.protobuf.RecordsRequest;
import io.microsamples.reactive.service.protobuf.TracksService;
import io.netty.buffer.ByteBuf;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static io.github.benas.randombeans.api.EnhancedRandom.randomStreamOf;

@Component
@Slf4j
public class DefaultfTracksService implements TracksService {

  @Override
  public Mono<Record> record(RecordsRequest message, ByteBuf metadata) {
    Record record = EnhancedRandomBuilder.aNewEnhancedRandom().nextObject(Record.class);
    return Mono.just(record);
  }

  @Override
  public Flux<Record> tracks(RecordsRequest message, ByteBuf metadata) {
    Flux<Long> interval = Flux.interval(Duration.ofMillis(1000));

    Stream<Record> recordStream = randomStreamOf(message.getMaxResults(), Record.class);

    Flux<Record> records = Flux.fromStream(recordStream);
    return Flux.zip(interval, records).map(Tuple2::getT2);
  }
}

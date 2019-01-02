package io.microsamples.reactive.service.source;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.observables.ConnectableObservable;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.IntSupplier;
import java.util.function.Supplier;

@Slf4j
public class TrackPublisher<T> {

    private final Flux<T> publisher;
    private Supplier<T> bitSupplier;
    private IntSupplier chunkSizeRandomizer;


    /**
     * @param bitsGenerator supplier of object to be emitted
     * @param chunkSizeRandomizer defines a size of chunks of objects to be emitted at once.  It is zero based.
     * @param emitInterval emit every specified interval - should be longer than 1 millisecond
     */
    public TrackPublisher(Supplier<T> bitsGenerator
            , IntSupplier chunkSizeRandomizer
            , Duration emitInterval) {

        if (1000000 > emitInterval.toNanos())
            throw new ExceptionInInitializerError("Interval must be longer than 1 millisecond.");

        this.bitSupplier = bitsGenerator;
        this.chunkSizeRandomizer = chunkSizeRandomizer;

        Observable<T> RecordObservable = Observable.create(emitter -> {

            ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);
            executorService.scheduleAtFixedRate(chunkOfBits(emitter)
                    , 0, emitInterval.toMillis(), TimeUnit.MILLISECONDS);

        });

        ConnectableObservable<T> connectableObservable = RecordObservable.share().publish();
        connectableObservable.connect();

        publisher = Flux.from(connectableObservable.toFlowable(BackpressureStrategy.BUFFER));
    }

    private Runnable chunkOfBits(ObservableEmitter<T> emitter) {
        return () -> {
            List<T> bits = getUpdates(randomChunkSize());
            if (bits != null) {
                emitBits(emitter, bits);
            }
        };
    }

    private int randomChunkSize() {
        return chunkSizeRandomizer.getAsInt();
    }

    private void emitBits(ObservableEmitter<T> emitter, List<T> bits) {
        for (T bit : bits) {
            try {
                emitter.onNext(bit);
            } catch (RuntimeException e) {
                log.error("Cannot send Record", e);
            }
        }
    }

    /**
     * Generate batch of random objects
     * @param number size of list to generate
     * @return list of random objects
     */

    private List<T> getUpdates(int number) {
        List<T> updates = new ArrayList<>();
        for (int i = 0; i < number; i++) {
            updates.add(bitSupplier.get());
        }
        return updates;
    }


    public Flux<T> getPublisher() {
        return publisher;
    }
}

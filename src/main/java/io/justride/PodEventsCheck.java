package io.justride;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.*;
import org.apache.kafka.streams.state.KeyValueIterator;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyWindowStore;
import org.apache.kafka.streams.state.WindowStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.binder.kafka.streams.InteractiveQueryService;
import org.springframework.kafka.support.serializer.JsonSerde;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.Duration;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@SpringBootApplication
public class PodEventsCheck {


    @EnableBinding(JustRideKStreamBinding.class)
    @EnableScheduling
    public static class PodEventsProcessor {

        @Autowired
        private InteractiveQueryService iqs;

        static final String INPUT_TOPIC = "pods-in";
        static final String OUTPUT_TOPIC = "violations-out";
        static final int WINDOW_SIZE_MS = 10000;
        static final long SPEED_THRESHOLD = 70L;
        static final String WINDOW_STORE = "pod-events-snapshots";

        private final Log logger = LogFactory.getLog(getClass());

        @StreamListener(INPUT_TOPIC)
        @SendTo(OUTPUT_TOPIC)
        public KStream<?, EnrichedFlaggedViolationEvent> process(KStream<Object, PodEvent> input) {
            ObjectMapper podEventMapper = new ObjectMapper();
            Serde<PodEvent> podEventSerde = new JsonSerde<>(PodEvent.class, podEventMapper);

            ObjectMapper flaggedViolationEventMapper = new ObjectMapper();
            Serde<FlaggedViolationEvent> flaggedViolationEventSerde =
                    new JsonSerde<>(FlaggedViolationEvent.class, flaggedViolationEventMapper);

            KTable<Windowed<String>, FlaggedViolationEvent> aggregateTable = input
                    .filter((key, value) -> value.getSpeed() > SPEED_THRESHOLD)
                    .groupBy((key, value) -> value.getUuid(),
                            Serialized.with(Serdes.String(), podEventSerde))
                    .windowedBy(TimeWindows.of(WINDOW_SIZE_MS))
                    .aggregate(FlaggedViolationEvent::new,
                            (key, pe, fve) -> fve.addPodEvent(pe),
                            Materialized
                                    .<String, FlaggedViolationEvent, WindowStore<Bytes, byte[]>
                                            >as(WINDOW_STORE)
                                    .withKeySerde(Serdes.String())
                                    .withValueSerde(flaggedViolationEventSerde)

                    );
            KStream<Windowed<String>, FlaggedViolationEvent> fveStream = aggregateTable.toStream();
            fveStream.foreach((key, value) -> System.out.println("fveStream - key =" + key.toString() + ", value = " + value.toString()));

            return fveStream.map((k, v) ->
                    new KeyValue(null, new EnrichedFlaggedViolationEvent(k.key(), k.window().start(), k.window().end(), v.getCount(), v.getLastLatitude(),
                            v.getLastLongitude(), v.getLastSpeed(), v.getMaxSpeed(), v.getUuid(), v.getViolationTime())));

        }

        // Scheduled Query on the WindowStore to find out FlaggedViolationEvents
        @Scheduled(fixedRate = 30000, initialDelay = 5000)
        public void printProductCounts() {

            ReadOnlyWindowStore violationsStore = iqs.getQueryableStore(WINDOW_STORE, QueryableStoreTypes.windowStore());

            KeyValueIterator all = violationsStore.all();

            all.forEachRemaining(o -> {
                KeyValue kv = (KeyValue) o;
                System.out.println("From key watch: " + ((Windowed) kv.key).window());
                System.out.println("From value watch: " + kv.value);
            });
        }

        /*
        // Working code with Windowing but without return OUTPUT_TOPIC
        @StreamListener(INPUT_TOPIC)
        //@SendTo(OUTPUT_TOPIC)
        public void process(KStream<Object, PodEvent> input) {
            ObjectMapper podEventMapper = new ObjectMapper();
            Serde<PodEvent> podEventSerde = new JsonSerde<>(PodEvent.class, podEventMapper );

            ObjectMapper flaggedViolationEventMapper = new ObjectMapper();
            Serde<FlaggedViolationEvent> flaggedViolationEventSerde =
                    new JsonSerde<>(FlaggedViolationEvent.class, flaggedViolationEventMapper);

            KTable<Windowed<String>, FlaggedViolationEvent> aggregateTable = input
                    .filter((key, value) -> value.getSpeed() > 70)
                    .groupBy((key, value) -> value.getUuid(),
                            Serialized.with(Serdes.String(), podEventSerde))
                    .windowedBy(TimeWindows.of(TimeUnit.SECONDS.toMillis(WINDOW_SIZE_MS)))
                    .aggregate(FlaggedViolationEvent::new,
                            (key, pe, fve) -> fve.addPodEvent(pe),
                            Materialized
                                    .<String, FlaggedViolationEvent, WindowStore<Bytes, byte[]>
                                            >as("pod-events-snapshots")
                                    .withKeySerde(Serdes.String())
                                    .withValueSerde(flaggedViolationEventSerde)

                    );
            KStream<Windowed<String>, FlaggedViolationEvent> fveStream = aggregateTable.toStream();
            fveStream.foreach((key, value) -> System.out.println("fveStream - " + value.toString()));
        }
        */

        /*
        // Working code without windowing
        public KStream<String, FlaggedViolationEvent> process(KStream<Object, PodEvent> input) {
            ObjectMapper podEventMapper = new ObjectMapper();
            Serde<PodEvent> podEventSerde = new JsonSerde<>(PodEvent.class, podEventMapper );

            ObjectMapper flaggedViolationEventMapper = new ObjectMapper();
            Serde<FlaggedViolationEvent> flaggedViolationEventSerde =
                    new JsonSerde<>(FlaggedViolationEvent.class, flaggedViolationEventMapper);

            KTable<String, FlaggedViolationEvent> aggregateTable = input
                    .filter((key, value) -> value.getSpeed() > 70)
                    .groupBy((key, value) -> value.getUuid(),
                            Serialized.with(Serdes.String(), podEventSerde))
                    .aggregate(FlaggedViolationEvent::new,
                            (key, pe, fve) -> fve.addPodEvent(pe),
                            Materialized.<String, FlaggedViolationEvent, KeyValueStore<Bytes, byte[]>>as("test-events-snapshots")
                                    .withKeySerde(Serdes.String())
                                    .withValueSerde(flaggedViolationEventSerde)

                    );
            KStream<String, FlaggedViolationEvent> fveStream = aggregateTable.toStream();
            return fveStream;
        }
        */
    }
}





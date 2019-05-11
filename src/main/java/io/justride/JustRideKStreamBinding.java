package io.justride;

import org.apache.kafka.streams.kstream.KStream;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;

interface JustRideKStreamBinding {

    @Input("pods-in")
    KStream<?, ?> podsIn();


    @Output("violations-out")
    KStream<?, ?> violationsOut();

    /*
    @Input("violations")
    KStream<?, ?> violationsIn();
    */
}

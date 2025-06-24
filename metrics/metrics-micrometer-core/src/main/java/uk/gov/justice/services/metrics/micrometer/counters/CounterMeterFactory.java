package uk.gov.justice.services.metrics.micrometer.counters;

import static uk.gov.justice.services.metrics.micrometer.meters.MetricsMeterNames.EVENTS_FAILED_COUNTER_NAME;
import static uk.gov.justice.services.metrics.micrometer.meters.MetricsMeterNames.EVENTS_IGNORED_COUNTER_NAME;
import static uk.gov.justice.services.metrics.micrometer.meters.MetricsMeterNames.EVENTS_PROCESSED_COUNTER_NAME;
import static uk.gov.justice.services.metrics.micrometer.meters.MetricsMeterNames.EVENTS_RECEIVED_COUNTER_NAME;
import static uk.gov.justice.services.metrics.micrometer.meters.MetricsMeterNames.EVENTS_SUCCEEDED_COUNTER_NAME;

import uk.gov.justice.services.metrics.micrometer.meters.MetricsMeter;
import uk.gov.justice.services.metrics.micrometer.meters.SourceComponentPair;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class CounterMeterFactory {

    private static final List<CounterMeterConfig> COUNTER_METER_CONFIGS = Arrays.asList(
            new CounterMeterConfig(EVENTS_FAILED_COUNTER_NAME, "Count of all events that failed when handled by the Event Handlers"),
            new CounterMeterConfig(EVENTS_IGNORED_COUNTER_NAME, "Count of all events ignored by the Subscription Event Processor"),
            new CounterMeterConfig(EVENTS_PROCESSED_COUNTER_NAME, "Count of all events passed to the Subscription Event Processor"),
            new CounterMeterConfig(EVENTS_RECEIVED_COUNTER_NAME, "Count of events received via the Event Topic"),
            new CounterMeterConfig(EVENTS_SUCCEEDED_COUNTER_NAME, "Count of all events successfully handled by the Event Handlers")
    );

    private Stream<MetricsMeter> createAllCounterMetersForSourceAndComponent(
            SourceComponentPair sourceComponentPair) {
        return COUNTER_METER_CONFIGS.stream()
                .map(config -> new CounterMeter(
                        sourceComponentPair,
                        config.metricName(),
                        config.metricDescription()
                ));
    }

    public List<MetricsMeter> createAllCounterMetersForSourceAndComponents(List<SourceComponentPair> sourceComponentPairs) {
        return sourceComponentPairs.stream()
                .flatMap(this::createAllCounterMetersForSourceAndComponent)
                .toList();
    }

    private record CounterMeter(
            SourceComponentPair sourceComponentPair,
            String metricName,
            String metricDescription) implements MetricsMeter {
    }

    private record CounterMeterConfig(String metricName, String metricDescription) {
    }
}

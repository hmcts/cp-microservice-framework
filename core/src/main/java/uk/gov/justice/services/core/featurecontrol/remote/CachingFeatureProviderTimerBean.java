package uk.gov.justice.services.core.featurecontrol.remote;

import static java.lang.String.format;
import static java.util.Optional.ofNullable;

import uk.gov.justice.services.core.featurecontrol.domain.Feature;
import uk.gov.justice.services.ejb.timer.TimerServiceManager;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.Timeout;
import javax.ejb.TimerService;
import javax.inject.Inject;

import org.slf4j.Logger;

@Singleton
@Startup
public class CachingFeatureProviderTimerBean {

    private static final String TIMER_JOB_NAME = "framework.feature-store-refresh.job";

    private AtomicReference<Map<String, Feature>> atomicFeatureMapReference = new AtomicReference<>(new HashMap<>());

    @Inject
    private FeatureFetcherFacade featureFetcherFacade;

    @Inject
    private TimerServiceManager timerServiceManager;

    @Resource
    private TimerService timerService;

    @Inject
    private FeatureControlConfiguration featureControlConfiguration;

    @Inject
    private Logger logger;

    @PostConstruct
    public void startTimerService() {

        timerServiceManager.createIntervalTimer(
                TIMER_JOB_NAME,
                featureControlConfiguration.getTimerStartWaitMilliseconds(),
                featureControlConfiguration.getTimerIntervalMilliseconds(),
                timerService);
    }

    @Timeout
    public void reloadFeaturesOnTimeout() {
        reloadFeatures();
    }

    /**
     * Replaces the cached features with a fresh fetch from the remote feature store. If the fetch
     * fails, the previously cached features are kept: replacing them with an empty map would
     * silently disable every feature until the next successful refresh.
     */
    public void reloadFeatures() {
        try {
            final Map<String, Feature> featureMap = new HashMap<>();

            featureFetcherFacade
                    .fetchFeatures()
                    .forEach(feature -> featureMap.put(feature.getFeatureName(), feature));

            atomicFeatureMapReference.set(featureMap);
        } catch (final RuntimeException e) {
            logger.error(format("Failed to refresh Features from remote feature store. Keeping the %d previously cached Features", atomicFeatureMapReference.get().size()), e);
        }
    }

    public Optional<Feature> lookup(final String featureName) {
        return ofNullable(atomicFeatureMapReference.get().get(featureName));
    }
}

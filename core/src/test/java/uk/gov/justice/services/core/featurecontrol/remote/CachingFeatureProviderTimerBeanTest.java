package uk.gov.justice.services.core.featurecontrol.remote;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import uk.gov.justice.services.core.featurecontrol.domain.Feature;
import uk.gov.justice.services.ejb.timer.TimerServiceManager;

import javax.ejb.TimerService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;

@ExtendWith(MockitoExtension.class)
public class CachingFeatureProviderTimerBeanTest {

    @Mock
    private FeatureFetcherFacade featureFetcherFacade;

    @Mock
    private TimerServiceManager timerServiceManager;

    @Mock
    private TimerService timerService;

    @Mock
    private FeatureControlConfiguration featureControlConfiguration;

    @Mock
    private Logger logger;

    @InjectMocks
    private CachingFeatureProviderTimerBean cachingFeatureProviderTimerBean;

    @Test
    public void shouldStartTheTimerServiceOnStartup() throws Exception {

        final long timerStartWaitMilliseconds = 934L;
        final long timerIntervalMilliseconds = 987234L;

        when(featureControlConfiguration.getTimerStartWaitMilliseconds()).thenReturn(timerStartWaitMilliseconds);
        when(featureControlConfiguration.getTimerIntervalMilliseconds()).thenReturn(timerIntervalMilliseconds);

        cachingFeatureProviderTimerBean.startTimerService();

        verify(timerServiceManager).createIntervalTimer(
                "framework.feature-store-refresh.job",
                timerStartWaitMilliseconds,
                timerIntervalMilliseconds,
                timerService);
    }

    @Test
    public void shouldFetchFeaturesAndStore() throws Exception {

        final Feature feature_1 = new Feature("some-feature-1", true);
        final Feature feature_2 = new Feature("some-feature-2", true);
        final Feature feature_3 = new Feature("some-feature-3", true);

        assertThat(cachingFeatureProviderTimerBean.lookup(feature_1.getFeatureName()).isPresent(), is(false));
        assertThat(cachingFeatureProviderTimerBean.lookup(feature_2.getFeatureName()).isPresent(), is(false));
        assertThat(cachingFeatureProviderTimerBean.lookup(feature_3.getFeatureName()).isPresent(), is(false));

        when(featureFetcherFacade.fetchFeatures()).thenReturn(asList(feature_1, feature_2, feature_3));
        cachingFeatureProviderTimerBean.reloadFeatures();

        final Feature foundFeature_1 = cachingFeatureProviderTimerBean.lookup(feature_1.getFeatureName())
                .orElseThrow(AssertionError::new);
        final Feature foundFeature_2 = cachingFeatureProviderTimerBean.lookup(feature_2.getFeatureName())
                .orElseThrow(AssertionError::new);
        final Feature foundFeature_3 = cachingFeatureProviderTimerBean.lookup(feature_3.getFeatureName())
                .orElseThrow(AssertionError::new);

        assertThat(foundFeature_1, is(feature_1));
        assertThat(foundFeature_2, is(feature_2));
        assertThat(foundFeature_3, is(feature_3));
    }

    @Test
    public void shouldFetchFeaturesAndStoreOnTimeout() throws Exception {

        final Feature feature_1 = new Feature("some-feature-1", true);
        final Feature feature_2 = new Feature("some-feature-2", true);
        final Feature feature_3 = new Feature("some-feature-3", true);

        assertThat(cachingFeatureProviderTimerBean.lookup(feature_1.getFeatureName()).isPresent(), is(false));
        assertThat(cachingFeatureProviderTimerBean.lookup(feature_2.getFeatureName()).isPresent(), is(false));
        assertThat(cachingFeatureProviderTimerBean.lookup(feature_3.getFeatureName()).isPresent(), is(false));

        when(featureFetcherFacade.fetchFeatures()).thenReturn(asList(feature_1, feature_2, feature_3));
        cachingFeatureProviderTimerBean.reloadFeaturesOnTimeout();

        final Feature foundFeature_1 = cachingFeatureProviderTimerBean.lookup(feature_1.getFeatureName())
                .orElseThrow(AssertionError::new);
        final Feature foundFeature_2 = cachingFeatureProviderTimerBean.lookup(feature_2.getFeatureName())
                .orElseThrow(AssertionError::new);
        final Feature foundFeature_3 = cachingFeatureProviderTimerBean.lookup(feature_3.getFeatureName())
                .orElseThrow(AssertionError::new);

        assertThat(foundFeature_1, is(feature_1));
        assertThat(foundFeature_2, is(feature_2));
        assertThat(foundFeature_3, is(feature_3));
    }

    @Test
    public void shouldKeepThePreviouslyCachedFeaturesAndLogAnErrorIfFetchingFeaturesFails() throws Exception {

        final Feature feature_1 = new Feature("some-feature-1", true);
        final Feature feature_2 = new Feature("some-feature-2", false);
        final RuntimeException fetchFailure = new RuntimeException("Ooops");

        when(featureFetcherFacade.fetchFeatures())
                .thenReturn(asList(feature_1, feature_2))
                .thenThrow(fetchFailure);

        cachingFeatureProviderTimerBean.reloadFeatures();
        cachingFeatureProviderTimerBean.reloadFeatures();

        final Feature foundFeature_1 = cachingFeatureProviderTimerBean.lookup(feature_1.getFeatureName())
                .orElseThrow(AssertionError::new);
        final Feature foundFeature_2 = cachingFeatureProviderTimerBean.lookup(feature_2.getFeatureName())
                .orElseThrow(AssertionError::new);

        assertThat(foundFeature_1, is(feature_1));
        assertThat(foundFeature_2, is(feature_2));

        verify(logger).error("Failed to refresh Features from remote feature store. Keeping the 2 previously cached Features", fetchFailure);
    }

    @Test
    public void shouldReplaceTheCachedFeaturesOnTheNextSuccessfulFetchAfterAFailure() throws Exception {

        final Feature oldFeature = new Feature("some-old-feature", true);
        final Feature newFeature = new Feature("some-new-feature", true);

        when(featureFetcherFacade.fetchFeatures())
                .thenReturn(singletonList(oldFeature))
                .thenThrow(new RuntimeException("Ooops"))
                .thenReturn(singletonList(newFeature));

        cachingFeatureProviderTimerBean.reloadFeatures();
        cachingFeatureProviderTimerBean.reloadFeatures();
        cachingFeatureProviderTimerBean.reloadFeatures();

        final Feature foundNewFeature = cachingFeatureProviderTimerBean.lookup(newFeature.getFeatureName())
                .orElseThrow(AssertionError::new);

        assertThat(foundNewFeature, is(newFeature));
        assertThat(cachingFeatureProviderTimerBean.lookup(oldFeature.getFeatureName()).isPresent(), is(false));
    }
}
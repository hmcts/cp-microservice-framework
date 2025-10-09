package uk.gov.justice.services.core.dispatcher;

import static co.unruly.matchers.OptionalMatchers.contains;
import static java.util.UUID.randomUUID;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import static uk.gov.justice.services.messaging.JsonEnvelope.envelopeFrom;
import static uk.gov.justice.services.messaging.JsonEnvelope.metadataBuilder;
import static uk.gov.justice.services.messaging.JsonObjects.jsonBuilderFactory;

import uk.gov.justice.services.messaging.JsonEnvelope;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


@ExtendWith(MockitoExtension.class)
public class SystemUserUtilTest {

    @Mock
    private SystemUserProvider systemUserProvider;

    @InjectMocks
    private SystemUserUtil systemUserUtil;

    @Test
    public void shouldReturnEnvelopeWithSystemUserId() throws Exception {

        final UUID sysUserId = randomUUID();
        when(systemUserProvider.getContextSystemUserId()).thenReturn(Optional.of(sysUserId));

        final UUID id = randomUUID();
        final String name = "name456";

        final JsonEnvelope returnedEnvelope = systemUserUtil.asEnvelopeWithSystemUserId(
                envelopeFrom(metadataBuilder()
                                .withId(id)
                                .withName(name)
                                .withUserId("someIdToBeReplaced"),
                        jsonBuilderFactory.createObjectBuilder().add("numName", BigDecimal.valueOf(123)))
        );

        assertThat(returnedEnvelope.metadata().id(), is(id));
        assertThat(returnedEnvelope.metadata().name(), is(name));
        assertThat(returnedEnvelope.metadata().userId(), contains(sysUserId.toString()));
        assertThat(returnedEnvelope.payloadAsJsonObject().getInt("numName"), is(123));
    }

    @Test
    public void shouldThrowExceptionIfSystemUserNotFound() throws Exception {
        when(systemUserProvider.getContextSystemUserId()).thenReturn(Optional.empty());

        assertThrows(IllegalStateException.class, () -> systemUserUtil.asEnvelopeWithSystemUserId(
                envelopeFrom(metadataBuilder()
                        .withId(randomUUID())
                        .withName("name"),
                        jsonBuilderFactory.createObjectBuilder())));
    }
}
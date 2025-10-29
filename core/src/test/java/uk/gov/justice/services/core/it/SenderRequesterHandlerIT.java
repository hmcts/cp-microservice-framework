package uk.gov.justice.services.core.it;

import org.apache.openejb.jee.Application;
import org.apache.openejb.jee.WebApp;
import org.apache.openejb.junit5.RunWithApplicationComposer;
import org.apache.openejb.testing.Classes;
import org.apache.openejb.testing.Module;
import org.junit.jupiter.api.Test;
import uk.gov.justice.schema.service.CatalogProducer;
import uk.gov.justice.schema.service.SchemaCatalogResolverProducer;
import uk.gov.justice.schema.service.SchemaCatalogService;
import uk.gov.justice.services.cdi.LoggerProducer;
import uk.gov.justice.services.common.annotation.ComponentNameExtractor;
import uk.gov.justice.services.common.configuration.GlobalValueProducer;
import uk.gov.justice.services.common.converter.ObjectToJsonValueConverter;
import uk.gov.justice.services.common.converter.StringToJsonObjectConverter;
import uk.gov.justice.services.common.converter.jackson.ObjectMapperProducer;
import uk.gov.justice.services.common.util.UtcClock;
import uk.gov.justice.services.core.accesscontrol.AccessControlFailureMessageGenerator;
import uk.gov.justice.services.core.accesscontrol.AllowAllPolicyEvaluator;
import uk.gov.justice.services.core.accesscontrol.DefaultAccessControlService;
import uk.gov.justice.services.core.accesscontrol.PolicyEvaluator;
import uk.gov.justice.services.core.annotation.FrameworkComponent;
import uk.gov.justice.services.core.annotation.Handles;
import uk.gov.justice.services.core.annotation.Remote;
import uk.gov.justice.services.core.annotation.ServiceComponent;
import uk.gov.justice.services.core.dispatcher.DispatcherCache;
import uk.gov.justice.services.core.dispatcher.DispatcherConfiguration;
import uk.gov.justice.services.core.dispatcher.DispatcherFactory;
import uk.gov.justice.services.core.dispatcher.EmptySystemUserProvider;
import uk.gov.justice.services.core.dispatcher.EnvelopePayloadTypeConverter;
import uk.gov.justice.services.core.dispatcher.JsonEnvelopeRepacker;
import uk.gov.justice.services.core.dispatcher.ServiceComponentObserver;
import uk.gov.justice.services.core.dispatcher.SystemUserUtil;
import uk.gov.justice.services.core.envelope.EnvelopeInspector;
import uk.gov.justice.services.core.envelope.MediaTypeProvider;
import uk.gov.justice.services.core.enveloper.Enveloper;
import uk.gov.justice.services.core.extension.BeanInstantiater;
import uk.gov.justice.services.core.extension.ServiceComponentScanner;
import uk.gov.justice.services.core.featurecontrol.FeatureControlAnnotationFinder;
import uk.gov.justice.services.core.handler.registry.TestHandlerRegistryCacheProducer;
import uk.gov.justice.services.core.it.util.producer.TestEnvelopeValidationExceptionHandlerProducer;
import uk.gov.justice.services.core.json.FileBasedJsonSchemaValidator;
import uk.gov.justice.services.core.json.JsonSchemaLoader;
import uk.gov.justice.services.core.json.PayloadExtractor;
import uk.gov.justice.services.core.json.SchemaCatalogAwareJsonSchemaValidator;
import uk.gov.justice.services.core.json.SchemaValidationErrorMessageGenerator;
import uk.gov.justice.services.core.mapping.ActionNameToMediaTypesMappingObserver;
import uk.gov.justice.services.core.mapping.DefaultMediaTypesMappingCache;
import uk.gov.justice.services.core.mapping.DefaultNameToMediaTypeConverter;
import uk.gov.justice.services.core.mapping.DefaultSchemaIdMappingCache;
import uk.gov.justice.services.core.mapping.MediaTypesMappingCacheInitialiser;
import uk.gov.justice.services.core.mapping.SchemaIdMappingCacheInitialiser;
import uk.gov.justice.services.core.mapping.SchemaIdMappingObserver;
import uk.gov.justice.services.core.producers.EnvelopeValidatorFactory;
import uk.gov.justice.services.core.producers.RequestResponseEnvelopeValidatorFactory;
import uk.gov.justice.services.core.producers.RequesterProducer;
import uk.gov.justice.services.core.producers.SenderProducer;
import uk.gov.justice.services.core.requester.Requester;
import uk.gov.justice.services.core.sender.Sender;
import uk.gov.justice.services.messaging.DefaultJsonObjectEnvelopeConverter;
import uk.gov.justice.services.messaging.JsonEnvelope;
import uk.gov.justice.services.messaging.logging.DefaultTraceLogger;
import uk.gov.justice.services.test.utils.common.envelope.TestEnvelopeRecorder;
import uk.gov.justice.services.test.utils.common.validator.DummyJsonSchemaValidator;
import uk.gov.justice.services.test.utils.messaging.jms.DummyJmsEnvelopeSender;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.UUID;

import static java.util.UUID.randomUUID;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static uk.gov.justice.services.core.annotation.Component.COMMAND_API;
import static uk.gov.justice.services.core.annotation.Component.COMMAND_CONTROLLER;
import static uk.gov.justice.services.core.annotation.Component.QUERY_API;
import static uk.gov.justice.services.core.annotation.Component.QUERY_CONTROLLER;
import static uk.gov.justice.services.messaging.JsonEnvelope.envelopeFrom;
import static uk.gov.justice.services.messaging.JsonEnvelope.metadataBuilder;
import static uk.gov.justice.services.messaging.JsonObjects.getJsonBuilderFactory;

/**
 * Integration tests to confirm that the correct handlers are available to senders and requesters
 * based on field-level annotations.
 */
@RunWithApplicationComposer
public class SenderRequesterHandlerIT {

    @Inject
    @ServiceComponent(COMMAND_CONTROLLER)
    private Sender commandControllerSender;

    @Inject
    @ServiceComponent(COMMAND_API)
    private Sender commandApiSender;

    @Inject
    @ServiceComponent(QUERY_CONTROLLER)
    private Requester queryControllerRequester;

    @Inject
    @ServiceComponent(QUERY_API)
    private Requester queryApiRequester;

    @Inject
    private TestCommandController testCommandController;

    @Inject
    private TestCommandApi testCommandApi;

    @Inject
    private TestQueryController testQueryController;

    @Inject
    private TestQueryApi testQueryApi;

    @Module
    @Classes(cdi = true, value = {
            ServiceComponentScanner.class,
            ServiceComponentObserver.class,
            RequesterProducer.class,
            SenderProducer.class,

            DummyJmsEnvelopeSender.class,
            DispatcherFactory.class,
            EnvelopePayloadTypeConverter.class,
            JsonEnvelopeRepacker.class,

            StringToJsonObjectConverter.class,
            DefaultJsonObjectEnvelopeConverter.class,
            ObjectToJsonValueConverter.class,
            ObjectMapperProducer.class,
            Enveloper.class,
            AccessControlFailureMessageGenerator.class,
            AllowAllPolicyEvaluator.class,
            DefaultAccessControlService.class,
            DispatcherCache.class,
            ComponentNameExtractor.class,
            PolicyEvaluator.class,
            LoggerProducer.class,
            BeanInstantiater.class,
            SystemUserUtil.class,
            EmptySystemUserProvider.class,
            UtcClock.class,

            TestEnvelopeValidationExceptionHandlerProducer.class,
            GlobalValueProducer.class,
            FileBasedJsonSchemaValidator.class,
            JsonSchemaLoader.class,
            DefaultTraceLogger.class,
            TestCommandController.class,
            TestCommandApi.class,
            TestQueryApi.class,
            TestQueryController.class,

            SchemaCatalogAwareJsonSchemaValidator.class,
            PayloadExtractor.class,
            DefaultNameToMediaTypeConverter.class,
            DefaultSchemaIdMappingCache.class,
            SchemaIdMappingObserver.class,

            SchemaCatalogService.class,
            CatalogProducer.class,
            SchemaCatalogResolverProducer.class,

            DefaultMediaTypesMappingCache.class,
            ActionNameToMediaTypesMappingObserver.class,

            MediaTypeProvider.class,
            EnvelopeInspector.class,
            DummyJsonSchemaValidator.class,

            MediaTypesMappingCacheInitialiser.class,
            SchemaIdMappingCacheInitialiser.class,

            SchemaValidationErrorMessageGenerator.class,

            DispatcherConfiguration.class,
            FeatureControlAnnotationFinder.class,
            TestHandlerRegistryCacheProducer.class,

            RequestResponseEnvelopeValidatorFactory.class,
            EnvelopeValidatorFactory.class
    })
    public WebApp war() {
        return new WebApp()
                .contextRoot("SenderRequesterHandlerIT")
                .addServlet("TestApp", Application.class.getName());
    }

    @Test
    public void shouldSendToCorrectSenderWithFieldLevelServiceComponentAnnotation() {
        UUID metadataId = randomUUID();
        commandControllerSender.send(envelopeFrom(
                metadataBuilder().withId(metadataId).withName("contexta.command.aaa"),
                getJsonBuilderFactory().createObjectBuilder().add("someField1", "abc")));

        assertThat(testCommandController.recordedEnvelopes(), hasSize(1));
        assertThat(testCommandController.firstRecordedEnvelope().metadata().id(), equalTo(metadataId));
        assertThat(testCommandController.firstRecordedEnvelope().metadata().name(), equalTo("contexta.command.aaa"));

        assertThat(testCommandApi.recordedEnvelopes(), hasSize(0));
    }

    @Test
    public void shouldSendToCorrectRequesterWithFieldLevelServiceComponentAnnotation() {
        UUID metadataId = randomUUID();
        final JsonEnvelope response = queryControllerRequester.request(envelopeFrom(
                metadataBuilder().withId(metadataId).withName("contexta.query.aaa"),
                getJsonBuilderFactory().createObjectBuilder().add("someField1", "abc")));

        assertThat(testQueryController.recordedEnvelopes(), hasSize(1));
        assertThat(testQueryController.firstRecordedEnvelope().metadata().id(), equalTo(metadataId));
        assertThat(testQueryController.firstRecordedEnvelope().metadata().name(), equalTo("contexta.query.aaa"));
        assertThat(response.metadata().name(), equalTo("contexta.response.aaa"));
        assertThat(response.payloadAsJsonObject().getString("someField1"), equalTo("abc"));

        assertThat(testQueryApi.recordedEnvelopes(), hasSize(0));
    }

    @Remote
    @FrameworkComponent(COMMAND_CONTROLLER)
    @ApplicationScoped
    public static class TestCommandController extends TestEnvelopeRecorder {

        @Handles("contexta.command.aaa")
        public void commandAA(final JsonEnvelope command) {
            record(command);
        }
    }

    @Remote
    @FrameworkComponent(COMMAND_API)
    @ApplicationScoped
    public static class TestCommandApi extends TestEnvelopeRecorder {

        @Handles("contexta.command.aaa")
        public void commandAA(final JsonEnvelope command) {
            record(command);
        }
    }

    @Remote
    @FrameworkComponent(QUERY_CONTROLLER)
    @ApplicationScoped
    public static class TestQueryController extends TestEnvelopeRecorder {

        @Handles("contexta.query.aaa")
        public JsonEnvelope queryAA(final JsonEnvelope query) {
            record(query);
            return envelopeFrom(
                    metadataBuilder().withId(randomUUID()).withName("contexta.response.aaa"),
                    getJsonBuilderFactory().createObjectBuilder().add("someField1", "abc"));
        }
    }

    @Remote
    @FrameworkComponent(QUERY_API)
    @ApplicationScoped
    public static class TestQueryApi extends TestEnvelopeRecorder {

        @Handles("contexta.query.aaa")
        public JsonEnvelope queryAA(final JsonEnvelope query) {
            record(query);
            return envelopeFrom(
                    metadataBuilder().withId(randomUUID()).withName("contexta.response.aaa"),
                    getJsonBuilderFactory().createObjectBuilder().add("someField1", "horses"));
        }
    }
}

package uk.gov.justice.services.adapters.rest.generator;


import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsCollectionContaining.hasItems;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.raml.model.ActionType.GET;
import static org.raml.model.ParamType.BOOLEAN;
import static org.raml.model.ParamType.INTEGER;
import static org.raml.model.ParamType.STRING;
import static uk.gov.justice.services.core.interceptor.InterceptorContext.interceptorContextWithInput;
import static uk.gov.justice.services.generators.test.utils.builder.HeadersBuilder.headersWith;
import static uk.gov.justice.services.generators.test.utils.builder.HttpActionBuilder.httpAction;
import static uk.gov.justice.services.generators.test.utils.builder.HttpActionBuilder.httpActionWithDefaultMapping;
import static uk.gov.justice.services.generators.test.utils.builder.MappingBuilder.mapping;
import static uk.gov.justice.services.generators.test.utils.builder.QueryParamBuilder.queryParam;
import static uk.gov.justice.services.generators.test.utils.builder.RamlBuilder.restRamlWithQueryApiDefaults;
import static uk.gov.justice.services.generators.test.utils.builder.RamlBuilder.restRamlWithQueryControllerDefaults;
import static uk.gov.justice.services.generators.test.utils.builder.ResourceBuilder.resource;
import static uk.gov.justice.services.generators.test.utils.config.GeneratorConfigUtil.configurationWithBasePackage;
import static uk.gov.justice.services.test.utils.core.reflection.ReflectionUtil.firstMethodOf;
import static uk.gov.justice.services.test.utils.core.reflection.ReflectionUtil.setField;

import uk.gov.justice.services.adapter.rest.exception.BadRequestException;
import uk.gov.justice.services.adapter.rest.mapping.ActionMapperHelper;
import uk.gov.justice.services.adapter.rest.mapping.BasicActionMapperHelper;
import uk.gov.justice.services.adapter.rest.parameter.Parameter;
import uk.gov.justice.services.core.interceptor.InterceptorContext;
import uk.gov.justice.services.generators.commons.config.CommonGeneratorProperties;
import uk.gov.justice.services.messaging.JsonEnvelope;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.function.Function;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

public class RestAdapterGenerator_GETMethodBodyTest extends BaseRestAdapterGeneratorTest {

    private static final String NULL_STRING_VALUE = null;

    @SuppressWarnings("unchecked")
    @Test
    public void shouldReturnResponseGeneratedByRestProcessor() throws Exception {
        generator.run(
                restRamlWithQueryApiDefaults()
                        .with(
                                resource("/path")
                                        .with(httpActionWithDefaultMapping(GET).withDefaultResponseType())
                        ).build(),
                configurationWithBasePackage(BASE_PACKAGE, outputFolder, new CommonGeneratorProperties()));

        final Class<?> resourceClass = COMPILER.compiledClassOf(
                outputFolder,
                outputFolder,
                BASE_PACKAGE,
                "resource",
                "DefaultQueryApiPathResource");

        final String action = "theAction";
        when(actionMapper.actionOf(any(String.class), any(String.class), eq(httpHeaders))).thenReturn(action);

        final Object resourceObject = getInstanceOf(resourceClass);

        final Response processorResponse = Response.ok().build();
        when(restProcessor.process(anyString(), any(Function.class), anyString(), any(HttpHeaders.class), any(Collection.class))).thenReturn(processorResponse);

        final Method method = firstMethodOf(resourceClass).get();

        final Object result = method.invoke(resourceObject);

        assertThat(result, is(processorResponse));
    }

    @Test
    @SuppressWarnings({"rawtypes", "unchecked"})
    public void shouldCallInterceptorChainProcessor() throws Exception {

        generator.run(
                restRamlWithQueryApiDefaults().with(
                        resource("/path")
                                .with(httpActionWithDefaultMapping(GET).withDefaultResponseType())
                ).build(),
                configurationWithBasePackage(BASE_PACKAGE, outputFolder, new CommonGeneratorProperties()));

        final Class<?> resourceClass = COMPILER.compiledClassOf(
                outputFolder,
                outputFolder,
                BASE_PACKAGE,
                "resource",
                "DefaultQueryApiPathResource");

        final String action = "theAction";
        when(actionMapper.actionOf(any(String.class), any(String.class), eq(httpHeaders))).thenReturn(action);

        final Object resourceObject = getInstanceOf(resourceClass);

        final Method method = firstMethodOf(resourceClass).get();

        method.invoke(resourceObject);

        ArgumentCaptor<Function> consumerCaptor = ArgumentCaptor.forClass(Function.class);
        verify(restProcessor).process(anyString(), consumerCaptor.capture(), anyString(), any(HttpHeaders.class), any(Collection.class));

        final InterceptorContext interceptorContext = interceptorContextWithInput(mock(JsonEnvelope.class));
        consumerCaptor.getValue().apply(interceptorContext);

        verify(interceptorChainProcessor).process(interceptorContext);

    }

    @SuppressWarnings("unchecked")
    @Test
    public void shouldPassEntityResponseStrategyToProcessor() throws Exception {

        generator.run(
                restRamlWithQueryApiDefaults()
                        .with(
                                resource("/path2")
                                        .with(httpActionWithDefaultMapping(GET).withDefaultResponseType())
                        ).build(),
                configurationWithBasePackage(BASE_PACKAGE, outputFolder, new CommonGeneratorProperties()));

        final String action = "theAction";
        when(actionMapper.actionOf(any(String.class), any(String.class), eq(httpHeaders))).thenReturn(action);

        final Class<?> resourceClass = COMPILER.compiledClassOf(
                outputFolder,
                outputFolder,
                BASE_PACKAGE,
                "resource",
                "DefaultQueryApiPath2Resource");

        firstMethodOf(resourceClass).get().invoke(getInstanceOf(resourceClass));

        verify(restProcessor).process(eq("OkStatusEnvelopePayloadEntityResponseStrategy"), any(Function.class), anyString(), any(HttpHeaders.class), any(Collection.class));


    }

    @SuppressWarnings("unchecked")
    @Test
    public void shouldPassEnvelopePayloadEntityResponseStrategyNameToProcessor() throws Exception {

        generator.run(
                restRamlWithQueryControllerDefaults().with(
                        resource("/path")
                                .with(httpActionWithDefaultMapping(GET).withDefaultResponseType())
                ).build(),
                configurationWithBasePackage(BASE_PACKAGE, outputFolder, new CommonGeneratorProperties()));

        final Class<?> resourceClass = COMPILER.compiledClassOf(
                outputFolder,
                outputFolder,
                BASE_PACKAGE,
                "resource",
                "DefaultQueryControllerPathResource");

        final String action = "theAction";
        when(actionMapper.actionOf(any(String.class), any(String.class), eq(httpHeaders))).thenReturn(action);

        firstMethodOf(resourceClass).get().invoke(getInstanceOf(resourceClass));

        verify(restProcessor).process(eq("OkStatusEnvelopeEntityResponseStrategy"), any(Function.class), anyString(), any(HttpHeaders.class), any(Collection.class));

    }

    @SuppressWarnings("unchecked")
    @Test
    public void shouldPassFileStreamReturningResponseStrategyNameToProcessor() throws Exception {

        generator.run(
                restRamlWithQueryApiDefaults()
                        .with(resource("/some/path")
                                .with(httpAction()
                                        .withHttpActionType(GET)
                                        .withResponseTypes("application/octet-stream")
                                        .with(mapping()
                                                .withName("action1")
                                                .withResponseType("application/octet-stream"))
                                )
                        ).build(),
                configurationWithBasePackage(BASE_PACKAGE, outputFolder, new CommonGeneratorProperties()));

        final String action = "theAction";
        when(actionMapper.actionOf(any(String.class), any(String.class), eq(httpHeaders))).thenReturn(action);

        final Class<?> resourceClass = COMPILER.compiledClassOf(
                outputFolder,
                outputFolder,
                BASE_PACKAGE,
                "resource",
                "DefaultQueryApiSomePathResource");

        firstMethodOf(resourceClass).get().invoke(getInstanceOf(resourceClass));

        verify(restProcessor).process(eq("FileStreamReturningResponseStrategy"), any(Function.class), anyString(), any(HttpHeaders.class), any(Collection.class));

    }


    @SuppressWarnings("unchecked")
    @Test
    public void shouldPassHttpHeadersToRestProcessor() throws Exception {
        generator.run(
                restRamlWithQueryApiDefaults().with(
                        resource("/path")
                                .with(httpActionWithDefaultMapping(GET).withDefaultResponseType())
                ).build(),
                configurationWithBasePackage(BASE_PACKAGE, outputFolder, new CommonGeneratorProperties()));

        final HttpHeaders headers = mock(HttpHeaders.class);
        final Class<?> resourceClass = COMPILER.compiledClassOf(
                outputFolder,
                outputFolder,
                BASE_PACKAGE,
                "resource",
                "DefaultQueryApiPathResource");

        final String action = "theAction";
        when(actionMapper.actionOf(any(String.class), any(String.class), eq(headers))).thenReturn(action);

        final Object resourceObject = getInstanceOf(resourceClass);


        setField(resourceObject, "headers", headers);

        final Method method = firstMethodOf(resourceClass).get();
        method.invoke(resourceObject);

        verify(restProcessor).process(anyString(), any(Function.class), anyString(), eq(headers), any(Collection.class));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void shouldPassActionToRestProcessor() throws Exception {
        generator.run(
                restRamlWithQueryApiDefaults().with(
                        resource("/cake")
                                .with(httpActionWithDefaultMapping(GET)
                                        .with(mapping()
                                                .withName("contextA.action1")
                                                .withResponseType("application/vnd.ctx.query.somemediatype1+json"))
                                        .with(mapping()
                                                .withName("contextA.action1")
                                                .withResponseType("application/vnd.ctx.query.somemediatype2+json"))
                                        .with(mapping()
                                                .withName("contextA.action2")
                                                .withResponseType("application/vnd.ctx.query.somemediatype3+json"))
                                        .withResponseTypes(
                                                "application/vnd.ctx.query.somemediatype1+json",
                                                "application/vnd.ctx.query.somemediatype2+json",
                                                "application/vnd.ctx.query.somemediatype3+json"))
                ).build(),
                configurationWithBasePackage(BASE_PACKAGE, outputFolder, new CommonGeneratorProperties()));

        final Class<?> resourceClass = COMPILER.compiledClassOf(
                outputFolder,
                outputFolder,
                BASE_PACKAGE,
                "resource",
                "DefaultQueryApiCakeResource");

        final Object resourceObject = getInstanceOf(resourceClass);

        final Class<?> actionMapperClass = COMPILER.compiledClassOf(
                outputFolder,
                outputFolder,
                BASE_PACKAGE,
                "mapper",
                "DefaultQueryApiCakeResourceActionMapper");

        final Object actionMapperObject = actionMapperClass.getConstructor(ActionMapperHelper.class).newInstance(new BasicActionMapperHelper());
        setField(resourceObject, "actionMapper", actionMapperObject);

        setField(resourceObject, "headers", headersWith("Accept", "application/vnd.ctx.query.somemediatype1+json"));

        final Method method = firstMethodOf(resourceClass).get();
        method.invoke(resourceObject);


        verify(restProcessor).process(anyString(), any(Function.class), eq("contextA.action1"),
                any(HttpHeaders.class), any(Collection.class));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void shouldPassActionToRestProcessor2() throws Exception {
        generator.run(
                restRamlWithQueryApiDefaults().with(
                        resource("/recipe")
                                .with(httpActionWithDefaultMapping(GET)
                                        .with(mapping()
                                                .withName("contextB.action1")
                                                .withResponseType("application/vnd.ctx.query.mediatype1+json"))
                                        .withResponseTypes("application/vnd.ctx.query.mediatype1+json"))
                ).build(),
                configurationWithBasePackage(BASE_PACKAGE, outputFolder, new CommonGeneratorProperties()));

        final Class<?> resourceClass = COMPILER.compiledClassOf(
                outputFolder,
                outputFolder,
                BASE_PACKAGE,
                "resource",
                "DefaultQueryApiRecipeResource");

        final Object resourceObject = getInstanceOf(resourceClass);

        final Class<?> actionMapperClass = COMPILER.compiledClassOf(outputFolder, outputFolder, BASE_PACKAGE, "mapper", "DefaultQueryApiRecipeResourceActionMapper");
        final Object actionMapperObject = actionMapperClass.getConstructor(ActionMapperHelper.class).newInstance(new BasicActionMapperHelper());
        setField(resourceObject, "actionMapper", actionMapperObject);

        setField(resourceObject, "headers", headersWith("Accept", "application/vnd.ctx.query.mediatype1+json"));

        final Method method = firstMethodOf(resourceClass).get();
        method.invoke(resourceObject);


        verify(restProcessor).process(anyString(), any(Function.class), eq("contextB.action1"),
                any(HttpHeaders.class), any(Collection.class));
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Test
    public void shouldPassOnePathParamToRestProcessor() throws Exception {

        generator.run(
                restRamlWithQueryApiDefaults().with(
                        resource("/some/path/{paramA}", "paramA")
                                .with(httpActionWithDefaultMapping(GET).withDefaultResponseType())
                ).build(),
                configurationWithBasePackage(BASE_PACKAGE, outputFolder, new CommonGeneratorProperties()));

        final Class<?> resourceClass = COMPILER.compiledClassOf(
                outputFolder,
                outputFolder,
                BASE_PACKAGE,
                "resource",
                "DefaultQueryApiSomePathParamAResource");

        final String action = "theAction";
        when(actionMapper.actionOf(any(String.class), any(String.class), eq(httpHeaders))).thenReturn(action);

        final Object resourceObject = getInstanceOf(resourceClass);

        final Method method = firstMethodOf(resourceClass).get();
        method.invoke(resourceObject, "paramValue1234");

        final ArgumentCaptor<Collection> pathParamsCaptor = ArgumentCaptor.forClass(Collection.class);

        verify(restProcessor).process(anyString(), any(Function.class), anyString(), any(HttpHeaders.class),
                pathParamsCaptor.capture());

        Collection<Parameter> pathParams = pathParamsCaptor.getValue();
        assertThat(pathParams, hasSize(1));
        final Parameter pathParam = pathParams.iterator().next();
        assertThat(pathParam.getName(), equalTo("paramA"));
        assertThat(pathParam.getStringValue(), equalTo("paramValue1234"));

    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Test
    public void shouldPassTwoPathParamsToRestProcessor() throws Exception {
        generator.run(
                restRamlWithQueryApiDefaults().with(
                        resource("/some/path/{param1}/{param2}", "param1", "param2")
                                .with(httpActionWithDefaultMapping(GET).withDefaultResponseType())
                ).build(),
                configurationWithBasePackage(BASE_PACKAGE, outputFolder, new CommonGeneratorProperties()));

        final Class<?> resourceClass = COMPILER.compiledClassOf(
                outputFolder,
                outputFolder,
                BASE_PACKAGE,
                "resource",
                "DefaultQueryApiSomePathParam1Param2Resource");

        final String action = "theAction";
        when(actionMapper.actionOf(any(String.class), any(String.class), eq(httpHeaders))).thenReturn(action);

        final Object resourceObject = getInstanceOf(resourceClass);

        final Method method = firstMethodOf(resourceClass).get();
        method.invoke(resourceObject, "paramValueABC", "paramValueDEF");

        final ArgumentCaptor<Collection> pathParamsCaptor = ArgumentCaptor.forClass(Collection.class);

        verify(restProcessor).process(anyString(), any(Function.class), anyString(), any(HttpHeaders.class),
                pathParamsCaptor.capture());

        final Collection<Parameter> pathParams = pathParamsCaptor.getValue();
        assertThat(pathParams, hasSize(2));

        assertThat(pathParams, hasItems(
                allOf(hasProperty("name", equalTo("param1")), hasProperty("stringValue", equalTo("paramValueABC"))),
                allOf(hasProperty("name", equalTo("param2")), hasProperty("stringValue", equalTo("paramValueDEF")))
        ));
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Test
    public void shouldPassOneQueryParamToRestProcessor() throws Exception {
        generator.run(
                restRamlWithQueryApiDefaults().with(
                        resource("/some/path")
                                .with(httpActionWithDefaultMapping(GET)
                                        .with(queryParam("queryParam"))
                                        .withDefaultResponseType())
                ).build(),
                configurationWithBasePackage(BASE_PACKAGE, outputFolder, new CommonGeneratorProperties()));

        final String action = "theAction";
        when(actionMapper.actionOf(any(String.class), any(String.class), eq(httpHeaders))).thenReturn(action);

        final Class<?> resourceClass = COMPILER.compiledClassOf(
                outputFolder,
                outputFolder,
                BASE_PACKAGE,
                "resource",
                "DefaultQueryApiSomePathResource");

        final Object resourceObject = getInstanceOf(resourceClass);

        final Method method = firstMethodOf(resourceClass).get();
        method.invoke(resourceObject, "paramValue1234");

        final ArgumentCaptor<Collection> queryParamsCaptor = ArgumentCaptor.forClass(Collection.class);

        verify(restProcessor).process(anyString(), any(Function.class), anyString(), any(HttpHeaders.class),
                queryParamsCaptor.capture());

        final Collection<Parameter> queryParams = queryParamsCaptor.getValue();

        assertThat(queryParams, hasSize(1));
        assertThat(queryParams, hasItems(
                allOf(hasProperty("name", equalTo("queryParam")), hasProperty("stringValue", equalTo("paramValue1234")))
        ));
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Test
    public void shouldPassStringAndNumericParamsToRestProcessor() throws Exception {
        generator.run(
                restRamlWithQueryApiDefaults().with(
                        resource("/some/path")
                                .with(httpActionWithDefaultMapping(GET)
                                        .with(
                                                queryParam("queryParam1").withType(STRING),
                                                queryParam("queryParam2").withType(INTEGER)
                                        )
                                        .withDefaultResponseType())
                ).build(),
                configurationWithBasePackage(BASE_PACKAGE, outputFolder, new CommonGeneratorProperties()));

        final String action = "theAction";
        when(actionMapper.actionOf(any(String.class), any(String.class), eq(httpHeaders))).thenReturn(action);
        final Class<?> resourceClass = COMPILER.compiledClassOf(
                outputFolder,
                outputFolder,
                BASE_PACKAGE,
                "resource",
                "DefaultQueryApiSomePathResource");

        final Object resourceObject = getInstanceOf(resourceClass);

        final Method method = firstMethodOf(resourceClass).get();

        boolean queryParam1IsFirstMethodParameter = method.getParameters()[0].getName().equals("queryParam1");
        if (queryParam1IsFirstMethodParameter) {
            method.invoke(resourceObject, "paramValueABC", "2");
        } else {
            method.invoke(resourceObject, "2", "paramValueABC");
        }

        final ArgumentCaptor<Collection> queryParamsCaptor = ArgumentCaptor.forClass(Collection.class);

        verify(restProcessor).process(anyString(), any(Function.class), anyString(), any(HttpHeaders.class),
                queryParamsCaptor.capture());

        final Collection<Parameter> queryParams = queryParamsCaptor.getValue();

        assertThat(queryParams, hasSize(2));
        assertThat(queryParams, hasItems(
                allOf(hasProperty("name", equalTo("queryParam1")), hasProperty("stringValue", equalTo("paramValueABC"))),
                allOf(hasProperty("name", equalTo("queryParam2")), hasProperty("numericValue", equalTo(BigDecimal.valueOf(2))))
        ));
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Test
    public void shouldPassBooleanParamToRestProcessor() throws Exception {
        generator.run(
                restRamlWithQueryApiDefaults().with(
                        resource("/some/path")
                                .with(httpActionWithDefaultMapping(GET)
                                        .with(
                                                queryParam("queryParam").withType(BOOLEAN)
                                        )
                                        .withDefaultResponseType())
                ).build(),
                configurationWithBasePackage(BASE_PACKAGE, outputFolder, new CommonGeneratorProperties()));

        final Class<?> resourceClass = COMPILER.compiledClassOf(
                outputFolder,
                outputFolder,
                BASE_PACKAGE,
                "resource",
                "DefaultQueryApiSomePathResource");

        final String action = "theAction";
        when(actionMapper.actionOf(any(String.class), any(String.class), eq(httpHeaders))).thenReturn(action);

        final Object resourceObject = getInstanceOf(resourceClass);

        final Method method = firstMethodOf(resourceClass).get();

        method.invoke(resourceObject, "false");

        final ArgumentCaptor<Collection> queryParamsCaptor = ArgumentCaptor.forClass(Collection.class);

        verify(restProcessor).process(anyString(), any(Function.class), anyString(), any(HttpHeaders.class),
                queryParamsCaptor.capture());

        final Collection<Parameter> queryParams = queryParamsCaptor.getValue();

        assertThat(queryParams, hasSize(1));
        assertThat(queryParams, hasItems(
                allOf(hasProperty("name", equalTo("queryParam")), hasProperty("booleanValue", equalTo(false)))
        ));


    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Test
    public void shouldPassOnePathParamAndOneQueryParamToRestProcessor() throws Exception {
        generator.run(
                restRamlWithQueryApiDefaults().with(
                        resource("/some/path/{param}", "param")
                                .with(httpActionWithDefaultMapping(GET)
                                        .with(queryParam("queryParam"))
                                        .withDefaultResponseType())
                ).build(),
                configurationWithBasePackage(BASE_PACKAGE, outputFolder, new CommonGeneratorProperties()));

        final Class<?> resourceClass = COMPILER.compiledClassOf(
                outputFolder,
                outputFolder,
                BASE_PACKAGE,
                "resource",
                "DefaultQueryApiSomePathParamResource");

        final Object resourceObject = getInstanceOf(resourceClass);
        final String action = "theAction";
        when(actionMapper.actionOf(any(String.class), any(String.class), eq(httpHeaders))).thenReturn(action);

        final Method method = firstMethodOf(resourceClass).get();
        method.invoke(resourceObject, "paramValueABC", "paramValueDEF");

        final ArgumentCaptor<Collection> paramsCaptor = ArgumentCaptor.forClass(Collection.class);

        verify(restProcessor).process(anyString(), any(Function.class), anyString(), any(HttpHeaders.class),
                paramsCaptor.capture());

        final Collection<Parameter> params = paramsCaptor.getValue();

        assertThat(params, hasSize(2));
        assertThat(params, hasItems(
                allOf(hasProperty("name", equalTo("param")), hasProperty("stringValue", equalTo("paramValueABC"))),
                allOf(hasProperty("name", equalTo("queryParam")), hasProperty("stringValue", equalTo("paramValueDEF")))
        ));
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Test
    public void shouldRemoveOptionalQueryParamIfSetToNull() throws Exception {
        generator.run(
                restRamlWithQueryApiDefaults().with(
                        resource("/some/path")
                                .with(httpActionWithDefaultMapping(GET)
                                        .with(queryParam("queryParam1").required(true), queryParam("queryParam2").required(false))
                                        .withDefaultResponseType())
                ).build(),
                configurationWithBasePackage(BASE_PACKAGE, outputFolder, new CommonGeneratorProperties()));

        final Class<?> resourceClass = COMPILER.compiledClassOf(
                outputFolder,
                outputFolder,
                BASE_PACKAGE,
                "resource",
                "DefaultQueryApiSomePathResource");

        final String action = "theAction";
        when(actionMapper.actionOf(any(String.class), any(String.class), eq(httpHeaders))).thenReturn(action);

        final Object resourceObject = getInstanceOf(resourceClass);

        final Method method = firstMethodOf(resourceClass).get();

        boolean queryParam1IsFirstMethodParameter = method.getParameters()[0].getName().equals("queryParam1");
        if (queryParam1IsFirstMethodParameter) {
            method.invoke(resourceObject, "paramValueABC", NULL_STRING_VALUE);
        } else {
            method.invoke(resourceObject, NULL_STRING_VALUE, "paramValueABC");
        }

        final ArgumentCaptor<Collection> queryParamsCaptor = ArgumentCaptor.forClass(Collection.class);

        verify(restProcessor).process(anyString(), any(Function.class), anyString(), any(HttpHeaders.class),
                queryParamsCaptor.capture());

        final Collection<Parameter> queryParams = queryParamsCaptor.getValue();

        assertThat(queryParams, hasSize(1));
        assertThat(queryParams, hasItems(
                allOf(hasProperty("name", equalTo("queryParam1")), hasProperty("stringValue", equalTo("paramValueABC")))
        ));

    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Test
    public void shouldThrowExceptionIfRequiredQueryParamIsNull() throws Exception {

        generator.run(
                restRamlWithQueryApiDefaults().with(
                        resource("/some/path")
                                .with(httpActionWithDefaultMapping(GET)
                                        .with(queryParam("queryParam1").required(true))
                                        .withDefaultResponseType())
                ).build(),
                configurationWithBasePackage(BASE_PACKAGE, outputFolder, new CommonGeneratorProperties()));

        final Class<?> resourceClass = COMPILER.compiledClassOf(
                outputFolder,
                outputFolder,
                BASE_PACKAGE,
                "resource",
                "DefaultQueryApiSomePathResource");

        final Object resourceObject = getInstanceOf(resourceClass);

        final Method method = firstMethodOf(resourceClass).get();

        final InvocationTargetException invocationTargetException = assertThrows(InvocationTargetException.class, () ->
                method.invoke(resourceObject, NULL_STRING_VALUE)
        );

        assertThat(invocationTargetException.getCause(), is(instanceOf(BadRequestException.class)));
    }
}

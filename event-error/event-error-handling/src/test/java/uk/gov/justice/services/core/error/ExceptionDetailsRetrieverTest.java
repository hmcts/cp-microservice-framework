package uk.gov.justice.services.core.error;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;

import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@Disabled
@ExtendWith(MockitoExtension.class)
public class ExceptionDetailsRetrieverTest {

    @Mock
    private StackTraceProvider stackTraceProvider;

    @InjectMocks
    private ExceptionDetailsRetriever exceptionDetailsRetriever;

    @Test
    public void shouldGetTheStackTraceAndExceptionDetailsFromException() throws Exception {

        final Exception exception = mock(Exception.class, "exception");
        final String stackTraceAsString = "the stack trace";

        final StackTraceElement exceptionStackTraceElement_1 = mock(StackTraceElement.class, "exceptionStackTraceElement_1");
        final StackTraceElement exceptionStackTraceElement_2 = mock(StackTraceElement.class, "exceptionStackTraceElement_2");

        final StackTraceElement[] exceptionStackTraceElements = {
                exceptionStackTraceElement_1,
                exceptionStackTraceElement_2,
        };

        when(stackTraceProvider.getStackTraceFrom(exception)).thenReturn(stackTraceAsString);

        when(exception.getStackTrace()).thenReturn(exceptionStackTraceElements);
        when(exception.getCause()).thenReturn(null);

        when(exceptionStackTraceElement_1.getClassName()).thenReturn("uk.gov.justice.services.exceptionClassName_1");
        when(exceptionStackTraceElement_2.getClassName()).thenReturn("uk.gov.justice.services.stuff.exceptionClassName_2");

        final ExceptionDetails exceptionDetails = exceptionDetailsRetriever.getExceptionDetailsFrom(exception);

        assertThat(exceptionDetails.getOriginalException(), is(exception));
        assertThat(exceptionDetails.getCause(), is(nullValue()));
        assertThat(exceptionDetails.getFullStackTrace(), is(stackTraceAsString));

        final List<StackTraceElement> stackTraceElements = exceptionDetails.getStackTraceElements();
        assertThat(stackTraceElements.size(), is(2));

        assertThat(stackTraceElements.get(0), is(exceptionStackTraceElement_1));
        assertThat(stackTraceElements.get(1), is(exceptionStackTraceElement_2));
    }

    @Test
    public void shouldHandleCauseExceptions() throws Exception {

        final Exception exception = mock(Exception.class, "exception");
        final Exception causeException = mock(Exception.class, "causeException");
        final String stackTraceAsString = "the stack trace";

        final StackTraceElement exceptionStackTraceElement_1 = mock(StackTraceElement.class, "exceptionStackTraceElement_1");
        final StackTraceElement exceptionStackTraceElement_2 = mock(StackTraceElement.class, "exceptionStackTraceElement_2");
        final StackTraceElement causeStackTraceElement_1 = mock(StackTraceElement.class, "earliestCauseStackTraceElement_1");
        final StackTraceElement causeStackTraceElement_2 = mock(StackTraceElement.class, "earliestCauseStackTraceElement_2");

        final StackTraceElement[] exceptionStackTraceElements = {
                exceptionStackTraceElement_1,
                exceptionStackTraceElement_2,
        };
        final StackTraceElement[] causeStackTraceElements = {
                causeStackTraceElement_1,
                causeStackTraceElement_2,
        };

        when(stackTraceProvider.getStackTraceFrom(exception)).thenReturn(stackTraceAsString);

        when(exception.getStackTrace()).thenReturn(exceptionStackTraceElements);
        when(exception.getCause()).thenReturn(causeException);

        when(causeException.getStackTrace()).thenReturn(causeStackTraceElements);
        when(causeException.getCause()).thenReturn(null);

        when(exceptionStackTraceElement_1.getClassName()).thenReturn("uk.gov.justice.services.exceptionClassName_1");
        when(exceptionStackTraceElement_2.getClassName()).thenReturn("uk.gov.justice.services.stuff.exceptionClassName_2");

        when(causeStackTraceElement_1.getClassName()).thenReturn("uk.gov.justice.services.causeClassName_1");
        when(causeStackTraceElement_2.getClassName()).thenReturn("uk.gov.justice.services.things.causeClassName_2");

        final ExceptionDetails exceptionDetails = exceptionDetailsRetriever.getExceptionDetailsFrom(exception);

        assertThat(exceptionDetails.getOriginalException(), is(exception));
        assertThat(exceptionDetails.getCause(), is(causeException));
        assertThat(exceptionDetails.getFullStackTrace(), is(stackTraceAsString));

        final List<StackTraceElement> stackTraceElements = exceptionDetails.getStackTraceElements();
        assertThat(stackTraceElements.size(), is(4));

        assertThat(stackTraceElements.get(0), is(causeStackTraceElement_1));
        assertThat(stackTraceElements.get(1), is(causeStackTraceElement_2));
        assertThat(stackTraceElements.get(2), is(exceptionStackTraceElement_1));
        assertThat(stackTraceElements.get(3), is(exceptionStackTraceElement_2));
    }

    @Test
    public void shouldFilterOutNonMojClassesFromStackTrace() throws Exception {

        final Exception exception = mock(Exception.class, "exception");
        final String stackTraceAsString = "the stack trace";

        final StackTraceElement exceptionStackTraceElement_1 = mock(StackTraceElement.class, "exceptionStackTraceElement_1");
        final StackTraceElement exceptionStackTraceElement_2 = mock(StackTraceElement.class, "exceptionStackTraceElement_2");
        final StackTraceElement exceptionStackTraceElement_3 = mock(StackTraceElement.class, "exceptionStackTraceElement_3");

        final StackTraceElement[] exceptionStackTraceElements = {
                exceptionStackTraceElement_1,
                exceptionStackTraceElement_2,
                exceptionStackTraceElement_3,
        };

        when(stackTraceProvider.getStackTraceFrom(exception)).thenReturn(stackTraceAsString);

        when(exception.getStackTrace()).thenReturn(exceptionStackTraceElements);
        when(exception.getCause()).thenReturn(null);

        when(exceptionStackTraceElement_1.getClassName()).thenReturn("uk.gov.justice.services.exceptionClassName_1");
        when(exceptionStackTraceElement_2.getClassName()).thenReturn("org.jboss.some.package.exceptionClassName_2");
        when(exceptionStackTraceElement_3.getClassName()).thenReturn("uk.gov.justice.services.stuff.exceptionClassName_2");

        final ExceptionDetails exceptionDetails = exceptionDetailsRetriever.getExceptionDetailsFrom(exception);

        assertThat(exceptionDetails.getOriginalException(), is(exception));
        assertThat(exceptionDetails.getCause(), is(nullValue()));
        assertThat(exceptionDetails.getFullStackTrace(), is(stackTraceAsString));

        final List<StackTraceElement> stackTraceElements = exceptionDetails.getStackTraceElements();
        assertThat(stackTraceElements.size(), is(2));

        assertThat(stackTraceElements.get(0), is(exceptionStackTraceElement_1));
        assertThat(stackTraceElements.get(1), is(exceptionStackTraceElement_3));
    }

}
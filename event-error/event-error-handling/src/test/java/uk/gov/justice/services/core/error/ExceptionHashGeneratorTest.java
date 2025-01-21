package uk.gov.justice.services.core.error;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ExceptionHashGeneratorTest {

    @InjectMocks
    private ExceptionHashGenerator exceptionHashGenerator;

    @Test
    public void shouldGenerateReadableHashStringFromExceptionClassNameCauseFailingClassMethodAndLineNumber() throws Exception {

        final String exception = "uk.gov.justice.event.SomeException";
        final String cause = NullPointerException.class.getName();
        final String className = "uk.gov.justice.event.SomeClass";
        final String methodName = "someMethod";
        final int lineNumber = 23;

        final StringBuilder stringBuilder = new StringBuilder();

        final String hash = stringBuilder
                .append(exception).append("_")
                .append(cause).append("_")
                .append(className).append("_")
                .append(methodName).append("_")
                .append(lineNumber)
                .toString();

        final String hashString = exceptionHashGenerator.createHashFrom(hash);

        assertThat(hashString, is("576b975aff05b7f2b4a1f7b26eb47aa5"));
    }
}
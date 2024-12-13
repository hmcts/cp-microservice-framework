package uk.gov.justice.services.core.error;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.hamcrest.MatcherAssert.assertThat;

import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class StackTraceProviderTest {

    @InjectMocks
    private StackTraceProvider stackTraceProvider;

    @Test
    public void shouldName() throws Exception {

        final String stackTrace = stackTraceProvider.getStackTraceFrom(new Exception());

        assertThat(stackTrace, startsWith("java.lang.Exception\n\tat uk.gov.justice.services.core.error.StackTraceProviderTest"));
    }
}
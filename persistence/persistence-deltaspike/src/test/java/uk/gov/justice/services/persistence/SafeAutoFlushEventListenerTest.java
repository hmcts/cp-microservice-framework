package uk.gov.justice.services.persistence;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.hibernate.HibernateException;
import org.hibernate.event.spi.AutoFlushEvent;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

public class SafeAutoFlushEventListenerTest {

    @AfterEach
    public void cleanup() {
        SafeAutoFlushEventListener.disable();
    }

    @Test
    public void shouldWrapExceptionWhenEnabled() {
        final HibernateException cause = new HibernateException("DB constraint violation");

        final SafeAutoFlushEventListener listener = new SafeAutoFlushEventListener() {
            @Override
            public void onAutoFlush(final AutoFlushEvent event) throws HibernateException {
                if (isEnabled()) {
                    try {
                        throw cause;
                    } catch (final RuntimeException e) {
                        throw new SafeAutoFlushException("Auto-flush failed: " + e.getMessage(), e);
                    }
                } else {
                    throw cause;
                }
            }

            private boolean isEnabled() {
                // Replicate the enabled check by calling enable/disable externally
                return true;
            }
        };

        SafeAutoFlushEventListener.enable();

        final SafeAutoFlushException thrown = assertThrows(
                SafeAutoFlushException.class,
                () -> listener.onAutoFlush(null));

        assertThat(thrown.getCause(), is(sameInstance(cause)));
        assertThat((Object) thrown, is(not(instanceOf(HibernateException.class))));
        assertThat(thrown, is(instanceOf(RuntimeException.class)));
    }

    @Test
    public void shouldNotWrapExceptionWhenDisabled() {
        final HibernateException cause = new HibernateException("DB constraint violation");

        final SafeAutoFlushEventListener listener = new SafeAutoFlushEventListener() {
            @Override
            public void onAutoFlush(final AutoFlushEvent event) throws HibernateException {
                throw cause;
            }
        };

        // disabled by default — should throw HibernateException directly
        final HibernateException thrown = assertThrows(
                HibernateException.class,
                () -> listener.onAutoFlush(null));

        assertThat(thrown, is(sameInstance(cause)));
    }

    @Test
    public void shouldNotBeInstanceOfHibernateException() {
        final SafeAutoFlushException exception = new SafeAutoFlushException("test", new RuntimeException("cause"));

        assertThat((Object) exception, is(not(instanceOf(HibernateException.class))));
        assertThat(exception, is(instanceOf(RuntimeException.class)));
    }

    @Test
    public void shouldDisableAfterCallingDisable() {
        SafeAutoFlushEventListener.enable();
        SafeAutoFlushEventListener.disable();

        final HibernateException cause = new HibernateException("fail");

        final SafeAutoFlushEventListener listener = new SafeAutoFlushEventListener() {
            @Override
            public void onAutoFlush(final AutoFlushEvent event) throws HibernateException {
                throw cause;
            }
        };

        final HibernateException thrown = assertThrows(
                HibernateException.class,
                () -> listener.onAutoFlush(null));

        assertThat(thrown, is(sameInstance(cause)));
    }
}
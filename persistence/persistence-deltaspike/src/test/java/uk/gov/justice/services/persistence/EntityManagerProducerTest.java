package uk.gov.justice.services.persistence;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.lang.reflect.Method;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.hibernate.Session;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.event.service.spi.EventListenerRegistry;
import org.hibernate.event.spi.EventType;
import org.hibernate.service.spi.ServiceRegistryImplementor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class EntityManagerProducerTest {

    @Mock
    private EntityManagerFactory entityManagerFactory;

    @Mock
    private EntityManager entityManager;

    @Mock
    private Session session;

    @Mock
    private HibernateSessionHolder hibernateSessionHolder;

    private EntityManagerProducer entityManagerProducer;

    @BeforeEach
    public void setup() {
        entityManagerProducer = new EntityManagerProducer();
        entityManagerProducer.entityManagerFactory = entityManagerFactory;
        entityManagerProducer.hibernateSessionHolder = hibernateSessionHolder;
        when(entityManagerFactory.createEntityManager()).thenReturn(entityManager);
    }

    @Test
    public void shouldProduceEntityManger() {
        when(entityManager.unwrap(Session.class)).thenReturn(session);

        EntityManager em = entityManagerProducer.create();

        assertThat(em, equalTo(entityManager));
    }

    @Test
    public void shouldStoreSessionInHibernateSessionHolder() {
        when(entityManager.unwrap(Session.class)).thenReturn(session);

        entityManagerProducer.create();

        verify(hibernateSessionHolder).setSession(session);
    }

    @Test
    public void shouldCloseOpenEntityManger() {
        when(entityManager.unwrap(Session.class)).thenReturn(session);
        when(entityManager.isOpen()).thenReturn(true);

        EntityManager em = entityManagerProducer.create();
        entityManagerProducer.close(em);

        verify(em, times(1)).close();
    }

    @Test
    public void shouldNotCloseAClosedEntityManger() {
        when(entityManager.unwrap(Session.class)).thenReturn(session);
        when(entityManager.isOpen()).thenReturn(false);

        EntityManager em = entityManagerProducer.create();
        entityManagerProducer.close(em);

        verify(em, never()).close();
    }

    @SuppressWarnings("unchecked")
    @Test
    public void shouldRegisterSafeAutoFlushListenerOnPostConstruct() throws Exception {

        final EntityManager tempEm = mock(EntityManager.class);
        final Session tempSession = mock(Session.class);
        final SessionFactoryImplementor sessionFactory = mock(SessionFactoryImplementor.class);
        final ServiceRegistryImplementor serviceRegistry = mock(ServiceRegistryImplementor.class);
        final EventListenerRegistry eventListenerRegistry = mock(EventListenerRegistry.class);

        when(entityManagerFactory.createEntityManager()).thenReturn(tempEm);
        when(tempEm.unwrap(Session.class)).thenReturn(tempSession);
        when(tempSession.getSessionFactory()).thenReturn(sessionFactory);
        when(sessionFactory.getServiceRegistry()).thenReturn(serviceRegistry);
        when(serviceRegistry.getService(EventListenerRegistry.class)).thenReturn(eventListenerRegistry);

        final Method postConstruct = EntityManagerProducer.class.getDeclaredMethod("registerSafeAutoFlushListener");
        postConstruct.setAccessible(true);
        postConstruct.invoke(entityManagerProducer);

        verify(eventListenerRegistry).setListeners(eq(EventType.AUTO_FLUSH), any(SafeAutoFlushEventListener.class));
        verify(tempEm).close();
    }
}
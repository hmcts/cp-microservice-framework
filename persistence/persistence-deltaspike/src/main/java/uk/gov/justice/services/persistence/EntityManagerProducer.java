package uk.gov.justice.services.persistence;

import java.util.TimeZone;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;

import org.hibernate.Session;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.event.service.spi.EventListenerRegistry;
import org.hibernate.event.spi.EventType;

/**
 * Producer of {:link EntityManager} for use with JPA (Delta-spike).
 */
@ApplicationScoped
public class EntityManagerProducer {
    private static final String UTC = "UTC";

    @PersistenceUnit
    EntityManagerFactory entityManagerFactory;

    @Inject
    HibernateSessionHolder hibernateSessionHolder;

    @PostConstruct
    private void registerSafeAutoFlushListener() {
        final EntityManager tempEm = entityManagerFactory.createEntityManager();
        try {
            final Session session = tempEm.unwrap(Session.class);
            final SessionFactoryImplementor sfi = (SessionFactoryImplementor) session.getSessionFactory();
            final EventListenerRegistry registry = sfi.getServiceRegistry()
                    .getService(EventListenerRegistry.class);
            registry.setListeners(EventType.AUTO_FLUSH, new SafeAutoFlushEventListener());
        } finally {
            tempEm.close();
        }
    }

    @Produces
    @RequestScoped
    public EntityManager create() {
        TimeZone.setDefault(TimeZone.getTimeZone(UTC));
        final EntityManager em = entityManagerFactory.createEntityManager();
        hibernateSessionHolder.setSession(em.unwrap(Session.class));
        return em;
    }

    public void close(@Disposes final EntityManager em) {
        if (em.isOpen()) {
            em.close();
        }
    }
}
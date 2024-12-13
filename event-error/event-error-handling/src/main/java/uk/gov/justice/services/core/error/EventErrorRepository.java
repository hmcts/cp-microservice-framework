package uk.gov.justice.services.core.error;

public interface EventErrorRepository {


    void save(final PersistableEventError persistableEventError);
}

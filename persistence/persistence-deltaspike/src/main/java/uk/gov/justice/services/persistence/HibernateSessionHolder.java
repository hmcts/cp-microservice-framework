package uk.gov.justice.services.persistence;

import javax.enterprise.context.RequestScoped;

import org.hibernate.Session;

@RequestScoped
public class HibernateSessionHolder {

    private Session session;

    public void setSession(final Session session) {
        this.session = session;
    }

    public Session getSession() {
        return session;
    }
}
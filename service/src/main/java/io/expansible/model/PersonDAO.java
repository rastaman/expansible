package io.expansible.model;


import java.util.Optional;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.SessionFactory;

import javax.inject.Inject;
import java.util.List;

public class PersonDAO extends AbstractDAO<Person> {

    @Inject
    public PersonDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public Person create(Person Person) {
        return persist(Person);
    }

    public List<Person> readAll() {
        return list(currentSession().createCriteria(Person.class));
    }

    public Optional<Person> readById(Long id) {
        return Optional.ofNullable(get(id));
    }
}


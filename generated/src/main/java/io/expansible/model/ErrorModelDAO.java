package io.expansible.model;


import java.util.Optional;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.SessionFactory;

import javax.inject.Inject;
import java.util.List;

public class ErrorModelDAO extends AbstractDAO<ErrorModel> {

    @Inject
    public ErrorModelDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public ErrorModel create(ErrorModel ErrorModel) {
        return persist(ErrorModel);
    }

    public List<ErrorModel> readAll() {
        return list(currentSession().createCriteria(ErrorModel.class));
    }

    public Optional<ErrorModel> readById(Long id) {
        return Optional.ofNullable(get(id));
    }
}


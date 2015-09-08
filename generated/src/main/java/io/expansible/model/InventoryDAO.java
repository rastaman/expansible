package io.expansible.model;


import java.util.Optional;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.SessionFactory;

import javax.inject.Inject;
import java.util.List;

public class InventoryDAO extends AbstractDAO<Inventory> {

    @Inject
    public InventoryDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public Inventory create(Inventory Inventory) {
        return persist(Inventory);
    }

    public List<Inventory> readAll() {
        return list(currentSession().createCriteria(Inventory.class));
    }

    public Optional<Inventory> readById(Long id) {
        return Optional.ofNullable(get(id));
    }
}


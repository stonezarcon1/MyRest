package com.stonezarcon.myrest;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class UserRepositoryCustomImpl implements UserRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<User> findUserByLastName(Set<String> lastNames) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> query = cb.createQuery(User.class);
        Root<User> user = query.from(User.class);

        Path<String> lastNamePath = user.get("lastName");

        List<Predicate> predicates = new ArrayList<>();
        for (String lastName : lastNames) {
            predicates.add(cb.like(lastNamePath, lastName));
        }
        query.select(user).where(cb.or(predicates.toArray(new Predicate[predicates.size()])));

        return entityManager.createQuery(query).getResultList();
    }
}

package com.itmo.nxzage.web4.repository;

import java.util.List;
import com.itmo.nxzage.web4.entity.Hit;
import com.itmo.nxzage.web4.entity.User;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

@ApplicationScoped
public class HitRepository {
    @PersistenceContext(unitName = "MainUnit")
    private EntityManager em;

    public void save(Hit hit) {
        em.persist(hit);
    }

    public List<Hit> getHitsByUser(User user) {
        TypedQuery<Hit> query =
                em.createQuery("SELECT h FROM Hit h JOIN FETCH h.user WHERE h.user.id = :id ",
                        Hit.class).setParameter("id", user.getId());
        return query.getResultList();
    }
}

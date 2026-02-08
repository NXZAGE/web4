package com.itmo.nxzage.web4.repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import com.itmo.nxzage.web4.entity.Role;
import com.itmo.nxzage.web4.entity.User;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

@Stateless
public class UserRespository {
    @PersistenceContext(unitName = "MainUnit")
    private EntityManager em;

    public List<User> getUsers() {
        TypedQuery<User> query = em.createQuery("SELECT u FROM User u", User.class);
        return query.getResultList();
    }

    public Optional<User> findByNameWithRoles(String name) {
        TypedQuery<User> query = em.createQuery(
                "SELECT u FROM User u LEFT JOIN FETCH u.roles WHERE u.username = :name", User.class)
                .setParameter("name", name);
        try {
            return Optional.of(query.getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    public boolean existsByUsername(String username) {
        TypedQuery<Long> query =
                em.createQuery("SELECT COUNT(u) FROM  User u WHERE u.username = :name", Long.class)
                        .setParameter("name", username);
        return query.getSingleResult() > 0;
    }

    public void save(User user) {
        em.persist(user);
    }

    public void createRole(Role role) {
        boolean exists = !em.createQuery("SELECT r FROM Role r WHERE r.name = :n")
                .setParameter("n", role.getName()).getResultList().isEmpty();
        if (!exists) {
            em.persist(role);
        }
    }

    public void assignRoleToUser(String username, String rolename) {
        User user = this.findByNameWithRoles(username).orElseThrow(
                () -> new RuntimeException("Attempt of assigning role to non-existing user "));
        Role role = em.createQuery("SELECT r FROM Role r WHERE r.name = :rolename", Role.class)
                .setParameter("rolename", rolename).getSingleResult();
        user.addRole(role);
        em.merge(user);
    }

    public void assignRokesToUser(String username, Collection<String> roles) {
        roles.forEach(role -> assignRoleToUser(username, role));
    }
}

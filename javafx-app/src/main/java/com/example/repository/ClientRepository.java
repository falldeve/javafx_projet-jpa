package com.example.repository;

import com.example.entites.Client;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;

public class ClientRepository {
    private EntityManagerFactory entityManagerFactory;

    public ClientRepository(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    public Client findByTelephone(String telephone) {
        EntityManager em = entityManagerFactory.createEntityManager();
        try {
            TypedQuery<Client> query = em.createQuery("SELECT c FROM Client c WHERE c.telephone = :telephone", Client.class);
            query.setParameter("telephone", telephone);
            List<Client> results = query.getResultList();
            return results.isEmpty() ? null : results.get(0);
        } finally {
            em.close();
        }
    }
    public List<Client> findAll() {
        EntityManager em = entityManagerFactory.createEntityManager();
        try {
            return em.createQuery("SELECT c FROM Client c", Client.class).getResultList();
        } finally {
            em.close();
        }
    }

    public void save(Client client) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        entityManager.persist(client);
        entityManager.getTransaction().commit();
        entityManager.close();
    }
}
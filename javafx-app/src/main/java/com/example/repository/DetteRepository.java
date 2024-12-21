package com.example.repository;

import com.example.entites.Dette;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;

public class DetteRepository {

    private EntityManagerFactory entityManagerFactory;

    public DetteRepository(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    public void save(Dette dette) {
        EntityManager em = entityManagerFactory.createEntityManager();
        em.getTransaction().begin();
        em.persist(dette);
        em.getTransaction().commit();
        em.close();
    }

    public Dette findById(Long id) {
        EntityManager em = entityManagerFactory.createEntityManager();
        Dette dette = em.find(Dette.class, id); // Récupérer la dette par son ID
        em.close();
        return dette; // Retourner la dette trouvée ou null si non trouvée
    }

     public List<Dette> findAll() {
        EntityManager em = entityManagerFactory.createEntityManager();
        TypedQuery<Dette> query = em.createQuery("SELECT d FROM Dette d", Dette.class);
        List<Dette> dettes = query.getResultList();
        em.close();
        return dettes;
    }
}
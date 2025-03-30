package dat.daos.impl;

import dat.entities.League;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

import java.util.List;

public class LeaugeDAO {
    private static EntityManagerFactory emf;

    public LeaugeDAO(EntityManagerFactory emf) {
        LeaugeDAO.emf = emf;
    }

    public List<League> readAll() {
        try (EntityManager em = emf.createEntityManager()) {
            return em.createQuery("SELECT l FROM League l", League.class).getResultList();
        }
    }

    public League read(int id) {
        try (EntityManager em = emf.createEntityManager()) {
            return em.find(League.class, id);
        }
    }

}

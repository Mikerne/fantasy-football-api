package dat.daos.impl;


import dat.entities.Match;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

import java.util.List;

public class MatchDAO {
    private static EntityManagerFactory emf;

    public MatchDAO(EntityManagerFactory emf) {
        MatchDAO.emf = emf;
    }

    public List<Match> readAll() {
        try (EntityManager em = emf.createEntityManager()) {
            return em.createQuery("SELECT m FROM Match m", Match.class).getResultList();
        }
    }

}

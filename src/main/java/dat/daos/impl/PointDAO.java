package dat.daos.impl;

import dat.dtos.PointCreateDTO;
import dat.entities.Match;
import dat.entities.Point;
import dat.security.entities.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;

import java.sql.Timestamp;
import java.util.List;

public class PointDAO {
    private static EntityManagerFactory emf;

    public PointDAO(EntityManagerFactory emf) {
        PointDAO.emf = emf;
    }

    //Henter alle brugernes points
    public List<Point> readAll() {
        try (EntityManager em = emf.createEntityManager()) {
            return em.createQuery("SELECT p FROM Point p", Point.class).getResultList();
        }
    }

    //Henter en brugers points
    public List<Point> readUserPoints(int id) {
        try (EntityManager em = emf.createEntityManager()) {
            return em.createQuery("SELECT p FROM Point p WHERE p.user.id = :id", Point.class)
                    .setParameter("id", id)
                    .getResultList();
        }
    }

    //Gemmer points i databasen eller opretter en ny hvis ikke findes
    public void addOrUpdatePoints(PointCreateDTO dto) {
        try (EntityManager em = emf.createEntityManager()) {
            EntityTransaction tx = em.getTransaction();
            tx.begin();

            // Check om brugeren allerede har point for denne kamp
            TypedQuery<Point> query = em.createQuery(
                    "SELECT p FROM Point p WHERE p.user.id = :userId AND p.match.id = :matchId", Point.class);
            query.setParameter("userId", dto.getUserId());
            query.setParameter("matchId", dto.getMatchId());

            List<Point> existing = query.getResultList();

            Point point;
            if (existing.isEmpty()) {
                point = new Point();
                point.setUser(em.find(User.class, dto.getUserId()));
                point.setMatch(em.find(Match.class, dto.getMatchId()));
                point.setEarnedAt(new Timestamp(System.currentTimeMillis()));
            } else {
                point = existing.get(0); // Opdater eksisterende
            }

            point.setPointsEarned(dto.getPointsEarned());

            em.persist(point); // Persist virker også som update her
            tx.commit();
        }
    }


}

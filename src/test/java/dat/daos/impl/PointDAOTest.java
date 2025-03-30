package dat.daos.impl;

import dat.config.HibernateConfig;
import dat.dtos.PointCreateDTO;
import dat.entities.Match;
import dat.entities.Point;
import dat.security.entities.User;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

import java.sql.Timestamp;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PointDAOTest {

    private PointDAO pointDAO;
    private MatchDAO matchDAO;
    private int userId;
    private int matchId;

    @BeforeAll
    void setup() throws Exception {
        EntityManagerFactory emf = HibernateConfig.createNewTestEMF();
        // Brug samme EMF til alle DAO’er
        pointDAO = new PointDAO(emf);
        matchDAO = new MatchDAO(emf);
        // Opret test-bruger og match manuelt
        EntityManager em = HibernateConfig.getEntityManagerFactoryForTest().createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        User user = new User();
        user.setUsername("testuser");
        user.setPassword("test");
        em.persist(user);

        Match match = new Match();
        match.setId(10001);
        match.setHomeTeam("Team A");
        match.setAwayTeam("Team B");
        match.setMatchDate(new Timestamp(System.currentTimeMillis()));
        match.setStatus("FINISHED");
        match.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        em.persist(match);

        tx.commit();
        em.close();

        this.userId = user.getId();
        this.matchId = match.getId();
    }

    @Test
    void testAddNewPoint() {
        PointCreateDTO dto = new PointCreateDTO();
        dto.setUserId(userId);
        dto.setMatchId(matchId);
        dto.setPointsEarned(10);

        pointDAO.addOrUpdatePoints(dto);

        List<Point> userPoints = pointDAO.readUserPoints(userId);
        assertEquals(1, userPoints.size());
        assertEquals(10, userPoints.get(0).getPointsEarned());
    }



    @Test
    void testUpdateExistingPoint() {
        PointCreateDTO dto = new PointCreateDTO();
        dto.setUserId(userId);
        dto.setMatchId(matchId);
        dto.setPointsEarned(15);

        pointDAO.addOrUpdatePoints(dto);

        List<Point> userPoints = pointDAO.readUserPoints(userId);
        assertEquals(1, userPoints.size());
        assertEquals(15, userPoints.get(0).getPointsEarned()); // Opdateret til 15
    }
    @Test
    void testReadAllPoints() {
        // Tilføj et point først
        PointCreateDTO dto = new PointCreateDTO();
        dto.setUserId(userId);
        dto.setMatchId(matchId);
        dto.setPointsEarned(20);
        pointDAO.addOrUpdatePoints(dto);

        // Nu burde listen ikke være tom
        List<Point> allPoints = pointDAO.readAll();
        assertFalse(allPoints.isEmpty());
    }

}

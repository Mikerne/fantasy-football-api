package dat.daos;

import dat.config.HibernateConfig;
import dat.dtos.TeamDTO;
import dat.entities.League;
import dat.entities.Team;
import dat.security.entities.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TeamDAOTest {

    private static EntityManagerFactory emf;
    private static TeamDAO teamDAO;
    private static User testUser;
    private static League testLeague;

    @BeforeAll
    static void setUpClass() {
        // Brug testdatabasen
        HibernateConfig.setTest(true);
        emf = HibernateConfig.getEntityManagerFactoryForTest();
        teamDAO = TeamDAO.getInstance(emf);

        // Opret testbruger og liga
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();

        testUser = new User();
        testUser.setUsername("testUser");
        testUser.setPassword("testPass");
        em.persist(testUser);

        testLeague = new League();
        testLeague.setName("Test League");
        em.persist(testLeague);

        em.getTransaction().commit();
        em.close();
    }

    @AfterAll
    static void tearDownClass() {
        emf.close();
    }


    @Test
    @Order(1)
    void shouldCreateTeam() {
        TeamDTO teamDTO = new TeamDTO();
        teamDTO.setName("Test Team");
        teamDTO.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));
        teamDTO.setCreatorId(testUser.getId());
        teamDTO.setLeagueId(testLeague.getId());

        TeamDTO createdTeam = teamDAO.create(teamDTO);

        assertNotNull(createdTeam);
        assertEquals("Test Team", createdTeam.getName());
        assertEquals(testUser.getId(), createdTeam.getCreatorId());
        assertEquals(testLeague.getId(), createdTeam.getLeagueId());
    }

    @Test
    @Order(2)
    void shouldReadTeam() {
        TeamDTO teamDTO = teamDAO.create(new TeamDTO(1,"Read Team", Timestamp.valueOf(LocalDateTime.now()), testUser.getId(), testLeague.getId()));
        TeamDTO foundTeam = teamDAO.read(teamDTO.getId());

        assertNotNull(foundTeam);
        assertEquals("Read Team", foundTeam.getName());
    }

    @Test
    @Order(3)
    void shouldReadAllTeams() {
        teamDAO.create(new TeamDTO(2 ,"Team 1", Timestamp.valueOf(LocalDateTime.now()), testUser.getId(), testLeague.getId()));
        teamDAO.create(new TeamDTO(3, "Team 2", Timestamp.valueOf(LocalDateTime.now()), testUser.getId(), testLeague.getId()));

        List<TeamDTO> teams = teamDAO.readAll();
        assertTrue(teams.size() >= 2);
    }

    @Test
    @Order(4)
    void shouldUpdateTeam() {
        TeamDTO teamDTO = teamDAO.create(new TeamDTO(4, "Old Name", Timestamp.valueOf(LocalDateTime.now()), testUser.getId(), testLeague.getId()));

        teamDTO.setName("New Name");
        TeamDTO updatedTeam = teamDAO.update(teamDTO.getId(), teamDTO);

        assertEquals("New Name", updatedTeam.getName());
    }

    @Test
    @Order(5)
    void shouldDeleteTeam() {
        TeamDTO teamDTO = teamDAO.create(new TeamDTO(5, "To Be Deleted", Timestamp.valueOf(LocalDateTime.now()), testUser.getId(), testLeague.getId()));

        teamDAO.delete(teamDTO.getId());

        assertNull(teamDAO.read(teamDTO.getId()));
    }
}

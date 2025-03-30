package dat.daos;

import dat.config.HibernateConfig;
import dat.dtos.PlayerDTO;
import dat.entities.Player;
import dat.entities.Team;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PlayerDAOTest {

    private static EntityManagerFactory emf;
    private static EntityManager em;
    private static PlayerDAO playerDAO;
    private static Team testTeam;

    @BeforeAll
    static void setup() {
        HibernateConfig.setTest(true);
        emf = HibernateConfig.getEntityManagerFactoryForTest();
        em = emf.createEntityManager();
        playerDAO = PlayerDAO.getInstance(emf);

        // Opretter en test-team, da Player kræver et Team
        em.getTransaction().begin();
        testTeam = new Team();
        em.persist(testTeam);
        em.getTransaction().commit();
    }

    @AfterAll
    static void teardown() {
        if (em.isOpen()) em.close();
        if (emf.isOpen()) emf.close();
    }

    @BeforeEach
    void beginTransaction() {
        if (!em.getTransaction().isActive()) {
            em.getTransaction().begin();
        }
    }

    @AfterEach
    void rollbackTransaction() {
        if (em.getTransaction().isActive()) {
            em.getTransaction().rollback(); // Ruller ændringer tilbage efter hver test
        }
    }

    @Test
    @Order(1)
    void shouldCreatePlayer() {
        Player player = new Player(1,"Lionel Messi", "Forward", testTeam, 95.0);
        PlayerDTO playerDTO = new PlayerDTO(player);
        playerDAO.create(playerDTO);

        List<PlayerDTO> players = playerDAO.readAll();
        assertEquals(1, players.size());
        assertEquals("Lionel Messi", players.get(0).getName());
        assertEquals(testTeam.getId(), players.get(0).getTeamId());
    }

    @Test
    @Order(2)
    void shouldFindPlayerById() {
        Player player = new Player(2,"Cristiano Ronaldo", "Forward", testTeam, 92.0);
        PlayerDTO playerDTO = new PlayerDTO(player);
        playerDAO.create(playerDTO);

        PlayerDTO found = playerDAO.read(player.getId());
        assertNotNull(found);
        assertEquals("Cristiano Ronaldo", found.getName());
    }

    @Test
    @Order(3)
    void shouldUpdatePlayer() {
        Player player = new Player(3,"Erling Haaland", "Forward", testTeam, 90.0);
        PlayerDTO playerDTO = new PlayerDTO(player);
        playerDAO.create(playerDTO);

        playerDTO.setPerformanceRating(95.0);
        playerDAO.update(3, playerDTO);

        PlayerDTO updated = playerDAO.read(player.getId());
        assertEquals(95.0, updated.getPerformanceRating());
    }

    @Test
    @Order(4)
    void shouldDeletePlayer() {
        Player player = new Player(4, "Kylian Mbappe", "Forward", testTeam, 94.0);
        PlayerDTO playerDTO = new PlayerDTO(player);
        playerDAO.create(playerDTO);

        playerDAO.delete(player.getId());
        PlayerDTO deleted = playerDAO.read(player.getId());
        assertNull(deleted);
    }
}

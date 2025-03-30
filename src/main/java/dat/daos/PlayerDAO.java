package dat.daos;

import dat.dtos.PlayerDTO;
import dat.entities.Player;
import dat.entities.Team;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

public class PlayerDAO implements IDAO<PlayerDTO, Integer> {

    private static final Logger logger = LoggerFactory.getLogger(PlayerDAO.class);
    private static PlayerDAO instance;
    private static EntityManagerFactory emf;

    public PlayerDAO(EntityManagerFactory emf) {
        PlayerDAO.emf = emf;
    }

    // Singleton getInstance-metode
    public static PlayerDAO getInstance(EntityManagerFactory _emf) {
        if (instance == null) {
            synchronized (PlayerDAO.class) { // Thread-safe singleton
                if (instance == null) {
                    instance = new PlayerDAO(_emf);
                }
            }
        }
        return instance;
    }

    @Override
    public PlayerDTO read(Integer id) {
        try (EntityManager em = emf.createEntityManager()) {
            Player player = em.find(Player.class, id);
            if (player == null) {
                logger.warn("Player med id {} blev ikke fundet.", id);
                return null;
            }
            return new PlayerDTO(player);
        } catch (Exception e) {
            logger.error("Fejl ved læsning af player med id {}", id, e);
            throw new RuntimeException("Databasefejl ved læsning af player.", e);
        }
    }

    @Override
    public List<PlayerDTO> readAll() {
        try (EntityManager em = emf.createEntityManager()) {
            List<Player> players = em.createQuery("FROM Player", Player.class).getResultList();
            return players.stream().map(PlayerDTO::new).collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("Fejl ved læsning af alle spillere", e);
            throw new RuntimeException("Databasefejl ved læsning af spillere.", e);
        }
    }

    @Override
    public PlayerDTO create(PlayerDTO playerDTO) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();

            if (playerDTO.getTeamId() == 0) {
                throw new IllegalArgumentException("TeamID er påkrævet for at oprette en spiller.");
            }

            Team team = em.find(Team.class, playerDTO.getTeamId());
            if (team == null) {
                throw new IllegalArgumentException("Hold med id " + playerDTO.getTeamId() + " blev ikke fundet.");
            }

            Player player = new Player();
            player.setName(playerDTO.getName());
            player.setPosition(playerDTO.getPosition());
            player.setPerformanceRating(playerDTO.getPerformanceRating());
            player.setTeam(team);

            em.persist(player);
            em.getTransaction().commit();
            return new PlayerDTO(player);

        } catch (IllegalArgumentException e) {
            logger.warn("Valideringsfejl ved oprettelse af spiller: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Databasefejl ved oprettelse af spiller", e);
            throw new RuntimeException("Databasefejl ved oprettelse af spiller.", e);
        }
    }

    @Override
    public PlayerDTO update(Integer id, PlayerDTO playerDTO) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();

            Player player = em.find(Player.class, id);
            if (player == null) {
                throw new IllegalArgumentException("Spiller med id " + id + " blev ikke fundet.");
            }

            // Opdater felter
            if (playerDTO.getName() != null) player.setName(playerDTO.getName());
            if (playerDTO.getPosition() != null) player.setPosition(playerDTO.getPosition());
            if (playerDTO.getPerformanceRating() != null) player.setPerformanceRating(playerDTO.getPerformanceRating());
            if (playerDTO.getTeamId() != 0) {
                Team team = em.find(Team.class, playerDTO.getTeamId());
                if (team == null) {
                    throw new IllegalArgumentException("Hold med id " + playerDTO.getTeamId() + " blev ikke fundet.");
                }
                player.setTeam(team);
            }

            em.merge(player);
            em.getTransaction().commit();
            return new PlayerDTO(player);

        } catch (IllegalArgumentException e) {
            logger.warn("Valideringsfejl ved opdatering af spiller: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Databasefejl ved opdatering af spiller med id {}", id, e);
            throw new RuntimeException("Databasefejl ved opdatering af spiller.", e);
        }
    }

    @Override
    public void delete(Integer id) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Player player = em.find(Player.class, id);
            if (player == null) {
                throw new IllegalArgumentException("Spiller med id " + id + " blev ikke fundet.");
            }
            em.remove(player);
            em.getTransaction().commit();
        } catch (IllegalArgumentException e) {
            logger.warn("Valideringsfejl ved sletning af spiller: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Databasefejl ved sletning af spiller med id {}", id, e);
            throw new RuntimeException("Databasefejl ved sletning af spiller.", e);
        }
    }

    @Override
    public boolean validatePrimaryKey(Integer id) {
        try (EntityManager em = emf.createEntityManager()) {
            return em.find(Player.class, id) != null;
        } catch (Exception e) {
            logger.error("Fejl ved validering af primary key for spiller med id {}", id, e);
            return false;
        }
    }
}

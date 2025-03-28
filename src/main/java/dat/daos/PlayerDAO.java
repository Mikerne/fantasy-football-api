package dat.daos;

import dat.dtos.PlayerDTO;
import dat.dtos.TeamDTO;
import dat.entities.League;
import dat.entities.Player;
import dat.entities.Team;
import dat.security.entities.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

import java.util.List;
import java.util.stream.Collectors;

public class PlayerDAO implements IDAO<PlayerDTO, Integer> {

    private static PlayerDAO instance;
    private static EntityManagerFactory emf;

    private PlayerDAO(EntityManagerFactory emf) {
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
            return player != null ? new PlayerDTO(player) : null;
        }
    }

    @Override
    public List<PlayerDTO> readAll() {
        try (EntityManager em = emf.createEntityManager()) {
            List<Player> players = em.createQuery("FROM Player", Player.class).getResultList();
            return players.stream().map(PlayerDTO::new).collect(Collectors.toList());
        }
    }

    @Override
    public PlayerDTO create(PlayerDTO playerDTO) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();

            if (playerDTO.getTeamId() == 0) {
                throw new IllegalArgumentException("TeamID is required");
            }

            Team team = em.find(Team.class, playerDTO.getTeamId());
            if (team == null) {
                throw new IllegalArgumentException("Team not found");
            }


            Player player = new Player();
            player.setName(playerDTO.getName());
            player.setPosition(playerDTO.getPosition());
            player.setPerformanceRating(playerDTO.getPerformanceRating());
            player.setTeam(team);

            em.persist(player);
            em.getTransaction().commit();
            return new PlayerDTO(player);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public PlayerDTO update(Integer id, PlayerDTO playerDTO) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();

            Player player = em.find(Player.class, id);
            if (player == null) {
                throw new IllegalArgumentException("Player not found");
            }

            // Opdater felter
            if (playerDTO.getName() != null) player.setName(playerDTO.getName());
            if (playerDTO.getPosition() != null) player.setPosition(playerDTO.getPosition());
            if (playerDTO.getPerformanceRating() != null) player.setPerformanceRating(playerDTO.getPerformanceRating());
            if (playerDTO.getTeamId() != 0) {
                Team team = em.find(Team.class, playerDTO.getTeamId());
                if (team != null) {
                    player.setTeam(team);
                }
            }

            em.merge(player);
            em.getTransaction().commit();
            return new PlayerDTO(player);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void delete(Integer id) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Player player = em.find(Player.class, id);
            if (player != null) {
                em.remove(player);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean validatePrimaryKey(Integer id) {
        try (EntityManager em = emf.createEntityManager()) {
            return em.find(Player.class, id) != null;
        }
    }
}

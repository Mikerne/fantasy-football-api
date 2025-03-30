package dat.daos;

import dat.dtos.TeamDTO;
import dat.entities.League;
import dat.entities.Team;
import dat.security.entities.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

public class TeamDAO implements IDAO<TeamDTO, Integer> {

    private static final Logger logger = LoggerFactory.getLogger(TeamDAO.class);
    private static TeamDAO instance;
    private static EntityManagerFactory emf;

    public TeamDAO(EntityManagerFactory emf) {
        TeamDAO.emf = emf;
    }

    // Singleton getInstance-metode
    public static TeamDAO getInstance(EntityManagerFactory _emf) {
        if (instance == null) {
            synchronized (TeamDAO.class) { // Thread-safe singleton
                if (instance == null) {
                    instance = new TeamDAO(_emf);
                }
            }
        }
        return instance;
    }

    @Override
    public TeamDTO read(Integer id) {
        try (EntityManager em = emf.createEntityManager()) {
            Team team = em.find(Team.class, id);
            if (team == null) {
                logger.warn("Team med id {} blev ikke fundet.", id);
                return null;
            }
            return new TeamDTO(team);
        } catch (Exception e) {
            logger.error("Fejl ved læsning af team med id {}", id, e);
            throw new RuntimeException("Databasefejl ved læsning af team.", e);
        }
    }

    @Override
    public List<TeamDTO> readAll() {
        try (EntityManager em = emf.createEntityManager()) {
            List<Team> teams = em.createQuery("FROM Team", Team.class).getResultList();
            return teams.stream().map(TeamDTO::new).collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("Fejl ved læsning af alle teams", e);
            throw new RuntimeException("Databasefejl ved læsning af teams.", e);
        }
    }

    @Override
    public TeamDTO create(TeamDTO teamDTO) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();

            // Valider input
            if (teamDTO.getCreatorId() == 0) {
                throw new IllegalArgumentException("CreatorId er påkrævet.");
            }
            if (teamDTO.getName() == null || teamDTO.getName().isEmpty()) {
                throw new IllegalArgumentException("Teamnavn er påkrævet.");
            }

            User creator = em.find(User.class, teamDTO.getCreatorId());
            if (creator == null) {
                throw new IllegalArgumentException("Creator med id " + teamDTO.getCreatorId() + " blev ikke fundet.");
            }

            League league = null;
            if (teamDTO.getLeagueId() != null) {
                league = em.find(League.class, teamDTO.getLeagueId());
                if (league == null) {
                    throw new IllegalArgumentException("League med id " + teamDTO.getLeagueId() + " blev ikke fundet.");
                }
            }

            // Opret team
            Team team = new Team();
            team.setName(teamDTO.getName());
            team.setCreatedAt(teamDTO.getCreatedAt());
            team.setCreator(creator);
            team.setLeague(league);

            em.persist(team);
            em.getTransaction().commit();
            return new TeamDTO(team);

        } catch (IllegalArgumentException e) {
            logger.warn("Valideringsfejl ved oprettelse af team: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Databasefejl ved oprettelse af team", e);
            throw new RuntimeException("Databasefejl ved oprettelse af team.", e);
        }
    }

    @Override
    public TeamDTO update(Integer id, TeamDTO teamDTO) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();

            Team team = em.find(Team.class, id);
            if (team == null) {
                throw new IllegalArgumentException("Team med id " + id + " blev ikke fundet.");
            }

            // Opdater felter
            if (teamDTO.getName() != null && !teamDTO.getName().isEmpty()) {
                team.setName(teamDTO.getName());
            }
            if (teamDTO.getCreatedAt() != null) {
                team.setCreatedAt(teamDTO.getCreatedAt());
            }

            // Opdater league hvis nødvendigt
            if (teamDTO.getLeagueId() != null) {
                League league = em.find(League.class, teamDTO.getLeagueId());
                if (league == null) {
                    throw new IllegalArgumentException("League med id " + teamDTO.getLeagueId() + " blev ikke fundet.");
                }
                team.setLeague(league);
            }

            em.merge(team);
            em.getTransaction().commit();
            return new TeamDTO(team);

        } catch (IllegalArgumentException e) {
            logger.warn("Valideringsfejl ved opdatering af team: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Databasefejl ved opdatering af team med id {}", id, e);
            throw new RuntimeException("Databasefejl ved opdatering af team.", e);
        }
    }

    @Override
    public void delete(Integer id) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Team team = em.find(Team.class, id);
            if (team == null) {
                throw new IllegalArgumentException("Team med id " + id + " blev ikke fundet.");
            }
            em.remove(team);
            em.getTransaction().commit();
        } catch (IllegalArgumentException e) {
            logger.warn("Valideringsfejl ved sletning af team: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Databasefejl ved sletning af team med id {}", id, e);
            throw new RuntimeException("Databasefejl ved sletning af team.", e);
        }
    }

    @Override
    public boolean validatePrimaryKey(Integer id) {
        try (EntityManager em = emf.createEntityManager()) {
            return em.find(Team.class, id) != null;
        } catch (Exception e) {
            logger.error("Fejl ved validering af primary key for team med id {}", id, e);
            return false;
        }
    }
}

package dat.daos;

import dat.dtos.TeamDTO;
import dat.entities.League;
import dat.entities.Team;
import dat.security.entities.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

import java.util.List;
import java.util.stream.Collectors;

public class TeamDAO implements IDAO<TeamDTO, Integer> {

    private static TeamDAO instance;
    private static EntityManagerFactory emf;

    private TeamDAO(EntityManagerFactory emf) {
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
            return team != null ? new TeamDTO(team) : null;
        }
    }

    @Override
    public List<TeamDTO> readAll() {
        try (EntityManager em = emf.createEntityManager()) {
            List<Team> teams = em.createQuery("FROM Team", Team.class).getResultList();
            return teams.stream().map(TeamDTO::new).collect(Collectors.toList());
        }
    }

    @Override
    public TeamDTO create(TeamDTO teamDTO) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();

            // Find creator (User) og league, hvis de eksisterer
            User creator = em.find(User.class, teamDTO.getCreatorId());
            League league = teamDTO.getLeagueId() != null ? em.find(League.class, teamDTO.getLeagueId()) : null;

            if (creator == null) {
                throw new IllegalArgumentException("Creator not found");
            }

            // Opret nyt Team objekt
            Team team = new Team();
            team.setName(teamDTO.getName());
            team.setCreatedAt(teamDTO.getCreatedAt());
            team.setCreator(creator);
            team.setLeague(league);

            em.persist(team);
            em.getTransaction().commit();
            return new TeamDTO(team);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public TeamDTO update(Integer id, TeamDTO teamDTO) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();

            // Find det eksisterende team
            Team team = em.find(Team.class, id);
            if (team == null) {
                throw new IllegalArgumentException("Team not found");
            }

            // Opdater felter
            if (teamDTO.getName() != null) team.setName(teamDTO.getName());
            if (teamDTO.getCreatedAt() != null) team.setCreatedAt(teamDTO.getCreatedAt());

            // Opdater creator hvis nødvendigt
            /*if (teamDTO.getCreatorId() != null) {
                User creator = em.find(User.class, teamDTO.getCreatorId());
                if (creator != null) {
                    team.setCreator(creator);
                }
            }*/

            // Opdater league hvis nødvendigt
            if (teamDTO.getLeagueId() != null) {
                League league = em.find(League.class, teamDTO.getLeagueId());
                if (league != null) {
                    team.setLeague(league);
                }
            }

            em.merge(team);
            em.getTransaction().commit();
            return new TeamDTO(team);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void delete(Integer id) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Team team = em.find(Team.class, id);
            if (team != null) {
                em.remove(team);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean validatePrimaryKey(Integer id) {
        try (EntityManager em = emf.createEntityManager()) {
            return em.find(Team.class, id) != null;
        }
    }
}

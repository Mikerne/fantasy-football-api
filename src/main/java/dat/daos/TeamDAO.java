package dat.daos;

import dat.dtos.TeamDTO;
import dat.entities.Team;
import io.javalin.http.Context;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

import java.util.List;

public class TeamDAO implements IDAO {
    private static EntityManagerFactory emf;

    public TeamDAO(EntityManagerFactory emf) {
        TeamDAO.emf = emf;
    }

    @Override
    public TeamDTO read(Integer integer) {
        try(EntityManager em = emf.createEntityManager()) {
            Team team = em.find(Team.class, integer);
            return new TeamDTO(team);
        }
    }

    @Override
    public List readAll() {
        return List.of();
    }

    @Override
    public Object create(Object o) {
        return null;
    }

    @Override
    public Object update(Object o, Object o2) {
        return null;
    }

    @Override
    public void delete(Object o) {

    }

    @Override
    public boolean validatePrimaryKey(Object o) {
        return false;
    }
}

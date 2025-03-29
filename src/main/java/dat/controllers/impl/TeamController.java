package dat.controllers.impl;

import dat.config.HibernateConfig;
import dat.controllers.IController;
import dat.daos.TeamDAO;
import dat.dtos.TeamDTO;
import io.javalin.http.Context;
import jakarta.persistence.EntityManagerFactory;

import java.util.List;

public class TeamController implements IController<TeamDTO, Integer> {

    private final TeamDAO teamDAO;

    public TeamController() {
        EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();
        this.teamDAO = TeamDAO.getInstance(emf);
    }

    @Override
    public void read(Context ctx) {
        try {
            int id = Integer.parseInt(ctx.pathParam("id"));
            TeamDTO team = teamDAO.read(id);
            if (team != null) {
                ctx.json(team);
            } else {
                ctx.status(404).result("Team not found");
            }
        } catch (NumberFormatException e) {
            ctx.status(400).result("Invalid ID");
        }
    }

    @Override
    public void readAll(Context ctx) {
        List<TeamDTO> teams = teamDAO.readAll();
        ctx.json(teams);
    }

    @Override
    public void create(Context ctx) {
        try {
            TeamDTO teamDTO = ctx.bodyAsClass(TeamDTO.class);
            TeamDTO createdTeam = teamDAO.create(teamDTO);
            if (createdTeam != null) {
                ctx.status(201).json(createdTeam);
            } else {
                ctx.status(400).result("Failed to create team");
            }
        } catch (Exception e) {
            e.printStackTrace();
            ctx.status(500).result("Failed to create team");
        }
    }

    @Override
    public void update(Context ctx) {
        try {
            int id = Integer.parseInt(ctx.pathParam("id"));
            TeamDTO teamDTO = ctx.bodyAsClass(TeamDTO.class);
            TeamDTO updatedTeam = teamDAO.update(id, teamDTO);
            if (updatedTeam != null) {
                ctx.json(updatedTeam);
            } else {
                ctx.status(404).result("Team not found");
            }
        } catch (NumberFormatException e) {
            ctx.status(400).result("Invalid ID");
        } catch (Exception e) {
            e.printStackTrace();
            ctx.status(500).result("Failed to update team");
        }
    }

    @Override
    public void delete(Context ctx) {
        try {
            int id = Integer.parseInt(ctx.pathParam("id"));
            boolean exists = teamDAO.validatePrimaryKey(id);
            if (exists) {
                teamDAO.delete(id);
                ctx.status(204).result("Team deleted");
            } else {
                ctx.status(404).result("Team not found");
            }
        } catch (NumberFormatException e) {
            ctx.status(400).result("Invalid ID");
        }
    }

    @Override
    public boolean validatePrimaryKey(Integer id) {
        return teamDAO.validatePrimaryKey(id);
    }

    @Override
    public TeamDTO validateEntity(Context ctx) {
        try {
            return ctx.bodyAsClass(TeamDTO.class);
        } catch (Exception e) {
            return null;
        }
    }
}
package dat.controllers.impl;

import dat.config.HibernateConfig;
import dat.controllers.IController;
import dat.daos.TeamDAO;
import dat.dtos.TeamDTO;
import io.javalin.http.Context;
import jakarta.persistence.EntityManagerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class TeamController implements IController<TeamDTO, Integer> {

    private static final Logger logger = LoggerFactory.getLogger(TeamController.class);
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
                logger.warn("Forsøgte at hente team med id {}, men det blev ikke fundet.", id);
                ctx.status(404).result("Hold med id " + id + " blev ikke fundet.");
            }
        } catch (NumberFormatException e) {
            logger.error("Ugyldigt id format ved hentning: {}", ctx.pathParam("id"), e);
            ctx.status(400).result("Ugyldigt id format. Id skal være et heltal.");
        } catch (Exception e) {
            logger.error("Fejl ved hentning af team med id {}", ctx.pathParam("id"), e);
            ctx.status(500).result("Der opstod en intern fejl ved hentning af holdet.");
        }
    }

    @Override
    public void readAll(Context ctx) {
        try {
            List<TeamDTO> teams = teamDAO.readAll();
            ctx.json(teams);
        } catch (Exception e) {
            logger.error("Fejl ved hentning af alle teams", e);
            ctx.status(500).result("Der opstod en intern fejl ved hentning af holdene.");
        }
    }

    @Override
    public void create(Context ctx) {
        try {
            TeamDTO teamDTO = ctx.bodyAsClass(TeamDTO.class);
            TeamDTO createdTeam = teamDAO.create(teamDTO);
            if (createdTeam != null) {
                ctx.status(201).json(createdTeam);
            } else {
                logger.warn("Oprettelse mislykkedes for team: {}", teamDTO);
                ctx.status(400).result("Kunne ikke oprette hold. Tjek at alle nødvendige data er inkluderet.");
            }
        } catch (Exception e) {
            logger.error("Fejl ved oprettelse af team", e);
            ctx.status(500).result("Der opstod en intern fejl ved oprettelsen af holdet.");
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
                logger.warn("Opdatering mislykkedes: Team med id {} blev ikke fundet eller kunne ikke opdateres.", id);
                ctx.status(404).result("Hold med id " + id + " blev ikke fundet eller kunne ikke opdateres.");
            }
        } catch (NumberFormatException e) {
            logger.error("Ugyldigt id format ved opdatering: {}", ctx.pathParam("id"), e);
            ctx.status(400).result("Ugyldigt id format. Id skal være et heltal.");
        } catch (Exception e) {
            logger.error("Fejl ved opdatering af team med id {}", ctx.pathParam("id"), e);
            ctx.status(500).result("Der opstod en intern fejl ved opdateringen af holdet.");
        }
    }

    @Override
    public void delete(Context ctx) {
        try {
            int id = Integer.parseInt(ctx.pathParam("id"));
            boolean exists = teamDAO.validatePrimaryKey(id);
            if (exists) {
                teamDAO.delete(id);
                ctx.status(204).result("Hold slettet");
            } else {
                logger.warn("Sletning mislykkedes: Hold med id {} findes ikke.", id);
                ctx.status(404).result("Hold med id " + id + " blev ikke fundet.");
            }
        } catch (NumberFormatException e) {
            logger.error("Ugyldigt id format ved sletning: {}", ctx.pathParam("id"), e);
            ctx.status(400).result("Ugyldigt id format. Id skal være et heltal.");
        } catch (Exception e) {
            logger.error("Fejl ved sletning af team med id {}", ctx.pathParam("id"), e);
            ctx.status(500).result("Der opstod en intern fejl ved sletningen af holdet.");
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
            logger.error("Fejl ved validering af teamdata", e);
            return null;
        }
    }
}

package dat.controllers.impl;

import dat.config.HibernateConfig;
import dat.controllers.IController;
import dat.daos.PlayerDAO;
import dat.dtos.PlayerDTO;
import io.javalin.http.Context;
import jakarta.persistence.EntityManagerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class PlayerController implements IController<PlayerDTO, Integer> {

    private static final Logger logger = LoggerFactory.getLogger(PlayerController.class);
    private final PlayerDAO playerDAO;

    public PlayerController(PlayerDAO playerDAO) {
        EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();
        this.playerDAO = PlayerDAO.getInstance(emf);
    }

    @Override
    public void read(Context ctx) {
        try {
            int id = Integer.parseInt(ctx.pathParam("id"));
            PlayerDTO player = playerDAO.read(id);
            if (player != null) {
                ctx.json(player);
            } else {
                ctx.status(404).result("Spiller med id " + id + " blev ikke fundet.");
            }
        } catch (NumberFormatException nfe) {
            logger.error("Ugyldigt id format: {}", ctx.pathParam("id"), nfe);
            ctx.status(400).result("Ugyldigt id format. Id skal være et heltal.");
        } catch (Exception e) {
            logger.error("Fejl ved hentning af spiller", e);
            ctx.status(500).result("Der opstod en intern fejl ved hentning af spilleren.");
        }
    }

    @Override
    public void readAll(Context ctx) {
        try {
            List<PlayerDTO> players = playerDAO.readAll();
            ctx.json(players);
        } catch (Exception e) {
            logger.error("Fejl ved hentning af alle spillere", e);
            ctx.status(500).result("Der opstod en intern fejl ved hentning af spillerne.");
        }
    }

    @Override
    public void create(Context ctx) {
        try {
            PlayerDTO playerDTO = ctx.bodyAsClass(PlayerDTO.class);
            PlayerDTO createdPlayer = playerDAO.create(playerDTO);
            if (createdPlayer != null) {
                ctx.status(201).json(createdPlayer);
            } else {
                logger.warn("Oprettelse mislykkedes for spiller: {}", playerDTO);
                ctx.status(400).result("Kunne ikke oprette spiller. Tjek at alle nødvendige data er inkluderet.");
            }
        } catch (Exception e) {
            logger.error("Fejl ved oprettelse af spiller", e);
            ctx.status(500).result("Der opstod en intern fejl ved oprettelsen af spilleren.");
        }
    }

    @Override
    public void update(Context ctx) {
        try {
            int id = Integer.parseInt(ctx.pathParam("id"));
            PlayerDTO playerDTO = ctx.bodyAsClass(PlayerDTO.class);
            PlayerDTO updatedPlayer = playerDAO.update(id, playerDTO);
            if (updatedPlayer != null) {
                ctx.json(updatedPlayer);
            } else {
                logger.warn("Opdatering mislykkedes for spiller med id: {}", id);
                ctx.status(404).result("Spiller med id " + id + " blev ikke fundet eller kunne ikke opdateres.");
            }
        } catch (NumberFormatException nfe) {
            logger.error("Ugyldigt id format: {}", ctx.pathParam("id"), nfe);
            ctx.status(400).result("Ugyldigt id format. Id skal være et heltal.");
        } catch (Exception e) {
            logger.error("Fejl ved opdatering af spiller", e);
            ctx.status(500).result("Der opstod en intern fejl ved opdateringen af spilleren.");
        }
    }

    @Override
    public void delete(Context ctx) {
        try {
            int id = Integer.parseInt(ctx.pathParam("id"));
            boolean exists = playerDAO.validatePrimaryKey(id);
            if (exists) {
                playerDAO.delete(id);
                ctx.status(204).result("Spiller slettet");
            } else {
                logger.warn("Sletning mislykkedes: Spiller med id {} findes ikke.", id);
                ctx.status(404).result("Spiller med id " + id + " blev ikke fundet.");
            }
        } catch (NumberFormatException nfe) {
            logger.error("Ugyldigt id format ved sletning: {}", ctx.pathParam("id"), nfe);
            ctx.status(400).result("Ugyldigt id format. Id skal være et heltal.");
        } catch (Exception e) {
            logger.error("Fejl ved sletning af spiller", e);
            ctx.status(500).result("Der opstod en intern fejl ved sletningen af spilleren.");
        }
    }

    @Override
    public boolean validatePrimaryKey(Integer integer) {
        return playerDAO.validatePrimaryKey(integer);
    }

    @Override
    public PlayerDTO validateEntity(Context ctx) {
        try {
            return ctx.bodyAsClass(PlayerDTO.class);
        } catch (Exception e) {
            logger.error("Fejl ved validering af spillerdata", e);
            return null;
        }
    }
}

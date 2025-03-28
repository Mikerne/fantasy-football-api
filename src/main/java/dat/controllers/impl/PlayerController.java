package dat.controllers.impl;

import dat.config.HibernateConfig;
import dat.controllers.IController;
import dat.daos.PlayerDAO;
import dat.dtos.PlayerDTO;
import dat.entities.Player;
import io.javalin.http.Context;
import jakarta.persistence.EntityManagerFactory;

import java.util.List;

public class PlayerController implements IController<PlayerDTO, Integer> {

    private final PlayerDAO playerDAO;

    public PlayerController() {
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
                ctx.status(400).result("Player not found");
            }
        } catch (Exception e) {
            ctx.status(400).result("Invalid ID");
        }
    }

    @Override
    public void readAll(Context ctx) {
        try {
            List<PlayerDTO> players = playerDAO.readAll();
            ctx.json(players);
        } catch (Exception e) {
            ctx.status(400).result("Could not find any players");
        }
    }

    @Override
    public void create(Context ctx) {
        try {
            PlayerDTO playerDTO =  ctx.bodyAsClass(PlayerDTO.class);
            PlayerDTO createdPlayer = playerDAO.create(playerDTO);
            if (createdPlayer != null) {
                ctx.json(createdPlayer);
            } else {
                ctx.status(400).result("Could not create player");
            }
        } catch (Exception e) {
            ctx.status(400).result("Could not create player");
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
                ctx.status(400).result("Could not update player");
            }
        } catch (Exception e) {
            ctx.status(400).result("Could not update player");
        }
    }

    @Override
    public void delete(Context ctx) {
        try {
            int id = Integer.parseInt(ctx.pathParam("id"));
            boolean exists = playerDAO.validatePrimaryKey(id);
            if (exists) {
                playerDAO.delete(id);
                ctx.status(204).result("Player deleted");
            } else {
                ctx.status(400).result("Player not found");
            }
        }  catch (Exception e) {
            ctx.status(400).result("Could not delete player");
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
            return null;
        }
    }
}

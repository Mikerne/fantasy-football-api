package dat.routes;

import dat.controllers.impl.PlayerController;
import dat.security.enums.Role;
import io.javalin.apibuilder.EndpointGroup;

import static io.javalin.apibuilder.ApiBuilder.*;
import static io.javalin.apibuilder.ApiBuilder.delete;

public class PlayerRoutes {

    private final PlayerController playerController;

    public PlayerRoutes(PlayerController playerController) {
        this.playerController = playerController;
    }

    protected EndpointGroup getRoutes() {
        return () -> {
            get(playerController::readAll, Role.ANYONE);                   // GET /api/teams
            post(playerController::create, Role.ADMIN);                   // POST /api/teams
            path("/{id}", () -> {
                get(playerController::read);                  // GET /api/teams/{id}
                put(playerController::update, Role.ADMIN);                // PUT /api/teams/{id}
                delete(playerController::delete, Role.ADMIN);             // DELETE /api/teams/{id}
            });
        };
    }
}

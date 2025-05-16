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
            get(playerController::readAll);
            post(playerController::create, Role.ADMIN);
            path("/{id}", () -> {
                get(playerController::read);
                put(playerController::update, Role.ADMIN);
                delete(playerController::delete, Role.ADMIN);
            });
        };
    }
}

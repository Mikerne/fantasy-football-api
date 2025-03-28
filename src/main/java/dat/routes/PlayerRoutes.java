package dat.routes;

import dat.controllers.impl.PlayerController;
import io.javalin.apibuilder.EndpointGroup;

import static io.javalin.apibuilder.ApiBuilder.*;
import static io.javalin.apibuilder.ApiBuilder.delete;

public class PlayerRoutes {

    private final PlayerController playerController = new PlayerController();

    protected EndpointGroup getRoutes() {
        return () -> {
            get(playerController::readAll);                   // GET /api/teams
            post(playerController::create);                   // POST /api/teams
            path("/{id}", () -> {
                get(playerController::read);                  // GET /api/teams/{id}
                put(playerController::update);                // PUT /api/teams/{id}
                delete(playerController::delete);             // DELETE /api/teams/{id}
            });
        };
    }
}

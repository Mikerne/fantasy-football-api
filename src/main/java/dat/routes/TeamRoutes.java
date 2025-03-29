package dat.routes;

import dat.controllers.impl.TeamController;
import io.javalin.apibuilder.EndpointGroup;

import static io.javalin.apibuilder.ApiBuilder.*;

public class TeamRoutes {

    private final TeamController teamController = new TeamController();

    protected EndpointGroup getRoutes() {
        return () -> {
            get(teamController::readAll);                   // GET /api/teams
            post(teamController::create);                   // POST /api/teams
            path("/{id}", () -> {
                get(teamController::read);                  // GET /api/teams/{id}
                put(teamController::update);                // PUT /api/teams/{id}
                delete(teamController::delete);             // DELETE /api/teams/{id}
            });
        };
    }
}

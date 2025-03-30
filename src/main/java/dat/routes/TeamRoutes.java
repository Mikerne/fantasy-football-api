package dat.routes;

import dat.controllers.impl.TeamController;
import dat.security.controllers.SecurityController;
import dat.security.enums.Role;
import io.javalin.apibuilder.EndpointGroup;

import static io.javalin.apibuilder.ApiBuilder.*;

public class TeamRoutes {


    private final TeamController teamController;

    public TeamRoutes(TeamController teamController) {
        this.teamController = teamController;
    }

    protected EndpointGroup getRoutes() {
        return () -> {
            get(teamController::readAll);                   // GET /api/teams
            post(teamController::create, Role.USER);                   // POST /api/teams
            path("/{id}", () -> {
                get(teamController::read);                  // GET /api/teams/{id}
                put(teamController::update, Role.ADMIN, Role.USER);                // PUT /api/teams/{id}
                delete(teamController::delete, Role.ADMIN, Role.USER);             // DELETE /api/teams/{id}
            });
        };
    }
}

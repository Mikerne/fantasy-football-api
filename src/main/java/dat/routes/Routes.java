package dat.routes;

import dat.controllers.impl.*;
import io.javalin.apibuilder.EndpointGroup;

import static io.javalin.apibuilder.ApiBuilder.*;

// Tilføj flere route-klasser her efter behov
public class Routes {
    private final MatchRoutes matchRoutes;
    private final TeamRoutes teamRoutes = new TeamRoutes();
    private final PlayerRoutes playerRoutes = new PlayerRoutes();
    public Routes(MatchController matchController) {
        this.matchRoutes = new MatchRoutes(matchController);
    }

    public EndpointGroup getRoutes() {
        return () -> {
            path("/matches", matchRoutes.getRoutes());
            path("/teams", teamRoutes.getRoutes());// Tilføjer alle Team-endpoints under /api/teams
              path("/players", playerRoutes.getRoutes());
        }
    }
}


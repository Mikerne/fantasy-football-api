package dat.routes;

import io.javalin.apibuilder.EndpointGroup;

import static io.javalin.apibuilder.ApiBuilder.*;

// Tilføj flere route-klasser her efter behov
public class Routes {

    private final TeamRoutes teamRoutes = new TeamRoutes();
    private final PlayerRoutes playerRoutes = new PlayerRoutes();

    public EndpointGroup getRoutes() {
        return () -> {
            path("/teams", teamRoutes.getRoutes());// Tilføjer alle Team-endpoints under /api/teams
            path("/players", playerRoutes.getRoutes());
        };
    }
}

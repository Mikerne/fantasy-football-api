package dat.routes;

import dat.controllers.impl.*;
import io.javalin.apibuilder.EndpointGroup;

import static io.javalin.apibuilder.ApiBuilder.*;

public class Routes {
    private final MatchRoutes matchRoutes;
    private final PointRoutes pointRoutes;
    private final TeamRoutes teamRoutes = new TeamRoutes();
    private final PlayerRoutes playerRoutes = new PlayerRoutes();

    public Routes(MatchController matchController, PointController pointController) {
        this.matchRoutes = new MatchRoutes(matchController);
        this.pointRoutes = new PointRoutes(pointController);
    }

    public EndpointGroup getRoutes() {
        return () -> {
            path("/matches", matchRoutes.getRoutes());
            path("/points", pointRoutes.getRoutes());
            path("/teams", teamRoutes.getRoutes());
            path("/players", playerRoutes.getRoutes());
        };
    }
}

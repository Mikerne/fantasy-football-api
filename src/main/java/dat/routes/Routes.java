package dat.routes;

import dat.controllers.impl.*;
import io.javalin.apibuilder.EndpointGroup;

import static io.javalin.apibuilder.ApiBuilder.*;

public class Routes {
    private final MatchRoutes matchRoutes;

    public Routes(MatchController matchController) {
        this.matchRoutes = new MatchRoutes(matchController);
    }

    public EndpointGroup getRoutes() {
        return () -> {
            path("/matches", matchRoutes.getRoutes());
        };
    }
}


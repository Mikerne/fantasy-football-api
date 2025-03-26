package dat.routes;

import dat.controllers.impl.MatchController;
import io.javalin.apibuilder.EndpointGroup;

import static io.javalin.apibuilder.ApiBuilder.*;

public class MatchRoutes {
    private final MatchController matchController;

    public MatchRoutes(MatchController matchController) {
        this.matchController = matchController;
    }

    public EndpointGroup getRoutes() {
        return () -> {
            get("", matchController::getAllMatches); // <--- VIGTIG!
        };
    }
}

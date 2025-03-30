package dat.routes;

import dat.controllers.impl.MatchController;
import dat.security.enums.Role;
import io.javalin.apibuilder.EndpointGroup;

import static io.javalin.apibuilder.ApiBuilder.*;

public class MatchRoutes {
    private final MatchController matchController;

    public MatchRoutes(MatchController matchController) {
        this.matchController = matchController;
    }

    public EndpointGroup getRoutes() {
        return () -> {
            get("", matchController::getAllMatches);
            post("import", matchController::importTodayMatches);
            get("import", ctx -> {
                matchController.importTodayMatches(ctx);
                ctx.result("Import triggered via GET");
            });
            get("{id}", matchController::getMatchFromId);
            put("{id}", matchController::updateMatch, Role.ADMIN);
            delete("{id}", matchController::deleteMatch, Role.ADMIN);
            delete("", matchController::deleteAllMatches, Role.ADMIN);
        };
    }
}

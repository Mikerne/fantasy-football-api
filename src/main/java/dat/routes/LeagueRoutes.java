package dat.routes;

import dat.controllers.impl.LeagueController;
import dat.entities.League;
import io.javalin.apibuilder.EndpointGroup;
import static io.javalin.apibuilder.ApiBuilder.*;
public class LeagueRoutes {
    private final LeagueController leagueController;
    public LeagueRoutes(LeagueController leagueController) {
        this.leagueController = leagueController;
    }

    public EndpointGroup getRoutes() {
        return () -> {
            get("", leagueController::getAllLeagues);
            get("{id}", leagueController::getLeagueFromId);
        };
    }
}

package dat.routes;

import dat.controllers.impl.*;
import dat.security.controllers.SecurityController;
import dat.security.routes.SecurityRoutes;  // Importér SecurityRoutes
import io.javalin.apibuilder.EndpointGroup;

import static io.javalin.apibuilder.ApiBuilder.*;

public class Routes {
    private final MatchRoutes matchRoutes;
    private final PointRoutes pointRoutes;
    private final TeamRoutes teamRoutes;
    private final PlayerRoutes playerRoutes;
    private final LeagueRoutes leagueRoutes;

    public Routes(MatchController matchController, PointController pointController, LeagueController leagueController, TeamController teamController, PlayerController playerController) {
        this.matchRoutes = new MatchRoutes(matchController);
        this.pointRoutes = new PointRoutes(pointController);
        this.leagueRoutes = new LeagueRoutes(leagueController);
        this.playerRoutes = new PlayerRoutes(playerController);
        this.teamRoutes = new TeamRoutes(teamController);

    }

    public EndpointGroup getRoutes() {
        return () -> {
            path("/matches", matchRoutes.getRoutes());
            path("/points", pointRoutes.getRoutes());
            path("/teams", teamRoutes.getRoutes());
            path("/players", playerRoutes.getRoutes());
            path("/leagues", leagueRoutes.getRoutes());
        };
    }
}

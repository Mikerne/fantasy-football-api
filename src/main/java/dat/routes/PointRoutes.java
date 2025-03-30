package dat.routes;

import dat.controllers.impl.PointController;
import io.javalin.apibuilder.EndpointGroup;

import static io.javalin.apibuilder.ApiBuilder.get;
import static io.javalin.apibuilder.ApiBuilder.post;

public class PointRoutes {
    private final PointController pointController;
    public PointRoutes(PointController pointController) {
        this.pointController = pointController;
    }

    public EndpointGroup getRoutes() {
        return () -> {
            get("", pointController::getAllUsersPoints);
            get("{id}", pointController::getUserPoints);
            post("", pointController::addOrUpdateUserPoints);
        };
    }
}

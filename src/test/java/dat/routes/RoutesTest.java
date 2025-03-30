/*package dat.routes;

import dat.controllers.impl.LeagueController;
import dat.controllers.impl.MatchController;
import dat.controllers.impl.PointController;
import io.javalin.apibuilder.EndpointGroup;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class RoutesTest {

    @Test
    void getRoutes() {
        // Arrange: Opret mock-kontrollere
        MatchController matchController = mock(MatchController.class);
        PointController pointController = mock(PointController.class);
        LeagueController leagueController = mock(LeagueController.class);

        // Opret Routes-objektet med de nødvendige controllere
        Routes routes = new Routes(matchController, pointController, leagueController);

        // Act: Hent EndpointGroup fra getRoutes()
        EndpointGroup endpointGroup = routes.getRoutes();

        // Assert: EndpointGroup skal ikke være null
        assertNotNull(endpointGroup, "EndpointGroup bør ikke være null");

        // Undlad at kalde addEndpoints(), da det kræver en routes() kontekst
    }
}*/

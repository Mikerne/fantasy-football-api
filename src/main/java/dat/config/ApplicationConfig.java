package dat.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import dat.controllers.impl.*;
import dat.daos.PlayerDAO;
import dat.daos.TeamDAO;
import dat.daos.impl.LeaugeDAO;
import dat.daos.impl.MatchDAO;
import dat.daos.impl.PointDAO;
import dat.routes.Routes;
import dat.security.controllers.AccessController;
import dat.security.controllers.SecurityController;
import dat.security.daos.SecurityDAO;
import dat.security.enums.Role;
import dat.security.exceptions.ApiException;
import dat.security.routes.SecurityRoutes;
import dat.utils.Utils;
import io.javalin.Javalin;
import io.javalin.config.JavalinConfig;
import io.javalin.http.Context;
import jakarta.persistence.EntityManagerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ApplicationConfig {

    private static final EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();

    // DAO & Controllere ...
    private static final MatchDAO matchDAO = new MatchDAO(emf);
    private static final PointDAO pointDAO = new PointDAO(emf);
    private static final LeaugeDAO leaugeDAO = new LeaugeDAO(emf);
    private static final PlayerDAO playerDAO = new PlayerDAO(emf);
    private static final TeamDAO teamDAO = new TeamDAO(emf);
    private static final SecurityDAO securityDAO = new SecurityDAO(emf);

    private static final MatchController matchController = new MatchController(matchDAO);
    private static final PointController pointController = new PointController(pointDAO);
    private static final LeagueController leagueController = new LeagueController(leaugeDAO);
    private static final PlayerController playerController = new PlayerController(playerDAO);
    private static final TeamController teamController = new TeamController(teamDAO);
    private static final SecurityController securityController = new SecurityController(securityDAO);

    private static final Routes routes = new Routes(
            matchController,
            pointController,
            leagueController,
            teamController,
            playerController);

    private static ObjectMapper jsonMapper = new Utils().getObjectMapper();
    private static AccessController accessController = new AccessController();
    private static Logger logger = LoggerFactory.getLogger(ApplicationConfig.class);
    private static int count = 1;

    public static void configuration(JavalinConfig config) {
        config.showJavalinBanner = false;
        config.bundledPlugins.enableRouteOverview("/routes", Role.ANYONE);
        config.router.contextPath = "/fantasyfootball/api"; // base path for all endpoints
        config.router.apiBuilder(routes.getRoutes());
        config.router.apiBuilder(SecurityRoutes.getSecuredRoutes());
        config.router.apiBuilder(SecurityRoutes.getSecurityRoutes());
    }

    public static Javalin startServer(int port) {
        Javalin app = Javalin.create(ApplicationConfig::configuration);

        // CORS-opsætning til React (http://localhost:5173)
        app.before(ctx -> {
            ctx.header("Access-Control-Allow-Origin", "http://localhost:5173"); // Din React app origin
            ctx.header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
            ctx.header("Access-Control-Allow-Headers", "Content-Type, Authorization");
            ctx.header("Access-Control-Allow-Credentials", "true"); // hvis du bruger cookies / auth headers
        });

        // OPTIONS preflight request håndtering
        app.options("/*", ctx -> {
            ctx.status(204);
        });

        app.beforeMatched(accessController::accessHandler);
        app.after(ApplicationConfig::afterRequest);

        app.exception(Exception.class, ApplicationConfig::generalExceptionHandler);
        app.exception(ApiException.class, ApplicationConfig::apiExceptionHandler);

        app.start(port);
        return app;
    }

    public static void afterRequest(Context ctx) {
        String requestInfo = ctx.req().getMethod() + " " + ctx.req().getRequestURI();
        logger.info(" Request {} - {} was handled with status code {}", count++, requestInfo, ctx.status());
    }

    public static void stopServer(Javalin app) {
        app.stop();
    }

    private static void generalExceptionHandler(Exception e, Context ctx) {
        logger.error("An unhandled exception occurred", e.getMessage());
        ctx.json(Utils.convertToJsonMessage(ctx, "error", e.getMessage()));
    }

    public static void apiExceptionHandler(ApiException e, Context ctx) {
        ctx.status(e.getCode());
        logger.warn("An API exception occurred: Code: {}, Message: {}", e.getCode(), e.getMessage());
        ctx.json(Utils.convertToJsonMessage(ctx, "warning", e.getMessage()));
    }

    public static EntityManagerFactory getEmf() {
        return emf;
    }

    public static MatchDAO getMatchDAO() {
        return matchDAO;
    }
}

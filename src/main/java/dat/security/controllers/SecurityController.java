package dat.security.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.nimbusds.jose.JOSEException;
import dat.utils.Utils;
import dat.config.HibernateConfig;
import dat.security.daos.ISecurityDAO;
import dat.security.daos.SecurityDAO;
import dat.security.entities.User;
import dat.security.exceptions.ApiException;
import dat.security.exceptions.NotAuthorizedException;
import dat.security.exceptions.ValidationException;
import dk.bugelhartmann.ITokenSecurity;
import dk.bugelhartmann.TokenSecurity;
import dk.bugelhartmann.UserDTO;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import io.javalin.http.HttpStatus;
import io.javalin.http.UnauthorizedResponse;
import io.javalin.security.RouteRole;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Purpose: To handle security in the API
 * Author: Thomas Hartmann
 */
public class SecurityController implements ISecurityController {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final ITokenSecurity tokenSecurity = new TokenSecurity();
    private static SecurityDAO securityDAO;
    private static SecurityController instance;
    private static final Logger logger = LoggerFactory.getLogger(SecurityController.class);

    public SecurityController(SecurityDAO securityDAO) { }

    public static SecurityController getInstance() {
        if (instance == null) {
            instance = new SecurityController(securityDAO);
        }
        securityDAO = new SecurityDAO(HibernateConfig.getEntityManagerFactory());
        return instance;
    }

    @Override
    public Handler login() {
        return ctx -> {
            ObjectNode returnObject = objectMapper.createObjectNode();
            try {
                UserDTO user = ctx.bodyAsClass(UserDTO.class);
                UserDTO verifiedUser = securityDAO.getVerifiedUser(user.getUsername(), user.getPassword());
                System.out.println(user);
                String token = createToken(verifiedUser);

                ctx.status(HttpStatus.OK)
                        .json(returnObject
                                .put("token", token)
                                .put("username", verifiedUser.getUsername()));

            } catch (EntityNotFoundException | ValidationException e) {
                logger.warn("Login-fejl: {}", e.getMessage());
                ctx.status(HttpStatus.UNAUTHORIZED)
                        .json(returnObject.put("msg", e.getMessage()));
            } catch (Exception e) {
                logger.error("Uventet fejl ved login", e);
                ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .json(returnObject.put("msg", "Unexpected error during login"));
            }
        };
    }

    @Override
    public Handler register() {
        return ctx -> {
            ObjectNode returnObject = objectMapper.createObjectNode();
            try {
                UserDTO userInput = ctx.bodyAsClass(UserDTO.class);
                User created = securityDAO.createUser(userInput.getUsername(), userInput.getPassword());

                String token = createToken(new UserDTO(created.getUsername(), Set.of("USER")));
                ctx.status(HttpStatus.CREATED)
                        .json(returnObject
                                .put("token", token)
                                .put("username", created.getUsername()));

            } catch (EntityExistsException e) {
                logger.warn("Bruger findes allerede: {}", e.getMessage());
                ctx.status(HttpStatus.UNPROCESSABLE_CONTENT)
                        .json(returnObject.put("msg", "User already exists"));
            } catch (Exception e) {
                logger.error("Uventet fejl ved registrering", e);
                ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .json(returnObject.put("msg", "Unexpected error during registration"));
            }
        };
    }

    @Override
    public Handler authenticate() {
        return ctx -> {
            String header = ctx.header("Authorization");
            if (header == null || !header.startsWith("Bearer ")) {
                throw new UnauthorizedResponse("Missing or malformed Authorization header");
            }
            String token = header.substring(7);
            UserDTO verifiedTokenUser = verifyToken(token);

            if (verifiedTokenUser == null) {
                throw new UnauthorizedResponse("Invalid User or Token");
            }
            logger.info("User verified: {}", verifiedTokenUser.getUsername());
            ctx.attribute("user", verifiedTokenUser);
        };
    }

    @Override
    public boolean authorize(UserDTO user, Set<RouteRole> allowedRoles) {
        if (user == null) {
            throw new UnauthorizedResponse("You need to log in.");
        }
        Set<String> roleNames = allowedRoles.stream()
                .map(RouteRole::toString)
                .collect(Collectors.toSet());
        return user.getRoles().stream()
                .map(String::toUpperCase)
                .anyMatch(roleNames::contains);
    }

    @Override
    public String createToken(UserDTO user) {
        try {
            String ISSUER = System.getenv("ISSUER") != null
                    ? System.getenv("ISSUER")
                    : Utils.getPropertyValue("ISSUER", "config.properties");
            String TOKEN_EXPIRE_TIME = System.getenv("TOKEN_EXPIRE_TIME") != null
                    ? System.getenv("TOKEN_EXPIRE_TIME")
                    : Utils.getPropertyValue("TOKEN_EXPIRE_TIME", "config.properties");
            String SECRET_KEY = System.getenv("SECRET_KEY") != null
                    ? System.getenv("SECRET_KEY")
                    : Utils.getPropertyValue("SECRET_KEY", "config.properties");

            return tokenSecurity.createToken(user, ISSUER, TOKEN_EXPIRE_TIME, SECRET_KEY);
        } catch (Exception e) {
            logger.error("Fejl ved oprettelse af token", e);
            throw new ApiException(500, "Could not create token");
        }
    }

    @Override
    public UserDTO verifyToken(String token) {
        try {
            String SECRET = System.getenv("SECRET_KEY") != null
                    ? System.getenv("SECRET_KEY")
                    : Utils.getPropertyValue("SECRET_KEY", "config.properties");

            if (tokenSecurity.tokenIsValid(token, SECRET) && tokenSecurity.tokenNotExpired(token)) {
                return tokenSecurity.getUserWithRolesFromToken(token);
            } else {
                throw new NotAuthorizedException(403, "Token is not valid");
            }
        } catch (ParseException | JOSEException e) {
            logger.error("Fejl ved verificering af token", e);
            throw new ApiException(401, "Unauthorized. Could not verify token");
        } catch (NotAuthorizedException e) {
            throw new RuntimeException(e);
        }
    }

    @NotNull
    public Handler addRole() {
        return ctx -> {
            ObjectNode returnObject = objectMapper.createObjectNode();
            try {
                String newRole = ctx.bodyAsClass(ObjectNode.class).get("role").asText();
                if (newRole == null || newRole.trim().isEmpty()) {
                    throw new ValidationException("Role must not be empty");
                }
                UserDTO user = ctx.attribute("user");
                securityDAO.addRole(user, newRole);
                ctx.status(HttpStatus.OK)
                        .json(returnObject.put("msg", "Role " + newRole + " added to user"));
            } catch (EntityNotFoundException e) {
                ctx.status(HttpStatus.NOT_FOUND)
                        .json(returnObject.put("msg", "User not found"));
            } catch (ValidationException e) {
                ctx.status(HttpStatus.BAD_REQUEST)
                        .json(returnObject.put("msg", e.getMessage()));
            }
        };
    }

    public void healthCheck(@NotNull Context ctx) {
        ctx.status(200).json("{\"msg\": \"API is up and running\"}");
    }
}

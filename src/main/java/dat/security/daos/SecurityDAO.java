package dat.security.daos;

import dat.security.entities.Role;
import dat.security.entities.User;
import dat.security.exceptions.ApiException;
import dat.security.exceptions.ValidationException;
import dk.bugelhartmann.UserDTO;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;
import java.util.stream.Collectors;

public class SecurityDAO implements ISecurityDAO {

    private static final Logger logger = LoggerFactory.getLogger(SecurityDAO.class);
    private static ISecurityDAO instance;
    private static EntityManagerFactory emf;

    public SecurityDAO(EntityManagerFactory _emf) {
        emf = _emf;
    }

    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    @Override
    public UserDTO getVerifiedUser(String username, String password) throws ValidationException {
        if (username == null || password == null) {
            throw new ValidationException("Username and password must not be null.");
        }
        try (EntityManager em = getEntityManager()) {
            User user = em.createQuery("SELECT u FROM User u WHERE u.username = :username", User.class)
                    .setParameter("username", username)
                    .getResultStream()
                    .findFirst()
                    .orElse(null);

            if (user == null) {
                logger.warn("Ingen bruger fundet med username: {}", username);
                throw new EntityNotFoundException("No user found with username: " + username);
            }

            user.getRoles().size(); // Force eager loading
            if (!user.verifyPassword(password)) {
                logger.warn("Forkert password for username: {}", username);
                throw new ValidationException("Wrong password.");
            }

            Set<String> roles = user.getRoles().stream()
                    .map(Role::getRoleName)
                    .collect(Collectors.toSet());

            return new UserDTO(user.getUsername(), roles);

        } catch (EntityNotFoundException | ValidationException e) {
            logger.warn("Valideringsfejl: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Databasefejl ved verificering af bruger", e);
            throw new ApiException(500, "Database error during user verification.");
        }
    }

    @Override
    public User createUser(String username, String password) {
        if (username == null || password == null) {
            try {
                throw new ValidationException("Username and password must not be null.");
            } catch (ValidationException e) {
                throw new RuntimeException(e);
            }
        }
        try (EntityManager em = getEntityManager()) {
            em.getTransaction().begin();

            // Tjek om brugeren allerede eksisterer
            User existingUser = em.createQuery("SELECT u FROM User u WHERE u.username = :username", User.class)
                    .setParameter("username", username)
                    .getResultStream()
                    .findFirst()
                    .orElse(null);

            if (existingUser != null) {
                logger.warn("Bruger med username {} findes allerede.", username);
                throw new EntityExistsException("User with username " + username + " already exists.");
            }

            // Opret ny bruger
            User user = new User(username, password);
            Role userRole = em.find(Role.class, "user");

            if (userRole == null) {
                userRole = new Role("user");
                em.persist(userRole);
            }

            user.addRole(userRole);
            em.persist(user);
            em.getTransaction().commit();

            logger.info("Ny bruger oprettet med username: {}", username);
            return user;

        } catch (EntityExistsException e) {
            logger.warn("Valideringsfejl: {}", e.getMessage());
            throw new ApiException(400, e.getMessage());
        } catch (Exception e) {
            logger.error("Databasefejl ved oprettelse af bruger", e);
            throw new ApiException(500, "Database error during user creation.");
        }
    }

    @Override
    public User addRole(UserDTO userDTO, String newRole) {
        if (userDTO == null || newRole == null || newRole.trim().isEmpty()) {
            try {
                throw new ValidationException("UserDTO and role must not be null or empty.");
            } catch (ValidationException e) {
                throw new RuntimeException(e);
            }
        }

        try (EntityManager em = getEntityManager()) {
            em.getTransaction().begin();

            // Find brugeren
            User user = em.createQuery("SELECT u FROM User u WHERE u.username = :username", User.class)
                    .setParameter("username", userDTO.getUsername())
                    .getResultStream()
                    .findFirst()
                    .orElse(null);

            if (user == null) {
                logger.warn("Ingen bruger fundet med username: {}", userDTO.getUsername());
                throw new EntityNotFoundException("No user found with username: " + userDTO.getUsername());
            }

            // Find eller opret rolle
            Role role = em.find(Role.class, newRole);
            if (role == null) {
                logger.info("Opretter ny rolle: {}", newRole);
                role = new Role(newRole);
                em.persist(role);
            }

            if (user.getRoles().contains(role)) {
                logger.warn("Bruger {} har allerede rollen: {}", user.getUsername(), newRole);
                throw new ApiException(400, "User already has role: " + newRole);
            }

            // Tilføj rolle og gem ændringer
            user.addRole(role);
            em.merge(user);
            em.getTransaction().commit();

            logger.info("Rolle '{}' tilføjet til bruger '{}'", newRole, user.getUsername());
            return user;

        } catch (EntityNotFoundException | ApiException e) {
            logger.warn("Valideringsfejl: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Databasefejl ved tilføjelse af rolle", e);
            throw new ApiException(500, "Database error during role addition.");
        }
    }
}

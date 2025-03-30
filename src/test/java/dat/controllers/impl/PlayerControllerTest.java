package dat.controllers.impl;

import dat.daos.PlayerDAO;
import dat.dtos.PlayerDTO;
import io.javalin.http.Context;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;

class PlayerControllerTest {

    @InjectMocks
    private PlayerController playerController;

    @Mock
    private PlayerDAO playerDAO;

    @Mock
    private Context ctx;

    @BeforeEach
    void setUp() throws Exception {
        // Open mocks for the annotations
        MockitoAnnotations.openMocks(this);

        // Mock EntityManagerFactory to avoid the real initialization logic
        EntityManagerFactory emf = mock(EntityManagerFactory.class);

        // Use the mocked EntityManagerFactory to initialize PlayerDAO
        PlayerDAO playerDAO = PlayerDAO.getInstance(emf);

        // Manually create the PlayerController with the mocked PlayerDAO
        playerController = new PlayerController(playerDAO);

        when(ctx.status(anyInt())).thenReturn(ctx);
    }

    @Test
    void readSuccess() {
        // Arrange
        when(ctx.pathParam("id")).thenReturn("1");
        PlayerDTO playerDTO = new PlayerDTO(1, "Test Player", "Forward", 1, 10.0);
        when(playerDAO.read(1)).thenReturn(playerDTO);

        // Act
        playerController.read(ctx);

        // Assert
        verify(ctx).json(playerDTO);
    }

    @Test
    void readNotFound() {
        // Arrange
        when(ctx.pathParam("id")).thenReturn("1");
        when(playerDAO.read(1)).thenReturn(null);

        // Act
        playerController.read(ctx);

        // Assert
        verify(ctx).status(404);
        verify(ctx).result("Spiller med id 1 blev ikke fundet.");
    }

    @Test
    void readInvalidId() {
        // Arrange
        when(ctx.pathParam("id")).thenReturn("invalid");

        // Act
        playerController.read(ctx);

        // Assert
        verify(ctx).status(400);
        verify(ctx).result("Ugyldigt id format. Id skal være et heltal.");
    }

    @Test
    void readAllSuccess() {
        // Arrange
        List<PlayerDTO> players = Arrays.asList(
                new PlayerDTO(1, "Player1", "Forward", 1, 9.5),
                new PlayerDTO(2, "Player2", "Midfielder", 2, 8.5)
        );
        when(playerDAO.readAll()).thenReturn(players);

        // Act
        playerController.readAll(ctx);

        // Assert
        verify(ctx).json(players);
    }

    @Test
    void createSuccess() {
        // Arrange
        PlayerDTO playerDTO = new PlayerDTO(0, "New Player", "Defender", 1, 7.0);
        when(ctx.bodyAsClass(PlayerDTO.class)).thenReturn(playerDTO);
        PlayerDTO createdPlayer = new PlayerDTO(3, "New Player", "Defender", 1, 7.0);
        when(playerDAO.create(playerDTO)).thenReturn(createdPlayer);

        // Act
        playerController.create(ctx);

        // Assert
        verify(ctx).status(201);
        verify(ctx).json(createdPlayer);
    }

    @Test
    void createFailure() {
        // Arrange
        PlayerDTO playerDTO = new PlayerDTO(0, "New Player", "Defender", 1, 7.0);
        when(ctx.bodyAsClass(PlayerDTO.class)).thenReturn(playerDTO);
        when(playerDAO.create(playerDTO)).thenReturn(null);

        // Act
        playerController.create(ctx);

        // Assert
        verify(ctx).status(400);
        verify(ctx).result("Kunne ikke oprette spiller. Tjek at alle nødvendige data er inkluderet.");
    }

    @Test
    void updateSuccess() {
        // Arrange
        when(ctx.pathParam("id")).thenReturn("1");
        PlayerDTO playerDTO = new PlayerDTO(1, "Updated Player", "Midfielder", 1, 8.0);
        when(ctx.bodyAsClass(PlayerDTO.class)).thenReturn(playerDTO);
        when(playerDAO.update(1, playerDTO)).thenReturn(playerDTO);

        // Act
        playerController.update(ctx);

        // Assert
        verify(ctx).json(playerDTO);
    }

    @Test
    void updateNotFound() {
        // Arrange
        when(ctx.pathParam("id")).thenReturn("1");
        PlayerDTO playerDTO = new PlayerDTO(1, "Updated Player", "Midfielder", 1, 8.0);
        when(ctx.bodyAsClass(PlayerDTO.class)).thenReturn(playerDTO);
        when(playerDAO.update(1, playerDTO)).thenReturn(null);

        // Act
        playerController.update(ctx);

        // Assert
        verify(ctx).status(404);
        verify(ctx).result("Spiller med id 1 blev ikke fundet eller kunne ikke opdateres.");
    }

    @Test
    void updateInvalidId() {
        // Arrange
        when(ctx.pathParam("id")).thenReturn("invalid");

        // Act
        playerController.update(ctx);

        // Assert
        verify(ctx).status(400);
        verify(ctx).result("Ugyldigt id format. Id skal være et heltal.");
    }

    @Test
    void deleteSuccess() {
        // Arrange
        when(ctx.pathParam("id")).thenReturn("1");
        when(playerDAO.validatePrimaryKey(1)).thenReturn(true);

        // Act
        playerController.delete(ctx);

        // Assert
        verify(playerDAO).delete(1);
        verify(ctx).status(204);
        verify(ctx).result("Spiller slettet");
    }

    @Test
    void deleteNotFound() {
        // Arrange
        when(ctx.pathParam("id")).thenReturn("1");
        when(playerDAO.validatePrimaryKey(1)).thenReturn(false);

        // Act
        playerController.delete(ctx);

        // Assert
        verify(ctx).status(404);
        verify(ctx).result("Spiller med id 1 blev ikke fundet.");
    }

    @Test
    void deleteInvalidId() {
        // Arrange
        when(ctx.pathParam("id")).thenReturn("invalid");

        // Act
        playerController.delete(ctx);

        // Assert
        verify(ctx).status(400);
        verify(ctx).result("Ugyldigt id format. Id skal være et heltal.");
    }
}

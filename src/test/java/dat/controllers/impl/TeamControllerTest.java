/*package dat.controllers.impl;

import dat.daos.TeamDAO;
import dat.dtos.TeamDTO;
import dat.entities.League;
import dat.entities.Team;
import dat.security.entities.User;
import io.javalin.http.Context;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

import static org.mockito.Mockito.*;

class TeamControllerTest {

    @InjectMocks
    private TeamController teamController;

    @Mock
    private Team team;

    @Mock
    private TeamDAO teamDAO;

    @Mock
    private Context ctx;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);

        // Mock EntityManagerFactory for at undgå den reelle initialisering
        EntityManagerFactory emf = mock(EntityManagerFactory.class);

        // Brug den mocked EntityManagerFactory til at initialisere TeamDAO
        teamDAO = TeamDAO.getInstance(emf);  // Brug den mocked DAO i stedet for en ny instans

        // Manuelt opret TeamController med den mocked TeamDAO
        teamController = new TeamController(teamDAO);

        // Brug reflection til at sætte den mocked teamDAO i TeamController
        Field daoField = TeamController.class.getDeclaredField("teamDAO");
        daoField.setAccessible(true);
        daoField.set(teamController, teamDAO);  // Sørg for at injicere den mocked DAO

        // Mock status-metoden, så den returnerer ctx (for method chaining)
        when(ctx.status(anyInt())).thenReturn(ctx);
    }




    @Test
    void read() {
        // Arrange
        when(ctx.pathParam("id")).thenReturn("1");

        // Mock creator
        User creator = mock(User.class);
        when(creator.getId()).thenReturn(100); // Sørg for, at ID ikke er null

        // Mock team
        Team team = mock(Team.class);
        when(team.getId()).thenReturn(1);
        when(team.getName()).thenReturn("Test Team");
        when(team.getCreatedAt()).thenReturn(Timestamp.from(Instant.parse("2025-03-30T00:00:00Z")));
        when(team.getCreator()).thenReturn(creator); // Sørg for at der returneres en bruger
        when(team.getLeague()).thenReturn(mock(League.class));

        // Konverter team til DTO uden at få NullPointerException
        TeamDTO teamDTO = new TeamDTO(team);

        when(teamDAO.read(1)).thenReturn(teamDTO);

        // Act
        teamController.read(ctx);

        // Assert
        verify(ctx).json(teamDTO);
    }


    @Test
    void readNotFound() {
        // Arrange
        when(ctx.pathParam("id")).thenReturn("1");
        when(teamDAO.read(1)).thenReturn(null);

        // Act
        teamController.read(ctx);

        // Assert
        verify(ctx).status(404);
        verify(ctx).result("Hold med id 1 blev ikke fundet.");
    }

    @Test
    void readInvalidId() {
        // Arrange
        when(ctx.pathParam("id")).thenReturn("invalid");

        // Act
        teamController.read(ctx);

        // Assert
        verify(ctx).status(400);
        verify(ctx).result("Ugyldigt id format. Id skal være et heltal.");
    }

    @Test
    void readAll() {
        // Arrange
        Team team1 = mock(Team.class);
        when(team1.getId()).thenReturn(1);
        when(team1.getName()).thenReturn("Team 1");
        when(team.getCreatedAt()).thenReturn(Timestamp.from(Instant.parse("2025-03-30T00:00:00Z")));
        when(team1.getCreator()).thenReturn(mock(User.class));
        when(team1.getLeague()).thenReturn(mock(League.class));

        TeamDTO teamDTO1 = new TeamDTO(team1);

        Team team2 = mock(Team.class);
        when(team2.getId()).thenReturn(2);
        when(team2.getName()).thenReturn("Team 2");
        when(team.getCreatedAt()).thenReturn(Timestamp.from(Instant.parse("2025-03-30T00:00:00Z")));
        when(team2.getCreator()).thenReturn(mock(User.class));
        when(team2.getLeague()).thenReturn(mock(League.class));
        TeamDTO teamDTO2 = new TeamDTO(team2);


        when(teamDAO.readAll()).thenReturn(List.of(teamDTO1, teamDTO2));



        // Act
        teamController.readAll(ctx);

        // Assert
        verify(ctx).json(List.of(teamDTO1, teamDTO2));
    }

    @Test
    void create() {
        // Arrange
        User creator = mock(User.class);
        when(creator.getId()).thenReturn(1);

        Team team = new Team();
        team.setCreator(creator); // Sørg for at team har en creator

        TeamDTO teamDTO = new TeamDTO(team);
        when(ctx.bodyAsClass(TeamDTO.class)).thenReturn(teamDTO);
        when(teamDAO.create(teamDTO)).thenReturn(teamDTO);

        // Act
        teamController.create(ctx);

        // Assert
        verify(ctx).status(201);
        verify(ctx).json(teamDTO);
    }



    @Test
    void update() {
        // Arrange
        when(ctx.pathParam("id")).thenReturn("1");

        TeamDTO teamDTO = mock(TeamDTO.class);
        when(ctx.bodyAsClass(TeamDTO.class)).thenReturn(teamDTO);

        // Opret et Team-objekt med en ikke-null creator
        Team updatedTeam = new Team();
        User creator = new User();
        creator.setId(1); // Antag, at User har en setId-metode
        updatedTeam.setCreator(creator);

        when(teamDAO.update(1, teamDTO)).thenReturn(new TeamDTO(updatedTeam));

        // Act
        teamController.update(ctx);

        // Assert
        verify(ctx).json(any(TeamDTO.class));
    }


    @Test
    void updateNotFound() {
        // Arrange
        when(ctx.pathParam("id")).thenReturn("1");
        TeamDTO teamDTO = mock(TeamDTO.class);
        when(ctx.bodyAsClass(TeamDTO.class)).thenReturn(teamDTO);
        when(teamDAO.update(1, teamDTO)).thenReturn(null);

        // Act
        teamController.update(ctx);

        // Assert
        verify(ctx).status(404);
        verify(ctx).result("Hold med id 1 blev ikke fundet eller kunne ikke opdateres.");
    }

    @Test
    void updateInvalidId() {
        // Arrange
        when(ctx.pathParam("id")).thenReturn("invalid");

        // Act
        teamController.update(ctx);

        // Assert
        verify(ctx).status(400);
        verify(ctx).result("Ugyldigt id format. Id skal være et heltal.");
    }

    @Test
    void delete() {
        // Arrange
        when(ctx.pathParam("id")).thenReturn("1");
        when(teamDAO.validatePrimaryKey(1)).thenReturn(true);

        // Act
        teamController.delete(ctx);

        // Assert
        verify(teamDAO).delete(1);
        verify(ctx).status(204);
        verify(ctx).result("Hold slettet");
    }

    @Test
    void deleteNotFound() {
        // Arrange
        when(ctx.pathParam("id")).thenReturn("1");
        when(teamDAO.validatePrimaryKey(1)).thenReturn(false);

        // Act
        teamController.delete(ctx);

        // Assert
        verify(ctx).status(404);
        verify(ctx).result("Hold med id 1 blev ikke fundet.");
    }

    @Test
    void deleteInvalidId() {
        // Arrange
        when(ctx.pathParam("id")).thenReturn("invalid");

        // Act
        teamController.delete(ctx);

        // Assert
        verify(ctx).status(400);
        verify(ctx).result("Ugyldigt id format. Id skal være et heltal.");
    }
}*/

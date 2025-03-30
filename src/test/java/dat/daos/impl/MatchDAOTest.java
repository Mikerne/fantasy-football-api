package dat.daos.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import dat.config.HibernateConfig;
import dat.entities.Match;
import org.junit.jupiter.api.*;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class MatchDAOTest {

    private MatchDAO matchDAO;

    @BeforeAll
    void setup() {
        HibernateConfig.setTest(true);
        matchDAO = new MatchDAO(HibernateConfig.getEntityManagerFactoryForTest());
    }

    @Test
    void testSaveTodayMatchesAndRead() throws IOException {
        String json = """
        {
          "matches": [
            {
              "id": 12345,
              "utcDate": "2025-03-30T20:00:00Z",
              "homeTeam": { "name": "Team A" },
              "awayTeam": { "name": "Team B" },
              "score": {
                "fullTime": {
                  "home": 3,
                  "away": 1
                }
              },
              "status": "FINISHED"
            }
          ]
        }
        """;

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(json);

        matchDAO.saveTodayMatches(jsonNode);

        Match match = matchDAO.read(12345);
        assertNotNull(match);
        assertEquals("Team A", match.getHomeTeam());
        assertEquals("3-1", match.getResult());
    }

    @Test
    void testDeleteMatch() throws Exception {
        // Først opret kampen
        String json = """
    {
      "matches": [
        {
          "id": 12346,
          "utcDate": "2025-03-30T20:00:00Z",
          "homeTeam": { "name": "Team C" },
          "awayTeam": { "name": "Team D" },
          "score": {
            "fullTime": {
              "home": 1,
              "away": 1
            }
          },
          "status": "FINISHED"
        }
      ]
    }
    """;

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(json);
        matchDAO.saveTodayMatches(jsonNode);

        // Slet kampen
        boolean deleted = matchDAO.deleteMatch(12346);
        assertTrue(deleted);

        // Bekræft at den er slettet
        assertNull(matchDAO.read(12346));
    }

    @Test
    void testReadAll() {
        List<Match> all = matchDAO.readAll();
        assertNotNull(all);
    }
}

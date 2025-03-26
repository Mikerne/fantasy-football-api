package dat.importers;

import com.fasterxml.jackson.databind.JsonNode;
import dat.daos.impl.MatchDAO;
import dat.entities.Match;
import dat.entities.Team;
import dat.external.FootballDataService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MatchImporter {

    private final MatchDAO matchDAO;
    private final EntityManagerFactory emf;
    private final FootballDataService footballService = new FootballDataService();

    public MatchImporter(MatchDAO matchDAO, EntityManagerFactory emf) {
        this.matchDAO = matchDAO;
        this.emf = emf;
    }


    public void getMatchData() {
        FootballDataService api = new FootballDataService();
        JsonNode data = api.fetchMatches("2024-03-25");

        JsonNode matches = data.get("response"); // bemærk: her ligger kampene
        for (JsonNode match : matches) {
            String home = match.get("teams").get("home").get("name").asText();
            String away = match.get("teams").get("away").get("name").asText();
            System.out.println("🏟️ " + home + " vs " + away);
        }



    }

    public void importMatches(String fromDate, String toDate) {
        JsonNode data = footballService.fetchMatches(fromDate);
        JsonNode matches = data.get("matches");

        if (matches == null) return;

        for (JsonNode matchNode : matches) {
            try {
                Match match = new Match();

                // Formatér datoen
                String utcDate = matchNode.get("utcDate").asText();
                Date matchDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").parse(utcDate);
                match.setMatchDate(matchDate);
                match.setCreatedAt(new Timestamp(System.currentTimeMillis()));
                match.setResult(matchNode.get("score").get("fullTime").get("home").asText() + "-" +
                        matchNode.get("score").get("fullTime").get("away").asText());

                // Dummy hold – burde mappes rigtigt
                Team home = new Team(); home.setId(1);
                Team away = new Team(); away.setId(2);
                match.setHomeTeam(home);
                match.setAwayTeam(away);

                // Gem i database
                EntityManager em = emf.createEntityManager();
                em.getTransaction().begin();
                em.persist(match);
                em.getTransaction().commit();
                em.close();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

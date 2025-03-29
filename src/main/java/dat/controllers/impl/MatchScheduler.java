package dat.controllers.impl;

import com.fasterxml.jackson.databind.JsonNode;
import dat.daos.impl.MatchDAO;
import dat.entities.Match;
import dat.external.FootballDataService;

import java.sql.Timestamp;
import java.time.Duration;
import java.util.concurrent.*;
import java.util.Date;

public class MatchScheduler {

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private final FootballDataService api = new FootballDataService();
    private final MatchDAO matchDAO;

    public MatchScheduler(MatchDAO matchDAO) {
        this.matchDAO = matchDAO;
    }

    public void scheduleMatchResultUpdate(Match match) {
        System.out.println("Kamp Data: " + match);
        Timestamp matchTime = (Timestamp) match.getMatchDate();
        long runAtMillis = matchTime.getTime() + Duration.ofMinutes(105).toMillis(); // kamp + 105 minutter
        long now = System.currentTimeMillis();
        long delay = runAtMillis - now;

        if (delay <= 0) {
            System.out.println("⏱ Kampen er allerede overstået – opdaterer nu!");
            updateMatchResult(match.getId());
            return;
        }

        Date expectedTime = new Date(System.currentTimeMillis() + delay);
        System.out.println("📅 Kamp " + match.getId() + " opdateres ca.: " + expectedTime);

        scheduler.schedule(() -> updateMatchResult(match.getId()), delay, TimeUnit.MILLISECONDS);
    }


    private void updateMatchResult(int matchId) {
        JsonNode matchData = api.getMatchDetails(matchId); // <-- Du skal implementere denne metode
        System.out.println("📊 Match detaljer:\n" + matchData.toPrettyString());

        //TJekker om kampen er færdig
        if (!matchData.get("status").asText().equals("FINISHED")) {
            System.out.println("Kampen er ikke færdig endnu – prøver igen om 5 minutter");
            scheduler.schedule(() -> updateMatchResult(matchId), 5, TimeUnit.MINUTES);
            return;
        }

        String result = api.extractResultFrom(matchData); // <-- også denne fx. "2-1"

        matchDAO.updateMatchResult(matchId, result); // <-- lav en metode der opdaterer match
        System.out.println("Kamp opdateret med resultat: " + result);
    }



}
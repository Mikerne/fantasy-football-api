package dat;

import com.fasterxml.jackson.databind.JsonNode;
import dat.config.ApplicationConfig;
import dat.external.FootballDataService;


public class Main {

    public static void main(String[] args) {
        System.out.println("Hello World!");
        ApplicationConfig.startServer(7070);


        FootballDataService service = new FootballDataService();
        //JsonNode todayMatches = service.getTodayMatches();
       // System.out.println("📅 Kampe i dag:\n" + todayMatches.toPrettyString());

//
//        JsonNode laligaTeams = service.getAllTeamsInLeaugeId(2014);
//        System.out.println("👥 La Liga hold:\n" + laligaTeams.toPrettyString());
//
//        JsonNode team = service.getTeamById(81);
//        System.out.println("👥 Barcelona:\n" + team.toPrettyString());





    }
}
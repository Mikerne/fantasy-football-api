package dat.controllers.impl;

import com.fasterxml.jackson.databind.JsonNode;
import dat.daos.impl.MatchDAO;
import dat.dtos.MatchDTO;
import dat.entities.Match;
import dat.external.FootballDataService;
import io.javalin.http.Context;

import java.sql.Timestamp;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

public class MatchController {
    private final MatchDAO matchDAO;
    private final FootballDataService footballDataService;
    public MatchController(MatchDAO matchDAO) {
        this.matchDAO = matchDAO;
        this.footballDataService = new FootballDataService();
    }


    public void importTodayMatches(Context ctx) {
        JsonNode todayMatches = footballDataService.getTodayMatches();
        System.out.println(todayMatches);
        matchDAO.saveTodayMatches(todayMatches);
        System.out.println("✅ Dagens kampe er gemt i databasen.");
    }

    public void getAllMatches(Context ctx) {
        List<MatchDTO> dtos = getAllMatchDTOs();
        ctx.json(dtos);
    }

    public void deleteMatch(Context ctx) {
        int id = Integer.parseInt(ctx.pathParam("id"));
        boolean status = matchDAO.deleteMatch(id);
        ctx.status(status ? 204 : 404);
    }

    public void deleteAllMatches(Context ctx) {
        matchDAO.deleteAllMatches();
        ctx.status(204);
    }

    public void getMatchFromId(Context ctx) {
        int id = Integer.parseInt(ctx.pathParam("id"));
        Match match = matchDAO.read(id);
        ctx.json(new MatchDTO(
                match.getId(),
                match.getMatchDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate(),
                match.getHomeTeam(),
                match.getAwayTeam(),
                match.getResult(),
                match.getCreatedAt().toInstant().atZone(ZoneId.systemDefault()).toLocalDate(),
                match.getStatus()
        ));
    }


    public void updateMatch(Context ctx) {
        int id = Integer.parseInt(ctx.pathParam("id"));
        Match match = matchDAO.read(id);
        MatchDTO matchDTO = ctx.bodyAsClass(MatchDTO.class);
        match.setMatchDate(Timestamp.valueOf(matchDTO.getMatchDate().atStartOfDay()));
        match.setHomeTeam(matchDTO.getHomeTeam());
        match.setAwayTeam(matchDTO.getAwayTeam());
        match.setResult(matchDTO.getResult());
        match.setStatus(matchDTO.getStatus());
        matchDAO.updateMatch(match);
        ctx.status(204);
    }


    private List<MatchDTO> getAllMatchDTOs() {
        List<Match> matches = matchDAO.readAll();
        return matches.stream().map(match -> new MatchDTO(
                match.getId(),
                match.getMatchDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate(),
                match.getHomeTeam(),
                match.getAwayTeam(),
                match.getResult(),
                match.getCreatedAt().toInstant().atZone(ZoneId.systemDefault()).toLocalDate(),
                match.getStatus()
        )).collect(Collectors.toList());
    }





}

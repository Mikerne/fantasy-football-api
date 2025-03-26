package dat.controllers.impl;

import dat.daos.impl.MatchDAO;
import dat.dtos.MatchDTO;
import dat.entities.Match;
import io.javalin.http.Context;

import java.sql.Timestamp;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

public class MatchController {
    private final MatchDAO matchDAO;
    public MatchController(MatchDAO matchDAO) {
        this.matchDAO = matchDAO;
    }

    public void getAllMatches(Context ctx) {
        List<MatchDTO> dtos = getAllMatchDTOs();
        ctx.json(dtos);
    }


    private List<MatchDTO> getAllMatchDTOs() {
        List<Match> matches = matchDAO.readAll();

        return matches.stream().map(match -> new MatchDTO(
                match.getId(),
                match.getMatchDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate(),
                match.getHomeTeam().getId(),
                match.getAwayTeam().getId(),
                match.getResult(),
                match.getCreatedAt().toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
        )).collect(Collectors.toList());
    }

}

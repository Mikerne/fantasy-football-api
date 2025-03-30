package dat.controllers.impl;

import dat.daos.impl.LeaugeDAO;
import dat.dtos.LeagueDTO;
import dat.dtos.MatchDTO;
import dat.entities.League;
import dat.entities.Match;
import io.javalin.http.Context;

import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

public class LeagueController {
    private final LeaugeDAO leagueDAO;

    public LeagueController(LeaugeDAO leagueDAO) {
        this.leagueDAO = leagueDAO;
    }

    public void getAllLeagues(Context ctx) {
        List<LeagueDTO> dtos = getAllLeagueDTOs();
        ctx.json(dtos);
    }

    public void getLeagueFromId(Context ctx) {
        int id = Integer.parseInt(ctx.pathParam("id"));
        League league = leagueDAO.read(id);
        ctx.json(new LeagueDTO(
                league.getId(),
                league.getName(),
                league.getCreatedAt(),
                league.getOwner() != null ? league.getOwner().getId() : null
        ));
    }



    private List<LeagueDTO> getAllLeagueDTOs() {
        List<League> leagues = leagueDAO.readAll();
        return leagues.stream()
                .filter(league -> league.getOwner() != null) // Fjern leagues uden owner
                .map(league -> new LeagueDTO(
                        league.getId(),
                        league.getName(),
                        league.getCreatedAt(),
                        league.getOwner().getId()
                ))
                .collect(Collectors.toList());
    }



}

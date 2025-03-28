package dat.controllers.impl;

import dat.daos.impl.PointDAO;
import dat.dtos.PointCreateDTO;
import dat.dtos.PointDTO;
import dat.entities.Point;
import io.javalin.http.Context;

import java.util.List;

public class PointController {
    private final PointDAO pointDAO;

    public PointController(PointDAO pointDAO) {
        this.pointDAO = pointDAO;
    }


    public void getAllUsersPoints(Context ctx) {
        List<PointDTO> dtos = getAllPointDTOs();
        ctx.json(dtos);
    }


    public void getUserPoints(Context ctx) {
        int id = Integer.parseInt(ctx.pathParam("id"));
        List<Point> points = pointDAO.readUserPoints(id);

        List<PointDTO> dtos = points.stream().map(point -> new PointDTO(
                point.getId(),
                point.getUser().getId(),
                point.getMatch().getId(),
                point.getPointsEarned(),
                point.getEarnedAt().toString()
        )).toList();

        ctx.json(dtos);
    }


    private List<PointDTO> getAllPointDTOs() {
        return pointDAO.readAll().stream()
                .map(p -> new PointDTO(
                        p.getId(),
                        p.getUser().getId(),
                        p.getMatch().getId(),
                        p.getPointsEarned(),
                        p.getEarnedAt().toString()
                ))
                .toList();
    }

    public void addOrUpdateUserPoints(Context ctx) {
        PointCreateDTO dto = ctx.bodyAsClass(PointCreateDTO.class);
        pointDAO.addOrUpdatePoints(dto);
        ctx.status(201).json("✅ Point tilføjet/opdateret");
    }


}

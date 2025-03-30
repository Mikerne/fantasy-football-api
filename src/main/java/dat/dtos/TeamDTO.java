package dat.dtos;

import dat.entities.Team;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TeamDTO {
    private int id;
    private String name;
    private Timestamp createdAt;
    private int creatorId;
    private Integer leagueId;

    public TeamDTO(Team team) {
        this.id = team.getId();
        this.name = team.getName();
        this.createdAt = team.getCreatedAt();
        this.creatorId = team.getCreator().getId();
        this.leagueId = team.getLeague() != null ? team.getLeague().getId() : null;
    }
}

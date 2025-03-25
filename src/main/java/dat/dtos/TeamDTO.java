package dat.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TeamDTO {
    private int id;
    private String name;
    private LocalDate createdAt;
    private int creatorId;
    private Integer leagueId;
}

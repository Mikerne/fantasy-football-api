package dat.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MatchDTO {
    private Integer id;
    private LocalDate matchDate;
    private int homeTeamId;
    private int awayTeamId;
    private String result;
    private LocalDate createdAt;
}
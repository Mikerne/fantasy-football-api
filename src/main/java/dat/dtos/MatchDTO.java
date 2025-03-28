package dat.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MatchDTO {
    private int id;
    private LocalDate matchDate;
    private String homeTeam;
    private String awayTeam;
    private String result;
    private LocalDate createdAt;
    private String status;
}
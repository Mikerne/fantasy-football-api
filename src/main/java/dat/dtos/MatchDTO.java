package dat.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MatchDTO {
    private Integer id;
    private Timestamp matchDate;
    private String homeTeam;
    private String awayTeam;
    private String result;
    private Timestamp createdAt;
}

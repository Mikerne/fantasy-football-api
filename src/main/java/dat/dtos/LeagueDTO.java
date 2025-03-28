package dat.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LeagueDTO {
    private int id;
    private String name;
    private Timestamp createdAt;
    private int ownerId;
}

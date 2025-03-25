package dat.dtos;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PointDTO {
    private int id;
    private int userId;
    private int matchId;
    private int pointsEarned;
    private String earnedAt;
}

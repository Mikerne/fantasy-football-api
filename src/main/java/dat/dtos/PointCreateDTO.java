package dat.dtos;

import lombok.Data;

@Data
public class PointCreateDTO {
    private int userId;
    private int matchId;
    private int pointsEarned;
}

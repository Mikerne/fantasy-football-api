package dat.dtos;

import dat.entities.Player;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlayerDTO {
    private int id;
    private String name;
    private String position;
    private int teamId;
    private int performanceRating;


    public PlayerDTO(Player player) {
        this.id = player.getId();
    }
}

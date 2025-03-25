package dat.entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "PLAYERS")
@Data
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;
    private String position;

    @ManyToOne
    @JoinColumn(name = "team_id")
    private Team team;

    @Column(name = "performance_rating")
    private int performanceRating;

}

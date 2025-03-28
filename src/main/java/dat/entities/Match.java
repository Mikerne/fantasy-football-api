package dat.entities;


import jakarta.persistence.*;
import lombok.Data;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
@Entity
@Table(name = "MATCHES")
@Data
public class Match {

    @Id
    @Column(name = "id")
    private int id; // <-- ikke længere auto-genereret

    @Column(name = "match_date")
    private Timestamp matchDate; // <--- ændret fra Date


    @ManyToOne
    @JoinColumn(name = "home_team_id")
    private Team homeTeam;

    @ManyToOne
    @JoinColumn(name = "away_team_id")
    private Team awayTeam;

    private String result;

    @Column(name = "created_at")
    private Timestamp createdAt;

    @Column(name = "status")
    private String status;
}
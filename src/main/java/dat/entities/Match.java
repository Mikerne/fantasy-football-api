package dat.entities;


import jakarta.persistence.*;
import lombok.Data;

import java.security.Timestamp;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "MATCHES")
@Data
public class Match {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "match_date")
    private Date matchDate;

    @ManyToOne
    @JoinColumn(name = "home_team")
    private Team homeTeam;


    @ManyToOne
    @JoinColumn(name = "away_team")
    private Team awayTeam;

    private String result;

    @Column(name = "created_at")
    private Timestamp createdAt;

    @OneToMany(mappedBy = "match")
    private List<Point> points;
}

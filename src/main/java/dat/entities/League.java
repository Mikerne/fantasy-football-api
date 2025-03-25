package dat.entities;


import dat.security.entities.User;
import jakarta.persistence.*;
import lombok.Data;

import java.security.Timestamp;
import java.util.List;

@Entity
@Table(name = "LEAGUES")
@Data
public class League {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    @Column(name = "created_at")
    private Timestamp createdAt;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;

    @OneToMany(mappedBy = "league")
    private List<Team> teams;
}

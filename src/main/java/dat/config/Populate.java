package dat.config;

import dat.entities.League;
import dat.entities.Team;
import dat.security.entities.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class Populate {
    public static void main(String[] args) {


        EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();


            try (EntityManager em = emf.createEntityManager()) {
                em.getTransaction().begin();

                // Opret test data for User (creator)
                User creator = new User();
                creator.setUsername("admin");
                creator.setPassword("password123"); // Husk at bruge hashing i produktion!
                em.persist(creator);

                // Opret test data for League
                League league = new League();
                league.setName("Fantasy League");
                em.persist(league);

                // Opret test data for Team
                Team team1 = new Team();
                team1.setName("Dream Team");
                team1.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));
                team1.setCreator(creator);
                team1.setLeague(league);
                em.persist(team1);

                Team team2 = new Team();
                team2.setName("Superstars");
                team2.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));
                team2.setCreator(creator);
                team2.setLeague(league);
                em.persist(team2);

                em.getTransaction().commit();
                System.out.println("Test data has been populated successfully.");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


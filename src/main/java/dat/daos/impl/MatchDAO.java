package dat.daos.impl;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import dat.controllers.impl.MatchScheduler;
import dat.entities.Match;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

public class MatchDAO {
    private static EntityManagerFactory emf;

    public MatchDAO(EntityManagerFactory emf) {
        MatchDAO.emf = emf;
    }

    public List<Match> readAll() {
        try (EntityManager em = emf.createEntityManager()) {
            return em.createQuery("SELECT m FROM Match m", Match.class).getResultList();
        }
    }


    public Match read(int id) {
        try (EntityManager em = emf.createEntityManager()) {
            return em.find(Match.class, id);
        }
    }


    public void saveTodayMatches(JsonNode matchData) {
        if (!matchData.has("matches")) return;
        System.out.println("🔧 Gemmer kampe i dag" + matchData);
        ArrayNode matches = (ArrayNode) matchData.get("matches");

        try (EntityManager em = emf.createEntityManager()) {
            EntityTransaction tx = em.getTransaction();
            tx.begin();

            for (JsonNode m : matches) {
                int matchId = m.get("id").asInt(); // Henter match-id for at tjekke om kampen allerede er gemt
                // Tjek om kampen allerede findes
                Match existing = em.find(Match.class, matchId);
                if (existing != null) {
                    System.out.println("⚠️ Kamp med ID " + matchId + " findes allerede – springer over.");
                    continue;
                }

                Match match = new Match();
                match.setId(m.get("id").asInt()); // Bruger match-id som primary key i databasen

                // Dato
                String utcDate = m.get("utcDate").asText();
                Timestamp ts = Timestamp.valueOf(utcDate.replace("T", " ").replace("Z", ""));
                match.setMatchDate(ts); // ts er allerede en Timestamp


                // Sæt hold-navne direkte
                String homeTeamName = m.get("homeTeam").get("name").asText();
                String awayTeamName = m.get("awayTeam").get("name").asText();
                match.setHomeTeam(homeTeamName);
                match.setAwayTeam(awayTeamName);

                // Resultat
                JsonNode score = m.get("score").get("fullTime");
                String result = (score.get("home").isNull() || score.get("away").isNull())
                        ? null
                        : score.get("home").asInt() + "-" + score.get("away").asInt();
                match.setResult(result);

                match.setCreatedAt(new Timestamp(System.currentTimeMillis()));


                //henter Status fra matchen
                String status = m.get("status").asText();
                match.setStatus(status);

                //Opretter vores cron jobs:
                new MatchScheduler(this).scheduleMatchResultUpdate(match);


                em.persist(match);
            }

            tx.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //PUT /api/matches/{id} Kommer også til at køre via et cron job
    public void updateMatchResult(int matchId, String result) {
        System.out.println("🔧 Opdaterer kamp " + matchId + " med resultat: " + result);
    }

    public void updateMatch(Match match) {
        try (EntityManager em = emf.createEntityManager()) {
            EntityTransaction tx = em.getTransaction();
            tx.begin();
            em.merge(match);
            tx.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public boolean deleteMatch(int matchId) {
        try (EntityManager em = emf.createEntityManager()) {
            EntityTransaction tx = em.getTransaction();
            tx.begin();

            Match match = em.find(Match.class, matchId);
            if (match == null) {
                System.out.println("⚠️ Kamp med ID " + matchId + " findes ikke.");
                return false;
            }

            em.remove(match);
            tx.commit();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}

package dat.external;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class FootballDataService {

    private static final String FOOTBALLDATA_API_KEY = "262343d711ec42bc89f246010e3a1bcc"; // <-- udskift
    private static final String FOOTBALLDATA_BASE_URL = "https://api.football-data.org/v4/";

    private final ObjectMapper mapper = new ObjectMapper();

    private JsonNode makeRequestFootballData(String endpoint) {
        try {
            URL url = new URL(FOOTBALLDATA_BASE_URL + endpoint);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("X-Auth-Token", FOOTBALLDATA_API_KEY);

            InputStream responseStream = conn.getResponseCode() >= 400 ? conn.getErrorStream() : conn.getInputStream();
            Scanner scanner = new Scanner(responseStream).useDelimiter("\\A");
            String response = scanner.hasNext() ? scanner.next() : "";

            return mapper.readTree(response);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    //Laliga ID: 2014
    public JsonNode getAllTeamsInLeaugeId(int leagueId) {
        try {
            String endpoint = "competitions/" + leagueId + "/teams";
            return makeRequestFootballData(endpoint);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Integer getPlayerIdFromSofascore(String playerName) {
        try {
            String encoded = URLEncoder.encode(playerName, StandardCharsets.UTF_8);
            String url = "https://api.sofascore.com/api/v1/search/multi?q=" + encoded;
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(new URL(url));
            JsonNode results = root.get("results");

            for (JsonNode result : results) {
                if (!result.get("type").asText().equals("player")) continue;

                JsonNode entity = result.get("entity");
                if (entity == null || !entity.has("name")) continue;

                String name = entity.get("name").asText();
                if (name.equalsIgnoreCase(playerName)) {
                    return entity.get("id").asInt();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("❌ Spilleren blev ikke fundet: " + playerName);
        return null;
    }


    public Double getLatestPlayerRating(int playerId) {
        try {
            String url = "https://api.sofascore.com/api/v1/player/" + playerId + "/matches/last/0";
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(new URL(url));
            JsonNode matches = root.get("events");

            for (JsonNode match : matches) {
                if (!match.has("playerStatistics")) continue;

                JsonNode statistics = match.get("playerStatistics");
                for (JsonNode teamStats : statistics) {
                    for (JsonNode player : teamStats.get("players")) {
                        if (player.get("player").get("id").asInt() == playerId) {
                            if (player.has("rating")) {
                                return player.get("rating").asDouble();
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("❌ Kunne ikke finde rating for spiller-ID: " + playerId);
        return null;
    }


    public Double getLatestPlayerRatingByName(String playerName) {
        Integer playerId = getPlayerIdFromSofascore(playerName);
        if (playerId == null) return null;
        return getLatestPlayerRating(playerId);
    }


    public JsonNode getTeamById(int teamId) {
        try {
            String endpoint = "teams/" + teamId;
            return makeRequestFootballData(endpoint);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public JsonNode getTodayMatches() {
        System.out.println("Forsøger at hente kampe?");
        try {
            String endpoint = "matches";
            JsonNode response = makeRequestFootballData(endpoint);

            if (response != null && response.has("matches")) {
                ArrayNode allMatches = (ArrayNode) response.get("matches");
                ArrayNode firstFiveMatches = mapper.createArrayNode();

                for (int i = 0; i < Math.min(5, allMatches.size()); i++) {
                    firstFiveMatches.add(allMatches.get(i));
                }

                ObjectNode limitedResponse = mapper.createObjectNode();
                limitedResponse.set("matches", firstFiveMatches);
                return limitedResponse;
            }

            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }




    public JsonNode getMatchDetails(int matchId) {
        try {
            String finalEndpoints = "matches/" + matchId;

            return makeRequestFootballData(finalEndpoints);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String extractResultFrom(JsonNode matchData) {
        try {
            JsonNode score = matchData.get("score");
            JsonNode fullTime = score.get("fullTime");

            int homeTeamGoals = fullTime.get("home").asInt();
            int awayTeamGoals = fullTime.get("away").asInt();

            return homeTeamGoals + "-" + awayTeamGoals;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }




}

package dat.external;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import okhttp3.*;
import java.util.HashMap;
import java.util.Map;
public class PlayerRating {

    private static final String API_KEY = "dd5e7e9cacmsh30e865f51ae9607p1b34d7jsne6a6a3a2426c";
    private static final String API_HOST = "api-football-v1.p.rapidapi.com";
    private static final String BASE_URL = "https://api-football-v1.p.rapidapi.com/v3/";

    private final OkHttpClient client = new OkHttpClient();
    private final ObjectMapper mapper = new ObjectMapper();

    // I toppen af klassen:
    private static final Map<String, Integer> LEAGUE_IDS = new HashMap<>();
    static {
        LEAGUE_IDS.put("La Liga", 140);
        LEAGUE_IDS.put("Premier League", 39);
        LEAGUE_IDS.put("Serie A", 135);
        LEAGUE_IDS.put("Bundesliga", 78);
        LEAGUE_IDS.put("Ligue 1", 61);
        LEAGUE_IDS.put("Eredivisie", 88);
        // Tilføj flere hvis nødvendigt
    }
    /**
     * Søger en spiller via navn og returnerer spillerens ID.
     */
    private Integer getPlayerIdByName(String playerName) {
        try {
            HttpUrl url = HttpUrl.parse(BASE_URL + "players/profiles")
                    .newBuilder()
                    .addQueryParameter("search", playerName)
                    .build();

            Request request = new Request.Builder()
                    .url(url)
                    .get()
                    .addHeader("x-rapidapi-key", API_KEY)
                    .addHeader("x-rapidapi-host", API_HOST)
                    .build();

            Response response = client.newCall(request).execute();
            if (!response.isSuccessful()) {
                System.out.println("Failed to get player ID: " + response.code());
                return null;
            }

            String json = response.body().string();
            JsonNode node = mapper.readTree(json);
            JsonNode responseArray = node.get("response");

            if (responseArray != null && responseArray.isArray() && responseArray.size() > 0) {
                return responseArray.get(0).get("player").get("id").asInt();
            } else {
                System.out.println("No player found with name: " + playerName);
                return null;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Henter spillerstatistik og rating via spillerens navn, liga og sæson.
     */
    public JsonNode getPlayerStats(String playerName, String leagueName, int season) {
        Integer leagueId = LEAGUE_IDS.get(leagueName);
        if (leagueId == null) {
            System.out.println("Unknown league: " + leagueName);
            return null;
        }

        Integer playerId = getPlayerIdByName(playerName);
        if (playerId == null) return null;

        try {
            HttpUrl url = HttpUrl.parse(BASE_URL + "players")
                    .newBuilder()
                    .addQueryParameter("id", String.valueOf(playerId))
                    .addQueryParameter("league", String.valueOf(leagueId))
                    .addQueryParameter("season", String.valueOf(season))
                    .build();

            Request request = new Request.Builder()
                    .url(url)
                    .get()
                    .addHeader("x-rapidapi-key", API_KEY)
                    .addHeader("x-rapidapi-host", API_HOST)
                    .build();

            Response response = client.newCall(request).execute();
            if (!response.isSuccessful()) {
                System.out.println("Failed to get player stats: " + response.code());
                return null;
            }

            String json = response.body().string();
            return mapper.readTree(json);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public ObjectNode getPlayerGameStatsSimplified(String playerName, String liga, int season) {
        JsonNode fullData = getPlayerStats(playerName, liga, season);

        if (fullData == null || !fullData.has("response")) return null;

        JsonNode response = fullData.get("response");
        if (!response.isArray() || response.size() == 0) return null;

        JsonNode playerNode = response.get(0).get("player");
        JsonNode gamesNode = response.get(0).get("statistics").get(0).get("games");

        ObjectMapper mapper = new ObjectMapper();
        ObjectNode simplified = mapper.createObjectNode();

        // Tilføj navn
        simplified.put("name", playerNode.get("name").asText());

        // Tilføj alle games-data
        simplified.set("games", gamesNode);

        return simplified;
    }

}

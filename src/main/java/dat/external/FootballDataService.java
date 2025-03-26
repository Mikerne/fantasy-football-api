package dat.external;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class FootballDataService {

    private static final String API_KEY = "6fb941cb79mshc49238750e664d2p109109jsndf6b90eabaf6"; // <-- sæt din key ind her
    private static final String BASE_URL = "https://api-football-v1.p.rapidapi.com/v3/";
    private static final String API_HOST = "api-football-v1.p.rapidapi.com";

    private final ObjectMapper mapper = new ObjectMapper();

    public JsonNode fetchMatches(String dateFrom) {
        try {
            String endpoint = BASE_URL + "fixtures?date=" + dateFrom; // RapidAPI understøtter én dato pr. request
            URL url = new URL(endpoint);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("X-RapidAPI-Key", API_KEY);
            conn.setRequestProperty("X-RapidAPI-Host", API_HOST);
            conn.setRequestMethod("GET");

            InputStream stream = conn.getResponseCode() >= 400 ? conn.getErrorStream() : conn.getInputStream();
            Scanner scanner = new Scanner(stream).useDelimiter("\\A");
            String response = scanner.hasNext() ? scanner.next() : "";

            System.out.println("⚠️ FEJL-RESPONS:");
            System.out.println(response); // <-- Se hvad RapidAPI svarer


            return mapper.readTree(response);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public JsonNode fetchMatchDetails(int matchId) {
        try {
            String endpoint = BASE_URL + "fixtures?id=" + matchId;
            URL url = new URL(endpoint);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("X-RapidAPI-Key", API_KEY);
            conn.setRequestProperty("X-RapidAPI-Host", API_HOST);
            conn.setRequestMethod("GET");

            InputStream responseStream = conn.getInputStream();
            Scanner scanner = new Scanner(responseStream).useDelimiter("\\A");
            String response = scanner.hasNext() ? scanner.next() : "";

            return mapper.readTree(response);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}

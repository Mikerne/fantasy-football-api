package dat;

import com.fasterxml.jackson.databind.JsonNode;
import dat.config.ApplicationConfig;


public class Main {

    public static void main(String[] args) {
        System.out.println("API is Starting up");
        ApplicationConfig.startServer(7070);
        System.out.println("API is Ready at port: 7070");





    }
}
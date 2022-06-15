package org.wildhamsters.gameroom.play;

import org.springframework.web.client.RestTemplate;

/**
 * Sends statistics of current match to the statistics microservice to be saved.
 *
 * @author Piotr Chowaniec
 */
public class Statistics {

    // private final GameRooms gameRooms = new GameRooms();
    private final RestTemplate restTemplate = new RestTemplate();

    public void saveMatchStatistics(GameRoom gameRoom) {
        var current = new CurrentMatchStatisticsMapper().map(gameRoom.getMatchStatistics());
        restTemplate.postForObject("http://stats:5500/", current, CurrentMatchStatistics.class);
    }
}

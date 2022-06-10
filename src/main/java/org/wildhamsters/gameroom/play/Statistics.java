package org.wildhamsters.gameroom.play;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;

/**
 * Sends statistics of current match to the statistics microservice to be saved.
 *
 * @author Piotr Chowaniec
 */
public class Statistics {

    private final GameRooms gameRooms = new GameRooms();
    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${url.statistics}")
    private String statisticsUrl;

    public void saveMatchStatistics(String roomId) {
        CurrentMatchStatistics current =
                new CurrentMatchStatisticsMapper().map(gameRooms.findRoom(roomId).getMatchStatistics());
        restTemplate.postForObject(statisticsUrl, current, CurrentMatchStatistics.class);
    }
}

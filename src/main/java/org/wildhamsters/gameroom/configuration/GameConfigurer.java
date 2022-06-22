package org.wildhamsters.gameroom.configuration;

import java.util.List;
import java.util.stream.IntStream;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.wildhamsters.gameroom.board.Board;
import org.wildhamsters.gameroom.fleet.Fleet;
import org.wildhamsters.gameroom.fleet.ShipsPositions;

/**
 * Creates configuration of a whole game.
 *
 * @author Dominik Żebracki
 */
@Component
public class GameConfigurer {

    private final RestTemplate restTemplate = new RestTemplate();
    private final String resourceUrl = "http://localhost:7000/placeShips";

    private RabbitTemplate rabbitTemplate;

    public GameConfigurer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public GameSettings createConfiguration(List<Integer> shipSizesToBePlaced,
                                            int boardHeight,
                                            int boardWidth,
                                            List<String> playersNames,
                                            List<String> playersIds) {

        return new GameSettings(IntStream.range(0, playersNames.size())
                .boxed()
                .map(i -> createPlayerSettings(
                        new ShipsPositions(restTemplate.getForObject(resourceUrl, PositionsDTO.class).positions()),
                        playersIds.get(i),
                        playersNames.get(i),
                        shipSizesToBePlaced))
                .toList());
    }

    public GameSettings createConfiguration(PositionsDTO shipsPositions,
                                            List<Integer> shipSizesToBePlaced,
                                            int boardHeight,
                                            int boardWidth,
                                            List<String> playersNames,
                                            List<String> playersIds) {

        return new GameSettings(IntStream.range(0, playersNames.size())
                .boxed()
                .map(i -> createPlayerSettings(
                        new ShipsPositions(shipsPositions.positions()),
                        playersIds.get(i),
                        playersNames.get(i),
                        shipSizesToBePlaced))
                .toList());
    }

    private GameSettings.PlayerSettings createPlayerSettings(ShipsPositions shipPositions,
            String id,
            String playerName,
            List<Integer> shipSizesToBePlaced) {
        return new GameSettings.PlayerSettings(id,
                playerName,
                Board.create(shipPositions),
                new Fleet(shipPositions));
    }
}

package org.wildhamsters.gameroom;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.wildhamsters.gameroom.configuration.PositionsDTO;

@Component
class MessageListener {

    private final GameService gameService;

    MessageListener(GameService gameService) {
        this.gameService = gameService;
    }

    @RabbitListener(queues = RabbitMQConfig.RESPONSE_QUEUE)
    void listener(PositionsDTO response) {
        gameService.createTwoPlayersConnectedStatus(response);
    }
}

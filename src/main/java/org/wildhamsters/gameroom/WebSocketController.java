package org.wildhamsters.gameroom;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Mariusz Bal
 */
@Controller
class WebSocketController {

    private final GameService gameService;
    private static Map<String, Boolean> roomSurrenderFix = new HashMap<>();
    private final RabbitTemplate rabbitTemplate;

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    WebSocketController(GameService gameService, RabbitTemplate rabbitTemplate) {
        this.gameService = gameService;
        this.rabbitTemplate = rabbitTemplate;
    }

    @MessageMapping("/room")
    public void sendSpecific(@Payload Message<String> msg, Principal user,
                             @Header("simpSessionId") String sessionId) throws JsonProcessingException {
        ConnectionStatus connectionStatus;


        gameService.addPlayer(new ConnectedPlayer(user.getName(), sessionId));
        if (gameService.areTwoPlayersConnected()) {
            rabbitTemplate.convertAndSend(RabbitMQConfig.MESSAGE_EXCHANGE, RabbitMQConfig.ROUTING_KEY,
                    "message from WebSocketController");
            connectionStatus = gameService.getConnectionStatus();
        } else {
            connectionStatus = gameService.createPlayerWaitingForOpponentStatus();


        }
        String resultJSON = new ObjectMapper().writeValueAsString(connectionStatus);
        simpMessagingTemplate.convertAndSendToUser(connectionStatus.playerOneSessionId(),
                "/queue/specific-user", resultJSON);
        if (connectionStatus.playerTwoSessionId() != null) {
            simpMessagingTemplate.convertAndSendToUser(connectionStatus.playerTwoSessionId(),
                    "/queue/specific-user", resultJSON);
            roomSurrenderFix.put(connectionStatus.roomId(), false);
        }
    }

    @MessageMapping("/gameplay")
    public void sendGameplay(String json, @Header("simpSessionId") String sessionId)
            throws JsonProcessingException {
        GameplayUserShotData data = new ObjectMapper().readValue(json, GameplayUserShotData.class);
        Result result = gameService.shoot(data.roomId(), data.cell());

        String resultJSON = new ObjectMapper().writeValueAsString(result);
    }

    @MessageMapping("/gameplay/surrender")
    public void giveUp(String json, @Header("simpSessionId") String sessionId) throws JsonProcessingException {
        GameplayUserShotData data = new ObjectMapper().readValue(json, GameplayUserShotData.class);
        String roomId = data.roomId();
        if (roomSurrenderFix.containsKey(roomId)) {
            SurrenderResult result = gameService.surrender(roomId, sessionId);
            String resultJSON = new ObjectMapper().writeValueAsString(result);
            simpMessagingTemplate.convertAndSendToUser(result.surrenderPlayerSessionId(),
                    "/queue/specific-user", resultJSON);
            simpMessagingTemplate.convertAndSendToUser(result.winPlayerSessionId(),
                    "/queue/specific-user", resultJSON);
            roomSurrenderFix.remove(roomId);
        }
    }
}

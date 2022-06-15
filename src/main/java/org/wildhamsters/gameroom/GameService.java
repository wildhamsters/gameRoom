package org.wildhamsters.gameroom;

import org.springframework.stereotype.Service;
import org.wildhamsters.gameroom.configuration.GameConfigurer;
import org.wildhamsters.gameroom.play.GameRoom;
import org.wildhamsters.gameroom.play.GameRooms;
import org.wildhamsters.gameroom.play.Statistics;

import java.util.ArrayList;
import java.util.List;

/**
 * Main entry point to the game.
 * Manages connection of players and handles interactions between players and a
 * game.
 *
 * @author Dominik Żebracki
 */
@Service
class GameService {

    private static final List<Integer> SHIP_SIZES_TO_BE_CREATED = List.of(4, 3, 3, 2, 2, 2, 1, 1, 1, 1);
    private static final int BOARD_WIDTH = 10;
    private static final int BOARD_HEIGHT = 10;

    private final GameRooms gameRooms = new GameRooms();
    private final GameConfigurer gameConfigurer;
    private final Statistics statistics;
    private GameRoom gameRoom;
    private ConnectedPlayers connectedPlayers;

    GameService() {
        this.gameRoom = null;
        this.connectedPlayers = new ConnectedPlayers(new ArrayList<>());
//        this.gameConfigurer = new GameConfigurer("http://shipplacement:7000/placeShips");
        this.gameConfigurer = new GameConfigurer("http://localhost:7000/placeShips");
        // "https://protected-stream-19238.herokuapp.com/placeShips");
        this.statistics = new Statistics();
    }

    /**
     * Manages process of connecting players.
     * After connecting the second player, ships and boards are created.
     *
     * @param connectedPlayer details of connected players.
     * @return players fleets, boards and their identifiers.
     */
    ConnectionStatus processConnectingPlayers(ConnectedPlayer connectedPlayer) {
        connectedPlayers = connectedPlayers.add(connectedPlayer);
        if (!connectedPlayers.areBothConnected()) {
            return createPlayerWaitingForOpponentStatus();
        } else {
            return createTwoPlayersConnectedStatus();
        }
    }

    /**
     * @param position of a shot on the board.
     * @return result of the shot.
     */
    Result shoot(String roomId, int position) {
        var gameRoom = gameRooms.findRoom(roomId);
        var result = gameRoom.makeShot(position);
        if (result.finished()) {
            statistics.saveMatchStatistics(gameRoom);
        }
        return result;
    }

    private ConnectionStatus createPlayerWaitingForOpponentStatus() {
        return new ConnectionStatus("No opponents for now",
                null,
                connectedPlayers.firstOneConnected().sessionId(), null,
                null, null,
                null, null,
                null, Event.CONNECT);
    }

    private ConnectionStatus createTwoPlayersConnectedStatus() {
        var gameSettings = gameConfigurer.createConfiguration(SHIP_SIZES_TO_BE_CREATED,
                BOARD_HEIGHT, BOARD_WIDTH, connectedPlayers.names(), connectedPlayers.ids());
        this.gameRoom = new GameRoom(gameSettings);
        var roomId = gameRooms.addRoom(gameRoom);
        var connectionStatus = new ConnectionStatus("Players paired.",
                roomId,
                connectedPlayers.firstOneConnected().sessionId(),
                gameSettings.firstPlayersFleet().getFleetPositions(),
                connectedPlayers.secondOneConnected().sessionId(),
                gameSettings.secondPlayersFleet().getFleetPositions(),
                connectedPlayers.firstOneConnected().name(),
                connectedPlayers.firstOneConnected().name(),
                connectedPlayers.secondOneConnected().name(),
                Event.CONNECT);

        Logger.log(Log.Level.INFO, this.getClass(), "Players  %s | %s  started new game in room %s.".formatted(
                connectedPlayers.firstOneConnected().name(), connectedPlayers.secondOneConnected().name(),
                roomId));

        clearConnectedPlayersAfterPairing();
        return connectionStatus;
    }

    private void clearConnectedPlayersAfterPairing() {
        connectedPlayers = new ConnectedPlayers(new ArrayList<>());
    }

    SurrenderResult surrender(String roomId, String surrenderPlayerSessionId) {
        String surrenderMessage = "You gave up.";
        String winnerMessage = "The opponent gave up. You won!";
        try {
            var gameRoom = gameRooms.findRoom(roomId);
            var winnerSessionId = gameRoom.findSurrenderPlayerOpponent(surrenderPlayerSessionId);
            statistics.saveMatchStatistics(gameRoom);
            return new SurrenderResult(Event.SURRENDER, surrenderPlayerSessionId, winnerSessionId,
                    surrenderMessage, winnerMessage);
        } catch (IllegalArgumentException e) {
            return new SurrenderResult(Event.SURRENDER, surrenderPlayerSessionId, null,
                    surrenderMessage, winnerMessage);
        }
    }
}
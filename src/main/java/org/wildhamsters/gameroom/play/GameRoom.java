package org.wildhamsters.gameroom.play;

import java.util.UUID;

import org.wildhamsters.gameroom.Result;
import org.wildhamsters.gameroom.configuration.GameSettings;

/**
 * Container for both players. Gives access to making shot and checking whether
 * round is finished.
 *
 * @author Piotr Chowaniec
 */
public class GameRoom {

    private final String id = UUID.randomUUID().toString();
    private final Player playerOne;
    private final Player playerTwo;
    private final SingleShot singleShot;

    public GameRoom(Player playerOne, Player playerTwo) {
        this.playerOne = playerOne;
        this.playerTwo = playerTwo;
        MatchStatistics matchStatistics = new MatchStatistics(id);
        MatchResult matchResult = new MatchResult(id, playerOne, playerTwo);
        singleShot = new SingleShot(playerOne, playerTwo, matchStatistics, matchResult);
    }

    public GameRoom(GameSettings gameSettings) {
        playerOne = Player.of(gameSettings.playerSettings().get(0));
        playerTwo = Player.of(gameSettings.playerSettings().get(1));
        MatchStatistics matchStatistics = new MatchStatistics(id);
        MatchResult matchResult = new MatchResult(id, playerOne, playerTwo);
        singleShot = new SingleShot(playerOne, playerTwo, matchStatistics, matchResult);
    }

    /**
     * Passes cell index to currentPlayer's makeShot() method.
     *
     * @param position cell index that is shot.
     * @return proper FieldState as a result of made shot.
     */
    public Result makeShot(int position) {
        return singleShot.makeShot(position);
    }

    public MatchStatistics getMatchStatistics() {
        return singleShot.getMatchStatistics();
    }

    String obtainUUID() {
        return id;
    }

    public String findSurrenderPlayerOpponent(String surrenderPlayerSessionId) {
        return playerOne.getId().equals(surrenderPlayerSessionId) ? playerTwo.getId() : playerOne.getId();
    }
}

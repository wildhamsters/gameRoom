package org.wildhamsters.gameroom.configuration;

import java.util.List;

import org.wildhamsters.gameroom.board.Board;
import org.wildhamsters.gameroom.fleet.Fleet;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * Game settings needed to start a game and creating
 * {@link org.wildhamsters.battleships.play.GameRoom}.
 *
 * @author Dominik Żebracki
 */
@SuppressFBWarnings(value = "EI_EXPOSE_REP", justification = "Can't fix that for now")
public record GameSettings(List<PlayerSettings> playerSettings) {

    public Fleet firstPlayersFleet() {
        return playerSettings.get(0).fleet();
    }

    public Fleet secondPlayersFleet() {
        return playerSettings.get(1).fleet();
    }

    /**
     * Individual player data.
     */
    public record PlayerSettings(String id,
            String name,
            Board board,
            Fleet fleet) {
    }
}

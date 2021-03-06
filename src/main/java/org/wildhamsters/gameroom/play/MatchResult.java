package org.wildhamsters.gameroom.play;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * @author Piotr Chowaniec
 */
@SuppressFBWarnings(
        value = "URF_UNREAD_FIELD",
        justification = "Can't fix that for now"
)
class MatchResult {

    private final String gameId;
    private final Player playerOne;
    private final Player playerTwo;
    private Player winner;

    MatchResult(String gameId, Player playerOne, Player playerTwo) {
        this.gameId = gameId;
        this.playerOne = playerOne;
        this.playerTwo = playerTwo;
    }

    public void setWinner(Player winner) {
        this.winner = winner;
    }
}

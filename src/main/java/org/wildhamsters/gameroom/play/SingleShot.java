package org.wildhamsters.gameroom.play;

import java.util.List;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.wildhamsters.gameroom.Cells;
import org.wildhamsters.gameroom.Event;
import org.wildhamsters.gameroom.Result;
import org.wildhamsters.gameroom.ShipCells;
import org.wildhamsters.gameroom.board.FieldState;

/**
 * Contains both players. Indicate which is current (making shot) and which is enemy (fire upon).
 * Players are changed if current player missed.
 *
 * @author Piotr Chowaniec
 */
@SuppressFBWarnings(
        value = "NP_NULL_PARAM_DEREF",
        justification = "Can't fix that for now"
)
class SingleShot {

    private final MatchStatistics matchStatistics;
    private final MatchResult matchResult;
    private Player current;
    private Player enemy;

    // Constructor for testing.
    SingleShot(Player current, Player enemy) {
        this(current, enemy, new MatchStatistics("id"), new MatchResult("id", current, enemy));
    }

    SingleShot(Player current, Player enemy, MatchStatistics matchStatistics, MatchResult matchResult) {
        this.current = current;
        this.enemy = enemy;
        this.matchStatistics = matchStatistics;
        this.matchResult = matchResult;
    }

    /**
     * Takes board cell index and checks what FieldState will be after shot.
     * It may be ACCURATE_SHOT if current player hit enemy's ship, MISSED_SHOT.
     *
     * @param position index of cell that is being shot.
     * @return Result DTO with data needed to prepare front-end.
     */
    Result makeShot(int position) {
        String error = "";
        FieldState state = null;
        try {
            state = enemy.enemyShotResult(position);
        } catch (IllegalShotException e) {
            error = e.getMessage();
        }
        var fieldsToMark = enemy.takeShot(position, state);

        matchStatistics.update(state);

        List<Integer> shipCells = (fieldsToMark.size() > 1) ? enemy.getSunkShipPositions(position) : null;

        switchPlayers(state);

        return new Result(Event.GAMEPLAY, new Cells(fieldsToMark), new ShipCells(shipCells), isWinner(), error, current.getId(), current.getName(), enemy.getId());
    }

    private void switchPlayers(FieldState state) {
        if (state == FieldState.MISSED_SHOT) {
            Player temp = current;
            current = enemy;
            enemy = temp;
        }
    }

    private boolean isWinner() {
        boolean result = enemy.isLost();
        if (result) {
            matchResult.setWinner(current);
        }
        return result;
    }

    MatchStatistics getMatchStatistics() {
        matchStatistics.adjustFinishTime();
        return matchStatistics;
    }
}

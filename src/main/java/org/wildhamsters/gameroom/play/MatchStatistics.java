package org.wildhamsters.gameroom.play;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.wildhamsters.gameroom.board.FieldState;

import java.time.LocalDateTime;

/**
 * Gathers data about single match like match id, total number of accurate
 * shots, total number of missed shots,
 * number of rounds played, time of start and end of the match.
 *
 * @author Piotr Chowaniec
 */
@SuppressFBWarnings(value = "SF_SWITCH_NO_DEFAULT", justification = "Can't fix that for now")
class MatchStatistics {

    private final String matchId;
    private final LocalDateTime startTime;
    private int accurateShots = 0;
    private int missedShots = 0;
    private int rounds = 1;
    private LocalDateTime finishTime;

    MatchStatistics(String matchId) {
        this.matchId = matchId;
        startTime = LocalDateTime.now();
    }

    void update(FieldState fieldState) {
        updateShots(fieldState);
        updateRound(fieldState);
    }

    private void updateShots(FieldState fieldState) {
        switch (fieldState) {
            case ACCURATE_SHOT -> accurateShots++;
            case MISSED_SHOT -> missedShots++;
        }
    }

    private void updateRound(FieldState fieldState) {
        if (fieldState == FieldState.MISSED_SHOT)
            rounds++;
    }

    void adjustFinishTime() {
        finishTime = LocalDateTime.now();
    }

    String getMatchId() {
        return matchId;
    }

    int getAccurateShots() {
        return accurateShots;
    }

    int getMissedShots() {
        return missedShots;
    }

    int getRounds() {
        return rounds;
    }

    LocalDateTime getStartTime() {
        return startTime;
    }

    LocalDateTime getFinishTime() {
        return finishTime;
    }
}

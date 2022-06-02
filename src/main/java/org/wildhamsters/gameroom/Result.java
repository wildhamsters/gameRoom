package org.wildhamsters.gameroom;

import java.util.List;
import java.util.Map;

import org.wildhamsters.gameroom.board.FieldState;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * DataTransferObject between GameRoom and GameService.
 *
 * @author Mariusz Bal
 */
@SuppressFBWarnings(value = "EI_EXPOSE_REP", justification = "Can't fix that for now")
public record Result(Event event,
                Map<Integer, FieldState> cells,
                List<Integer> shipCells,
                Boolean finished,
                String error,
                String currentTurnPlayer,
                String currentTurnPlayerName,
                String opponent) {
}

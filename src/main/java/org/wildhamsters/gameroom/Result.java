package org.wildhamsters.gameroom;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * DataTransferObject between GameRoom and GameService.
 *
 * @author Mariusz Bal
 */

@ExcludeFromJacocoGeneratedReport
@SuppressFBWarnings(value = "EI_EXPOSE_REP", justification = "JaCoCo treats records as bugs")
public record Result(Event event,
                Cells cells,
                ShipCells shipCells,
                Boolean finished,
                String error,
                String currentTurnPlayer,
                String currentTurnPlayerName,
                String opponent) {
}

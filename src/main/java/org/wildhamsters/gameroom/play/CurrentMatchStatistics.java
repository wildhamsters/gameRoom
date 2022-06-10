package org.wildhamsters.gameroom.play;

import java.time.LocalDateTime;

/**
 * A data transfer object of current match statistics used in REST communication with
 * the statistics microservice.
 *
 * @author Piotr Chowaniec
 */
record CurrentMatchStatistics(String matchId,
                                     LocalDateTime startTime,
                                     int accurateShots,
                                     int missedShots,
                                     int rounds,
                                     LocalDateTime finishTime) {
}

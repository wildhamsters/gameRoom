package org.wildhamsters.gameroom;

/**
 * @author Mariusz Bal
 */
@ExcludeFromJacocoGeneratedReport
record SurrenderResult(Event event,
                       String surrenderPlayerSessionId,
                       String winPlayerSessionId,
                       String surrenderMessage,
                       String winnerMessage) {
}

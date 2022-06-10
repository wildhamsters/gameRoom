package org.wildhamsters.gameroom.play;

/**
 * Mapping from MatchStatistics to CurrentMatchStatistics.
 *
 * @author Piotr Chowaniec
 */
class CurrentMatchStatisticsMapper implements Mapper<MatchStatistics, CurrentMatchStatistics> {

    @Override
    public CurrentMatchStatistics map(MatchStatistics key) {
        return new CurrentMatchStatistics(
                key.getMatchId(),
                key.getStartTime(),
                key.getAccurateShots(),
                key.getMissedShots(),
                key.getRounds(),
                key.getFinishTime()
        );
    }
}

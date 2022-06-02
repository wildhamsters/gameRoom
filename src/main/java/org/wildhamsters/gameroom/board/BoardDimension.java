package org.wildhamsters.gameroom.board;

/**
 * @author Dominik Żebracki
 */
public record BoardDimension(int min, int max) {
    public boolean isWithinDimension(int value) {
        return value >= min && value <= max;
    }
}

package org.wildhamsters.gameroom.board;

import org.wildhamsters.gameroom.fleet.ShipsPositions;

/**
 * @author Dominik Żebracki
 */
public interface Board {
    static Board create() {
        return new DefaultBoard();
    }

    static Board create(ShipsPositions shipsPositions) {
        return new DefaultBoard(shipsPositions);
    }

    FieldState getField(int position);

    void setField(FieldState fieldState, int position);

    void clearBoard();

    BoardDimension size();
}

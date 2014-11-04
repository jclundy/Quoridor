package com.games.jclundy.quoridor.GameRules;

/**
 * Created by devfloater65 on 11/3/14.
 */
class Move{
    int playerPosition;
    int chipPosition;

    Move(int square){
        playerPosition = square;
        chipPosition = -1;
    }

    Move(int playerPosition, int chipPosition){
        this.playerPosition = playerPosition;
        this.chipPosition = chipPosition;
    }
}
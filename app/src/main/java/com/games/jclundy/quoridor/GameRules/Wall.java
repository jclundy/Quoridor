package com.games.jclundy.quoridor.GameRules;

/**
 * Created by devfloater65 on 10/22/14.
 */
public class Wall {
    private int square1;
    private int square2;

    Wall(int first, int second){
        square1 = first;
        square2 = second;
    }
}
/*

1 *----*------*
    0  |   1
0 *----*------*
 */
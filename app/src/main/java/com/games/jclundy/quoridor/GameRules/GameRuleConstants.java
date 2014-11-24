package com.games.jclundy.quoridor.GameRules;

/**
 * Created by devfloater65 on 10/22/14.
 */
public class GameRuleConstants {
    static final int BOARD_SIZE = 81;
    static final int NUM_ROWS = 9;
    static final int NUM_COLS = 9;

    static final int EMPTY = -111;
    static final int FORWARD = 9;
    static final int BACK = -9;
    static final int RIGHT = 1;
    static final int LEFT = -1;

    static final int PLAYER_1 = 0;
    static final int PLAYER_2 = 1;
    static final int PLAYER_3 = 2;
    static final int PLAYER_4 = 3;
    public static final int PLAYER_IDS[] = {PLAYER_1, PLAYER_2, PLAYER_3, PLAYER_4};

    public static final int START_POSITIONS[] = {4, 76, 36, 44};

    static final String PLACE_WALL = "placeWall";
    static final String MOVE_PAWN = "movePawn";

    static final int TOTAL_WALL_NUM = 20;
}

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

    public static final int [] PLAYER_1_ENDZONE = {72, 73, 74, 75, 76, 77, 78, 79, 80};
    public static final int [] PLAYER_2_ENDZONE = {0, 1, 2, 3, 4, 5, 6, 7, 8};
    public static final int [] PLAYER_3_ENDZONE = {8, 17, 26, 35, 44, 53, 62, 71, 80};
    public static final int [] PLAYER_4_ENDZONE = {0, 9, 18, 27, 36, 45, 54, 63, 72};
    public static final int [][] PLAYER_ENDZONES = {PLAYER_1_ENDZONE, PLAYER_2_ENDZONE, PLAYER_3_ENDZONE, PLAYER_4_ENDZONE};

    static final String PLACE_WALL = "placeWall";
    static final String MOVE_PAWN = "movePawn";


    static final int TOTAL_WALL_NUM = 20;
}

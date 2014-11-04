package com.games.jclundy.quoridor.GameRules;
import android.app.Application;
import android.test.ApplicationTestCase;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

public class BoardTest extends ApplicationTestCase<Application>{

    public BoardTest() {
        super(Application.class);
    }

    public void testInitializesProperly(){
        for(int i = 1; i <= 4; i ++) {
            Board board = new Board(i);
            int numPlayers = board.numPlayers;
            assertEquals(i, numPlayers);
            for(int j = 0; j < numPlayers; j ++) {
                assertEquals(GameRuleConstants.START_POSITIONS[j], board.playerPositions[j]);
                assertEquals(j, board.playerIDs[j]);
            }
        }

    }

    public void testGetPlayerPosition(){
        Board board = new Board(4);
        for(int i = 0; i < 4; i++){
            int  position = board.getPlayerPosition(i);
            assertEquals(GameRuleConstants.START_POSITIONS[i], position);
            assertEquals(position, board.playerPositions[i]);
        }
    }

    public void testValidID(){
        Board board = new Board(4);
        for(int i = 0; i < 4; i ++){
            assertTrue(board.validID(i));
        }
    }

    public void testMovePiece(){
        Board board = new Board(4);
        int id = 0;
        board.movePiece(id, 13);
        assertEquals(13, board.getPlayerPosition(id));

        board.movePiece(id, 23);
        assertEquals(23, board.getPlayerPosition(id));

        board.movePiece(id, 13);

        board.movePiece(id, 21);
        assertEquals(21, board.getPlayerPosition(id));
        board.movePiece(id, 13);

        board.movePiece(id, 3);
        assertEquals(3, board.getPlayerPosition(id));
        board.movePiece(id, 13);

        board.movePiece(id, 5);
        assertEquals(5, board.getPlayerPosition(id));

        board.movePiece(id, 89);
        assertEquals(5, board.getPlayerPosition(id));

        board.movePiece(id, 4);
        assertEquals(4, board.getPlayerPosition(id));

        board.movePiece(id, 5);
        assertEquals(5, board.getPlayerPosition(id));

        board.movePiece(id, 4);
        assertEquals(4, board.getPlayerPosition(id));
    }

    public void testPlaceWall(){
        Board board = new Board(4);
        //case 1: placing vertical in left edge
        board.placeWall(0, true);
        int[] expected1 = {9};

        compareSets(expected1, board, 0);
        int[] expected2 = {0, 18, 19};
        compareSets(expected2, board, 9);
        //case 2: placing horizontal on left edge of board
        board.placeWall(9, false);
        int [] expected3 = {0};
        compareSets(expected3, board, 9);
        //case 3: placing horizontal in right edge of board
        board.placeWall(17, false);
        int[] expected4 = {16, 7, 8};
        compareSets(expected4, board, 17);
        //case 4: place vertical right edge
        int[] expected5 = {25, 34, 35};
        compareSets(expected5, board, 26);

        board.placeWall(26, true);
        int[] expected6 = {35};
        compareSets(expected6, board, 26);
        //case 5: trying to overlap walls
        board.placeWall(22, false);
        List<Integer> expected7 = board.squares[22].adjacencySet;
        board.placeWall(22, true);
        compareSets(expected7, board, 22);


        //case 7: place on top right corner of board
        board.placeWall(80, true);
        int[] expected8 = {71};
        compareSets(expected8, board, 80);

        //case  8: place in top edge of board
        board.placeWall(76, false);
        int [] expected9 = {75, 66, 77};
        compareSets(expected9, board, 76);

    }

    private void compareSets(int[] expected, Board board, int index){
        List<Integer> actual = board.squares[index].adjacencySet;
        assertEquals(expected.length, actual.size());
        for(int i = 0; i < actual.size() && i < expected.length; i++)
            assertTrue(actual.contains(expected[i]));
    }

    private void compareSets(List<Integer> expected, Board board, int index){
        List<Integer> actual = board.squares[index].adjacencySet;
        assertEquals(expected.size(), actual.size());
        for(int i = 0; i < actual.size() && i < expected.size(); i++)
            assertTrue(actual.contains(expected.get(i)));
    }
}

/*
18 19
------
9 | 10
0 | 1

34  35
25  26
------
16  17
7   8


      33  31 | 32
      24  22 | 23
      12  13   14


  79  80
  70  71



75  76  77
66  67  68

 */


package com.games.jclundy.quoridor.GameRules;

import android.app.Application;
import android.test.ApplicationTestCase;

public class SessionTest extends ApplicationTestCase<Application> {

    public SessionTest() {
        super(Application.class);
    }

    public void testInit() {

        verifyInitialization(2);
        verifyInitialization(3);
        verifyInitialization(4);
    }

    private void verifyInitialization(int num) {
        Session session = new Session(num);
        assertTrue(session.isTurnToMove(0));
        assertEquals(num, session.numPlayers);
        assertEquals(num, session.playerDict.size());
        for (int i = 0; i < session.playerDict.size(); i++) {
            int id = GameRuleConstants.PLAYER_IDS[i];
            assertEquals(i, session.playerDict.get(id).id);
            assertEquals(GameRuleConstants.START_POSITIONS[i], session.playerDict.get(id).getCurrentSquare());
        }
    }

    public void testIsMoveValid() {
        assertMoveIntoOpenTile();
        assertMoveIntoOccupiedTile();
        assertMoveIntoEdge();
    }

    private void assertMoveIntoOpenTile() {
        int startPosition = 4;
        Session session = new Session(2);
        assertTrue(session.isTurnToMove(GameRuleConstants.PLAYER_1));
        assertEquals(GameRuleConstants.START_POSITIONS[0], session.getPlayerPosition(GameRuleConstants.PLAYER_1));
        assertTrue(session.isMoveValid(startPosition + 9));
        assertTrue(session.isMoveValid(startPosition - 1));
        assertTrue(session.isMoveValid(startPosition + 1));
        assertFalse(session.isMoveValid(startPosition - 9));

    }

    private void assertMoveIntoWalledTile() {

    }

    private void assertMoveIntoOccupiedTile() {
        Session session = new Session(2);
        session.board.transportPiece(GameRuleConstants.PLAYER_2, 13);
        assertEquals(13, session.getPlayerPosition(GameRuleConstants.PLAYER_2));
        assertTrue(session.isTurnToMove(GameRuleConstants.PLAYER_1));
        assertFalse(session.isMoveValid(13));
    }

    private void assertMoveIntoEdge() {
        Session session = new Session(2);
        session.board.transportPiece(GameRuleConstants.PLAYER_1, 8);
        assertFalse(session.isMoveValid(9));
    }

    public void testMakeMove() {
        Session session = new Session(2);
        assertEquals(GameRuleConstants.PLAYER_1, session.getCurrentPlayerID());
        session.makeMove(13);
        assertEquals(13, session.getPlayerPosition(GameRuleConstants.PLAYER_1));
        assertEquals(1, session.getMoveCount());
        assertEquals(GameRuleConstants.PLAYER_2, session.getCurrentPlayerID());
    }

    private void assertOnlyMoveWhenValid() {

    }

    public void testPlaceWall() {

        assertPlaceWallEmptyBoard();
        assertCannotPlaceOverlappingWalls();
        assertCurrentTurnUpdated();
        assertPlayerChipCountsUpdated();
        assertSessionChipCountUpdated();
    }

    private void assertPlaceWallEmptyBoard() {
        Session session = new Session(2);
        assertTrue(session.canPlaceWall(24, false));
        assertTrue(session.canPlaceWall(24, true));
    }

    private void assertCannotPlaceOverlappingWalls() {
        assertNoOverlappingVerticalWalls();
        assertNoOverlappingHorizontalWalls();
    }

    private void assertNoOverlappingVerticalWalls() {
        Session session = new Session(2);
        assertTrue(session.canPlaceWall(24, false));
        assertTrue(session.canPlaceWall(24, true));

        session.placeWall(24, true);
        //on the square
        assertFalse(session.canPlaceWall(24, true));
        assertFalse(session.canPlaceWall(24, false));

        // right
        assertTrue(session.canPlaceWall(25, false));
        assertTrue(session.canPlaceWall(25, true));

        //left
        assertTrue(session.canPlaceWall(23, false));
        assertTrue(session.canPlaceWall(23, true));

        //above
        assertFalse(session.canPlaceWall(33, true));
        assertTrue(session.canPlaceWall(33, false));

        //below
        assertFalse(session.canPlaceWall(15, true));
        assertTrue(session.canPlaceWall(15, false));
    }

    private void assertNoOverlappingHorizontalWalls() {
        Session session = new Session(2);
        assertTrue(session.canPlaceWall(24, false));
        assertTrue(session.canPlaceWall(24, true));

        session.placeWall(24, false);
        //on the square
        assertFalse(session.canPlaceWall(24, true));
        assertFalse(session.canPlaceWall(24, false));

        // right
        assertFalse(session.canPlaceWall(25, false));
        assertTrue(session.canPlaceWall(25, true));

        //left
        assertFalse(session.canPlaceWall(23, false));
        assertTrue(session.canPlaceWall(23, true));

        //above
        assertTrue(session.canPlaceWall(33, true));
        assertTrue(session.canPlaceWall(33, false));

        //below
        assertTrue(session.canPlaceWall(15, true));
        assertTrue(session.canPlaceWall(15, false));
    }

    private void assertEdgeWallsPlacedCorrectly(){

    }

    private void assertCurrentTurnUpdated() {
        Session session = new Session(4);
        for (int i = 0; i < 8; i++){
            assertEquals(GameRuleConstants.PLAYER_IDS[i % 4], session.getCurrentPlayerID());
            session.placeWall(i, true);
            assertEquals(GameRuleConstants.PLAYER_IDS[(i + 1) % 4], session.getCurrentPlayerID());
        }
    }

    private void assertPlayerChipCountsUpdated() {
        Session session = new Session(4);
        int id = session.getCurrentPlayerID();
        int numChips = session.playerDict.get(id).getChipsLeft();
        assertEquals(5, numChips);
        session.placeWall(24, false);
        int newNumChips = session.playerDict.get(id).getChipsLeft();
        assertEquals(4, newNumChips);
    }

    private void assertSessionChipCountUpdated() {
        Session session = new Session(4);
        int count = 0;
        for (int row = 0; row < 3; row += 2) {
            for (int col = 0; col < 6; col ++){
                int square = Board.getSquareNum(col, row);
                if(count < 20)
                    assertTrue(session.canPlaceWall(square, true));
                else
                    assertFalse(session.canPlaceWall(square, true));
                session.placeWall(square, true);
                count ++;
            }
        }
    }

    private void assertAdjacencyUpdatedAfterPlacingWall(){

    }

    public void testPlayerHasWon() {
        assertVictoryForRangeOfSquares(GameRuleConstants.PLAYER_1, 72, 80, 1);
        assertVictoryForRangeOfSquares(GameRuleConstants.PLAYER_2, 0, 8, 1);
        assertVictoryForRangeOfSquares(GameRuleConstants.PLAYER_3, 8, 80, 9);
        assertVictoryForRangeOfSquares(GameRuleConstants.PLAYER_4, 0, 72, 9);
    }

    private void assertVictoryForRangeOfSquares(int id, int start, int end, int interval) {
        for (int i = start; i <= end; i += interval) {
            assertPlayerWin(id, i);
        }
    }

    private void assertPlayerWin(int id, int position) {
        Session session = new Session(4);
        session.board.transportPiece(id, position);
        assertTrue(session.playerHasWon(id));
    }
}
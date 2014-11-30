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

    public void testPlayerCanJumpOver() {
        assertCanJumpForward();
        assertCanJumpBackward();
        assertCanJumpLeft();
        assertCanJumpRight();
        assertCannotJumpOverLeftEdge();
        assertCannotJumpOverRightEdge();
    }

    private void assertJump(boolean expected, int wallPosition, boolean isWallVertical,
                                      int position1, int position2, int jumpToSquare)
    {
        Session session = new Session(2);
        session.board.transportPiece(GameRuleConstants.PLAYER_1, position1);
        session.board.transportPiece(GameRuleConstants.PLAYER_2, position2);
        session.board.placeWall(wallPosition, isWallVertical);
        assertEquals(expected, session.canJumpOver(GameRuleConstants.PLAYER_1, jumpToSquare));
        assertEquals(expected, session.isMoveValid(jumpToSquare));
    }

    private void assertJump(boolean expected, int position1, int position2, int jumpToSquare)
    {
        Session session = new Session(2);
        session.board.transportPiece(GameRuleConstants.PLAYER_1, position1);
        session.board.transportPiece(GameRuleConstants.PLAYER_2, position2);
        assertEquals(expected, session.canJumpOver(GameRuleConstants.PLAYER_1, jumpToSquare));
        assertEquals(expected, session.isMoveValid(jumpToSquare));

    }

    private void assertCanJumpForward()
    {
        int position1 = 20;
        int position2 = position1 + 9;
        int jumpToSquare = position1 + 18;
        assertJump(true, position1, position2, jumpToSquare);

        assertJumpOverWall(false, position1, position2, jumpToSquare, position1);
        assertJumpOverWall(false, position1, position2, jumpToSquare, position2);
        assertJumpOverWall(true, position1, position2, jumpToSquare, jumpToSquare);
        assertCannotJumpIntoPlayer(position1, position2, jumpToSquare);
    }

    private void assertCanJumpBackward()
    {
        int position1 = 20;
        int position2 = position1 - 9;
        int jumpToSquare = position1 - 18;
        assertJump(true, position1, position2, jumpToSquare);

        assertJumpOverWall(true, position1, position2, jumpToSquare, position1);
        assertJumpOverWall(false, position1, position2, jumpToSquare, position2);
        assertJumpOverWall(false, position1, position2, jumpToSquare, jumpToSquare);
        assertCannotJumpIntoPlayer(position1, position2, jumpToSquare);
    }

    private void assertCanJumpLeft()
    {
        int position1 = 22;
        int position2 = position1 - 1;
        int jumpToSquare = position1 - 2;
        assertJump(true, position1, position2, jumpToSquare);

        assertJumpOverWall(true, position1, position2, jumpToSquare, position1);
        assertJumpOverWall(false, position1, position2, jumpToSquare, position2);
        assertJumpOverWall(false, position1, position2, jumpToSquare, jumpToSquare);
        assertCannotJumpIntoPlayer(position1, position2, jumpToSquare);
    }

    private void assertCanJumpRight()
    {
        int position1 = 22;
        int position2 = position1 + 1;
        int jumpToSquare = position1 + 2;
        assertJump(true, position1, position2, jumpToSquare);

        assertJumpOverWall(false, position1, position2, jumpToSquare, position1);
        assertJumpOverWall(false, position1, position2, jumpToSquare, position2);
        assertJumpOverWall(true, position1, position2, jumpToSquare, jumpToSquare);
        assertCannotJumpIntoPlayer(position1, position2, jumpToSquare);
    }

    private void assertJumpOverWall(boolean expected, int position1, int position2,
                                          int jumpToSquare, int wallPosition){
        boolean isVertical;
        int diff = position1 - position2;

        isVertical = (Math.abs(diff) == 1);
        assertJump(expected, wallPosition, isVertical, position1, position2, jumpToSquare);

    }

    private void assertCannotJumpIntoPlayer(int position1, int position2, int jumpToSquare){
        Session session = new Session(3);
        session.board.transportPiece(GameRuleConstants.PLAYER_1, position1);
        session.board.transportPiece(GameRuleConstants.PLAYER_2, position2);
        session.board.transportPiece(GameRuleConstants.PLAYER_3, jumpToSquare);
        assertEquals(false, session.canJumpOver(GameRuleConstants.PLAYER_1, jumpToSquare));
    }

    private void assertCannotJumpOverRightEdge(){
        Session session = new Session(2);

        session.board.transportPiece(GameRuleConstants.PLAYER_2, 8);
        session.board.transportPiece(GameRuleConstants.PLAYER_1, 7);

        assertEquals(GameRuleConstants.PLAYER_1, session.getCurrentPlayerID());
        assertFalse(session.canJumpOver(GameRuleConstants.PLAYER_1, 9));
    }

    private void assertCannotJumpOverLeftEdge(){
        Session session = new Session(2);

        session.board.transportPiece(GameRuleConstants.PLAYER_2, 9);
        session.board.transportPiece(GameRuleConstants.PLAYER_1, 10);

        assertEquals(GameRuleConstants.PLAYER_1, session.getCurrentPlayerID());
        assertFalse(session.canJumpOver(GameRuleConstants.PLAYER_1, 8));
    }

    public void testJumpPiece()
    {
        Session session = new Session(2);
        int position1 = 20;
        int position2 = position1 + 9;
        int jumpToSquare = position1 + 18;
        session.board.transportPiece(GameRuleConstants.PLAYER_1, position1);
        session.board.transportPiece(GameRuleConstants.PLAYER_2, position2);

        assertEquals(position1, session.getCurrentPlayerPosition());
        assertEquals(position2, session.getPlayerPosition(GameRuleConstants.PLAYER_2));

        session.jumpToSquare(jumpToSquare);
        assertEquals(jumpToSquare, session.getPlayerPosition(GameRuleConstants.PLAYER_1));

        assertEquals(GameRuleConstants.PLAYER_2, session.getCurrentPlayerID());
        assertEquals(position2, session.getPlayerPosition(session.getCurrentPlayerID()));

    }

    public void testDiagonalMove()
    {
        assertCanMoveDiagonally();
        assertCanMoveDiagonallyWithPiece();
        assertCannotMoveDiagonallyIntoWall();
        assertCannotMoveDiagonallyOverEdge();
    }

    private void assertCanMoveDiagonally()
    {
        int position2 = 4;
        int position1 = 13;
        int wallPosition = 13;
        Session session = new Session(2);
        session.board.transportPiece(GameRuleConstants.PLAYER_1, position1);
        session.board.transportPiece(GameRuleConstants.PLAYER_2, position2);

        session.placeWall(0, true);

        assertFalse(session.canMoveDiagonally(position2 + 10));
        assertFalse(session.canMoveDiagonally(position2 + 8));

        session.placeWall(40, true);

        session.placeWall(wallPosition, false);

        assertTrue(session.canMoveDiagonally(position2 + 10));
        assertTrue(session.canMoveDiagonally(position2 + 8));
    }

    private void assertCanMoveDiagonallyWithPiece()
    {
        int position2 = 13;
        int position1 = 4;
        int position3 = 22;
        Session session = new Session(3);
        session.board.transportPiece(GameRuleConstants.PLAYER_1, position1);
        session.board.transportPiece(GameRuleConstants.PLAYER_2, position2);

        assertFalse(session.canMoveDiagonally(position1 + 10));
        assertFalse(session.canMoveDiagonally(position1 + 8));

        session.board.transportPiece(GameRuleConstants.PLAYER_3, position3);

        assertTrue(session.canMoveDiagonally(position1 + 10));
        assertTrue(session.canMoveDiagonally(position1 + 8));
    }

    private void assertCannotMoveDiagonallyIntoWall()
    {
        int position2 = 13;
        int position1 = 4;
        int wallPosition1 = 4;
        int wallPosition2 = 12;

        Session session = new Session(3);
        session.board.transportPiece(GameRuleConstants.PLAYER_1, position1);
        session.board.transportPiece(GameRuleConstants.PLAYER_2, position2);

        session.placeWall(wallPosition2, true);
        session.placeWall(wallPosition1, true);

        assertFalse(session.canMoveDiagonally(position1 + 10));
        assertFalse(session.canMoveDiagonally(position1 + 8));
    }

    private void assertCannotMoveDiagonallyOverEdge()
    {
        Session session = new Session(2);

        int position2 = 72;
        int position1 = position2 - 9;
        session.board.transportPiece(GameRuleConstants.PLAYER_1, position1);
        assertFalse(session.canMoveDiagonally(position2 + 1));
        assertFalse(session.canMoveDiagonally(position2 - 1));
    }

    public void testIsSquareOccupied()
    {
        int startPosition1 = GameRuleConstants.START_POSITIONS[0];
        Session session = new Session(2);
        assertTrue(session.isSquareOccupied(startPosition1));
        assertTrue(session.isSquareOccupied(GameRuleConstants.START_POSITIONS[1]));

        session.makeMove(startPosition1 + 9);
        assertTrue(session.isSquareOccupied(startPosition1 + 9));

        assertFalse(session.isSquareOccupied(80));
        assertFalse(session.isSquareOccupied(0));
        assertFalse(session.isSquareOccupied(-1));
        assertFalse(session.isSquareOccupied(81));
    }
}
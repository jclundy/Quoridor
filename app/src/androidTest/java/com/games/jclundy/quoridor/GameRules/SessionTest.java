package com.games.jclundy.quoridor.GameRules;

import android.app.Application;
import android.test.ApplicationTestCase;

public class SessionTest extends ApplicationTestCase<Application> {

    public SessionTest() {
        super(Application.class);
    }

    public void testInit(){

        verifyInitialization(2);
        verifyInitialization(3);
        verifyInitialization(4);
    }

    private void verifyInitialization(int num){
        Session session = new Session(num);
        assertTrue(session.isTurnToMove(0));
        assertEquals(num, session.numPlayers);
        assertEquals(num, session.playerList.length);
        for(int i = 0 ; i < session.playerList.length; i ++){
            assertEquals(i, session.playerList[i].id);
            assertEquals(GameRuleConstants.START_POSITIONS[i], session.playerList[i].getCurrentSquare());
        }
    }

    public void testIsMoveValid(){
        assertMoveIntoOpenTile();
        assertMoveIntoOccupiedTile();
        assertMoveIntoEdge();
    }

    private void assertMoveIntoOpenTile(){
        int startPosition = 4;
        Session session = new Session(2);
        assertTrue(session.isTurnToMove(GameRuleConstants.PLAYER_1));
        assertEquals(GameRuleConstants.START_POSITIONS[0],session.getPlayerPosition(GameRuleConstants.PLAYER_1));
        assertTrue(session.isMoveValid(startPosition + 9));
        assertTrue(session.isMoveValid(startPosition - 1));
        assertTrue(session.isMoveValid(startPosition + 1));
        assertFalse(session.isMoveValid(startPosition - 9));

    }

    private void assertMoveIntoWalledTile(){

    }

    private void assertMoveIntoOccupiedTile(){
        Session session =  new Session(2);
        session.board.transportPiece(GameRuleConstants.PLAYER_2, 13);
        assertEquals(13, session.getPlayerPosition(GameRuleConstants.PLAYER_2));
        assertTrue(session.isTurnToMove(GameRuleConstants.PLAYER_1));
        assertFalse(session.isMoveValid(13));
    }

    private void assertMoveIntoEdge(){
        Session session = new Session(2);
        session.board.transportPiece(GameRuleConstants.PLAYER_1, 8);
        assertFalse(session.isMoveValid(9));
    }

    public void testMakeMove()
    {
        Session session = new Session(2);
        assertEquals(GameRuleConstants.PLAYER_1, session.getCurrentPlayerID());
        session.makeMove(13);
        assertEquals(13, session.getPlayerPosition(GameRuleConstants.PLAYER_1));
        assertEquals(1, session.getMoveCount());
        assertEquals(GameRuleConstants.PLAYER_2, session.getCurrentPlayerID());
    }

    private void assertOnlyMoveWhenValid()
    {

    }

    public void testPlaceWall()
    {
//        Session session = new Session(2);
//        session.placeWall(67, )
    }

//    public void testIsTurnToMove(){
//        Session session = new Session(2);
//        int id1 = session.playerList[0].id;
//        int id2 = session.playerList[1].id;
//
//        assertTrue(session.isTurnToMove(id1));
//        assertFalse(session.isTurnToMove(id2));
//
//        session.makeMove(13);
//        assertFalse(session.isTurnToMove(id1));
//        assertTrue(session.isTurnToMove(id2));
//
//        Move move2 = new Move(67);
//        session.makeMove(move2);
//        assertFalse(session.isTurnToMove(id2));
//        assertTrue(session.isTurnToMove(id1));
//
//        session.makeMove(move2);
//
//        Move move3 = new Move(13, 52);
//        session.makeMove(move3);
//        assertFalse(session.isTurnToMove(id1));
//        assertTrue(session.isTurnToMove(id2));
//
//        //can't place chip in same position twice
//        session.makeMove(move2);
//        assertTrue(session.isTurnToMove(id2));
//        assertFalse(session.isTurnToMove(id1));
//
//        //can't move piece to same position
//        session.makeMove(67);
//        assertTrue(session.isTurnToMove(id2));
//        assertFalse(session.isTurnToMove(id1));
//
//        //cam't move out of turn
//        session.makeMove(22);
//    }
//
//    public void testCanJumpOver(){
//        assertTrue(false);
//    }
//
//    public void testCanJumpDiagonally(){
//        assertTrue(false);
//    }
//
//    public void testGetMoveStatus(){
//        assertTrue(false);
//    }
//
//    public void testIsValidMove(){
//        assertTrue(false);
//    }
//
//    public void testMoveForward(){
//        assertTrue(false);
//    }
//
//    public void testMoveBackward(){
//        assertTrue(false);
//    }
//
//    public void testMoveLeft(){
//        assertTrue(false);
//    }
//
//    public void testMoveRight(){
//        assertTrue(false);
//    }
//
//    public void testJumpOver(){
//        assertTrue(false);
//    }
//
//    public void testMoveDiagonally(){
//        assertTrue(false);
//    }
//
//    public void testCheckForVictory(){
//        assertTrue(false);
//    }
}

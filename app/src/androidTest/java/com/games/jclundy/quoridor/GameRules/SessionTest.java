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

        Session session = new Session(2);

        assertTrue(false);
    }

    public void testIsTurnToMove(){
        Session session = new Session(2);
        int id1 = session.playerList[0].id;
        int id2 = session.playerList[1].id;

        assertTrue(session.isTurnToMove(id1));
        assertFalse(session.isTurnToMove(id2));

        session.makeMove(id1, 13);
        assertFalse(session.isTurnToMove(id1));
        assertTrue(session.isTurnToMove(id2));

        Move move2 = new Move(67);
        session.makeMove(id2, move2);
        assertFalse(session.isTurnToMove(id2));
        assertTrue(session.isTurnToMove(id1));

        session.makeMove(id2, move2);

        Move move3 = new Move(13, 52);
        session.makeMove(id1, move3);
        assertFalse(session.isTurnToMove(id1));
        assertTrue(session.isTurnToMove(id2));

        //can't place chip in same position twice
        session.makeMove(id2, move2);
        assertTrue(session.isTurnToMove(id2));
        assertFalse(session.isTurnToMove(id1));

        //can't move piece to same position
        session.makeMove(id2, 67);
        assertTrue(session.isTurnToMove(id2));
        assertFalse(session.isTurnToMove(id1));

        //cam't move out of turn
        session.makeMove(id1, 22);
    }

    public void testCanJumpOver(){
        assertTrue(false);
    }

    public void testCanJumpDiagonally(){
        assertTrue(false);
    }

    public void testGetMoveStatus(){
        assertTrue(false);
    }

    public void testIsValidMove(){
        assertTrue(false);
    }

    public void testMoveForward(){
        assertTrue(false);
    }

    public void testMoveBackward(){
        assertTrue(false);
    }

    public void testMoveLeft(){
        assertTrue(false);
    }

    public void testMoveRight(){
        assertTrue(false);
    }

    public void testJumpOver(){
        assertTrue(false);
    }

    public void testMoveDiagonally(){
        assertTrue(false);
    }

    public void testCheckForVictory(){
        assertTrue(false);
    }
}

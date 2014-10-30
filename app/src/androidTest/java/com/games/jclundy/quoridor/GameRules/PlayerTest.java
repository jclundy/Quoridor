package com.games.jclundy.quoridor.GameRules;

import android.app.Application;
import android.test.ApplicationTestCase;

public class PlayerTest extends ApplicationTestCase<Application> {

    public PlayerTest() {
        super(Application.class);
    }

    private Player zPlayer(){
        return new Player(0, 4, 10);
    }

    public void testInit(){
        Player player = zPlayer();
        assertEquals(0, player.id);
        assertEquals(4, player.startPosition);
        assertEquals(10, player.chipsLeft);
        assertEquals(0, player.moveHistory.size());
    }

    public void testHasReachedEndZone(){
        Player player = zPlayer();
        player.currentSquare = 13;
        assertFalse(player.hasReachedEndZone());

        player.currentSquare = 72;
        assertTrue(player.hasReachedEndZone());
    }

    public void testRecordMove(){
        
    }

    public void testGetLastMove(){

    }

    public void testUndoLastMove(){

    }

    public void testMoveHistory(){

    }
}

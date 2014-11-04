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
        assertEquals(1, player.moveHistory.size());
    }

    public void testHasReachedEndZone(){
        Player player = zPlayer();
        player.recordMove(13, false);
        assertFalse(player.hasReachedEndZone());

        player.recordMove(80, false);
        assertTrue(player.hasReachedEndZone());
    }

    public void testGetCurrentSquare(){
        Player player = zPlayer();
        int firstSquare = player.getLastMove().playerPosition;
        assertEquals(4, firstSquare);

    }

    public void testRecordMove(){
        int start = 0;
        int chips = 10;
        int id = 1;
        Player player = new Player(id, start, chips);
        player.recordMove(1, false);

        assertEquals(0, player.startPosition);
        assertEquals(1, player.getCurrentSquare());
        assertEquals(2, player.moveHistory.size());
    }

    public void testGetLastMove(){
        Player player = zPlayer();
        player.recordMove(1, false);
        assertEquals(1, player.getLastMove().playerPosition);
        assertEquals(1, player.getCurrentSquare());
        assertEquals(-1, player.getLastMove().chipPosition);
    }

    public void testUndoMove(){
        Player player = zPlayer();
        player.recordMove(13, false);
        player.recordMove(22, false);
        player.undoMove();

        assertEquals(13, player.getCurrentSquare());
        assertEquals(-1, player.getLastMove().chipPosition);

        player.recordMove(40, true);
        assertEquals(13, player.getCurrentSquare());
        assertEquals(40, player.getLastMove().chipPosition);
        assertEquals(9, player.getChipsLeft());
    }
}

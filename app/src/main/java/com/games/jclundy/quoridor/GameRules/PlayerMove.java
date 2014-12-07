package com.games.jclundy.quoridor.GameRules;

/**
 * Created by devfloater65 on 12/7/14.
 */
public class PlayerMove {
    public int playerID;
    public int position;
    public int moveType;
    //0 - move piece, 1 - vertical wall, 2 - horizontal wall

    public PlayerMove(int id, int position, int moveType)
    {
        playerID = id;
        this.position = position;
        this.moveType = moveType;
    }

    public PlayerMove()
    {
        playerID = -1;
        position = -1;
        moveType = -1;
    }

    public boolean moveIsEmpty()
    {
        return playerID == -1 || position == -1 || moveType == -1;
    }
}

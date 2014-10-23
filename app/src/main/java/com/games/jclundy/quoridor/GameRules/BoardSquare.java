package com.games.jclundy.quoridor.GameRules;

/**
 * Created by devfloater65 on 10/22/14.
 */
public class BoardSquare {
    private int occupierID;
    private int id;
    private AdjacencySet adjacencySet;

    public BoardSquare(int number){
        id = number;
        occupierID = GameRuleConstants.EMPTY;
        adjacencySet = new AdjacencySet(id);

    }
    public boolean didOccupySquare(int playerID){
        if(occupierID == GameRuleConstants.EMPTY){
            occupierID = playerID;
            return true;
        }
        else
            return false;
    }
    public boolean isAdjacent(int squareNum){
        return adjacencySet.isInAdjacencySet(squareNum);
    }
    public void removePiece(){
        occupierID = GameRuleConstants.EMPTY;
    }
    public void placePiece(int id){
        occupierID = id;
    }

}
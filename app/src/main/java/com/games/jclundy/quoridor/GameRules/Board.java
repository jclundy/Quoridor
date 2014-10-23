package com.games.jclundy.quoridor.GameRules;

import android.provider.SyncStateContract;

/**
 * Created by devfloater65 on 10/22/14.
 */
public class Board {
    BoardSquare squares[];
    int numPlayers;
    int playerPositions[];
    int playerIDs[];

    public Board(int playerNum){
        squares = new BoardSquare[GameRuleConstants.BOARD_SIZE];
        numPlayers = playerNum;
        initializePlayerPositions();
        initializePlayerIDs();
        for(int i = 0; i < numPlayers; i++){
            squares[playerPositions[i]].didOccupySquare(playerIDs[i]);
        }
    }
    private void initializePlayerPositions(){
        playerPositions = new int[numPlayers];
        for(int i = 0; i < numPlayers; i++){
            playerPositions[i] = GameRuleConstants.START_POSITIONS[i];
        }
    }
    private void initializePlayerIDs(){
        playerIDs = new int[numPlayers];
        for(int i = 0; i < numPlayers; i++){
            playerIDs[i] = GameRuleConstants.PLAYER_IDS[i];
        }
    }
    private void placePiece(int squareNumber, int playerID){
        squares[squareNumber].didOccupySquare(playerID);
    }
    public int getPlayerPosition(int playerID)
    {
        if(validID(playerID)){
            return playerPositions[playerID];
        }
        else
            return -1;
    }
    private boolean validID(int playerID){
        return playerID < numPlayers;
    }

    public void movePiece(int playerID, int toSquareNum){
        int fromSquareNum = getPlayerPosition(playerID);
        BoardSquare fromSquare = squares[fromSquareNum];
        if(fromSquare.isAdjacent(toSquareNum)){
            BoardSquare toSquare = squares[toSquareNum];
            fromSquare.removePiece();
            toSquare.placePiece(playerID);
        }
    }
}



/*


72
.
.
.
[36]
.
.
.
0 1 2 3 [4] 5 6 7 8

 */
package com.games.jclundy.quoridor.GameRules;

public class Board {
    Square squares[];
    int numPlayers;
    int playerPositions[];
    int playerIDs[];

    public Board(int playerNum){
        squares = new Square[GameRuleConstants.BOARD_SIZE];
        for(int i = 0; i < GameRuleConstants.BOARD_SIZE; i ++){
            squares[i] = new Square(i);
        }
        numPlayers = playerNum;
        initializePlayerPositions();
        initializePlayerIDs();
        for(int i = 0; i < numPlayers; i++){
            int position = playerPositions[i];
            squares[position].didOccupySquare(i);
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

    public int getPlayerPosition(int playerID) {
        if(validID(playerID)){
            return playerPositions[playerID];
        }
        else
            return -1;
    }

    boolean validID(int playerID){
        return playerID < numPlayers;
    }

    public void movePiece(int playerID, int toSquareNum){
        int fromSquareNum = getPlayerPosition(playerID);
        Square fromSquare = squares[fromSquareNum];
        if(fromSquare.isInAdjacencySet(toSquareNum)){
            Square toSquare = squares[toSquareNum];
            fromSquare.removePiece();
            toSquare.placePiece(playerID);
            playerPositions[playerID] = toSquareNum;
        }
    }

    public void placeWall(int squareNum, boolean isVertical){
        Square square = squares[squareNum];
        if(!square.hasWall){
            square.hasWall = true;
            if(isVertical)
                insertVerticalWall(squareNum);
            else
                insertHorizontalWall(squareNum);
        }
    }

    private void insertHorizontalWall(int squareNum){
        int above = squareNum + 9;
        int aboveRight = squareNum + 10;
        int right = squareNum + 1;
        removeAdjacencyOfSquares(squareNum, above);
        removeAdjacencyOfSquares(squareNum, aboveRight);
        removeAdjacencyOfSquares(right, above);
        removeAdjacencyOfSquares(right, aboveRight);
    }

    private void insertVerticalWall(int squareNum){
        int above = squareNum + 9;
        int aboveRight = squareNum + 10;
        int right = squareNum + 1;
        removeAdjacencyOfSquares(squareNum, right);
        removeAdjacencyOfSquares(squareNum, aboveRight);
        removeAdjacencyOfSquares(above, right);
        removeAdjacencyOfSquares(above, aboveRight);
    }

    private void removeAdjacencyOfSquares(int first, int second){
        Square firstSquare = squares[first];
        Square secondSquare = squares[second];
        firstSquare.removeFromAdjacencySet(second);
        secondSquare.removeFromAdjacencySet(first);
    }

}
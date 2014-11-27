package com.games.jclundy.quoridor.GameRules;

import java.util.HashMap;

public class Session {

    protected int numPlayers;
    protected Board board;
    protected HashMap<Integer, Player> playerDict;
    private int currentPlayerID;
    private int moveCount;

    public Session(int numberOfPlayers){
        moveCount = 0;
        numPlayers = (numberOfPlayers >= 2 && numberOfPlayers <= 4)? numberOfPlayers : 2;
        board = new Board(numPlayers);
        initializePlayers();
        currentPlayerID = playerDict.get(GameRuleConstants.PLAYER_1).id;
    }

    private void initializePlayers(){
        playerDict = new HashMap<Integer, Player>(numPlayers);
        int numberOfChips = GameRuleConstants.TOTAL_WALL_NUM/numPlayers;

        for(int i = 0; i < numPlayers; i++){
            Player player = new Player(i, GameRuleConstants.START_POSITIONS[i],numberOfChips);
            playerDict.put(GameRuleConstants.PLAYER_IDS[i], player);
        }
    }

    public boolean isMoveValid(int toSquare){
        if (toSquare > 80 || toSquare < 0)
            return false;
        int currentSquare = board.getPlayerPosition(currentPlayerID);
        return isTileOpen(toSquare) && isTileConnected(currentSquare, toSquare)
                && isNotMovingIntoEdge(currentSquare, toSquare);
    }

    public int getMoveCount(){
        return moveCount;
    }
    public int getCurrentPlayerID(){
        return currentPlayerID;
    }

    public int getCurrentPlayerPosition() {
        return board.getPlayerPosition(currentPlayerID);
    }

    private boolean isTileOpen(int squareNum) {
        return board.getOccupierAtSquare(squareNum) == GameRuleConstants.EMPTY;
    }

    private boolean isTileConnected(int fromSquare, int toSquare) {
        return board.squaresAreConnected(fromSquare, toSquare);
    }

    private boolean isNotMovingIntoEdge(int fromSquare, int toSquare) {
        int fromCol = board.getCol(fromSquare);
        int fromRow = board.getRow(fromSquare);

        int toCol = board.getCol(toSquare);
        int toRow = board.getRow(toSquare);

        boolean isValidColumn = Math.abs(fromCol - toCol) <= 1;
        boolean isValidRow = Math.abs(fromRow - toRow) <= 1;

        return isValidColumn && isValidRow;
    }

    boolean isTurnToMove(int playerID){
        return playerID == currentPlayerID;
    }

    public int getPlayerPosition(int playerID){
        return board.getPlayerPosition(playerID);
    }

    public void makeMove(int squareNum){
        if(isMoveValid(squareNum)){
            moveCount ++;
            board.movePiece(currentPlayerID, squareNum);
            currentPlayerID = GameRuleConstants.PLAYER_IDS[moveCount % numPlayers];
        }
    }

    public void placeWall(int squareNum, boolean isVertical)
    {
        if(board.canPlaceWall(squareNum, isVertical))
        {
            board.placeWall(squareNum, isVertical);
            moveCount ++;
            Player player = playerDict.get(currentPlayerID);
            player.recordMove(squareNum, true);
            playerDict.put(currentPlayerID, player);
            currentPlayerID = GameRuleConstants.PLAYER_IDS[moveCount % numPlayers];
        }
    }

    public boolean canPlaceWall(int squareNum, boolean isVertical)
    {
        int chipsLeft  = playerDict.get(currentPlayerID).getChipsLeft();
        return board.canPlaceWall(squareNum, isVertical)  && chipsLeft > 0;
    }

    boolean canJumpOver(int id, int squareNum){
        //squareNum is 2 rows XOR 2 columns away
        //another player occupies an adjacent square
        //the squareNum is adjacent to the player's occupying square

        int currentPosition = getPlayerPosition(id);
        int col = Board.getCol(currentPosition);
        int row = Board.getRow(currentPosition);
        int toCol = Board.getCol(squareNum);
        int toRow = Board.getRow(squareNum);

        int occupierOfToSquare = board.getOccupierAtSquare(squareNum);

        int colDiff = Math.abs(row - toRow);
        int rowDiff = Math.abs(col - toCol);
        int occupier;
        if(colDiff == 1 && rowDiff == 0){
            occupier = board.getOccupierAtSquare(Board.getSquareNum(toCol, row));
            return occupier != GameRuleConstants.EMPTY && occupierOfToSquare == GameRuleConstants.EMPTY;
        }
        else if (colDiff == 0 && rowDiff == 1){
            occupier = board.getOccupierAtSquare(Board.getSquareNum(col, toRow));
            return occupier != GameRuleConstants.EMPTY && occupierOfToSquare == GameRuleConstants.EMPTY;
        }
        else
            return false;
    }

    boolean canJumpDiagonally(int id, int squareNum){
        //squareNum is in adjacency set of current square
        //square Num is in a different row and different column
        //another player occupies the square in adjacent column XOR adjacent row
        //square occupied by above player is adjacent to squareNum
//
//
//         __ __ [ ]
//        [ ][P][X]
//        [ ][ ][ ]
//
//

        int currentPosition = getPlayerPosition(id);
        int col = Board.getCol(currentPosition);
        int row = Board.getRow(currentPosition);
        int toCol = Board.getCol(squareNum);
        int toRow = Board.getRow(squareNum);

        int rowDiff = Math.abs(row - toRow);
        int colDiff = Math.abs(col - toCol);
        if (rowDiff + colDiff == 2 && board.squares[currentPosition].isInAdjacencySet(squareNum)){
            int horizontalSquare = Board.getSquareNum(toCol, row);
            int verticalSquare = Board.getSquareNum(col, toRow);

            boolean validMove = board.getOccupierAtSquare(horizontalSquare) != GameRuleConstants.EMPTY &&
                    board.squares[verticalSquare].isAdjacent(squareNum) && !board.squares[verticalSquare].isInAdjacencySet(squareNum);
            return validMove;
        }
        return false;
    }

    public boolean playerHasWon(int playerID){

       int position = getPlayerPosition(playerID);
       int column = Board.getCol(position);
       int row = Board.getRow(position);
       switch(playerID){
           case GameRuleConstants.PLAYER_1:
               return row == 8;
           case GameRuleConstants.PLAYER_2:
               return row == 0;
           case GameRuleConstants.PLAYER_3:
               return column == 8;
           case GameRuleConstants.PLAYER_4:
               return column == 0;
       }
        return false;
    }
}

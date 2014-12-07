package com.games.jclundy.quoridor.GameRules;

import com.games.jclundy.quoridor.PathFinding.Graph;

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

    public boolean isMoveValid(int toSquare)
    {
        return canSlideTo(toSquare) || canJumpOver(currentPlayerID, toSquare) || canMoveDiagonally(toSquare);
    }

    private boolean canSlideTo(int toSquare)
    {
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
        if(canJumpOver(currentPlayerID, squareNum))
            board.jumpPiece(currentPlayerID, squareNum);
        else if(canMoveDiagonally(squareNum))
            board.moveDiagonally(currentPlayerID, squareNum);
        else if(canSlideTo(squareNum)){
            board.movePiece(currentPlayerID, squareNum);
        }
        updateCurrentPlayer();
    }

    public void playTurn(PlayerMove move)
    {
        int squareNum = move.position;
        int moveType = move.moveType;

        switch (moveType)
        {
            case 1:
                makeMove(squareNum);
                break;
            case 2:
                placeWall(squareNum, true);
                break;
            case 3:
                placeWall(squareNum, false);
                break;
            default:
                break;
        }
    }

    public void placeWall(int squareNum, boolean isVertical)
    {
        if(board.canPlaceWall(squareNum, isVertical))
        {
            board.placeWall(squareNum, isVertical);
            Player player = playerDict.get(currentPlayerID);
            player.recordMove(squareNum, true);
            playerDict.put(currentPlayerID, player);
            updateCurrentPlayer();
        }
    }

    private void updateCurrentPlayer()
    {
        moveCount ++;
        currentPlayerID = GameRuleConstants.PLAYER_IDS[moveCount % numPlayers];
    }

    public boolean canPlaceWall(int squareNum, boolean isVertical)
    {
        int chipsLeft  = playerDict.get(currentPlayerID).getChipsLeft();
        boolean doesNotBlockOffPlayer = wallDoesNotBlockOffPlayer(squareNum, isVertical);
        return board.canPlaceWall(squareNum, isVertical) && chipsLeft > 0 && doesNotBlockOffPlayer;
    }

    public void jumpToSquare(int toSquare)
    {
        if(canJumpOver(currentPlayerID, toSquare)){
            board.jumpPiece(currentPlayerID, toSquare);
            updateCurrentPlayer();
        }
    }

    public boolean canJumpOver(int id, int toSquare){
        if (!Board.isValidNumber(toSquare))
            return false;
        int start = getPlayerPosition(id);
        return board.canJumpOver(start, toSquare);
    }

    public boolean canMoveDiagonally(int toSquare){
        if (!Board.isValidNumber(toSquare))
            return false;
        int currentSquare = getCurrentPlayerPosition();
        return board.canMoveDiagonally(currentSquare, toSquare);
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

    public boolean isSquareOccupied(int square)
    {
        if(Board.isValidNumber(square)){
            return !board.squareIsEmpty(square);
        }
        return false;
    }

    private boolean wallDoesNotBlockOffPlayer(int wallPos, boolean isVertical)
    {
        Board boardTemp = new Board(this.board);
        boardTemp.placeWall(wallPos, isVertical);

        for (int i = 0; i < numPlayers; i ++) {
            int playerPos = getPlayerPosition(GameRuleConstants.PLAYER_IDS[i]);
            Graph graph = new Graph(playerPos, boardTemp.squares, GameRuleConstants.PLAYER_ENDZONES[i]);
            graph.runDijkstra();
            boolean hasOpenPathToFinish = graph.hasOpenPathToFinish();
            if(!hasOpenPathToFinish)
                return false;
        }
        return true;
    }
}

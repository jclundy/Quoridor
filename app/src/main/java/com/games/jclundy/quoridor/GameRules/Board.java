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
            updatePlayerPosition(playerID, toSquareNum, fromSquareNum);
        }
    }

    private void updatePlayerPosition(int playerID, int toSquareNum, int fromSquareNum)
    {
        Square fromSquare = squares[fromSquareNum];
        Square toSquare = squares[toSquareNum];
        fromSquare.removePiece();
        toSquare.placePiece(playerID);
        playerPositions[playerID] = toSquareNum;
    }

    public void jumpPiece(int playerID, int toSquareNum) {
        int fromSquareNum = getPlayerPosition(playerID);
        if(canJumpOver(fromSquareNum, toSquareNum))
            updatePlayerPosition(playerID, toSquareNum, fromSquareNum);
    }

    public void moveDiagonally(int playerID, int toSquareNum)
    {
        int fromSquareNum = getPlayerPosition(playerID);
        if(canMoveDiagonally(fromSquareNum, toSquareNum))
            updatePlayerPosition(playerID, toSquareNum, fromSquareNum);
    }

    public void placeWall(int squareNum, boolean isVertical){
        Square square = squares[squareNum];
        if(!square.hasWall){
            if(isVertical)
                insertVerticalWall(squareNum);
            else
                insertHorizontalWall(squareNum);
        }
    }

    protected boolean canPlaceWall(int squareNum, boolean isVertical)
    {
        if(isValidNumber(squareNum)){
            Square square = squares[squareNum];
            boolean doesNotHaveWall = !square.hasWall;
            boolean notBlocked;
            if(isVertical)
                notBlocked = square.canPlaceVerticalWall;
            else
                notBlocked = square.canPlaceHorizontalWall;

            return doesNotHaveWall && notBlocked;
        }
        return false;
    }

    public int getOccupierAtSquare(int squareNum){
         return squares[squareNum].occupierID;
    }

    private void insertHorizontalWall(int squareNum){
        int right = squareNum + 1;
        int above = squareNum + 9;
        int aboveRight = squareNum + 10;

        Square first = squares[squareNum];
        first.hasWall = true;
        first.canPlaceVerticalWall = false;
        first.canPlaceHorizontalWall = false;

        Square second = squares[right];
        second.canPlaceHorizontalWall = false;

        if(getCol(squareNum) > 0){
            int left = squareNum - 1;
            Square leftSquare= squares[left];
            leftSquare.canPlaceHorizontalWall = false;
        }

        removeAdjacencyOfSquares(squareNum, above);
        removeAdjacencyOfSquares(squareNum, aboveRight);
        removeAdjacencyOfSquares(right, above);
        removeAdjacencyOfSquares(right, aboveRight);
    }

    private void insertVerticalWall(int squareNum){
        int above = squareNum + 9;
        int aboveRight = squareNum + 10;
        int right = squareNum + 1;

        Square first = squares[squareNum];
        first.hasWall = true;
        first.canPlaceVerticalWall = false;
        first.canPlaceHorizontalWall = false;

        Square aboveSquare = squares[above];
        aboveSquare.canPlaceVerticalWall = false;

        if(getRow(squareNum) > 0){
            int below = squareNum - 9;
            Square belowSquare = squares[below];
            belowSquare.canPlaceVerticalWall = false;
        }

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

    public static int getRow(int squareNum){
        return squareNum/9;
    }
    public static int getCol(int squareNum){
        return squareNum % 9;
    }
    public static int getSquareNum(int col, int row){

        return row*9 + col;
    }
    public static boolean isValidRow(int index){
        int row = getRow(index);
        return row >= 0 && row < 9;
    }

    public static boolean isValidColumn(int index){
        int col = getCol(index);
        return col >= 0 && col < 9;
    }

    public static boolean squareAreAdjacent(int first, int second){
        return false;
    }

    public boolean squaresAreConnected(int first, int second)
    {
        Square fromSquare = squares[first];
        return fromSquare.isInAdjacencySet(second);
    }

    public void transportPiece(int playerID, int toSquareNum)
    {
        int fromSquareNum = getPlayerPosition(playerID);
        Square fromSquare = squares[fromSquareNum];
        Square toSquare = squares[toSquareNum];
        fromSquare.removePiece();
        toSquare.placePiece(playerID);
        playerPositions[playerID] = toSquareNum;
    }

    public static int getValidSquareForWall(int squareNum)
    {
        int validNum = squareNum;
        if(getCol(squareNum) == 8)
            validNum --;
        if(getRow(squareNum) == 8)
            validNum -=9;
        return validNum;
    }
    public static boolean isValidNumber(int number){
        return number < 80 && number >= 0;
    }

    boolean canJumpOver(int start, int end)
    {
        boolean squaresAreRightDistance = checkDistance(start, end);

        if (squaresAreRightDistance) {
            int middle = getSquareBetween(start, end);
            boolean squaresAreNotBlocked = squaresNotBlocked(start, middle, end);
            boolean midContainsPlayer = !squareIsEmpty(middle);
            boolean endSquareIsEmpty = squareIsEmpty(end);
            return squaresAreNotBlocked && midContainsPlayer && endSquareIsEmpty;
        }

        return false;
    }

    private boolean checkDistance(int start, int end)
    {
        int startCol = getCol(start);
        int startRow = getRow(start);

        int endCol = getCol(end);
        int endRow = getRow(end);

        int colDiff = Math.abs(startCol - endCol);
        int rowDiff = Math.abs(startRow - endRow);

        return colDiff == 0 || rowDiff == 0 && colDiff == 2 || rowDiff == 2;
    }

    private int getSquareBetween(int start, int end)
    {
        int diff = end - start;
        return start + diff/2;
    }

    private boolean squaresNotBlocked(int start, int middle, int end)
    {
        boolean startAndMidConnected = squaresAreConnected(start, middle);
        boolean midAndEndConnected = squaresAreConnected(middle, end);
        return startAndMidConnected && midAndEndConnected;
    }

    protected boolean squareIsEmpty(int squareNum)
    {
        return getOccupierAtSquare(squareNum) == GameRuleConstants.EMPTY;
    }

    boolean canMoveDiagonally(int start, int end)
    {
        boolean hasAdjacentOpponent = hasAdjacentOpponent(start);
        if(hasAdjacentOpponent)
        {
            int opponentPos = getAdjacentOpponent(start);
            Square endSquare = squares[end];
            boolean endIsAdjacentOpponent = endSquare.isInAdjacencySet(opponentPos);
            boolean startIsDiagonalFromEndSquare = squaresAreDiagonal(start, end);
            boolean opponentIsBlockedFromBehind = squareIsBlockedFromBehind(opponentPos, start);

            return endIsAdjacentOpponent && startIsDiagonalFromEndSquare
                    && opponentIsBlockedFromBehind;
        }

        return false;
    }

    private boolean hasAdjacentOpponent(int squareNum)
    {
        Square fromSquare = squares[squareNum];
        for(int i = 0; i < playerPositions.length; i++)
        {
            int playerPosition = playerPositions[i];
            if (fromSquare.isInAdjacencySet(playerPosition) && squareNum != playerPosition)
                return true;
        }
        return false;
    }

    private int getAdjacentOpponent(int squareNum)
    {
        Square fromSquare = squares[squareNum];
        for(int i = 0; i < playerPositions.length; i++)
        {
            int playerPosition = playerPositions[i];
            if (fromSquare.isInAdjacencySet(playerPosition) && squareNum != playerPosition)
                return playerPosition;
        }
        return -1;
    }

    private boolean squaresAreDiagonal(int first, int second)
    {
        int row1 = getRow(first);
        int row2 = getRow(second);
        int col1 = getCol(first);
        int col2 = getCol(second);

        return Math.abs(row2 - row1) == 1 && Math.abs(col2- col1) == 1;
    }

    private boolean squareIsBlockedFromBehind(int occupiedSquare, int squareInFront)
    {
        int diff = occupiedSquare - squareInFront;
        int squareBehindPos = occupiedSquare + diff;

        boolean squareBehindIsValid = isValidNumber(squareBehindPos);

        if(squareBehindIsValid)
        {
            Square squareBehind = squares[squareBehindPos];
            return squareBehind.isOccupied() || !squareBehind.isInAdjacencySet(occupiedSquare);
        }
        return false;
    }
}
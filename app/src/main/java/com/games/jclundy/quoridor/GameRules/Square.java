package com.games.jclundy.quoridor.GameRules;

import java.util.ArrayList;
import java.util.List;

public class Square {
    int occupierID;
    public int squareNum;
    public int row;
    public int col;
    boolean hasWall;
    boolean canPlaceVerticalWall;
    boolean canPlaceHorizontalWall;

    public List<Integer> adjacencySet;

    public Square(int number){
        squareNum = number;
        occupierID = GameRuleConstants.EMPTY;
        hasWall = false;
        row = getRow(number);
        col = getCol(number);
        initializeAdjacencySet();
        canPlaceHorizontalWall = true;
        canPlaceVerticalWall = true;
    }
    public void initializeAdjacencySet() {
        adjacencySet = new ArrayList<Integer>();
        int adjacentSquares[] = new int[] {squareNum - 1, squareNum - 9, squareNum + 1, squareNum + 9};
        for(int i = 0; i < adjacentSquares.length; i++) {
            int square = adjacentSquares[i];
            if (Board.isValidNumber(square) && isBeside(square)) {
                adjacencySet.add(square);
            }
        }
    }

    public boolean didOccupySquare(int playerID){
        if(occupierID == GameRuleConstants.EMPTY){
            occupierID = playerID;
            return true;
        }
        else
            return false;
    }

    public void occupySquare(int playerID){
        if(occupierID == GameRuleConstants.EMPTY)
            occupierID = playerID;
    }

    public void removePiece(){
        occupierID = GameRuleConstants.EMPTY;
    }

    public boolean isInAdjacencySet(int squareNum){
        return adjacencySet.contains(squareNum);
    }

    protected void removeFromAdjacencySet(int squareNum){
        if(adjacencySet.contains(squareNum)){
            int index = adjacencySet.indexOf(squareNum);
            adjacencySet.remove(index);
        }
    }

    public boolean isBeside(int squareNum){
        int row = getRow(squareNum);
        int col = getCol(squareNum);
        int rowDiff = Math.abs(this.row - row);
        int colDiff = Math.abs(this.col - col);
        return rowDiff <= 1 && colDiff <= 1;
    }

    public void placePiece(int id){
        occupierID = id;
    }

    public int getRow(int squareNum){
        return squareNum/9;
    }
    public int getCol(int squareNum){
        return squareNum % 9;
    }

    public boolean isEmpty()
    {
        return occupierID == GameRuleConstants.EMPTY;
    }

    public boolean isOccupied()
    {
        return !isEmpty();
    }
}
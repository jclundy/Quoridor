package com.games.jclundy.quoridor.GameRules;

import java.util.ArrayList;
import java.util.List;

public class Square {
    int occupierID;
    int squareNum;
    int row;
    int col;
    boolean hasWall;
    List<Integer> adjacencySet;

    public Square(int number){
        squareNum = number;
        occupierID = GameRuleConstants.EMPTY;
        hasWall = false;
        row = getRow(number);
        col = getCol(number);
        initializeAdjacencySet();
    }
    public void initializeAdjacencySet(){
        adjacencySet = new ArrayList<Integer>();
        int rowBelow = row - 1;
        int rowAbove = row + 1;
        int rows[] = {rowBelow, rowAbove};

        int colLeft = col - 1;
        int colRight = col + 1;
        int cols[] = {colLeft, colRight};

        for(int i = 0; i < rows.length; i++){
            for (int j = 0; j < cols.length; j++)
                if(isValidRow(rows[i]) && isValidCol(cols[j])){
                    int squareIndex = getSquareNum(rows[i], cols[j]);
                    adjacencySet.add(squareIndex);
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

    public boolean isAdjacent(int squareNum){
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
    public int getSquareNum(int col, int row){
        return row*9 + col;
    }

    private boolean isValidRow(int row){
        return row >=0  && row < GameRuleConstants.NUM_ROWS;
    }
    private boolean isValidCol(int col){
        return col >= 0 && col < GameRuleConstants.NUM_COLS;
    }
}
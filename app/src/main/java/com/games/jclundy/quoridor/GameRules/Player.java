package com.games.jclundy.quoridor.GameRules;

import java.util.ArrayList;
import java.util.List;

public class Player {
    int id;
    int startPosition;
    int chipsLeft;
    boolean hasWon;
    Stack moveHistory;

    Player(int playerID, int start, int chips){
        id = playerID;
        startPosition = start;
        chipsLeft = chips;
        hasWon = false;
        moveHistory = new Stack();
        Move firstMove = new Move(start);
        moveHistory.push(firstMove);
    }

    public boolean hasReachedEndZone(){
        int startCol = startPosition % GameRuleConstants.NUM_COLS;
        int currentCol = getCurrentSquare() % GameRuleConstants.NUM_COLS;

        int startRow = startPosition / GameRuleConstants.NUM_ROWS;
        int currentRow = getCurrentSquare()/ GameRuleConstants.NUM_ROWS;

        int colDiff = Math.abs(startCol - currentCol);
        int rowDiff = Math.abs(startRow - currentRow);
        return colDiff == 8 || rowDiff == 8;
    }

    public int getCurrentSquare(){
        return moveHistory.peek().playerPosition;
    }

    public Move getLastMove(){
        return moveHistory.peek();
    }

    public void undoMove(){
        Move lastMove = moveHistory.peek();
        if(lastMove.chipPosition > -1)
            chipsLeft ++;
        moveHistory.pop();
    }

    public void recordMove(int square, boolean didPlaceChip){
        Move latestMove;
        if(didPlaceChip && chipsLeft >= 0) {
            latestMove = new Move(getLastMove().playerPosition, square);
            chipsLeft --;
        }
        else
            latestMove = new Move(square);
        moveHistory.push(latestMove);
    }

    public int getChipsLeft(){
        return chipsLeft;
    }

    protected class Stack {
        private int count;
        private List<Move> stack;
        Stack(){
            count = 0;
            stack = new ArrayList<Move>();
        }
        void push(Move move){
            stack.add(move);
            count++;
        }
        void pop(){
            if(stack.size() > 1){
                stack.remove(count - 1);
                count--;
            }
        }

        Move peek(){
            return stack.get(count - 1);
        }

        int size(){
            return stack.size();
        }
    }
}
package com.games.jclundy.quoridor.GameRules;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by devfloater65 on 10/22/14.
 */
public class Player {
    int id;
    int startPosition;
    int currentSquare;
    int chipsLeft;
    boolean hasWon;
    Stack moveHistory;

    Player(int playerID, int start, int chips){
        id = playerID;
        startPosition = start;
        currentSquare = start;
        chipsLeft = chips;
        hasWon = false;
        moveHistory = new Stack();
    }

    public boolean hasReachedEndZone(){
        int startCol = startPosition % GameRuleConstants.NUM_COLS;
        int currentCol = currentSquare % GameRuleConstants.NUM_COLS;

        int startRow = startPosition / GameRuleConstants.NUM_ROWS;
        int currentRow = currentSquare / GameRuleConstants.NUM_ROWS;

        int colDiff = Math.abs(startCol - currentCol);
        int rowDiff = Math.abs(startRow - currentRow);
        return colDiff == 9 || rowDiff == 9;
    }

    protected class Stack {
        private int top;
        private List<Move> stack;
        Stack(){
            top = 0;
            stack = new ArrayList<Move>();
        }
        void push(){

        }
        void pop(){

        }

        Move peek(){
            if(stack.size() > 0)
                return stack.get(top);
            else
                return null;
        }

        int size(){
            return stack.size();
        }
    }

    protected class Move{
        int playerPosition;
        int chipPosition;

        Move(int square){
            playerPosition = square;
            chipPosition = -1;
        }

        Move(int playerPosition, int chipPosition){
            this.playerPosition = playerPosition;
            this.chipPosition = chipPosition;
        }
    }
}
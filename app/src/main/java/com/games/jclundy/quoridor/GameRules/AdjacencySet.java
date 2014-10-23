package com.games.jclundy.quoridor.GameRules;

/**
 * Created by devfloater65 on 10/22/14.
 */
public class AdjacencySet{
    private int nodeID;
    private int [] adjacentTiles;
    private boolean [] adjacencyStatus;

    public AdjacencySet(int id){
        nodeID = id;
        createAdjacencySet();
    }
    private void createAdjacencySet(){
        adjacentTiles = new int[8];
        adjacencyStatus = new boolean[8];
        initializeTiles();
    }
    private void initializeTiles(){
        initializeForwardTiles();
        initializeRightAndLeft();
        initializeRearTiles();
        initializeAdjacency();
    }
    private void initializeForwardTiles(){
        for (int i = 0; i < 3; i++){
            adjacentTiles[i] = nodeID + 8 + i;
        }
    }
    private void initializeRightAndLeft(){
        adjacentTiles[4] = nodeID + 1;
        adjacentTiles[3] = nodeID - 1;
    }
    private void initializeRearTiles(){
        for(int i = 5; i < 8; i++){
            adjacentTiles[i] = nodeID - 8 - i;
        }
    }
    private void initializeAdjacency(){
        for(int i = 0; i < 8; i++){
            if(isValid(adjacentTiles[i])){
                adjacencyStatus[i] = true;
            }
            else
                adjacencyStatus[i] = false;
        }
    }
    private boolean isEven(int n){
        return n % 2 == 0;
    }
    private boolean isPositive(int n){
        return n >= 0;
    }
    private boolean isValid(int n){
        return isEven(n) && isPositive(n);
    }
    public boolean isInAdjacencySet(int node){
        for(int i = 0; i < adjacentTiles.length; i++){
            return node == adjacentTiles[i] && adjacencyStatus[i];
        }
        return false;
    }
}
/*
  representation:
  0 1 2
  3 x 4
  5 6 7
 */

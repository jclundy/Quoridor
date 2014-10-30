package com.games.jclundy.quoridor.GameRules;

import android.app.Application;
import android.test.ApplicationTestCase;

import java.util.ArrayList;
import java.util.List;

public class SquareTest extends ApplicationTestCase<Application> {
    public SquareTest() {
        super(Application.class);
    }

    public void testInitialization(){
        Square square = new Square(40);
        assertEquals(40, square.squareNum);
        assertEquals(4, square.row);
        assertEquals(4, square.col);
        assertEquals(8, square.adjacencySet.size());
        assertFalse(square.hasWall);
        assertEquals(0, square.occupierID);
    }

    public void testInitializationEdgeCases(){
        Square square = new Square(0);
        assertEquals(0, square.squareNum);
        assertEquals(0, square.row);
        assertEquals(0, square.col);
        assertEquals(3, square.adjacencySet.size());
        assertFalse(square.hasWall);
        assertEquals(0, square.occupierID);
    }

    public void testOccupySquare(){
        Square square = new Square(0);
        square.occupySquare(1);
        assertEquals(1, square.occupierID);

        square.occupySquare(2);
        assertEquals(1, square.occupierID);

        square.removePiece();
        assertEquals(GameRuleConstants.EMPTY, square.occupierID);

        square.occupySquare(2);
        assertEquals(2, square.occupierID);
    }

    public void testisInAdjacencySet(){
        //left-hand corner
        Square square = new Square(0);
        compareSets(TestConstants.ZERO_SET, square);

        //right-hand corner
        square = new Square(80);
        compareSets(TestConstants.EIGHTY_SET, square);

        //middle-of board

        square = new Square(40);
        compareSets(TestConstants.FORTY_SET, square);
    }

    private void compareSets(int[] expected, Square square){
        List<Integer> actual = square.adjacencySet;
        assertEquals(expected.length, actual.size());
        for(int i = 0; i < actual.size() && i < expected.length; i++)
            assertTrue(actual.contains(expected[i]));
    }
    private void compareSets(List<Integer> expected, Square square){
        List<Integer> actual = square.adjacencySet;
        assertEquals(expected.size(), actual.size());
        for(int i = 0; i < actual.size() && i < expected.size(); i++)
            assertTrue(actual.contains(expected.get(i)));
    }

    public void testRemoveFromAdjacencySet(){
        compareRemoveEachSquareInSet(0, TestConstants.ZERO_SET);
        compareRemoveEachSquareInSet(40, TestConstants.FORTY_SET);
        compareRemoveEachSquareInSet(80, TestConstants.EIGHTY_SET);
    }

    private List<Integer> initalizeListWithArray(int [] array){
        List<Integer> set = new ArrayList<Integer>();
        for(int i = 0; i < array.length; i++)
            set.add(array[i]);
        return set;
    }

    private void compareRemovalFromAdjacency(int squareNum, int notInSet, int inSet, List<Integer> finalSet){
        Square square = new Square(squareNum);
        List<Integer> originalSet = square.adjacencySet;
        square.removeFromAdjacencySet(notInSet);
        compareSets(originalSet, square);
        square.removeFromAdjacencySet(inSet);
        compareSets(finalSet, square);
    }

    private void compareRemoveEachSquareInSet(int squareNum, int[] defaultSet){
        List<Integer> set = initalizeListWithArray(defaultSet);
        for(int i = 0; i < set.size(); i++){
            List<Integer> finalSet = set;
            int itemToRemove = finalSet.get(i);
            finalSet.remove(itemToRemove);
            compareRemovalFromAdjacency(squareNum, squareNum + 18, itemToRemove, finalSet);
        }
    }

    public void testIsAdjacent(){
        compareAdjacencyOfSet(0, TestConstants.ZERO_SET);
        compareAdjacencyOfSet(40, TestConstants.FORTY_SET);
        compareAdjacencyOfSet(80, TestConstants.EIGHTY_SET);
    }

    private void compareAdjacencyOfSet(int number, int [] set){
        Square square = new Square(number);
        for(int i = 0; i < set.length; i++){
            assertTrue(square.isAdjacent(set[i]));
        }
    }

    public void testRemovePiece(){
        compareRemoval(0, 4);
        compareRemoval(40, 4);
        compareRemoval(80, 4);
    }

    private void compareRemoval(int number, int playerID){
        Square square = new Square(number);
        square.placePiece(playerID);
        assertEquals(playerID, square.occupierID);
        square.removePiece();
        assertEquals(GameRuleConstants.EMPTY, square.occupierID);
    }
}

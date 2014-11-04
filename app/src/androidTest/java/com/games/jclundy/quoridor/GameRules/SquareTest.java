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
        assertEquals(GameRuleConstants.EMPTY, square.occupierID);
    }

    public void testInitializationEdgeCases(){
        Square square = new Square(0);
        assertEquals(0, square.squareNum);
        assertEquals(0, square.row);
        assertEquals(0, square.col);
        assertEquals(3, square.adjacencySet.size());
        assertFalse(square.hasWall);
        assertEquals(GameRuleConstants.EMPTY, square.occupierID);
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
        isAdjacentShouldBeTrueForSquaresInAdjacencySet(0, TestConstants.ZERO_SET);

        //right-hand corner
        isAdjacentShouldBeTrueForSquaresInAdjacencySet(80, TestConstants.EIGHTY_SET);

        //middle-of board

        isAdjacentShouldBeTrueForSquaresInAdjacencySet(40, TestConstants.FORTY_SET);

        isAdjacentShouldBeTrueForSquaresInAdjacencySet(4, TestConstants.FOUR_SET);
    }

    private void isAdjacentShouldBeTrueForSquaresInAdjacencySet(int index, int[] expected){
        Square square = new Square(index);
        List<Integer> actual = square.adjacencySet;
        assertEquals(expected.length, actual.size());
        for(int i = 0; i < actual.size(); i++){
            assertTrue(square.isAdjacent(expected[i]));
            assertTrue(actual.contains(expected[i]));
        }
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

    private void compareRemoveEachSquareInSet(int squareNum, int[] defaultSet){
        List<Integer> expectedSet = initalizeListWithArray(defaultSet);
        Square square = new Square(squareNum);
        for(int i = 0; i < expectedSet.size(); i++){
            assertEquals(expectedSet.size(),square.adjacencySet.size());
            int itemToRemove = expectedSet.get(i);
            expectedSet.remove(i);
            square.removeFromAdjacencySet(itemToRemove);
            compareSets(expectedSet, square);
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

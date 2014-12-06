package com.games.jclundy.quoridor.PathFinding;

import android.app.Application;
import android.test.ApplicationTestCase;

import com.games.jclundy.quoridor.GameRules.Board;
import com.games.jclundy.quoridor.GameRules.GameRuleConstants;
import com.games.jclundy.quoridor.GameRules.Square;

/**
 * Created by devfloater65 on 12/3/14.
 */
public class GraphTest extends ApplicationTestCase<Application> {
    public GraphTest() {
        super(Application.class);
    }

    public void testDijkstra()
    {
        Square[] squares = new Square[81];
        for (int i = 0; i < squares.length; i++)
            squares[i] = new Square(i);

        int startIndex = 0;
        Graph graph = new Graph(startIndex,squares);
        graph.runDijkstra();

        assertEquals(0, graph.getDistanceToNode(startIndex));
        for(int i = 0; i < squares[startIndex].adjacencySet.size(); i++)
        {
            assertEquals(1, graph.getDistanceToNode(squares[startIndex].adjacencySet.get(i)));
        }

        assertEquals(8, graph.getDistanceToNode(72));
    }

    public void testDijkstraEmptyBoard()
    {
        Square[] squares = new Square[81];
        for (int i = 0; i < squares.length; i++)
            squares[i] = new Square(i);
        int startIndex = 0;
        Graph graph = new Graph(startIndex,squares);
        graph.runDijkstra();

        for(int i = 0; i < 81; i++)
        {
            assertTrue(graph.getDistanceToNode(i) != Graph.INFINTE);
        }
    }

    public void testDijkstraWithAWall()
    {
        Board board = new Board(2);
        int startIndex = 0;
        board.placeWall(startIndex, false);

        Square [] squares = board.squares;

        Graph graph = new Graph(startIndex, squares);
        graph.runDijkstra();

        assertEquals(0, graph.getDistanceToNode(startIndex));
        assertEquals(12, graph.getDistanceToNode(72));
    }

    public void testDijkstraBlockOffStart()
    {
        Board board = new Board(2);
        int startIndex = 13;
        board.placeWall(startIndex, false);
        board.placeWall(4, true);
        board.placeWall(3, false);
        board.placeWall(12, true);

        Graph graph = new Graph(startIndex, board.squares);
        graph.runDijkstra();

        for(int i = 0; i < board.squares[startIndex].adjacencySet.size(); i++)
        {
            assertEquals(Graph.INFINTE, graph.getDistanceToNode(board.squares[startIndex].adjacencySet.get(i)));
        }
    }

    public void testDijkstraPath()
    {
        Square[] squares = new Square[81];
        for (int i = 0; i < squares.length; i++)
            squares[i] = new Square(i);

        int startIndex = 0;
        Graph graph = new Graph(startIndex,squares);
        graph.runDijkstra();
        Stack path = graph.getShortestPathToNode(72);
        assertEquals(8, path.size());

        for (int i = 0; i < path.size(); i++)
        {
            assertEquals(i * 9, path.peek());
            path.pop();
        }
    }

    public void testDikstraPathsOfInterest()
    {
        Board board = new Board(2);
        int startIndex = 13;
        board.placeWall(startIndex, false);
        board.placeWall(4, true);

        board.placeWall(3, false);
        Graph graph = new Graph(startIndex, board.squares, GameRuleConstants.PLAYER_2_ENDZONE);
        graph.runDijkstra();
        assertTrue(graph.hasOpenPathToFinish()) ;

        board.placeWall(12, true);
        graph = new Graph(startIndex, board.squares, GameRuleConstants.PLAYER_2_ENDZONE);
        graph.runDijkstra();
        assertFalse(graph.hasOpenPathToFinish());
    }
}
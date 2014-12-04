package com.games.jclundy.quoridor.PathFinding;

import android.app.Application;
import android.test.ApplicationTestCase;

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
        int startIndex = 0;
        Graph graph = new Graph(startIndex,squares);
        graph.runDijkstra();

        assertEquals(0, graph.getDistanceToNode(startIndex));
    }
}

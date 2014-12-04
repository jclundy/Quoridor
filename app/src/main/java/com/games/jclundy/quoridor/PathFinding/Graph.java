package com.games.jclundy.quoridor.PathFinding;

import com.games.jclundy.quoridor.GameRules.Square;

import java.util.List;

/**
 * Created by devfloater65 on 12/3/14.
 */
public class Graph {

    private int start;
    private int[] distances;
    private boolean [] visited;
    private Square[] nodes;
    private int unvisitedCount;

    private static final int INFINTE = 99999;
    private static final int UNDEFINED = -1111;

    public Graph(int startNode, Square[] squares)
    {
        start = startNode;
        nodes = squares;
        distances = new int[squares.length];
        visited = new boolean[squares.length];
    }

    public int getDistanceToNode(int nodeIndex)
    {
        return distances[nodeIndex];
    }

    public void runDijkstra()
    {
        initialize();
        iterateThroughNodes();
    }

    private void initialize()
    {
        for(int i = 0; i < visited.length; i++)
        {
            unvisitedCount = nodes.length;
            visited[i] = false;
            distances[i] = INFINTE;
        }
        distances[start] = 0;
    }

    private void iterateThroughNodes()
    {
        while(unvisitedCount > 0)
        {
            int currentNodeIndex = getIndexOfClosestVertexToSource();
            if(currentNodeIndex == UNDEFINED)
                return;

            visited[currentNodeIndex] = true;
            unvisitedCount --;
            iterateThroughAdjacencySetOfNode(currentNodeIndex);
        }
    }

    private int getIndexOfClosestVertexToSource()
    {
        int minDistance = INFINTE;
        int indexOfClosestVertex = UNDEFINED;
        for(int i = 0; i < distances.length; i++)
        {
            if(!visited[i] && distances[i] < minDistance)
            {
                minDistance = distances[i];
                indexOfClosestVertex = i;
            }
        }
        return indexOfClosestVertex;
    }

    private void iterateThroughAdjacencySetOfNode(int nodeIndex)
    {
        Square node = nodes[nodeIndex];
        for (int i = 0; i < node.adjacencySet.size(); i++)
        {
            int neighbourIndex = node.adjacencySet.get(i);
            int newDistance = distances[nodeIndex] + 1;
            if(newDistance < distances[neighbourIndex])
                distances[neighbourIndex] = newDistance;
        }
    }
}

/*
Algorithms

function Dijkstra(Graph, source):
 2      dist[source]  := 0                     // Distance from source to source
 3      for each vertex v in Graph:            // Initializations
 4          if v ≠ source
 5              dist[v]  := infinity           // Unknown distance function from source to v
 6              previous[v]  := undefined      // Previous node in optimal path from source
 7          end if
 8          add v to Q                         // All nodes initially in Q (unvisited nodes)
 9      end for
10
11      while Q is not empty:                  // The main loop
12          u := vertex in Q with min dist[u]  // Source node in first case
13          remove u from Q
14
15          for each neighbor v of u:           // where v has not yet been removed from Q.
16              alt := dist[u] + length(u, v)
17              if alt < dist[v]:               // A shorter path to v has been found
18                  dist[v]  := alt
19                  previous[v]  := u
20              end if
21          end for
22      end while
23      return dist[], previous[]
24  end function
If we are only interested in a shortest path between vertices source and target,
we can terminate the search at line 13 if u = target. Now we can read the shortest path from source
to target by reverse iteration:

function A*(start,goal)
        closedset := the empty set    // The set of nodes already evaluated.
        openset := {start}    // The set of tentative nodes to be evaluated, initially containing the start node
        came_from := the empty map    // The map of navigated nodes.

        g_score[start] := 0    // Cost from start along best known path.
        // Estimated total cost from start to goal through y.
        f_score[start] := g_score[start] + heuristic_cost_estimate(start, goal)

        while openset is not empty
        current := the node in openset having the lowest f_score[] value
        if current = goal
        return reconstruct_path(came_from, goal)

        remove current from openset
        add current to closedset
        for each neighbor in neighbor_nodes(current)
        if neighbor in closedset
        continue
        tentative_g_score := g_score[current] + dist_between(current,neighbor)

        if neighbor not in openset or tentative_g_score < g_score[neighbor]
        came_from[neighbor] := current
        g_score[neighbor] := tentative_g_score
        f_score[neighbor] := g_score[neighbor] + heuristic_cost_estimate(neighbor, goal)
        if neighbor not in openset
        add neighbor to openset

        return failure

        function reconstruct_path(came_from,current)
        total_path := [current]
        while current in came_from:
        current := came_from[current]
        total_path.append(current)
        return total_path
        Remark: the above pseudocode assumes that the heuristic function is monotonic (or consistent, see below),
        which is a frequent case in many practical problems, such as the Shortest Distance Path in road networks.
        However, if the assumption is not true, nodes in the closed set may be rediscovered and their cost improved.
        In other words, the closed set can be omitted (yielding a tree search algorithm) if a solution is guaranteed
        to exist, or if the algorithm is adapted so that new nodes are added to the open set only if they have a lower
        f value than at any previous iteration.
        */
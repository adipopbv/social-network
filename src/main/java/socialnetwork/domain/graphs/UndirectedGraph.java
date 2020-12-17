package socialnetwork.domain.graphs;

import socialnetwork.domain.Id;

import java.util.*;

public class UndirectedGraph {
    protected Map<Id, HashSet<Id>> adjMap;
    protected Map<Id, Boolean> visited = new HashMap<>();

    public UndirectedGraph(Map<Id, HashSet<Id>> adjMap) {
        this.adjMap = adjMap;
        for (Id userId : adjMap.keySet()) {
            for (Id friendId : adjMap.get(userId)) {
                visited.put(userId, false);
                visited.put(friendId, false);
            }
        }
    }

    private void resetVisited() {
        visited.replaceAll((k, v) -> false);
    }

    private int visitNextVertex(Id vertex) {
        int maxPathLength = 0;
        visited.put(vertex, true);
        for (Id adjVertex : adjMap.get(vertex)) {
            if (!visited.get(adjVertex)) {
                int pathLength = visitNextVertex(adjVertex);
                maxPathLength = Math.max(maxPathLength, pathLength);
            }
        }
        return maxPathLength + 1;
    }

    public int getConnectedComponentsCount() {
        int count = 0;
        resetVisited();
        for (Id vertex : visited.keySet()) {
            if (!visited.get(vertex)) {
                visitNextVertex(vertex);
                count++;
            }
        }
        return count;
    }

    private void makeSubgraph(Id vertex, Collection<Id> connectedComponent) {
        connectedComponent.add(vertex);
        visited.put(vertex, true);
        for (Id adjVertex : adjMap.get(vertex)) {
            if (!visited.get(adjVertex)) {
                makeSubgraph(adjVertex, connectedComponent);
            }
        }
    }

    public Iterable<Id> getConnectedComponentWithLongestRoad() {
        resetVisited();
        int maxPathLength = 0;
        Id startVertex = new Id(-1);
        for (Id vertex : visited.keySet()) {
            if (!visited.get(vertex)) {
                int pathLength = visitNextVertex(vertex);
                if (maxPathLength < pathLength) {
                    maxPathLength = pathLength;
                    startVertex = vertex;
                }
            }
        }
        Collection<Id> connectedComponent = new ArrayList<>();
        resetVisited();
        makeSubgraph(startVertex, connectedComponent);
        return connectedComponent;
    }
}

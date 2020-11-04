package socialnetwork.domain.graphs;

import java.util.*;

public class UndirectedGraph {
    protected Map<Long, HashSet<Long>> adjMap;
    protected Map<Long, Boolean> visited = new HashMap<>();

    public UndirectedGraph(Map<Long, HashSet<Long>> adjMap) {
        this.adjMap = adjMap;
        for (long userId : adjMap.keySet()) {
            for (long friendId : adjMap.get(userId)) {
                visited.put(userId, false);
                visited.put(friendId, false);
            }
        }
    }

    private void resetVisited() {
        visited.replaceAll((k, v) -> false);
    }

    private int visitNextVertex(Long vertex) {
        int maxPathLength = 0;
        visited.put(vertex, true);
        for (long adjVertex : adjMap.get(vertex)) {
            if (!visited.get(adjVertex)) {
                int pathLenght = visitNextVertex(adjVertex);
                maxPathLength = Math.max(maxPathLength, pathLenght);
            }
        }
        return maxPathLength + 1;
    }

    public int getConnectedComponentsCount() {
        int count = 0;
        resetVisited();
        for (long vertex : visited.keySet()) {
            if (!visited.get(vertex)) {
                visitNextVertex(vertex);
                count++;
            }
        }
        return count;
    }

    private void makeSubgraph(long vertex, Collection<Long> connectedComponent) {
        connectedComponent.add(vertex);
        visited.put(vertex, true);
        for (long adjVertex : adjMap.get(vertex)) {
            if (!visited.get(adjVertex)) {
                makeSubgraph(adjVertex, connectedComponent);
            }
        }
    }

    public Iterable<Long> getConnectedComponentWithLongestRoad() {
        resetVisited();
        int maxPathLength = 0;
        long startVertex = -1;
        for (long vertex : visited.keySet()) {
            if (!visited.get(vertex)) {
                int pathLenght = visitNextVertex(vertex);
                if (maxPathLength < pathLenght) {
                    maxPathLength = pathLenght;
                    startVertex = vertex;
                }
            }
        }
        Collection<Long> connectedComponent = new ArrayList<>();
        resetVisited();
        makeSubgraph(startVertex, connectedComponent);
        return connectedComponent;
    }
}

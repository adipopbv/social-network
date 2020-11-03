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

    private void findNextVertex(Long vertex) {
        visited.put(vertex, true);
        for (long adjVertex : adjMap.get(vertex)) {
            if (!visited.get(adjVertex)) {
                findNextVertex(adjVertex);
            }
        }
    }

    public int getConnectedComponentsCount() {
        int count = 0;
        for (long vertex : visited.keySet()) {
            if (!visited.get(vertex)) {
                findNextVertex(vertex);
                count++;
            }
        }
        return count;
    }
}

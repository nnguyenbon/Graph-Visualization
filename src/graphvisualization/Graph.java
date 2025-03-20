/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphvisualization;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

/**
 *
 * @author CE191239 Nguyen Kim Bao Nguyen
 */
public class Graph {

    private final int MAX_VERTEX = 100;
    private int[][] graph;
    private int numberOfVertices;
    private int startTraversal, endTraversal, sum = 0;
    private int[] isVisited = new int[MAX_VERTEX];
    private int[] distance = new int[MAX_VERTEX];
    private int[] parent = new int[MAX_VERTEX];
    private String result;

    public Graph() {
        this.graph = new int[MAX_VERTEX][MAX_VERTEX];
        for (int i = 0; i < MAX_VERTEX; i++) {
            for (int j = 0; j < MAX_VERTEX; j++) {
                graph[i][j] = 0;
            }
        }
    }

    private void resetIsVisited() {
        for (int i = 0; i < isVisited.length; i++) {
            isVisited[i] = 0;
        }
    }

    private void resetParent() {
        for (int i = 0; i < parent.length; i++) {
            parent[i] = 0;
        }
    }

    private void resetDistance() {
        for (int i = 0; i < MAX_VERTEX; i++) {
            distance[i] = Integer.MAX_VALUE;
        }
    }

    private void DFS(int start) {
//        resetIsVisited();
//        resetParent();
        result = "";
        Stack<Integer> s = new Stack<>();
        s.add(start);

        int u;
        while (!s.isEmpty()) {
            u = s.pop();
            if (isVisited[u] == 0) {
                result += "," + u;
                isVisited[u] = 1;
                for (int v = numberOfVertices - 1; v >= 0; v--) {
                    if (graph[u][v] >= 1 && isVisited[v] == 0) {
                        s.add(v);
                        parent[v] = u;
                    }
                }
            }
        }
    }

    private void BFS(int start) {
//        resetIsVisited();
//        resetParent();
        result = "";
        Queue<Integer> q = new LinkedList<>();
        q.add(start);
        isVisited[start] = 1;
        int u;
        while (!q.isEmpty()) {
            u = q.poll();
            result += "," + u;
            for (int v = 0; v < numberOfVertices; v++) {
                if (graph[u][v] >= 1 && isVisited[v] == 0) {
                    q.add(v);
                    isVisited[v] = 1;
                }
            }
        }
    }

    private int findNearestint() {
        int minIndex = -1;
        int minValue = Integer.MAX_VALUE;
        for (int i = 0; i < numberOfVertices; i++) {
            if (distance[i] < minValue && isVisited[i] == 0) {
                minValue = distance[i];
                minIndex = i;
            }
        }
        return minIndex;
    }

    private void prim() {
        resetIsVisited();
        resetDistance();
        resetParent();
        sum = 0;
        int u;
        distance[0] = 0;
        for (int i = 0; i < numberOfVertices; i++) {
            u = findNearestint(); // 0
            if (u == -1) {
                result = "Impossible";
                return;
            }
            isVisited[u] = 1;
            sum += distance[u];
            for (int v = 0; v < numberOfVertices; v++) {
                if (graph[u][v] > 0 && isVisited[v] == 0
                        && distance[v] > graph[u][v]) {
                    distance[v] = graph[u][v];
                    parent[v] = u;
                }
            }
        }
        for (int i = 1; i < numberOfVertices; i++) {
            result += "\n" + i + " " + parent[i] + ":\t " + distance[i];
        }
    }

    private void spDijkstra(int start) {
        resetIsVisited();
        resetDistance();
        resetParent();
        result = "";
        int u;
        distance[start] = 0;
        for (int i = 0; i < numberOfVertices; i++) {
            u = findNearestint(); // 0
            if (u == -1) {
                result = "  No Path";
                return;
            }
            isVisited[u] = 1;
            System.out.print(u + " - distance: " + distance[u] + "\n");
            for (int v = 0; v < numberOfVertices; v++) {
                if (graph[u][v] > 0 && isVisited[v] == 0
                        && distance[v] > distance[u] + graph[u][v]) {
                    System.out.println("v: " + v);
                    distance[v] = distance[u] + graph[u][v];
                    parent[v] = u;
                }
            }
        }

        for (int i = 0; i < numberOfVertices; i++) {
            result += i + "--" + distance[i] + "\n";
        }
    }

    private void spDijkstra(int start, int end) {
        spDijkstra(start);
        result = distance[end] + "\n";
    }
}

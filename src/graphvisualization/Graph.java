/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphvisualization;

import java.util.ArrayList;
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
    private int sum = 0;
    private int[] isVisited = new int[MAX_VERTEX];
    private int[] distance = new int[MAX_VERTEX];
    private int[] parent = new int[MAX_VERTEX];
    private String result;
    private int startV, endV;
    private ArrayList<Integer> path;

    /**
     *
     */
    public Graph() {
        this.numberOfVertices = numberOfVertices;
        this.graph = new int[MAX_VERTEX][MAX_VERTEX];
        for (int i = 0; i < MAX_VERTEX; i++) {
            for (int j = 0; j < MAX_VERTEX; j++) {
                graph[i][j] = 0;
            }
        }
    }

    /**
     *
     */
    public void print() {
        for (int i = 0; i < numberOfVertices; i++) {
            for (int j = 0; j < numberOfVertices; j++) {
                System.out.print(graph[i][j] + " ");
            }
            System.out.println("");
        }
    }

    /**
     *
     * @param graph
     */
    public void setGraph(int[][] graph) {
        this.graph = graph;
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

    public void DFS(int start) {
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

    public void BFS(int start) {
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
        System.out.println("minIndex " + minIndex);
        return minIndex;
    }

    public void prim() {
        path = new ArrayList<>();
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
            path.add(u);
            for (int v = 0; v < numberOfVertices; v++) {
                if (graph[u][v] > 0 && isVisited[v] == 0
                        && distance[v] > graph[u][v]) {
                    distance[v] = graph[u][v];
                    parent[v] = u;
                }
            }
        }
        
        result = String.valueOf(sum);
    }

    public void spDijkstra(int start) {
        System.out.println("start: " + start);
        result = "";
        int u;
        distance[start] = 0;
        
        for (int i = 0; i < numberOfVertices; i++) {
            System.out.println("i: " + i);
            u = findNearestint(); // 0
            System.out.println("176: " + u);
            if (u == -1) {
                result = " No Path 178";
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

        result = "";
        backtrackDijkstra(startV, endV);
    }

    public void spDijkstra(int start, int end) {
        path = new ArrayList<>();
        startV = start;
        endV = end;
        System.out.println("start: " + startV + " end: " + endV);
        spDijkstra(startV);
    }

    public void backtrackDijkstra(int start, int des) {
        print();
        int u = des;
        Stack<Integer> s = new Stack<>();
        s.add(u);
        while (u != start) {
            u = parent[u];
            s.add(u);
            System.out.println(u);
        }
        while (!s.empty()) {
            int v = s.pop();
            path.add(v);
            result += "->" + (v); //index đỉnh là 0
            System.out.println("re: " + result);
        }

        System.out.println("distance " + distance[des]);
        if (distance[des] != 0) {
            String r = "-" + distance[des] + ":" + result.substring(2);
            result = r;
        } else {
            result = " No Path 227";
        }
    }

    public String getResult() {
        return result.substring(1);
    }

    public ArrayList<Integer> getPath() {
        return path;
    }
}

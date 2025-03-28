/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphvisualization;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 *
 * @author CE191239 Nguyen Kim Bao Nguyen
 */
public class GraphPanel extends JPanel {

    private Map<Point, Map<Point, Integer>> edges;
    private JPanel drawPanel;
    private JLabel result;
    private final MainFrame mainFrame;
    private int count = 0;
    private final int diameter = 30;
    private Point firstSelectedVertex = null;
    private boolean isLine = false, isHighLight = false;
    private int[][] matrix;
    private Graph graph;
    private ConfigurationPanel confi;
    private StringBuilder sb;

    /**
     * constructor
     *
     * @param mainFrame
     * @param confi
     */
    public GraphPanel(MainFrame mainFrame, ConfigurationPanel confi) {
        this.mainFrame = mainFrame;
        this.confi = confi;
        edges = new HashMap<>();
        setPreferredSize(new Dimension(MainFrame.WINDOW_SIZE_X / 2 + 230, MainFrame.WINDOW_SIZE_Y / 2 + 280));
        setBackground(null);

        JLabel label = new JLabel("Draw graph yourself");
        label.setFont(new Font("Arial", Font.BOLD, 20));
        add(label, BorderLayout.NORTH);
        add(displayPanel(), BorderLayout.CENTER);

        Timer timer = new Timer(100, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!edges.isEmpty()) {
                    convertIntoMatrix();
                    if (!confi.isIsMatrix()) {
                        convertIntoList();
                    }
                }
            }
        });

        timer.start();
    }

    /**
     * Create right panel
     */
    private JPanel displayPanel() {
        //create canvas
        drawPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                for (Point p : edges.keySet()) {
                    drawNode(g, p.getX(), p.getY(), p.getNumber());
                }

                for (Map.Entry<Point, Map<Point, Integer>> entry : edges.entrySet()) {
                    for (Map.Entry<Point, Integer> edge : entry.getValue().entrySet()) {
                        if (edge.getValue() > 0) {
                            drawEdge(g, entry.getKey(), edge.getKey(), edge.getValue());
                        }
                    }
                }

                if (isHighLight) {
                    ArrayList<Integer> path = graph.getPath();
                    for (Integer element : path) {
                        for (Point p : edges.keySet()) {
                            if (p.getNumber() == element) {
                                highlightNode(g, p.getX(), p.getY(), p.getNumber());
                            }
                        }
                    }

                    for (int i = 0; i < path.size() - 1; i++) {
                        Point start = null, end = null;

                        // Tìm điểm tương ứng với path[i]
                        for (Point p : edges.keySet()) {
                            if (p.getNumber() == path.get(i)) {
                                start = p;
                            } else if (p.getNumber() == path.get(i + 1)) {
                                end = p;
                            }
                        }

                        // Nếu tìm được cả hai điểm, vẽ cạnh nối giữa chúng
                        if (start != null && end != null) {
                            Integer weight = edges.get(start).get(end); // Lấy trọng số nếu có
                            if (weight != null) {
                                highlightEdge(g, start, end, weight);
                            }
                        }
                    }
                }
            }
        };
        drawPanel.setBackground(Color.white);
        drawPanel.setPreferredSize(new Dimension(MainFrame.WINDOW_SIZE_X / 2 + 200, MainFrame.WINDOW_SIZE_Y / 2 + 280));

        drawPanel.add(createResultField());

        setUpMouseListener();

        return drawPanel;
    }

    /**
     * Create result field, for algorithm
     */
    private JLabel createResultField() {
        result = new JLabel();
        result.setFont(new Font("", Font.LAYOUT_LEFT_TO_RIGHT, 20));
        return result;
    }

    /**
     * set action listener for mouse
     */
    private void setUpMouseListener() {
        drawPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {

                resetFunction();
                int x = e.getX();
                int y = e.getY();
                int panelWidth = drawPanel.getWidth();
                int panelHeight = drawPanel.getHeight();

                System.out.println("x " + x + " - y " + y);

                if (mainFrame.isCtrlPressed()) {
                    System.out.println("ctrl");
                    if (x >= panelWidth - 30 || y >= panelHeight - 30 || x <= 30 || y <= 50) {
                        result.setText("Too close the border");
                        return;
                    }
                    System.out.println("add roi " + x + " " + y);
                    addPoint(x, y);
                    drawPanel.repaint();
                } else if (mainFrame.isShiftPressed()) {
                    System.out.println("shift");
                    removePoint(x, y);
                } else {
                    if (getVertexAt(x, y) != null) {
                        selectVertex(x, y);
                    } else {

                    }
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                System.out.println("drop");
            }
        });

        drawPanel.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                System.out.println("move");
            }
        });
    }

    /**
     * add Point to graph
     *
     * @param x
     * @param y
     */
    public void addPoint(int x, int y) {
        if (!checkCollision(x, y)) {
            result.setText("Collision, please draw it further");
            return;
        }
        Point newPoint = new Point(x, y, count++);
        edges.put(newPoint, new HashMap<>());
        result.setText("Done");
        drawPanel.repaint();
    }

    /**
     * remove Point from graph
     *
     * @param x
     * @param y
     */
    public void removePoint(int x, int y) {
        Point target = getVertexAt(x, y);
        if (target != null) {
            edges.remove(target);
            for (Map<Point, Integer> adj : edges.values()) {
                adj.remove(target);
            }
            result.setText("Remove Successfully");
            drawPanel.repaint();
        } else {
            result.setText("Please choose a vertex");
        }
    }

    /**
     *
     */
    private void selectVertex(int x, int y) {
        Point selected = getVertexAt(x, y);
        if (selected == null) {
            return;
        }

        if (firstSelectedVertex == null) {
            firstSelectedVertex = selected;
        } else {
            if (firstSelectedVertex != selected) {
                addEdge(firstSelectedVertex, selected);
            }
            firstSelectedVertex = null;

            drawPanel.repaint();
        }
    }

    /**
     *
     * @param p1
     * @param p2
     */
    public void addEdge(Point p1, Point p2) {
        String input = JOptionPane.showInputDialog("Please enter edge's weight", 1);
        int weight = 1;

        try {
            int parsedWeight = Integer.parseInt(input);
            if (parsedWeight >= 1) {
                weight = parsedWeight;

                edges.get(p1).put(p2, weight);
                edges.get(p2).put(p1, weight);
            } else if (parsedWeight == 0) {
                edges.get(p1).remove(p2);
                edges.get(p2).remove(p1);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Invalid input!");
        }
    }

    public void dfs() {
        if (edges.isEmpty()) {
            confi.getResultTextField().setText("Graph is empty");
            return;
        }

        int size = edges.size() - 1;
        try {
            String start = JOptionPane.showInputDialog("Enter start vertex", 0);
            int startV = Integer.parseInt(start);
            if (startV >= 0 && startV <= size) {
                graph.DFS(startV);
                result.setText("The DFS traversal from " + startV + " is: " + graph.getResult());
            } else if (startV > size) {
                JOptionPane.showMessageDialog(null, "Out of vertex number");
            } else {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Invalid input! Using default start vertex: 0");
        }
    }

    public void bfs() {
        if (edges.isEmpty()) {
            confi.getResultTextField().setText("Graph is empty");
            return;
        }

        int size = edges.size() - 1;
        try {
            String start = JOptionPane.showInputDialog("Enter start vertex", 0);
            int startV = Integer.parseInt(start);
            if (startV >= 0 && startV <= size) {
                graph.DFS(startV);
                result.setText("The BFS traversal from " + startV + " is: " + graph.getResult());
            } else if (startV > edges.size()) {
                JOptionPane.showMessageDialog(null, "Out of vertex number");
            } else {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Invalid input! Using default start vertex: 0");
        }
    }

    public void sp() {
        if (edges.isEmpty()) {
            confi.getResultTextField().setText("Graph is empty");
            return;
        }

        int size = edges.size() - 1;
        try {
            String start = JOptionPane.showInputDialog("Enter start vertex", 0);
            String end = JOptionPane.showInputDialog("Enter start vertex", size);

            int startV = Integer.parseInt(start);
            int endV = Integer.parseInt(end);
            if ((startV >= 0 && startV <= size) && (endV >= 0 && endV <= size && endV != startV)) {
                graph.spDijkstra(startV, endV);
                isHighLight = true;
                drawPanel.repaint();
                result.setText("The length of the shortest path from " + startV + " to " + endV + " is " + graph.getResult());
            } else if (startV > size || endV > size) {
                JOptionPane.showMessageDialog(null, "Out of vertex number");
            } else {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Invalid input! Using default start vertex: 0; end vertex: " + size);
        }
    }

    public void minimumst() {
        if (edges.isEmpty()) {
            confi.getResultTextField().setText("Graph is empty");
            return;
        }

        graph.prim();
        isHighLight = true;
        drawPanel.repaint();
        System.out.println(graph.getResult());
        result.setText("The minimum spanning tree is " + graph.getResult());
    }

    private boolean checkCollision(int x, int y) {
        for (Point point : edges.keySet()) {
            if (Math.sqrt(Math.pow((point.getX() - x), 2) + Math.pow((point.getY() - y), 2)) <= diameter * 2) {
                return false;
            }
        }
        return true;
    }

    private Point getVertexAt(int x, int y) {
        for (Point vertex : edges.keySet()) {
            if (Math.sqrt(Math.pow(vertex.getX() - x, 2) + Math.pow(vertex.getY() - y, 2)) <= diameter / 2) {
                return vertex;
            }
        }
        return null;
    }

    private boolean isPointOnEdge(int x, int y, Point p1, Point p2) {
        double dx = p2.getX() - p1.getX();
        double dy = p2.getY() - p1.getY();
        double length = Math.sqrt(dx * dx + dy * dy);

        // Tránh chia cho 0
        if (length == 0) {
            return false;
        }

        // Kiểm tra nếu (x, y) nằm giữa p1 và p2
        if (x < Math.min(p1.getX(), p2.getX()) || x > Math.max(p1.getX(), p2.getX())
                || y < Math.min(p1.getY(), p2.getY()) || y > Math.max(p1.getY(), p2.getY())) {
            return false;
        }

        // Tính khoảng cách từ điểm (x, y) đến đoạn thẳng (p1, p2)
        double distance = Math.abs((dy * x - dx * y + p2.getX() * p1.getY() - p2.getY() * p1.getX()) / length);

        // Ngưỡng epsilon: nếu khoảng cách nhỏ hơn 3 pixel thì coi như nằm trên cạnh
        return distance < 3;
    }

    /**
     *
     */
    public void convertIntoMatrix() {
        graph = new Graph();
        graph.setNumberOfVertices(edges.size());
        matrix = new int[edges.size()][edges.size()];
        sb = new StringBuilder();

        sb.append(edges.size()).append("\n");

        for (Map.Entry<Point, Map<Point, Integer>> entry : edges.entrySet()) {
            for (Map.Entry<Point, Integer> edge : entry.getValue().entrySet()) {
                matrix[entry.getKey().getNumber()][edge.getKey().getNumber()] = edge.getValue();
            }
        }

        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix.length; j++) {
                sb.append(matrix[i][j] + " ");
            }
            sb.append("\n");
        }
        graph.setGraph(Arrays.copyOf(matrix, edges.size()));
        confi.getResultTextField().setText(sb.toString().trim());
    }

    /**
     *
     */
    public void convertIntoList() {
        int n = edges.size();
        sb = new StringBuilder();

        sb.append(n).append("\n");

        ArrayList<Map.Entry<Point, Map<Point, Integer>>> sortedEntries = new ArrayList<>(edges.entrySet());
        sortedEntries.sort((a, b) -> Integer.compare(b.getKey().getNumber(), a.getKey().getNumber()));

        for (Map.Entry<Point, Map<Point, Integer>> entry : edges.entrySet()) {
            for (Map.Entry<Point, Integer> edge : entry.getValue().entrySet()) {
                sb.append(entry.getKey().getNumber()).append(" ").append(edge.getKey().getNumber()).append(" ").append(edge.getValue()).append("\n");
            }
        }
        confi.getResultTextField().setText(sb.toString());
    }

    /**
     * draw Vertex
     */
    private void drawNode(Graphics g, int x, int y, int count) {
        g.setColor(Color.WHITE);
        g.fillOval(x - diameter / 2, y - diameter / 2, diameter, diameter);

        g.setColor(Color.BLACK);
        g.drawOval(x - diameter / 2, y - diameter / 2, diameter, diameter);

        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 14));
        String text = String.valueOf(count);
        FontMetrics fm = g.getFontMetrics();
        int textX = x - fm.stringWidth(text) / 2;
        int textY = y + fm.getAscent() / 2;
        g.drawString(text, textX, textY);
    }

    private void drawEdge(Graphics g, Point p1, Point p2, int weight) {
        int nodeRadius = diameter / 2;
        g.setColor(Color.BLACK);

        // Tính vector hướng từ p1 đến p2
        double dx = p2.getX() - p1.getX();
        double dy = p2.getY() - p1.getY();
        double length = Math.sqrt(dx * dx + dy * dy);

        // Tính toán điểm bắt đầu và kết thúc sao cho không đè lên node
        int startX = (int) (p1.getX() + nodeRadius * dx / length);
        int startY = (int) (p1.getY() + nodeRadius * dy / length);
        int endX = (int) (p2.getX() - nodeRadius * dx / length);
        int endY = (int) (p2.getY() - nodeRadius * dy / length);

        // Vẽ cạnh
        g.drawLine(startX, startY, endX, endY);

        // Tính vị trí đặt trọng số, đẩy lên trên đường thẳng một chút
        int midX = (startX + endX) / 2;
        int midY = (startY + endY) / 2 - 5; // Dịch lên 5 pixel tránh bị đè

        g.drawString(String.valueOf(weight), midX, midY);
    }

    private void highlightNode(Graphics g, int x, int y, int count) {
        g.setColor(Color.RED);
        g.fillOval(x - diameter / 2, y - diameter / 2, diameter, diameter);

        g.setColor(Color.YELLOW);
        g.drawOval(x - diameter / 2, y - diameter / 2, diameter, diameter);

        g.setColor(Color.YELLOW);
        g.setFont(new Font("Arial", Font.BOLD, 14));
        String text = String.valueOf(count);
        FontMetrics fm = g.getFontMetrics();
        int textX = x - fm.stringWidth(text) / 2;
        int textY = y + fm.getAscent() / 2;
        g.drawString(text, textX, textY);
    }

    private void highlightEdge(Graphics g, Point p1, Point p2, int weight) {
        int nodeRadius = diameter / 2;
        g.setColor(Color.RED);

        // Tính vector hướng từ p1 đến p2
        double dx = p2.getX() - p1.getX();
        double dy = p2.getY() - p1.getY();
        double length = Math.sqrt(dx * dx + dy * dy);

        // Tính toán điểm bắt đầu và kết thúc sao cho không đè lên node
        int startX = (int) (p1.getX() + nodeRadius * dx / length);
        int startY = (int) (p1.getY() + nodeRadius * dy / length);
        int endX = (int) (p2.getX() - nodeRadius * dx / length);
        int endY = (int) (p2.getY() - nodeRadius * dy / length);

        // Vẽ cạnh
        g.drawLine(startX, startY, endX, endY);

        // Tính vị trí đặt trọng số, đẩy lên trên đường thẳng một chút
        int midX = (startX + endX) / 2;
        int midY = (startY + endY) / 2 - 5; // Dịch lên 5 pixel tránh bị đè

        g.drawString(String.valueOf(weight), midX, midY);
    }

    private void resetFunction() {
        result.setText("");
        isHighLight = false;
    }

    public void clearAll() {
        edges.clear();
        count = 0;
        firstSelectedVertex = null;
        isLine = false;
        resetFunction();
        graph = new Graph();
        confi.getResultTextField().setText("");
        drawPanel.repaint();
    }

    public JLabel getResult() {
        return result;
    }

    public StringBuilder getSb() {
        return sb;
    }

    public Map<Point, Map<Point, Integer>> getEdges() {
        return edges;
    }
}

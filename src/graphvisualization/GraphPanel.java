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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

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
    private boolean isLine = false;

    public GraphPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        edges = new HashMap<>();
        setPreferredSize(new Dimension(MainFrame.WINDOW_SIZE_X / 2 + 230, MainFrame.WINDOW_SIZE_Y / 2 + 280));
        setBackground(null);

        JLabel label = new JLabel("Draw graph yourself");
        label.setFont(new Font("Arial", Font.BOLD, 20));
        add(label, BorderLayout.NORTH);
        add(displayPanel(), BorderLayout.CENTER);
    }

    private JPanel displayPanel() {
        drawPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                for (Point p : edges.keySet()) {
                    drawNode(g, p.getX(), p.getY(), p.getNumber());
                }

                if (isLine) {
                    System.out.println("60");
                    for (Map.Entry<Point, Map<Point, Integer>> entry : edges.entrySet()) {
                        for (Map.Entry<Point, Integer> edge : entry.getValue().entrySet()) {
                            drawEdge(g, entry.getKey(), edge.getKey(), edge.getValue());
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

    private JLabel createResultField() {
        result = new JLabel();
        result.setFont(new Font("", Font.LAYOUT_LEFT_TO_RIGHT, 20));
        return result;
    }

    private void setUpMouseListener() {
        drawPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int x = e.getX();
                int y = e.getY();
                int panelWidth = drawPanel.getWidth();
                int panelHeight = drawPanel.getHeight();

                if (mainFrame.isCtrlPressed()) {
                    if (x >= panelWidth - 30 || y >= panelHeight - 30 || x <= 30 || y <= 30) {
                        result.setText("Too close the border");
                        return;
                    }
                    addPoint(x, y);
                    drawPanel.repaint();
                } else if (mainFrame.isShiftPressed()) {
                    removePoint(x, y);
                } else {
                    selectVertex(x, y);
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

    public void addEdge(Point p1, Point p2) {
        System.out.println("182");
        String input = JOptionPane.showInputDialog("Please enter edge's weight", 1);
        int weight = 1;

        try {
            int parsedWeight = Integer.parseInt(input); 
            if (parsedWeight >= 1) {
                weight = parsedWeight; 
            } else {
                JOptionPane.showMessageDialog(null, "Weight must be at least 1. Using default weight: 1");
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Invalid input! Using default weight: 1");
        }

        edges.get(p1).put(p2, weight);
        edges.get(p2).put(p1, weight);
        isLine = true;
    }

    private void drawEdge(Graphics g, Point p1, Point p2, int weight) {
        int nodeRadius = diameter / 2;
        System.out.println("190");
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
}

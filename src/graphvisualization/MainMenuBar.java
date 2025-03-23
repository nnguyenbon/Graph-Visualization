/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphvisualization;

import java.awt.Dimension;
import java.io.File;
import java.io.FileWriter;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author CE191239 Nguyen Kim Bao Nguyen
 */
public class MainMenuBar extends JMenuBar implements ActionListener {

    private final JMenu fileMenu, graphMenu, algorithmsMenu, helpMenu;
    private final JMenuItem saveItem, openItem, clearItem, exitItem;
    private final JMenuItem dfsItem, bfsItem;
    private final JMenuItem spItem, minSTItem;
    private final JMenuItem docsItem, aboutItem;
    private GraphPanel gP;

    /**
     * constructor
     */
    public MainMenuBar(GraphPanel gP) {
        this.gP = gP;
        UIManager.put("Menu.font", new Font("Arial", Font.BOLD, 16));
        UIManager.put("MenuItem.font", new Font("Arial", Font.PLAIN, 20));

        // File Menu
        fileMenu = new JMenu("File");
        saveItem = createMenuItem("Save", this);
        openItem = createMenuItem("Open", this);
        clearItem = createMenuItem("Clear", this);
        exitItem = createMenuItem("Exit", this);

        fileMenu.add(openItem);
        fileMenu.add(saveItem);
        fileMenu.add(clearItem);
        fileMenu.add(exitItem);

        // Graph Menu
        graphMenu = new JMenu("Graph");
        dfsItem = createMenuItem("DFS", this);
        bfsItem = createMenuItem("BFS", this);

        graphMenu.add(dfsItem);
        graphMenu.add(bfsItem);

        // Algorithms Menu
        algorithmsMenu = new JMenu("Algorithms");
        spItem = createMenuItem("Finds shortest path", this);
        minSTItem = createMenuItem("Finds Minimum spanning tree", this);

        algorithmsMenu.add(spItem);
        algorithmsMenu.add(minSTItem);

        // Help Menu
        helpMenu = new JMenu("Help");
        docsItem = createMenuItem("Docs", this);
        aboutItem = createMenuItem("About", this);

        helpMenu.add(docsItem);
        helpMenu.add(aboutItem);

        // Add Menus to MenuBar
        this.add(fileMenu);
        this.add(graphMenu);
        this.add(algorithmsMenu);
        this.add(helpMenu);
    }

    private JMenuItem createMenuItem(String title, ActionListener listeners) {
        JMenuItem item = new JMenuItem(title);
        item.addActionListener(listeners);
        return item;
    }

    /**
     * Get function
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        JMenuItem source = (JMenuItem) e.getSource();

        if (source == openItem) {
            openFile();
        } else if (source == saveItem) {
            saveFile();
        } else if (source == clearItem) {
            clearData();
        } else if (source == exitItem) {
            System.exit(0);
        } else if (source == dfsItem) {
            gP.dfs();
        } else if (source == bfsItem) {
            gP.bfs();
        } else if (source == spItem) {
            gP.sp();
        } else if (source == minSTItem) {
            gP.minimumst();
        } else if (source == docsItem) {
            docsItem();
        } else if (source == aboutItem) {
            aboutMe();
        }
    }

    /**
     * open file
     */
    private void openFile() {
        File parentDirectory = new File("").getAbsoluteFile();

        JFileChooser fileChooser = new JFileChooser(parentDirectory);
        fileChooser.setFileFilter(new FileNameExtensionFilter("Text Files (*.txt)", "txt"));

    }

    /**
     * save file
     */
    private void saveFile() {
        try {
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy_HH-mm-ss");
            String formattedDate = now.format(formatter);
            StringBuilder sb = new StringBuilder();
            sb.append("graph_").append(formattedDate).append(".txt");

            File file = new File(sb.toString());

            try (FileWriter out = new FileWriter(file)) {
            }
        } catch (IOException e) {
        } catch (Exception e) {
        }
    }

    /**
     * reset all
     */
    private void clearData() {
    }

    /**
     * instruction
     */
    private void docsItem() {
        String userGuide = "User Guide\n\n"
                + "I. Menu Functions\n"
                + "------------------\n"
                + "1. File\n"
                + "   - Open: Open a saved graph data file.\n"
                + "   - Save: Save the current graph to a file.\n"
                + "   - Clear: Delete all graph data.\n"
                + "   - Exit: Exit the program.\n\n"
                + "2. Traversal\n"
                + "   - DFS: Traverse the tree in Depth-First Search.\n"
                + "   - BFS: Traverse the tree using Breadth-First Search.\n\n"
                + "3. Algorithms\n"
                + "   - Finds shortest path: Finds the shortest paths from a source "
                + "vertex to all other vertices in a weighted graph without negative\n"
                + "   - Finds Minimum spanning tree: Find the edges that connect all "
                + "vertices with the minimum total weight\n\n"
                + "4. Help\n"
                + "   - Docs: Display the user guide.\n"
                + "   - About: Show program information.\n\n";

        JTextArea textArea = new javax.swing.JTextArea(userGuide);
        textArea.setFont(new java.awt.Font("Arial", Font.PLAIN, 20));
        textArea.setWrapStyleWord(true);
        textArea.setLineWrap(true);
        textArea.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(600, 400));

        JOptionPane.showMessageDialog(null, scrollPane, "User Guide", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * author
     */
    private void aboutMe() {
        String introduce = "CSD201 - SE1905 - Group 3\n"
                + "Mentor: Le Thi Phuong Dung\n"
                + "Member: \n"
                + "1. Nguyen Kim Bao Nguyen\n"
                + "2. Do Duc Hai\n"
                + "3. Truong Khai Toan\n"
                + "4. Truong Doan Minh Phuc\n"
                + "5. Duong Nguyen Kim Chi\n"
                + "6. Pham Nguyen Khanh";

        JOptionPane.showMessageDialog(null, introduce, "About Us", JOptionPane.INFORMATION_MESSAGE);
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphvisualization;

import java.awt.BorderLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JFrame;

/**
 *
 * @author CE191239 Nguyen Kim Bao Nguyen
 */
public class MainFrame extends JFrame {

    //Window size

    /**
     *
     */
    public static final int WINDOW_SIZE_X = 1200;

    /**
     *
     */
    public static final int WINDOW_SIZE_Y = 800;
    private boolean isCtrlPressed = false;
    private boolean isShiftPressed = false;

    /**
     *
     */
    public MainFrame() {
        setTitle("Graph Visualizer");
        setSize(WINDOW_SIZE_X, WINDOW_SIZE_Y);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        ConfigurationPanel confi = new ConfigurationPanel();
        GraphPanel graphPanel = new GraphPanel(this, confi);
        MainMenuBar menu = new MainMenuBar(graphPanel);
        
        this.setFocusable(true);
        this.requestFocus();

        this.setupKeyListener();
        setJMenuBar(menu);
        add(confi, BorderLayout.WEST);
        add(graphPanel, BorderLayout.EAST);

        setVisible(true);
    }

    private void setupKeyListener() {
        System.out.println("cut");
        this.addKeyListener(new KeyListener() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_CONTROL) {
                    isCtrlPressed = true;
                }
                if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
                    isShiftPressed = true;
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_CONTROL) {
                    isCtrlPressed = false;
                }
                if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
                    isShiftPressed = false;
                }
            }

            @Override
            public void keyTyped(KeyEvent e) {

            }
        });
    }

    /**
     *
     * @return
     */
    public boolean isCtrlPressed() {
        return isCtrlPressed;
    }

    /**
     *
     * @return
     */
    public boolean isShiftPressed() {
        return isShiftPressed;
    }
}

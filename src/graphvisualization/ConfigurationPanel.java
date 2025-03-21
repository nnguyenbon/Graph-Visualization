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
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;
import javax.swing.border.TitledBorder;

/**
 *
 * @author CE191239 Nguyen Kim Bao Nguyen
 */
public class ConfigurationPanel extends JPanel {

    private JPanel graphInformationPanel, instructionPanel;
    private JRadioButton matrixCheck, listCheck;
    private JTextArea resultTextField;
    private boolean isMatrix = true, isList = false;

    /**
     * constructor
     */
    public ConfigurationPanel() {
        System.out.println("confiPanel");
        setPreferredSize(new Dimension(MainFrame.WINDOW_SIZE_X / 2 - 260, MainFrame.WINDOW_SIZE_Y));
        setBackground(null);
        add(createInstructionPanel(), BorderLayout.NORTH);
        add(createGraphInformationPanel(), BorderLayout.SOUTH);
    }

    /**
     * Bottom Left Panel - Matrix or List
     */
    private JPanel createGraphInformationPanel() {
        graphInformationPanel = new JPanel();
        graphInformationPanel.setLayout(new BorderLayout());
        graphInformationPanel.setBackground(null);
        graphInformationPanel.setPreferredSize(new Dimension(MainFrame.WINDOW_SIZE_X / 2 - 270,
                MainFrame.WINDOW_SIZE_Y / 2));
        graphInformationPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.DARK_GRAY),
                "Graph's information", TitledBorder.LEFT, TitledBorder.TOP,
                new Font("Arial", Font.BOLD, 16),
                Color.BLACK));
        matrixCheck = createRadioButton("Matrix");
        matrixCheck.setSelected(true);
        listCheck = createRadioButton("List");
        
//        matrixCheck.addActionListener(e -> {
//            isMatrix = true;
//            isList = false;
//        }); 
//        listCheck.addActionListener(e -> {
//            isMatrix = false;
//            isList = true;
//        });

        ButtonGroup group = new ButtonGroup();
        group.add(matrixCheck);
        group.add(listCheck);

        JPanel radioPanel = new JPanel();
        radioPanel.setBackground(null);
        radioPanel.add(matrixCheck);
        radioPanel.add(listCheck);

        graphInformationPanel.add(radioPanel, BorderLayout.NORTH);

        JPanel text = new JPanel();
        text.setBackground(null);
        text.setPreferredSize(new Dimension(100, 100));
        text.add(createResultTextField());

        graphInformationPanel.add(text, BorderLayout.CENTER);

        return graphInformationPanel;
    }

    /**
     * Create radio button
     */
    private JRadioButton createRadioButton(String name) {
        JRadioButton button = new JRadioButton(name);

        button.setBackground(null);
        button.setFont(new Font("Arial", Font.BOLD, 20));
        return button;
    }

    /**
     * Create text field for matrix/list
     */
    private JTextArea createResultTextField() {
        resultTextField = new JTextArea(20, 20);
        resultTextField.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        resultTextField.setFont(new Font("Arial", Font.LAYOUT_LEFT_TO_RIGHT, 16));
        resultTextField.setEditable(false);
        return resultTextField;
    }

    /**
     * getter
     * @return
     */
    public JTextArea getResultTextField() {
        return resultTextField;
    }

    /**
     * setter
     * @param resultTextField
     */
    public void setResultTextField(JTextArea resultTextField) {
        this.resultTextField = resultTextField;
    }

    /**
     * Create top left panel
     */
    private JPanel createInstructionPanel() {
        instructionPanel = new JPanel(new BorderLayout()); // Sử dụng BorderLayout để căn chỉnh dễ dàng
        instructionPanel.setPreferredSize(new Dimension(MainFrame.WINDOW_SIZE_X / 2 - 270,
                MainFrame.WINDOW_SIZE_Y / 2 - 90));
        instructionPanel.setBackground(null);

        JTextArea instructionText = new JTextArea(
                "###### Draws vertices #####\n"
                + "#1 Hold Ctrl and click on space to add new vertex.\n"
                + "#2 Hold Shift and click on the vertex that you want to remove.\n"
                + "##### Draws edges #####\n"
                + "#1 Select start vertex (click it again to unselect).\n"
                + "#2 Select destination vertex.\n"
                + "#3 Input edge's value.\n"
                + "#4 Click on value of the exist edge to update."
        );
        instructionText.setFont(new Font("Arial", Font.PLAIN, 18));
        instructionText.setBackground(null);
        instructionText.setEditable(false);
        instructionText.setLineWrap(true);
        instructionText.setWrapStyleWord(true);
        instructionText.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        instructionPanel.add(instructionText, BorderLayout.CENTER);
        return instructionPanel;
    }

    /**
     * getter
     * @return
     */
    public boolean isIsMatrix() {
        return isMatrix;
    }

    /**
     * setter
     * @param isMatrix
     */
    public void setIsMatrix(boolean isMatrix) {
        this.isMatrix = isMatrix;
    }

    /**
     *
     * @return
     */
    public boolean isIsList() {
        return isList;
    }

    /**
     *
     * @param isList
     */
    public void setIsList(boolean isList) {
        this.isList = isList;
    }
}

package com.redhat.scripts.metadata.view.gui.jswing;

import com.redhat.scripts.metadata.model.entities.Directory;

import java.awt.*;
import java.util.Objects;
import com.redhat.scripts.metadata.model.entities.Menu;
import javax.swing.*;
import javax.swing.border.Border;

public class MenuGUI
{
    private Directory currentDirectory;
    private Menu menu;
    private Border directoryBorder;

    public MenuGUI(Menu menu)
    {
        Objects.requireNonNull(menu);
        if (null == menu.getInfoActions() || menu.getInfoActions().isEmpty())
            throw new RuntimeException("Menu must have at least one InfoAction set. Programming error!");

        if (null == menu.getMenuOptionList() || menu.getMenuOptionList().isEmpty())
            throw new RuntimeException("Menu must have at least one MenuOption set. Programming error!");

        this.menu = menu;
        init(menu);
    }

    public void display()
    {

    }

    private void init(Menu menu)
    {
        SwingUtilities.invokeLater(() -> {
            // Create the main frame
            JFrame frame = new JFrame("Scripts data");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(600, 400);

            // Create the main panel with GridLayout (2 rows, 2 columns)
            JPanel mainPanel = new JPanel(new GridLayout(2, 2));

            directoryBorder = BorderFactory.createTitledBorder("Directory: /home/user/scripts");

            //Add directory on the title
            mainPanel.setBorder(directoryBorder);

            // Add 4 panels with scrollbars
            for (int i = 0; i < 4; i++) {
                // Placeholder text area to demonstrate scrolling
                JTextArea textArea = new JTextArea("Section " + (i + 1) + "\nAdd content here...\n");
                StringBuilder sb = new StringBuilder(("Line: %d\n".length() + 2) * 20);
                for (int j = 0; j < 20; j++)
                {
                    sb.append(String.format("Line: %d\n", (j+1)));
                }
                textArea.append(sb.toString());
                textArea.setLineWrap(true);
                textArea.setWrapStyleWord(true);

                // Add text area to a scroll pane
                JScrollPane scrollPane = new JScrollPane(textArea);
                scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

                // Add scroll pane to the main panel
                mainPanel.add(scrollPane);
            }

            // Add the main panel to the frame
            frame.add(mainPanel);

            // Make the frame visible
            //frame.setVisible(true);

            //https://github.com/mightychip/spring-boot-swing/blob/master/src/main/java/ca/purpleowl/examples/swing/SpringBootSwingCommandLineRunner.java
            //This boots up the GUI.
            EventQueue.invokeLater(() -> frame.setVisible(true));
        });
    }

}
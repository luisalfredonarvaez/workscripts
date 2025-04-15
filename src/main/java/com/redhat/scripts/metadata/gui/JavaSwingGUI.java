package com.redhat.scripts.metadata.gui;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class JavaSwingGUI {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Create the main frame
            JFrame frame = new JFrame("2x2 Panel with Scrollbars");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(600, 400);

            // Create the main panel with GridLayout (2 rows, 2 columns)
            JPanel mainPanel = new JPanel(new GridLayout(2, 2));

            //Add directory on the title
            mainPanel.setBorder(BorderFactory.createTitledBorder("Directory: /home/user/scripts"));

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
            frame.setVisible(true);
        });
    }

}
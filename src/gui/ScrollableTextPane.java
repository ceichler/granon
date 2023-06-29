package gui;

import javax.swing.*;
import java.awt.*;

/**
 * a scrollable text pane to display the large text
 * @author khai
 *
 */
public class ScrollableTextPane extends JFrame {

    public ScrollableTextPane(String text) {
        setTitle("Scrollable Text Pane Example");
//        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create a JTextArea to display the text
        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);

        // Add some example text
        StringBuilder sb = new StringBuilder();
//        for (int i = 1; i <= 1000; i++) {
//            sb.append("Line ").append(i).append("\n");
//        }
        sb.append(text);
        textArea.setText(sb.toString());

        // Create a JScrollPane and add the JTextArea to it
        JScrollPane scrollPane = new JScrollPane(textArea);

        // Set the preferred size of the scroll pane
        scrollPane.setPreferredSize(new Dimension(600, 350));

        // Add the scroll pane to the frame
        add(scrollPane);

        pack();
        setLocationRelativeTo(null);
    }

//    public static void main(String[] args) {
//        
//    }
}
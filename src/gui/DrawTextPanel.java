package gui;

import javax.swing.JPanel;
import java.awt.*;

public class DrawTextPanel extends JPanel {
	private String textToDraw = "";
    private Color textColor = Color.BLACK;
    private Graphics2D g2d;
    private int x = 10;
	private int y = 20;
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        // Cast Graphics to Graphics2D
        g2d = (Graphics2D) g;
        
        // Set font and color for the text
        Font font = new Font("Arial", Font.BOLD, 16);
        g2d.setFont(font);
        g2d.setColor(textColor);
        
        
        // Draw the text
        g2d.drawString(textToDraw, x, y + font.getSize());
    }
    
    public void setTextToDraw(String text) {
        // Update the text to be drawn
        textToDraw = text;
        
        // Repaint the panel to show the new text
        repaint();
    }
    
    public void setTextColor(Color color) {
        // Update the text color
        textColor = color;
        
        // Repaint the panel to show the new color
        repaint();
    }
    
    public void clearText() {
    	// Update the text to ""
    	textToDraw = "";
    	
    	// Set the text color to default
        textColor = Color.BLACK;
    	
    	// Repaint the panel to clear all text
    	repaint();
    }

}



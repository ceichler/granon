package gui;

import java.awt.Color;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.Collection;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.JTextField;

public class ComboBoxUtils {

	// list all apenl that is created for getting arguments
	public static ArrayList<JPanel> listPanel = new ArrayList<JPanel>();
	
	public static void generateDynamicPanel(JPanel panel) {
        // Clear the panel
        panel.removeAll();
        // Generate and add the specified number of JComboBoxes to the panel
        for (JPanel panl : listPanel) {
            panel.add(panl);
        }

        // Repaint the panel to reflect the changes
        panel.revalidate();
        panel.repaint();
    }
	    
    public static void createListPanel(ArrayList<String> requiredForm) {
    	if (listPanel != null) {
    		listPanel.removeAll(listPanel);
    	}

        // Generate and add the specified number of JComboBoxes to the panel
        for (int i = 0; i < requiredForm.size(); i++) {
        	JPanel npanel;
        	if (requiredForm.get(i).equals("S") || requiredForm.get(i).equals("Set")) {
        		npanel = new JPanel(new GridLayout(3, 0, 0, 0));
        	}else {
        		npanel = new JPanel(new GridLayout(1, 0, 0, 0));
        	}
        	Border blackline = BorderFactory.createLineBorder(Color.ORANGE);
        	npanel.setBorder(blackline);
            listPanel.add(npanel);
        }
    }
    
    public static void generateDynamicComboBoxes(JPanel panel, ArrayList<JComboBox> listComboBox) {
        // Clear the panel
        panel.removeAll();
        // Generate and add the specified number of JComboBoxes to the panel
        for (JComboBox<String> cbox : listComboBox) {
        	if (cbox == null) {
        		continue;
        	}
            panel.add(cbox);
        }

        // Repaint the panel to reflect the changes
        panel.revalidate();
        panel.repaint();
    }
    
    public static void generateDynamicTextField(JPanel panel, JTextField textField) {
        // Clear the panel
        panel.removeAll();        
        panel.add(textField);

        // Repaint the panel to reflect the changes
        panel.revalidate();
        panel.repaint();
    }
    
//    public static void addItemToBox(String[] items, int indexBox, ArrayList<JComboBox<String>> listComboBox) {
//    	for (String item: items) {
//    		listComboBox.get(indexBox).addItem(item);
//    	}
//    }
    
    public static ArrayList<JComboBox> createListBox(int numberOfComboBoxes, ArrayList<String> listAtt, ArrayList<String> listProp, boolean isStr) {
    	ArrayList<JComboBox> listComboBox = new ArrayList<JComboBox>();
    	// sort the arraylist in alphabetical order
//    	listAtt.sort(String::compareToIgnoreCase);
//    	listProp.sort(String::compareToIgnoreCase);
    	// required form == Str
    	if (numberOfComboBoxes == 1 && !isStr) {
    		JComboBox comboBox = new AutoCompleteJComboBox(listProp.toArray());
//    		AutoCompleteDecorator.decorate(comboBox);
            listComboBox.add(comboBox);
    	}
    	// required form == str
    	else if (numberOfComboBoxes == 1 && isStr) {
    		// there are no combobox, must create a textfield here
    		listComboBox.add(null);
    	}
    	// for required form == S
    	else if (numberOfComboBoxes == 3) {
    	
	        // Generate and add the specified number of JComboBoxes to the panel
	        for (int i = 0; i < numberOfComboBoxes; i++) {
	        	if (i==1) {
	        		JComboBox comboBox = new AutoCompleteJComboBox(listProp.toArray());
	            	listComboBox.add(comboBox);
	        	}else if (i==0){
	        		// S or X cannot be null
	        		ArrayList<String> listAtt_noNull = new ArrayList<>();
	        		listAtt_noNull.addAll(listAtt);
	        		listAtt_noNull.remove("null");
	            	JComboBox comboBox = new AutoCompleteJComboBox(listAtt_noNull.toArray());
	            	listComboBox.add(comboBox);
	        	}else {
	        		JComboBox comboBox = new AutoCompleteJComboBox(listAtt.toArray());
	            	listComboBox.add(comboBox);
	        	}
	        }
	        
    	}
    	// for required form = Set
    	else if (numberOfComboBoxes == 2) {
    		listComboBox.add(new AutoCompleteJComboBox(new String[]{"*"}));
    		// Generate and add the specified number of JComboBoxes to the panel
	        for (int i = 0; i < numberOfComboBoxes; i++) {
	        	if (i==0) {
	        		JComboBox comboBox = new AutoCompleteJComboBox(listProp.toArray());
	            	listComboBox.add(comboBox);
	        	}else {
	            	JComboBox comboBox = new AutoCompleteJComboBox(listAtt.toArray());
	            	listComboBox.add(comboBox);
	        	}
	        }
    	}
        return listComboBox;
    }
//    public static ArrayList<JTextField> createTextField(int numberOfComboBoxes, boolean isStr) {
//    	ArrayList<JTextField> listField = new ArrayList<JTextField>();
//    	if (numberOfComboBoxes == 1 && isStr) {
//    		JTextField tfed = new JTextField();
//    		listField.add(tfed);
//    		return listField;
//    	}
//        return null;
//    }
}

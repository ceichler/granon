package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics2D;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import agg.xt_basis.Arc;
import agg.xt_basis.Graph;
import agg.xt_basis.Node;
import executable.granonui.Tui;
import graphVisu.MyGraph;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;

import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.awt.event.ActionEvent;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import java.awt.GridLayout;
import utils.GraGraUtils;
import utils.Grammar;
import parser.Parser;

public class MainJframe extends JFrame {

	private JPanel contentPane;
	private JTextField textField;
	private JButton clearButton;
	private JComboBox comboBox ;
	private GuiParse guiParse = new GuiParse();
	
	String testStr = graphToString(Tui.grammar.getHostGraph());
	ArrayList<String> listOperators = Parser.listOperators;
	String listOp[] = listOperators.toArray(new String[listOperators.size()]);
	
	public HashMap<String,ArrayList<JComboBox>> mapComboBox = new HashMap<String,ArrayList<JComboBox>>();
	public HashMap<String,JTextField> mapTextField = new HashMap<String,JTextField>();

	/**
	 * Create the frame.
	 */
	public MainJframe() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1421, 739);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBounds(12, -15, 1400, 705);
		contentPane.add(panel);
		panel.setLayout(null);
		
		JLabel lblCommand = new JLabel("Command");
		lblCommand.setBounds(12, 69, 113, 25);
		panel.add(lblCommand);
		
		textField = new JTextField();
		textField.setBounds(110, 64, 1100, 36);
		panel.add(textField);
		textField.setColumns(10);
		
		JLabel lblNewLabel = new JLabel("GUI");
		lblNewLabel.setFont(new Font("Arial",Font.PLAIN | Font.BOLD, 25));
		lblNewLabel.setForeground(Color.BLUE);
		lblNewLabel.setBounds(677, 12, 128, 53);
		panel.add(lblNewLabel);
		
		JLabel lblOperator = new JLabel("Operator");
		lblOperator.setBounds(12, 130, 70, 15);
		panel.add(lblOperator);
		
		DrawTextPanel panel_1 = new DrawTextPanel();
		panel_1.setBounds(407, 109, 811, 41);
		panel.add(panel_1);
		
		JPanel panel_2 = new JPanel();
		panel_2.setBounds(114, 167, 1100, 105);
		panel.add(panel_2);
		panel_2.setLayout(new GridLayout(1, 0, 0, 0));
		
		DrawTextPanel panel_3 = new DrawTextPanel();
		panel_3.setBounds(114, 376, 1404, 291); // 114, 376, 704, 299 || 114, 484, 1404, 191
		panel.add(panel_3);
		panel_3.setLayout(null);
		
		JPanel panel_4 = new JPanel();
		panel_4.setBounds(1230, 46, 155, 239);
		panel.add(panel_4);
		panel_4.setLayout(new GridLayout(0, 1, 0, 0));
		
		comboBox = new JComboBox(listOp);
//		comboBox.setSelectedItem("Choose");
		comboBox.setBounds(110, 119, 185, 36);
		panel.add(comboBox);
		
		// ChangeListener for the initial JComboBox to generate ComboBoxes
        comboBox.addActionListener(new ActionListener() { 
			@Override
			public void actionPerformed(ActionEvent e) {
                // Clear the dynamic panel
                panel_3.removeAll();
                textField.setText("");
                guiParse.setOperator((String)comboBox.getSelectedItem());
                
                drawTextToPanel(panel_1, guiParse.getSyntax() , Color.MAGENTA);
                
                ArrayList<String> args = guiParse.getArgs();
                ArrayList<String> requiredForm = guiParse.getRequiredForm();
                
                ComboBoxUtils.createListPanel(requiredForm);
                ComboBoxUtils.generateDynamicPanel(panel_2);
                
                
                
                for (int i=0;i<ComboBoxUtils.listPanel.size();i++) {
                	if (requiredForm.get(i).equals("S") ) {
                		ArrayList<JComboBox> listComboBox = ComboBoxUtils.createListBox(3, getAtt(Tui.grammar.getHostGraph()),getProp(Tui.grammar.getHostGraph()),false);
                		mapComboBox.put(args.get(i), listComboBox);
                		ComboBoxUtils.addListComboBoxesToPanel(ComboBoxUtils.listPanel.get(i), listComboBox);
                		listComboBox.get(0).setSelectedItem("*");
                		listComboBox.get(1).setSelectedItem("null");
                		listComboBox.get(2).setSelectedItem("null");
                	}
                	else if (requiredForm.get(i).equals("Set")) {
                		ArrayList<JComboBox> listComboBox = ComboBoxUtils.createListBox(2, getAtt(Tui.grammar.getHostGraph()),getProp(Tui.grammar.getHostGraph()),false);
                		mapComboBox.put(args.get(i), listComboBox);
                		ComboBoxUtils.addListComboBoxesToPanel(ComboBoxUtils.listPanel.get(i), listComboBox);
                		listComboBox.get(1).setSelectedItem("null");
                		listComboBox.get(2).setSelectedItem("null");
                	}else if (!requiredForm.get(i).equals("Str")){
                		ArrayList<JComboBox> listComboBox = ComboBoxUtils.createListBox(1, getAtt(Tui.grammar.getHostGraph()),getProp(Tui.grammar.getHostGraph()),false);
                		mapComboBox.put(args.get(i), listComboBox);
                		ComboBoxUtils.addListComboBoxesToPanel(ComboBoxUtils.listPanel.get(i), listComboBox);
                		
                	}else {
                		JTextField txf = new JTextField();
                		mapTextField.put(args.get(i), txf);
                		ComboBoxUtils.addTextFieldToPanel(ComboBoxUtils.listPanel.get(i), txf);
                	}
                }
             }
        });
        
        JButton btnHelp = new JButton("?");
		btnHelp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String selectedItem = (String)comboBox.getSelectedItem();
				displayGuide(selectedItem);
			}
		});
		btnHelp.setBounds(310, 118, 30, 37);
		panel.add(btnHelp);
        
        // Execute button (Execute command on JTextPanel)
        JButton btnExecute = new JButton("Execute");
		btnExecute.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String textCommand = textField.getText();
				if (!textCommand.equals("")) {
					writeCommand(panel_1, "");
					panel_2.removeAll();
					panel_2.repaint();
					execute(panel_3, textCommand);
					
				}else {
					try {
						dropdownExec();
						panel_2.removeAll();
						panel_2.repaint();
						
					} catch (Exception e) {
						e.printStackTrace();
						drawTextToPanel(panel_3, e.getMessage(), Color.RED);
					}
				}
				String msg = Parser.command + " successfully executed";
				drawTextToPanel(panel_3, msg , Color.BLUE);
			}
		});
		panel_4.add(btnExecute);
		
		// Display the current graph button
		JButton btnDrawGraph = new JButton("Display");
		btnDrawGraph.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				drawGraph();
			}
		});
		panel_4.add(btnDrawGraph);
				
		// Create the "Load Graph" button
        JButton loadGraphButton = new JButton("Load Graph");
        panel_4.add(loadGraphButton);

        // Add action listener to the "Load Graph" button
        loadGraphButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	loadSaveGraphFromFile(1, panel_3); 
            }
        });
        
        // Create the "Save Graph" button
        JButton saveGraphButton = new JButton("Save Graph");
        panel_4.add(saveGraphButton);

        // Add action listener to the "Load Graph" button
        saveGraphButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	loadSaveGraphFromFile(0, panel_3); 
            }
        });
		
		
		clearButton = new JButton("Clear");
		clearButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				panel_1.clearText();
				panel_2.removeAll();
				panel_2.repaint();
				panel_3.clearText();
				
			}
		});
		panel_4.add(clearButton);
		
		// Quit GUI button
		JButton btnQuit = new JButton("Quit");
		btnQuit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);
			}
		});
//		btnQuit.setBounds(651, 168, 113, 36);
		panel_4.add(btnQuit);
	
		
	}
	
	
	/**
	 * Execute the command entered in JTextField
	 * @param panel_3 status panel
	 * @param command command string
	 */
	public void execute(DrawTextPanel panel_3, String command) {
		
		try {
			Parser.command = command;
			Parser.executeGui();
		}catch (Exception e){
			drawTextToPanel(panel_3, e.getMessage(), Color.RED);
		}
	}
	
	/**
	 *  Display the current graph on a new pop up windows
	 */
//	public void drawGraph() {
//		String text = graphToString(Tui.grammar.getHostGraph());
//		SwingUtilities.invokeLater(() -> {
//            new ScrollableTextPane(text).setVisible(true);
//        });
//	}
	public void drawGraph() {
		HashSet<String> nodes = new HashSet<String>();
		HashSet<ArrayList<String>> edges = new HashSet<ArrayList<String>>();
				
		for (agg.xt_basis.Node node:Tui.grammar.getHostGraph().getNodesSet()) {
			String att = node.getAttribute().getValueAsString(1).replace("\"", "");
			nodes.add(att);
		}
		
		int count_edge = 0;
		for (agg.xt_basis.Arc edge:Tui.grammar.getHostGraph().getArcsSet()) {
			ArrayList<String> edgeStr = new ArrayList<String>();
			
			String prop = edge.getAttribute().getValueAsString(1).replace("\"", "")+"$_"+count_edge;
			count_edge++;
			// Arc and Nodes share a common superclass GraphObject
			agg.xt_basis.Node src = (agg.xt_basis.Node) edge.getSource();
			agg.xt_basis.Node dst = (agg.xt_basis.Node) edge.getTarget();

			// Get node values
			String srcName = src.getAttribute().getValueAsString(1).replace("\"", "");
			String dstName = dst.getAttribute().getValueAsString(1).replace("\"", "");
			
			edgeStr.add(prop);
			edgeStr.add(srcName);
			edgeStr.add(dstName);
			edges.add(edgeStr);
		}
		
		MyGraph mygraph = new MyGraph(nodes,edges);
		mygraph.getGraph().display();
	}
	
	/**
	 *  Display the message on provided panel
	 * @param panel panel that will display the message
	 * @param text the message string
	 * @param color color of message
	 */
	public void drawTextToPanel(DrawTextPanel panel, String text, Color color) {
		panel.setTextToDraw(text);
		panel.setTextColor(color);
	}
	
	
	/**
	 *  write down the command's syntax
	 * @param panel_1 the panel to display the command's syntax
	 * @param text the command's syntax
	 */
	public void writeCommand(DrawTextPanel panel_1, String text) {
		drawTextToPanel(panel_1, text, Color.MAGENTA);
	}
	

	private ArrayList<String> getAtt(Graph graph){
		ArrayList<String> listAtt = new ArrayList<String> ();
		for (Node n : graph.getNodesSet()) {
			// Get node name
			String nodeName = n.getAttribute().getValueAsString(1);
			nodeName = nodeName.replace("\"", "");
			if (listAtt.contains(nodeName)) {
				continue;
			}
			listAtt.add(nodeName);
		}
		// sort the arraylist in alphabetical order
    	listAtt.sort(String::compareToIgnoreCase);
    	listAtt.add(0,"*");
		listAtt.add(0,"null");
//    	System.out.println(listAtt);
		return listAtt;
	}
	
	// get all prop of edges in current graph
	private ArrayList<String> getProp(Graph graph){
		ArrayList<String> listProp = new ArrayList<String> ();
		for (Arc a : graph.getArcsSet()) {

			// Get arc name
			String arcName = a.getAttribute().getValueAsString(1);
			arcName = arcName.replace("\"", "");
			if (listProp.contains(arcName)) {
				continue;
			}
			listProp.add(arcName);
		}
		listProp.sort(String::compareToIgnoreCase);
    	listProp.add(0,"*");
		listProp.add(0,"null");
		return listProp;
	}
	
	/**
	 *  execute the command entered by DropDown JComboBox
	 * @throws Exception
	 */
	public void dropdownExec() throws Exception {
		String command = generateCommand();
		Parser.command = command;
		System.out.println("\u001B[32m"+Parser.command+"\u001B[0m");
		Parser.executeGui();
	}
	
	
	
	/**
	 *  Construct the command string from selected value in JComboBox
	 * @return String command string for parser 
	 */
	public String generateCommand() {
		ArrayList<String> args = guiParse.getArgs();
        ArrayList<String> requiredForm = guiParse.getRequiredForm();
		HashMap<String,ArrayList<String>> mapArgs = getHashMapInfo();

		
		// generate command
		
		// get operator
		String command = new String((String) comboBox.getSelectedItem());
		
		// Anatomization ( idn={"name"}, qID={"knows" }, sens={"livesIn"} )
		if (command.equals("Anatomization")) {
			command += "(";
			for (String key:mapArgs.keySet()) {
				command = command + key + "=";
				command += "{";
				for (String value:mapArgs.get(key).get(0).split(",")) {
					value = value.replace(" ", "");
					command = command + value + ",";
				}
				command = command.substring(0, command.length()-1);
				command += "},";
			}
			command = command.substring(0, command.length()-1);
			command += ")";
			return command;
		}else if (command.equals("JoinSet")) {
			command = command + "(" + mapArgs.get("x").get(0) + "," + mapArgs.get("X_att").get(0) + ") ";
			for (String key:mapArgs.keySet()) {
				if (key.equals("x") || key.equals("X_att")) {
					continue;
				}
				command = command + key + "{";
				String[] parts = mapArgs.get(key).get(0).split("(?<=\\))\\s*,\\s*");
				for (String part: parts) {
					command = command + part + ",";
				}
				command = command.substring(0, command.length()-1);
				command += "} ";
				
			}
			return command;
		}
		
		
		command += "(";
		for (String key:mapArgs.keySet()) {
			command = command + key + "=";
			if (mapArgs.get(key).size()==1) {
				command = command + "\"" + mapArgs.get(key).get(0).replace("[", "\"") + "\"" + ",";
			}else {
				command += "(";
				for (String valA:mapArgs.get(key)) {
					command = command + valA + ","; 
					
				}
				command = command.substring(0, command.length()-1);
				command += "),";
				
			}
		}
		// if the last character in command is "(" ===> this user'command don't contain any argument 
		if (command.substring(command.length()-1, command.length()).equals("(")) {
			
			command = command + ")";
		}
		else {
			command = command.substring(0, command.length()-1) + ")";
		}
		return command;
	}
	
	/**
	 *  Extract data from JCOmboBox
	 *  
	 * @return HashMap extracted data
	 */
	public HashMap<String, ArrayList<String>> getHashMapInfo(){
		HashMap<String,ArrayList<String>> mapArgs = new HashMap<String,ArrayList<String>>();
		for (String key:mapComboBox.keySet()) {
			ArrayList<String> val = new ArrayList<String>();
			for (int i=0;i<mapComboBox.get(key).size();i++) {
				String value = (String) mapComboBox.get(key).get(i).getSelectedItem();
				val.add(value);
			}
			mapArgs.put(key, val);
		}
		for (String key:mapTextField.keySet()) {
			ArrayList<String> val = new ArrayList<String>();
			val.add(mapTextField.get(key).getText());
			mapArgs.put(key, val);
		}
//		System.out.println("\u001B[32m"+mapArgs+"\u001B[0m");
		return mapArgs;
	}
	
	
	// get the current graph in String form
	private String graphToString(Graph graph) {
		StringBuilder display = new StringBuilder();

		// Display graph infos
		display.append(String.format(
				"[Graph]%nname: %s%nnumber of nodes: %d%nnumber of edges: %d%n",
				graph.getName(),
				graph.getSizeOfNodes(),
				graph.getSizeOfArcs()
		));

		// Display nodes infos
		display.append("\n[Nodes]\n");
		for (Node n : graph.getNodesSet()) {
			// Get node type and name
			String nodeType = n.getType().getStringRepr();
			String nodeName = n.getAttribute().getValueAsString(1);

			// Display node
			display.append(String.format("(%-7s) %s%n", nodeType, nodeName));
		}

		// Display edges infos
		display.append("\n[Edges]\n");
		for (Arc a : graph.getArcsSet()) {
			// Arc and Nodes share a common superclass GraphObject
			Node src = (Node) a.getSource();
			Node dst = (Node) a.getTarget();

			// Get node values
			String srcName = src.getAttribute().getValueAsString(1);
			String dstName = dst.getAttribute().getValueAsString(1);

			// Get arc name
			String arcName = a.getAttribute().getValueAsString(1);

			// Display arc
			display.append(String.format("%10s -- %-8s --> %s%n", srcName, arcName, dstName));
		}

		return display.toString();
	}
	
	/**
	 * Select file for this class
	 */
	private String selectFile() {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setCurrentDirectory(new File("."));
		int result = fileChooser.showOpenDialog(this);
		File selectedFile = null;
		if (result == JFileChooser.APPROVE_OPTION) {
		    selectedFile = fileChooser.getSelectedFile();
		    System.out.println("Selected file: " + selectedFile.getAbsolutePath());
		}
		return selectedFile.getAbsolutePath();
	}
	
	/**
	 * Get the path absolute chosen by user 
	 * @return String path absolute of file
	 */
	public String saveToFile() {
        // Create a file chooser dialog
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File("."));
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        int result = fileChooser.showSaveDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            String directoryPath = fileChooser.getSelectedFile().getAbsolutePath();

            // Get the file name from user input
            String fileName = JOptionPane.showInputDialog(this, "Enter the file name:");

           System.out.println("Saved file "+directoryPath+"  "+fileName);
           return directoryPath+"/"+fileName;
        }
        return null;
    }
	
	// pop up window to get file name
	// choice == 1 ===> load
	// choice == 0 ===> save
	/**
	 * Load or save graph from an absolute path of a file
	 * @param choice if choice=1 this function will load the graph from a file, if choice=0 this function will save the graph to a file  
	 * @param panel panel for displaying the message
	 */
	public void loadSaveGraphFromFile(int choice, DrawTextPanel panel) {
			if (choice != 0 && choice != 1) {
				return;
			}
			
			
		    if (choice==1) {
				String filePath = selectFile();
				panel.setTextColor(Color.BLUE);
				panel.setTextToDraw("Loaded file: " + filePath);
//				System.out.println("Loaded file: " + filePath);
				Tui.grammar = new Grammar(filePath);
		    } else if (choice==0) {
			     String filePath = saveToFile();
//			     System.out.println("Saved to file: " + filePath+"ggx");
			     panel.setTextColor(Color.BLUE);
			     panel.setTextToDraw("Saved to file: " + filePath+".ggx");
			     GraGraUtils.save(filePath, Grammar.graphGrammar);
		  }
		
	}
	
	/**
	 * Display the description of an operator or procedure
	 * @param operator_linePrefix operator name is always at the beginning of the paragraph
	 */
	public void displayGuide(String operator_linePrefix) {
		 String filePath = "src/gui/guide.txt";
	        String delimiter = "#########";
//	        String linePrefix = "NewNode";

	        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
	            String line;
	            boolean isContentSection = false;

	            while ((line = reader.readLine()) != null) {
	                if (line.contains(delimiter)) {
	                    isContentSection = true;
	                    continue;
	                }

	                if (isContentSection && line.startsWith(operator_linePrefix)) {
	                	String[] text = new String[] {""};
	                	while (!(line = reader.readLine()).contains(delimiter)) {
	                		text[0] = text[0] + line + "\n";
	                	}
	                	SwingUtilities.invokeLater(() -> {
                            new ScrollableTextPane(text[0]).setVisible(true);
                        });
	                }
	            }
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	}
	
}

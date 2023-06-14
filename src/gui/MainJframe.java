package gui;

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

import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.awt.event.ActionEvent;
import javax.swing.JComboBox;
import java.awt.GridLayout;
import java.util.Scanner;
import utils.GraGraUtils;
import utils.Grammar;
import parser.Parser;

public class MainJframe extends JFrame {

	private JPanel contentPane;
	private JTextField textField;
	private JButton clearButton;
	private JComboBox comboBox ;
	private GuiParse guiParse = new GuiParse();
	
	String testStr = printGraph(Tui.grammar.getHostGraph());
	ArrayList<String> listOperators = Parser.listOperators;
	String listOp[] = listOperators.toArray(new String[listOperators.size()]);
	
	public HashMap<String,ArrayList<JComboBox>> mapComboBox = new HashMap<String,ArrayList<JComboBox>>();
	public HashMap<String,JTextField> mapTextField = new HashMap<String,JTextField>();

	/**
	 * Create the frame.
	 */
	public MainJframe() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		setBounds(100, 100, 800, 550);
		setBounds(100, 100, 1021, 739);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JPanel panel = new JPanel();
//		panel.setBounds(12, -15, 776, 516);
		panel.setBounds(12, -15, 997, 705);
		contentPane.add(panel);
		panel.setLayout(null);
		
		JLabel lblCommand = new JLabel("Command");
		lblCommand.setBounds(12, 69, 113, 25);
		panel.add(lblCommand);
		
		textField = new JTextField();
		textField.setBounds(110, 64, 708, 36);
		panel.add(textField);
		textField.setColumns(10);
		
		JLabel lblNewLabel = new JLabel("GUI");
		lblNewLabel.setFont(new Font("Arial",Font.PLAIN | Font.BOLD, 25));
		lblNewLabel.setForeground(Color.BLUE);
		lblNewLabel.setBounds(477, 12, 128, 53);
		panel.add(lblNewLabel);
		
		JLabel lblOperator = new JLabel("Operator");
		lblOperator.setBounds(12, 130, 70, 15);
		panel.add(lblOperator);
		
		DrawTextPanel panel_1 = new DrawTextPanel();
		panel_1.setBounds(307, 109, 511, 41);
		panel.add(panel_1);
		
		JPanel panel_2 = new JPanel();
		panel_2.setBounds(120, 167, 698, 120);
		panel.add(panel_2);
		panel_2.setLayout(new GridLayout(0, 2, 0, 0));
		
		DrawTextPanel panel_3 = new DrawTextPanel();
		panel_3.setBounds(114, 299, 704, 376);
		panel.add(panel_3);
		panel_3.setLayout(null);
		
		JPanel panel_4 = new JPanel();
		panel_4.setBounds(830, 46, 155, 239);
		panel.add(panel_4);
		panel_4.setLayout(new GridLayout(0, 1, 0, 0));
		
		comboBox = new JComboBox(listOp);
		comboBox.setSelectedItem("Choose");
		comboBox.setBounds(110, 119, 185, 36);
		panel.add(comboBox);
		
		// ChangeListener for the initial JComboBox to generate ComboBoxes
        comboBox.addActionListener(new ActionListener() { 
			@Override
			public void actionPerformed(ActionEvent e) {
                // Clear the dynamic panel
                panel_3.removeAll();
                
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
                		ComboBoxUtils.generateDynamicComboBoxes(ComboBoxUtils.listPanel.get(i), listComboBox);
                	}
                	else if (requiredForm.get(i).equals("Set")) {
                		ArrayList<JComboBox> listComboBox = ComboBoxUtils.createListBox(2, getAtt(Tui.grammar.getHostGraph()),getProp(Tui.grammar.getHostGraph()),false);
                		mapComboBox.put(args.get(i), listComboBox);
                		ComboBoxUtils.generateDynamicComboBoxes(ComboBoxUtils.listPanel.get(i), listComboBox);
                	}else if (!requiredForm.get(i).equals("Str")){
                		ArrayList<JComboBox> listComboBox = ComboBoxUtils.createListBox(1, getAtt(Tui.grammar.getHostGraph()),getProp(Tui.grammar.getHostGraph()),false);
                		mapComboBox.put(args.get(i), listComboBox);
                		ComboBoxUtils.generateDynamicComboBoxes(ComboBoxUtils.listPanel.get(i), listComboBox);
                	}else {
                		JTextField txf = new JTextField();
                		mapTextField.put(args.get(i), txf);
                		ComboBoxUtils.generateDynamicTextField(ComboBoxUtils.listPanel.get(i), txf);
                	}
                }
             }
        });
        
        JButton btnExecute = new JButton("Execute");
		btnExecute.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				execute(panel_3, textField.getText());
			}
		});
		// btnExecute.setBounds(651, 63, 113, 36);
		panel_4.add(btnExecute);
		
		JButton btnDropdownexec = new JButton("DropDownExec");
		btnDropdownexec.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				dropdownExec();
			}
		});
		panel_4.add(btnDropdownexec);
		
		
		JButton btnDrawGraph = new JButton("Display");
		btnDrawGraph.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				drawGraph();
			}
		});
		// btnDrawText.setBounds(651, 109, 113, 36);
		panel_4.add(btnDrawGraph);
		
		clearButton = new JButton("Clear");
		clearButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				panel_1.clearText();
				panel_2.removeAll();
				panel_2.repaint();
				panel_3.clearText();
				
			}
		});
		// clearButton.setBounds(651, 229, 113, 25);
		panel_4.add(clearButton);
		
		JButton btnQuit = new JButton("Quit");
		btnQuit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);
			}
		});
		btnQuit.setBounds(651, 168, 113, 36);
		panel_4.add(btnQuit);
	
		
	}
	
	public void execute(DrawTextPanel panel_3, String text) {
		
		try {
			Parser.command = text;
			Parser.executeGui();
		}catch (Exception e){
			drawTextToPanel(panel_3, e.getMessage(), Color.RED);
		}
	}
	
	public void drawGraph() {
		String text = printGraph(Tui.grammar.getHostGraph());
		SwingUtilities.invokeLater(() -> {
            new ScrollableTextPane(text).setVisible(true);
        });
	}
	
	public void drawTextToPanel(DrawTextPanel panel, String text, Color color) {
		panel.setTextToDraw(text);
		panel.setTextColor(color);
	}
	
	public void writeCommand(DrawTextPanel panel_1, String text) {
		drawTextToPanel(panel_1, text, Color.MAGENTA);
	}
	
	public ArrayList<String> getAtt(Graph graph){
		ArrayList<String> listAtt = new ArrayList<String> ();
		listAtt.add("default");
		listAtt.add("*");
		listAtt.add("null");
		for (Node n : graph.getNodesSet()) {
			// Get node name
			String nodeName = n.getAttribute().getValueAsString(1);
			nodeName = nodeName.replace("\"", "");
			if (listAtt.contains(nodeName)) {
				continue;
			}
			listAtt.add(nodeName);
		}
		return listAtt;
	}
	
	public ArrayList<String> getProp(Graph graph){
		ArrayList<String> listProp = new ArrayList<String> ();
		listProp.add("default");
		listProp.add("*");
		listProp.add("null");
		for (Arc a : graph.getArcsSet()) {

			// Get arc name
			String arcName = a.getAttribute().getValueAsString(1);
			arcName = arcName.replace("\"", "");
			if (listProp.contains(arcName)) {
				continue;
			}
			listProp.add(arcName);
		}
		return listProp;
	}
	
	public void dropdownExec() {
		String command = generateCommand();
		System.out.println(command);
		Parser.command = command;
		Parser.execute();
	}
	
	public String generateCommand() {
		ArrayList<String> args = guiParse.getArgs();
        ArrayList<String> requiredForm = guiParse.getRequiredForm();
		HashMap<String,ArrayList<String>> mapArgs = getHashMapInfo();
		
		for (String key:mapArgs.keySet()) {
			if (mapArgs.get(key).size() == 3) {
				int index = args.indexOf(key);
				if (requiredForm.get(index).equals("S") || requiredForm.get(index).equals("Set")) {
					mapArgs.replace(key, new ArrayList<String>(Arrays.asList("*",null,null)));
				}
			}
			else if (mapArgs.get(key).get(0).equals("default")) {
				int index = args.indexOf(key);
				if (requiredForm.get(index).equals("*")) {
					mapArgs.replace(key, new ArrayList<String>(Arrays.asList("*")));
				}else if (requiredForm.get(index).equals("null")) {
					mapArgs.replace(key, new ArrayList<String>(Arrays.asList(null)));
				}
			}
		}
		System.out.println(mapArgs);
		System.out.println(comboBox.getSelectedItem());
		String command = new String((String) comboBox.getSelectedItem());
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
		command = command.substring(0, command.length()-1) + ")";
		System.out.println(command);
		return command;
	}
	
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
		
		return mapArgs;
	}
	
	public String printGraph(Graph graph) {
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
	
}

package gui;


import java.awt.Color;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.ComboBoxEditor;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;
import javax.swing.plaf.basic.BasicComboBoxEditor;

/**
 * 
 * Create an autocomplete JComboBox (based on its item list)
 *
 */
//Implementtion of AutoCompleteComboBox
@SuppressWarnings("rawtypes")
class AutoCompleteJComboBox extends JComboBox {
	private static final long serialVersionUID = 1L;
	
	
	public int caretPos = 0;
	public JTextField tfield = null;
	
	@SuppressWarnings("unchecked")
	public AutoCompleteJComboBox(Object[] objects) {
	   super(objects);
	   this.setBorder(new LineBorder(Color.ORANGE));
	   setEditor(new BasicComboBoxEditor());
	   setEditable(true);
	}
	
	/**
	 * set the selected item by providing its index
	 * @param index the index of the item that we want to select
	 */
	public void setSelectedIndex(int index) {
	   super.setSelectedIndex(index);
	   tfield.setText(getItemAt(index).toString());
	   tfield.setSelectionEnd(caretPos + tfield.getText().length());
	   tfield.moveCaretPosition(caretPos);
	}
	
	/**
	 * Sets a custom editor used to paint and edit the selected item in the JComboBox field.
	 * @param editor the custom editor
	 */
	public void setEditor(ComboBoxEditor editor) {
		
		
	   super.setEditor(editor);
	   if(editor.getEditorComponent() instanceof JTextField) {
	      tfield = (JTextField) editor.getEditorComponent();
	      tfield.addKeyListener(new KeyAdapter() {
	    	  
	         public void keyReleased(KeyEvent ke) {
	            char key = ke.getKeyChar();
	            if (!(Character.isLetterOrDigit(key) || Character.isSpaceChar(key) )) return;
	            caretPos = tfield.getCaretPosition();
	            String text="";
	            try {
	               text = tfield.getText(0, caretPos);
	            } catch (javax.swing.text.BadLocationException e) {
	               e.printStackTrace();
	            }
	            
	            for (int i=0; i < getItemCount(); i++) {
	               String element = (String) getItemAt(i);
	               if (element.startsWith(text)) {
	                  setSelectedIndex(i);
	                  return;
	               }
	            }
	            
	         }
	         
	      });
	   }
	   
	   
	}
}

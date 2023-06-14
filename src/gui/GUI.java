package gui;

import java.awt.EventQueue;

import executable.granonui.Tui;
import utils.Grammar;

public class GUI {
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
			EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Tui.grammar = new Grammar();
					MainJframe frame = new MainJframe();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}

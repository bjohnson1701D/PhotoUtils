package com.photo.renamer;

import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class Main {
	private static final long serialVersionUID = 1L;
	static JFrame frame;
	private JPanel content;
	public static MainPanel mp = new MainPanel();
	public static GroupPanel gp = new GroupPanel();
		
	private void run(){
		frame = new JFrame("Photo Utilities");
		frame.setLocationRelativeTo(null);
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {System.exit(0);}
		});
		content = new JPanel();
		content.setLayout(new CardLayout());
		content.add(mp);
		content.add(gp);
		frame.setContentPane(content);
		frame.setJMenuBar(new MenuHelper().setupMenu(frame));
		frame.setSize(new Dimension(250,150));
		frame.setVisible(true);
		frame.setResizable(false);
	}
	
	public static void main(String s[]) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				new Main().run();
			}
		});
	}

	public static void setSize(Dimension dimension) {
		frame.setSize(dimension);		
	}
}
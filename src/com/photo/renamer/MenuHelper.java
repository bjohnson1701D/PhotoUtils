package com.photo.renamer;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

public class MenuHelper {

	public JMenuBar setupMenu(final JFrame frame){
		JMenuBar menuBar = new JMenuBar();
		JMenu file = new JMenu("File");
		JMenu help = new JMenu("Help");
		JMenuItem exit = new JMenuItem("Exit");
		JMenuItem mainScreen = new JMenuItem("Main Screen");
		JMenuItem removeTags = new JMenuItem("Remove Tags");
		JMenuItem createGroup = new JMenuItem("Create Group");
		JMenuItem about = new JMenuItem("About");

		mainScreen.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
    			Main.mp.setVisible(true);
    			Main.gp.setVisible(false);
    			Main.setSize(Main.mp.defaultSize());
            }
        });
		createGroup.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
    			Main.mp.setVisible(false);
    			Main.gp.setVisible(true);
    			Main.setSize(Main.gp.defaultSize());
            }
        });
		exit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                System.exit(0);
            }
        });
		removeTags.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                Main.mp.removeTagButtonClick();
            }
        });
		about.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {//todo better dialog.
				JOptionPane.showMessageDialog(frame,"VERSION: 0.2\nLYSM - HG","About",JOptionPane.PLAIN_MESSAGE);
			}
		});
		
		file.add(mainScreen);
		file.add(removeTags);
		file.add(createGroup);
		file.addSeparator();
		file.add(exit);
		
		help.add(about);
		
		menuBar.add(file);
		menuBar.add(help);
		return menuBar;
	}
}

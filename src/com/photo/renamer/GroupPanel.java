package com.photo.renamer;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class GroupPanel extends JPanel implements ActionListener {
	private static final long serialVersionUID = 1L;
	private JLabel label = new JLabel("Group Name: ");
	private JLabel info = new JLabel("");
	private JButton cancel = new JButton("Cancel");
	private JButton ok = new JButton("OK");
	private JButton select = new JButton("Select Files");
	private JFileChooser fileChooser = new JFileChooser("Select Files To Rename");
	private JTextField groupName = new JTextField();
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
	private String dateRegex = "((19[0-9]{2})|(20[0-9]{2}))[0-1][0-9][0-3][0-9]";
	File[] files = new File[]{};
	
	public GroupPanel() {
		this.setLayout(null);
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		fileChooser.setAcceptAllFileFilterUsed(false);
		fileChooser.setCurrentDirectory(new java.io.File("."));
		fileChooser.setMultiSelectionEnabled(true);

		label.setBounds(15,15,100,25);
		groupName.setBounds(100, 15, 200, 25);
		cancel.setBounds(250, 75 ,75,25);
		ok.setBounds(150, 75 ,75,25);
		select.setBounds(15, 75 ,110,25);
		info.setBounds(15, 45, 250, 25);
		
		this.setListeners();
		
		add(info);
		add(ok);
		add(select);
		add(groupName);
		add(cancel);
		add(label);
		
		ok.setEnabled(false);
		this.setVisible(false);
	}
		
	private void setListeners() {
		cancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				info.setText("");
				Main.mp.setVisible(true);
				Main.gp.setVisible(false);
				Main.setSize(Main.mp.defaultSize());
			}
		});
		
		groupName.addKeyListener(new KeyListener() {
			public void keyTyped(KeyEvent e) {}
			public void keyReleased(KeyEvent e) {
				if (groupName.getText() != null && !groupName.getText().isEmpty()) {
					ok.setEnabled(true);
				} else {
					ok.setEnabled(false);
				}
			}
			public void keyPressed(KeyEvent e) {}
		});
		
		select.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				files = new File[]{};//start with new array
				if (fileChooser.showOpenDialog(Main.gp) == JFileChooser.APPROVE_OPTION) {
					files = fileChooser.getSelectedFiles();
					fileChooser.setCurrentDirectory(fileChooser.getCurrentDirectory());
					info.setText("Selected "+files.length+" files");
				}
			}
		});
		
		ok.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				File folder = fileChooser.getCurrentDirectory();
				String group = groupName.getText();
				int i = 1;//increment
				int a = 0;//added
				for(File f : files){
					if (f.isFile()) {
						String fileName = f.getName();
						//Sample
						if(fileName.matches(dateRegex+"\\s*("+group+")\\s*[0-9]*.[a-zA-Z]*")){
							continue;
						}
						String[] fileParts = fileName.split("\\.");
						String extension = "." + fileParts[fileParts.length-1];
						
						/*
						 * Expects files in a format where the year, month, and day
						 * are appended to the beginning of the file: yyyyMMdd_000000.jpg
						 * If it is not in this format then the files lastModified date will
						 * be used when constructing the new file name.
						 */
						String dateString = null;
						Pattern datePattern = Pattern.compile(dateRegex);
						Matcher m = datePattern.matcher(fileName);
						while (m.find()) {
						    dateString = m.group();
						}
						if(null==dateString||dateString.isEmpty()||!isDate(dateString)){
							dateString = sdf.format(f.lastModified());
						}
						
						String updatedName = String.format("%s %s %s", dateString, group, i);
						File newFile = new File(folder + "/" + updatedName + extension);
						if (!newFile.exists()) {//if the file does not exist then create the new file
							f.renameTo(newFile);
							i++;
							a++;
						}else{
						//if the file exists keep incrementing until we find a free name.
						//should not result in infinite loop.
							boolean success = false;
							i++;
							do{
								updatedName = String.format("%s %s %s",dateString, group, i);
								newFile = new File(folder + "/" + updatedName + extension);
								if (!newFile.exists()) {//try again!
									f.renameTo(newFile);
									success = true;
									a++;
									i++;
								}
								i++;
							}while(!success);
						}
					}
				}
				
				info.setText("");
				groupName.setText(null);
				ok.setEnabled(false);
				JOptionPane.showMessageDialog(Main.frame,
						a +" files were added to group '"+group+"'.", 
						"Complete", JOptionPane.INFORMATION_MESSAGE);
				files = new File[]{};
			}

			private boolean isDate(String dateString) {
				try{
					sdf.setLenient(false);
					sdf.parse(dateString);
					return true;
				}catch(ParseException p){
					return false;
				}
			}
		});
		
		groupName.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(groupName.getText()!=null || !groupName.getText().isEmpty()){
					ok.setEnabled(true);
				}else{
					ok.setEnabled(false);
				}
			}
		});
	}

	public Dimension defaultSize() {
		return new Dimension(350,175);
	}

	@Override
	public void actionPerformed(ActionEvent e) {}

}

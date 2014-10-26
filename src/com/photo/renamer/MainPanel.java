package com.photo.renamer;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class MainPanel extends JPanel implements ActionListener {

	private static final long serialVersionUID = 1L;

	private JButton removeTagButton = new JButton("Remove Image/Video tags");;
	private JButton groupButton = new JButton("Group Photos");
	private JFileChooser folderChooser = new JFileChooser();

	public void removeTagButtonClick() {
		removeTagButton.doClick();
	}

	public MainPanel() {
		removeTagButton.setPreferredSize(new Dimension(250, 25));
		groupButton.setPreferredSize(new Dimension(250, 25));

		add(removeTagButton);
		add(groupButton);

		groupButton.addActionListener(this);

		removeTagButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				removeTags();
			}
		});

		folderChooser.setDialogTitle("Select A Folder");
		folderChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		folderChooser.setAcceptAllFileFilterUsed(false);
		folderChooser.setCurrentDirectory(new java.io.File("."));
	}

	private void removeTags() {
		if (folderChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
			File folder = folderChooser.getSelectedFile();
			folderChooser.setCurrentDirectory(folderChooser.getCurrentDirectory());
			this.removeImgVidTags(folder);
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(groupButton)) {
			Main.mp.setVisible(false);
			Main.gp.setVisible(true);
			Main.setSize(Main.gp.defaultSize());
		}
	}

	public Dimension defaultSize() {
		return new Dimension(250, 150);
	}

	public void removeImgVidTags(File folder) {
		int renamed = 0;
		for (final File f : folder.listFiles()) {
			if (f.isFile()) {
				String originalName = f.getName();
				String updatedName = f.getName();
				//TODO: REGEX!
				updatedName = updatedName.replace("IMG_", "");
				updatedName = updatedName.replace("VID_", "");
				if (updatedName.equals(originalName))
					continue;
				File newFile = new File(folder + "/" + updatedName);
				if (!newFile.exists()) {
					f.renameTo(newFile);
					renamed ++;
				} else {
					String msg = String
							.format("Error renaming file '%s' to '%s', "
									+ "file name may already exist.\n\n Continue with rest of files?",
									f.getName(), newFile.getName());
					int optVal = JOptionPane.showConfirmDialog(Main.frame, msg,
							"About", JOptionPane.YES_NO_OPTION);
					if (optVal != 0) {// If not YES
						break;
					}
				}
			}
		}
		JOptionPane.showMessageDialog(Main.frame,
				renamed+" files were successfully renamed.", "Complete", JOptionPane.INFORMATION_MESSAGE);
	}
}

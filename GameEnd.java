
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import java.util.*;
import java.net.*;
import java.io.*;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class GameEnd extends JFrame {
	
	public GameEnd (char status) {
		super();

		// Creates the Action Listener
		GameEndActionListener e = new GameEndActionListener();

		// Determines which Background to use
		String background;
		if (status == 'W') {
			background = "YouWin.png";
			this.setSize(450, 381);
		}
		else {
			background = "YouLose.png";
			this.setSize(500, 400);
		}

		// Sets the Location
		this.setLocation(450, 250);

		// Making the JPanel with the Background
		BackgroundPanel endGame = new BackgroundPanel(background);
	
		// Making the End Game Button
		JButton endButton = new JButton ("End Game");
		endButton.setPreferredSize(new Dimension(200, 50));	
		endButton.setBackground(Color.BLUE);
		endButton.setForeground(Color.WHITE);
		endButton.setOpaque(true);
		endButton.setBorderPainted(false);

		endButton.addActionListener(e);
		endGame.add(endButton);
		// Adding the Panel
		this.add(endGame);

		// Removes Border
		this.setUndecorated(true);

		// Adds the Audio

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	
	}
	
	// Exits the Program when the End Game Button is clicked
	private class GameEndActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			System.exit(0);
		}
	}
}

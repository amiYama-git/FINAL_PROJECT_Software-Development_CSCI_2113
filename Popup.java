
import java.util.*;
import java.net.*;
import java.io.*;

import javax.swing.*;
import java.awt.*;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

// A Thread that creates a popup for Special Cards based on the inputed type
/* TO RUN: 
                    // Special Cards Play GIF
                    if (num > 9) {
                        Thread popup = new Popup(color, num);
                        popup.start();
                    }
*/

public class Popup extends Thread  {        
	// To determine what Image Pops Up
        private char col;
	private int num;
	protected int xCord;
	protected int yCord;
	protected int xLen;
	protected int yLen;
	protected int runTime;

        // Thread Constructor
        public Popup (char col, int num) {
                this.col = col;
		this.num = num;
		this.xCord = 550;
		this.yCord = 250;
		this.xLen = 230;
		this.yLen = 350;
		this.runTime = 3500;
        }
		
	// Shows for a couple of second before deleting itself
        public void run() {

		// Makes a frame without borders
                JFrame pop = new JFrame();
                pop.setUndecorated(true);

		// Makes a Square
		pop.setSize(xLen, yLen);

		// Puts it in the Middle
		// Will this be relative based on size of computer screen? If so we may need it to pass where the Draw / Place pile is on the main GUI?
		pop.setLocation(xCord, yCord);	

		// Makes String for calling the GIF from file
		String type = "special_gifs/" + col + "_" + num + ".gif";

		// Plays the GIF on a Label
		JLabel label = new JLabel ();
		ImageIcon icon = null;

		try {
			icon = new ImageIcon (type);
		} catch (Exception e) {
			System.out.println("File Not Found");
		}

		label.setIcon(icon);
		pop.getContentPane().add(label);

		// Makes the Frame Itself Transparent. 
		pop.setBackground(new Color(1.0f,1.0f,1.0f,0.0f));

		// Makes it visible
                pop.setVisible(true);

		// Makes String for Audio File
		String audio = "sound_effects/" + num + ".wav";

		// Plays the Audio File
		try {
			File file = new File(audio);
	                Clip clip = AudioSystem.getClip();
	                clip.open(AudioSystem.getAudioInputStream(file));
	                clip.start();
		}
		catch (Exception e) {
			System.out.println("Failed to play Audio");
		}

		// Waits three seconds
		try {
			sleep(runTime);
		} catch (Exception e) {
			System.out.println("Popup Failed");
		}

		// Destroys the popup. 
		pop.dispose();
       }
}

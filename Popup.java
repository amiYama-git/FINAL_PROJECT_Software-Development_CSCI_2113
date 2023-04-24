
import java.util.*;
import java.net.*;
import java.io.*;

import javax.swing.*;
import java.awt.*;

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

        // Thread Constructor
        public Popup (char col, int num) {
                this.col = col;
		this.num = num;
        }
		
	// Shows for a couple of second before deleting itself
        public void run() {

		// Makes a frame without borders
                JFrame pop = new JFrame();
                pop.setUndecorated(true);

		// Makes a Square
		pop.setSize(230, 350);

		// Puts it in the Middle
		// Will this be relative based on size of computer screen? If so we may need it to pass where the Draw / Place pile is on the main GUI?
		pop.setLocation(550, 250);	

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

		// Waits three seconds
		try {
			sleep(3800);
		} catch (Exception e) {
			System.out.println("Popup Failed");
		}

		// Destroys the popup. 
		pop.dispose();
       }
}


import java.util.*;
import java.net.*;
import java.io.*;

import javax.swing.*;
import java.awt.*;

// A Thread that creates a popup for Special Cards based on the inputed type

public class Popup extends Thread  {        
	// To determine what Image Pops Up
        private String type;

        // Thread Constructor
        public Popup (String type) {
                this.type = type;
        }
		
	// Shows for a couple of second before deleting itself
        public void run() {

		// Makes a frame without borders
                JFrame pop = new JFrame();
                pop.setUndecorated(true);

		// Makes a Square
		pop.setSize(300, 300);

		// Puts it in the Middle
		// Will this be relative based on size of computer screen? If so we may need it to pass where the Draw / Place pile is on the main GUI?
		pop.setLocation(550, 250);	

                // Calls Method making the frame depending on the type
JLabel label = new JLabel ("BEANS"); // Test
pop.add(label);
		// Makes the Frame Itself Transparent. 
		pop.setBackground(new Color(1.0f,1.0f,1.0f,0.0f));

		// Makes it visible
                pop.setVisible(true);

		// Waits three seconds
		try {
			sleep(4000);
		} catch (Exception e) {
			System.out.println("Popup Failed");
		}

		// Destroys the popup. 
		pop.dispose();
       }
}

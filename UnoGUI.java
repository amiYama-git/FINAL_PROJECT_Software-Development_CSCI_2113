
import java.util.*;
import java.net.*;
import java.io.*;

import javax.swing.*;
import java.awt.*;

/*
 * The GUI
 */

public class UnoGUI {
	public static void main(String[] args) {
		
		// TEST
		// Do the Following lines to Make Popup for Special Cards
		Thread t = new Popup(/* Insert Card Type */ "B");
		t.start();
		// END TEST


		
	}	
/*	
	// Starts a thread that makes a Popup for when a Special Card is played
	// Shows for a couple of second before deleting itself
	private class Popup extends Thread {
		// To determine what Image Pops Up
		String type;
		
		// Thread Constructor 
		public Popup (String type) {
			this.type = type;
		}
	
		public void run() {

			JFrame pop = new JFrame();
			pop.setUndecorated(true);
	
			// Calls Method making the frame depending on the type
	
			pop.setVisible(true);		
		}
	}
*/
}




import java.util.*;
import java.net.*;
import java.io.*;

import javax.swing.*;
import java.awt.*;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;


/* TO RUN: 
	Thread unoPopup = new UnoPopup(color, num);
	unoPopup.start();
*/

// Color is U, Number is either 1 or 2
// U, 1 is Standard Uno
// U, 2 is You Failed Uno

// Starts a thread that extends Popup - Same Functionality - Different Gif Size. 
public class UnoPopup extends Popup {
	// Constructor
	public UnoPopup (char col, int num) {
		super(col, num);
		this.xCord = 350;
		this.yCord = 250;
		this.xLen = 640;
		this.yLen = 360;
		
	}

	// Shows for a couple of seconds before deleting itself
	public void run() {
		super.run();
	}
}

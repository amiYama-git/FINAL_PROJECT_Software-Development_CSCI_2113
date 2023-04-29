
import java.util.*;
import java.net.*;
import java.io.*;

import javax.swing.*;
import java.awt.*;

/*
 * The GUI
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

//break teh array down

public class UnoGUI_copy {


	public static void main(String[] args) {
		//TEST
		char col = 'b';
		int num = 100;
		Player player = new Player("localhost", 8181, "Billy", null);

		// When card is received from server
		// Special Cards Play GIF
                 if (num > 9) {
                        Thread popup = new Popup(col, num);
                        popup.start();
                 }

		// if UNO Button is Pressed
		player.uno();
	}
}

	
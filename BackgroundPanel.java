
import java.util.*;
import java.net.*;
import java.io.*;

import javax.swing.*;
import java.awt.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

public class BackgroundPanel extends JPanel{

	private BufferedImage image; 

	// Constructor
	public BackgroundPanel (String hello) {
		try {

			image = ImageIO.read(new File(hello));

		} catch (Exception e) {
			System.out.println("FAILED BACKGROUND");
		}
	}
	
	// Makes the Image the Background of the Panel
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(image, 0, 0, this);
	}
}

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class UnoScreen extends UnoGUI { //the actual playing screen
	private Player player;
	private int toReturn = 5;
	JLabel currentCol = new JLabel();

	//just make the frame and panels
	public UnoScreen (String ip, int host, String name){
		player = new Player(ip, host, name, this);

		frame.setSize(new Dimension(500, 730));//730
		frame.setLayout(null);
		frame.setVisible(true);

		yourside.setBounds(0, 0, 900, 240);
		stackpanel.setBounds(0, 270, 500, 190);
		addstack.setBounds(0,0, 80, 110);//same as card size
		stackpanel.add(addstack);
		mysidepanel.setBounds(0, 490, 900, 190);//240 690
		unobuttonpanel.setBounds(0, 640, 500, 40);

		JButton unobutton = new JButton("UNO");
			unobutton.addActionListener(unoclick -> {
			System.out.println("uno clicked");
			player.uno();
		});
		
		unobutton.setPreferredSize(new Dimension(60, 40));
		unobutton.setBounds(150, 8, 200, 24);
		unobuttonpanel.add(unobutton);

		JButton drawButton = new JButton("Draw");
		drawButton.addActionListener((e) -> {
			player.drawCard();
		});
		drawButton.setPreferredSize(new Dimension(60, 40));
		drawButton.setBounds(250, 8, 200, 24);
		unobuttonpanel.add(drawButton);

		currentCol.setBounds(400, 8, 200, 24);
		mysidepanel.add(currentCol);

		frame.add(yourside);
		frame.add(stackpanel);
		frame.add(mysidepanel);
		frame.add(unobuttonpanel);

		frame.setTitle("UNO");
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		startingframe.setVisible(false);
	}

	public void updateOpponent(int numcards){

		for(int i = 0; i < numcards; i++){

			String imagefilename = "./" + "cards/" + "back.png";

			ImageIcon dabIcon = new ImageIcon(imagefilename);
			Image dabImage = dabIcon.getImage();
			Image modifiedImage = dabImage.getScaledInstance(80, 110, java.awt.Image.SCALE_SMOOTH);
			dabIcon = new ImageIcon(modifiedImage);

			JButton button = new JButton(dabIcon);
			button.setBounds((i*40) + 130, 10, 80, 110);
			yourside.add(button);
			
		}
		
		yourside.invalidate();
		yourside.validate();
		yourside.repaint();

		// If opponent has 0 cards, Ends Game
		if (numcards == 0) {
			GameEnd end = new GameEnd('L');
			end.setVisible(true);
		}
	}

	public void updateStack(Card card){
		try{
			ImageIcon dabIcon;
			dabIcon = addimage(card);
			addstack.setIcon(dabIcon);//adding a imageicon
			System.out.println("stack updated");
			currentCol.setText("Color: " + card.getCol());
		}
		catch(Exception e){
			System.out.println("image cannot be found");
		}
	}

	/***************************************************************************/

	public void cardarray(ArrayList<Card> hand){
		System.out.println("IN CARDARRAY");

		mysidepanel.removeAll();

		cards.clear();
		
		//break card array
		for(int i = 0; i < hand.size(); i++){ //go through one by one
			cards.add(hand.get(i));
			ImageIcon dabIcon = addimage(hand.get(i));
			addfunc(dabIcon, hand.get(i));
		}

		System.out.println("HAND CONTAINS: " + cards.size() + " cards");
		System.out.println("LAYERED PANE CONTAINS: " + mysidepanel.getComponentCount() + " things");
		
		frame.invalidate();
		frame.validate();
		frame.repaint();
	}

	public void addfunc(ImageIcon dabIcon, Card onecard){
		JButton button = new JButton(dabIcon);
		mysidepanel.add(button);

		for(int i = 0; i < cards.size(); i++){
			button.setBounds((i*40) + 130, 10, 80, 110); //30 80 110
		}

		button.addActionListener(new ActionListener(){

		public void actionPerformed(ActionEvent e){
			char color = onecard.getCol();
			int num = onecard.getNum();
			
			if(num > 9){
			   Thread popup = new Popup(color, num);
			  popup.start();
			}

			boolean stop = false;

			remove = false;
			
			if (player.playCard(num, color)) {
				mysidepanel.remove(button);
			}
		}
		});
	}

	public ImageIcon addimage(Card onecard){
		String imagefilename = "./cards/" + choseImage(onecard.getCol(), onecard.getNum());
		System.out.println("Image added: " + imagefilename);

		ImageIcon dabIcon = new ImageIcon(imagefilename);
		Image dabImage = dabIcon.getImage();
		Image modifiedImage = dabImage.getScaledInstance(80, 110, java.awt.Image.SCALE_SMOOTH);
		dabIcon = new ImageIcon(modifiedImage);

		return dabIcon;
	}

	/***************************************************************************/

	public String choseImage(char color, int num){

		String col = String.valueOf(color);
		return (col +"_"+ num + ".png");
	}

	public int colorpicker(){
			JFrame colorframe = new JFrame();
			colorframe.setSize(400,400);

			JPanel Colorchange = new JPanel(new GridLayout(2,2));
			//startingframe.add(North, BorderLayout.NORTH); 0 red, 1, yellow, 2 blue 3 green

			JButton red = new JButton("red");
			Colorchange.add(red);
			red.setBackground(Color.RED);
			red.setOpaque(true);
				red.addActionListener((redclick) -> {
					toReturn = 0;
					colorframe.dispose();
				});

			JButton blue = new JButton("blue");
			Colorchange.add(blue);
			blue.setBackground(Color.BLUE);
			blue.setOpaque(true);
				blue.addActionListener(blueclick -> {
					toReturn = 2;
					colorframe.dispose();
				});

			JButton yellow = new JButton("yellow");
			Colorchange.add(yellow);
			yellow.setBackground(Color.YELLOW);
			yellow.setOpaque(true);
				yellow.addActionListener(yellowclick -> {
					toReturn = 1;
					colorframe.dispose();
				});
			
			JButton green = new JButton("green");
			Colorchange.add(green);
			green.setBackground(Color.GREEN);
			green.setOpaque(true);
				green.addActionListener(greenclick -> {
					toReturn = 3;
					colorframe.dispose();
				});

			//send choosecolor to somewhere
			colorframe.setVisible(true);
			colorframe.add(Colorchange);

			System.out.println("CHOSE " + toReturn);
			return toReturn;
		}
}

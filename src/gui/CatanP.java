package gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.MouseInfo;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import board.*;
import game.*;


public class CatanP extends JPanel{

	private int state = 0;
		//0 = rien
		//1 = choix de tuile
		//2 = choix de colonne
		//3 = chois de route
		//4 = choix de ville 
		//5 = choix de setup colonne
	private Jeu game;
	private int hexagonSide;
	private int rectangSide;
	private int heightMarginR = 100;
	private int widthMarginR;
	private int boardHeight;
	private final int structSize = 12;
	private final int roadSize = 20;

	private Tuile[][] tiles;
	private Chemin[][][] roads;
	private Peuplement[][] structures;
	private ArrayList<Joeur> players;
	
	private int index;
	private boolean capitol = false;
	
	
	
	public CatanP(ArrayList<Joeur> players) {

		game = new Jeu(players);
		this.players = players;
		tiles = game.getP().getTiles();
		roads = game.getP().getRoads();
		structures = game.getP().getStructures();
		setBackground(new Color(164,200,218)); //TODO add background
		
		this.addComponentListener(new ComponentListener() {
    		public void componentResized(ComponentEvent e) {
    			boardHeight = getHeight(); 			
    			rectangSide = (boardHeight - 2 * heightMarginR) / 4;    		
    			widthMarginR= (getWidth() - (int) (6 *rectangSide)) / 2;
    		}

    		public void componentHidden(ComponentEvent e) {}

    		public void componentMoved(ComponentEvent e) {}

    		public void componentShown(ComponentEvent e) {}
    	});

		MouseListener m = new AMouseListener();
		addMouseListener(m);
	}

	public void paintComponent(Graphics g) {
		boardHeight = getHeight();	
		rectangSide = (boardHeight - 2 * heightMarginR) / 4;		
		widthMarginR= (getWidth() - (int) (6 *rectangSide)) / 2;
		Graphics2D g2 = (Graphics2D)g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setFont(new Font("Courier", Font.PLAIN, 22));
		super.paintComponent(g2);

		for(int y=2;y<6;y++) {
			for (int x = 2; x <= 5; x++) {
			drawHex(tiles[x][y],g2);
		}
		}
		//draw numbers
		for(int y=2;y<6;y++) {
			for (int x = 2; x <= 5; x++) {
				drawNumber(tiles[x][y],g2);
		}
		}

		//draw robber
		for(int y=2;y<6;y++) {
			for (int x = 2; x <= 5; x++) {
				drawRobber(tiles[x][y],g2);
		}
		}

		//Draw roads
		for (int i = 0; i < roads.length; i++){
			for (int k = 0; k < roads[0].length; k++){
				for (int o = 0; o < roads[0][0].length; o++){
					drawRoad(roads[i][k][o], false, g2);
				}
			}
		}

		//Draw structures
		for (int i = 0; i < structures.length; i++){
			for (int k = 0; k < structures[0].length; k++){
					drawStructure(structures[i][k], false, g2);
			}
		}

		//Highlights
		Point p = MouseInfo.getPointerInfo().getLocation();
		SwingUtilities.convertPointFromScreen(p,this);

		if (state == 1) {
			Localisation loc = pxToTile(p);
			if (loc != null) {
				highlightTile(loc, g2);

			}
		}
		else if (state == 2 || state == 4 || state == 5) {
			Localisation loc = pxToStructure(p);

			if (loc != null) {

				drawStructure(structures[loc.getXCoord()][loc.getYCoord()], true, g2);
			}
		}
		else if (state == 3) {
			Loca_Chemin loc = pxToRoad(p);
			if (loc != null) {

				drawRoad(roads[loc.getXCoord()][loc.getYCoord()][loc.getOrientation()], true, g2);
			}
		}
		labelPorts(g2);
	}

	public void setState(int newState) {
		state = newState;
	}
	
	public int getState() {
		return state;
	}

	public void labelPorts(Graphics2D g2) {
	
	}

	public void drawHex(Tuile tile, Graphics2D g2){
		int x = tile.getLocation().getXCoord();
		int y = tile.getLocation().getYCoord();
		
		String type = tile.getType();
		switch (type) {
			case "DESERT":
				g2.setColor(new Color(0xFF, 0xFF, 0xA9));
				break;
			case "ARGILE":
				g2.setColor(new Color(0xAD, 0x33, 0x33));
				break;
			case "LAINE":
				g2.setColor(Color.GREEN);
				break;
			case "BOIS":
				g2.setColor(new Color(0x99, 0x66, 0x33));
				break;
			case "BLE":
				g2.setColor(Color.YELLOW);
				break;
			case "ROCHER":
				g2.setColor(Color.LIGHT_GRAY);
				break;
			default:
				g2.setColor(Color.WHITE);
				break;
		}
		Point p=findCenterR(x,y);
		g2.fillRect(p.x,p.y,rectangSide,rectangSide);
		g2.setColor(Color.BLACK);
		g2.drawRect(p.x,p.y,rectangSide,rectangSide);
	}

	public void highlightTile(Localisation loc, Graphics2D g2) {
		int x = loc.getXCoord();
		int y = loc.getYCoord();
		if (tiles[x][y].hasRobber()) {
			return;
		}
		Point p = findCenterRr(x,y);
		Shape shape = new Ellipse2D.Double((int)p.getX() - 25, (int)p.getY() - 25, 50, 50);
		g2.setColor(Color.WHITE);
		g2.fill(shape);
		g2.draw(shape);
	}
	
	public void drawNumber(Tuile tile, Graphics2D g2) {
		if (tile.getNumber() == 0) {
			return;
		}
		int x = tile.getLocation().getXCoord();
		int y = tile.getLocation().getYCoord();
		Point p = findCenterRr(x,y);
		
		g2.setColor(Color.BLACK);
		if (tile.getNumber() == 6 || tile.getNumber() == 8)
			g2.setColor(Color.RED);
		g2.drawString("" + tile.getNumber(), (int)p.getX() - 5, (int)p.getY() + 5);
	}

	public void drawRobber(Tuile tile, Graphics2D g2) {
		if (!tile.hasRobber()) {
			return;
		}
		int x = tile.getLocation().getXCoord();
		int y = tile.getLocation().getYCoord();
		Point p = findCenterRr(x,y);
		
		Shape shape = new Ellipse2D.Double((int)p.getX()-25, (int)p.getY()-25, 40, 40);

		g2.setColor(Color.black);
		g2.fill(shape);
		g2.draw(shape);
	}

	public void drawRoad(Chemin r, boolean highlighted, Graphics2D g2) {
		Joeur player = r.getOwner();
		Graphics2D g2c = (Graphics2D) g2.create();
		if (player == null) {
			if (highlighted)
				g2c.setColor(Color.WHITE);
			else
			 return;
		}
		else {
			if (highlighted)
				return;
			else
				g2c.setColor(player.getColor());
		}
		AffineTransform transformer = new AffineTransform();
		
		Point tileCenter = findCenterR(r.getLocation().getXCoord(), r.getLocation().getYCoord());
		int y = (int) tileCenter.getY();
		int x = (int) tileCenter.getX();
		int o = r.getLocation().getOrientation();
		int height = rectangSide / 10;
		Rectangle rect = new Rectangle(x,y, (int) (0.9 * rectangSide), height);

			if (o == 1) {
			transformer.rotate(Math.toRadians(-90), x+0.95* rectangSide, y);
		}

		g2c.transform(transformer);
		g2c.fill(rect);
		g2c.setColor(Color.BLACK);
		g2c.draw(rect);
	}

	public void drawStructure(Peuplement s, boolean highlighted, Graphics2D g2) {
		Joeur player = s.getOwner();
		if (state == 2 || state == 5) {
			if (player == null) {
				if (highlighted)
					g2.setColor(Color.WHITE);
				else
					return;
			}
			else {
				if (highlighted)
					return;
				else
					g2.setColor(player.getColor());
			}
		}
		else if (state == 4) {
			if (player == null)
				return;
			else if (player == GameRunner.getCurrentPlayer()) {
				if (highlighted){
					if (s.getType() == 0)
						g2.setColor(Color.WHITE);
					else
						g2.setColor(player.getColor());
				}
				else
					g2.setColor(player.getColor());
			}
			else
				g2.setColor(player.getColor());
		}
		else {
			if (player == null)
				return;
			else
				g2.setColor(player.getColor());
		}
		
		Shape shape;
		Point tileCenter = findCenterRr(s.getLocation().getXCoord(), s.getLocation().getYCoord());
		int y = (int) tileCenter.getY();
		int x = (int) tileCenter.getX();

		if (s.getType() == 0) {
			shape = new Rectangle(x + rectangSide/2-12, y - rectangSide/2-12, 2*structSize, 2*structSize);
		}
		else {
			shape = new Ellipse2D.Double(x - structSize, y - structSize, 2*structSize, 2*structSize);
		}

		g2.fill(shape);
		g2.setColor(Color.BLACK);
		g2.draw(shape);

	}

	public Point findCenterR(int x, int y){
		int xCenter = widthMarginR + (int) (3 * rectangSide)
				+ (int) ((x - 1)  * rectangSide)- (int) (3 * rectangSide );
		int yCenter = boardHeight - (heightMarginR 
				+ (int) ((y - 1) * rectangSide));

		return new Point(xCenter,yCenter);
	}
	
	public Point findCenterRr(int x, int y){
		int xCenter = widthMarginR + (int) (3 * rectangSide)
				+ (int) ((x - 1)  * rectangSide)- (int) (3 * rectangSide )+rectangSide/2;
		int yCenter = boardHeight - (heightMarginR 
				+ (int) ((y - 1) * rectangSide))+rectangSide/2;

		return new Point(xCenter,yCenter);
	}
	
	public Localisation pxToTile(Point p) {
		double x = p.getX();
		double y = p.getY();

		int xCoord = 0,
			yCoord = 0;
			
		//1 star
		if (boardHeight-(heightMarginR)> y && y > boardHeight-(heightMarginR+rectangSide)) {
			yCoord = 2;
			if (widthMarginR +  (3 * rectangSide)
					+  (1  * rectangSide)-  (3 * rectangSide ) +rectangSide> x && x >widthMarginR +  (3 * rectangSide)
					+  (1  * rectangSide)-  (3 * rectangSide )){
				xCoord = 2;
			}
			else if (widthMarginR +  (4 * rectangSide)
					+  (1  * rectangSide)-  (3 * rectangSide ) +rectangSide> x && x >widthMarginR +  (4 * rectangSide)
					+  (1  * rectangSide)-  (3 * rectangSide ) ){
				xCoord = 3;
			}
			else if (widthMarginR +  (5 * rectangSide)
					+  (1  * rectangSide)-  (3 * rectangSide ) +rectangSide> x && x >widthMarginR +  (5 * rectangSide)
					+  (1  * rectangSide)-  (3 * rectangSide ) ){
				xCoord = 4;
			}
			else if (widthMarginR +  (6 * rectangSide)
					+  (1  * rectangSide)-  (3 * rectangSide ) +rectangSide> x && x >widthMarginR +  (6 * rectangSide)
					+  (1  * rectangSide)-  (3 * rectangSide ) ){
				xCoord = 5;
			}
		}
		//2 star
				if (boardHeight-(heightMarginR+rectangSide)> y && y > boardHeight-(heightMarginR+2*rectangSide)) {
					yCoord = 3;
					if (widthMarginR +  (3 * rectangSide)
							+  (1  * rectangSide)-  (3 * rectangSide ) +rectangSide> x && x >widthMarginR +  (3 * rectangSide)
							+  (1  * rectangSide)-  (3 * rectangSide )){
						xCoord = 2;
					}
					else if (widthMarginR +  (4 * rectangSide)
							+  (1  * rectangSide)-  (3 * rectangSide ) +rectangSide> x && x >widthMarginR +  (4 * rectangSide)
							+  (1  * rectangSide)-  (3 * rectangSide ) ){
						xCoord = 3;
					}
					else if (widthMarginR +  (5 * rectangSide)
							+  (1  * rectangSide)-  (3 * rectangSide ) +rectangSide> x && x >widthMarginR +  (5 * rectangSide)
							+  (1  * rectangSide)-  (3 * rectangSide ) ){
						xCoord = 4;
					}
					else if (widthMarginR +  (6 * rectangSide)
							+  (1  * rectangSide)-  (3 * rectangSide ) +rectangSide> x && x >widthMarginR +  (6 * rectangSide)
							+  (1  * rectangSide)-  (3 * rectangSide ) ){
						xCoord = 5;
					}
				}
				//3 star
				if (boardHeight-(heightMarginR+2*rectangSide)> y && y > boardHeight-(heightMarginR+3*rectangSide)) {
					yCoord = 4;
					if (widthMarginR +  (3 * rectangSide)
							+  (1  * rectangSide)-  (3 * rectangSide ) +rectangSide> x && x >widthMarginR +  (3 * rectangSide)
							+  (1  * rectangSide)-  (3 * rectangSide )){
						xCoord = 2;
					}
					else if (widthMarginR +  (4 * rectangSide)
							+  (1  * rectangSide)-  (3 * rectangSide ) +rectangSide> x && x >widthMarginR +  (4 * rectangSide)
							+  (1  * rectangSide)-  (3 * rectangSide ) ){
						xCoord = 3;
					}
					else if (widthMarginR +  (5 * rectangSide)
							+  (1  * rectangSide)-  (3 * rectangSide ) +rectangSide> x && x >widthMarginR +  (5 * rectangSide)
							+  (1  * rectangSide)-  (3 * rectangSide ) ){
						xCoord = 4;
					}
					else if (widthMarginR +  (6 * rectangSide)
							+  (1  * rectangSide)-  (3 * rectangSide ) +rectangSide> x && x >widthMarginR +  (6 * rectangSide)
							+  (1  * rectangSide)-  (3 * rectangSide ) ){
						xCoord = 5;
					}
				}
				//4 star
				if (boardHeight-(heightMarginR+3*rectangSide)> y && y > boardHeight-(heightMarginR+4*rectangSide)) {
					yCoord = 5;
					if (widthMarginR +  (3 * rectangSide)
							+  (1  * rectangSide)-  (3 * rectangSide ) +rectangSide> x && x >widthMarginR +  (3 * rectangSide)
							+  (1  * rectangSide)-  (3 * rectangSide )){
						xCoord = 2;
					}
					else if (widthMarginR +  (4 * rectangSide)
							+  (1  * rectangSide)-  (3 * rectangSide ) +rectangSide> x && x >widthMarginR +  (4 * rectangSide)
							+  (1  * rectangSide)-  (3 * rectangSide ) ){
						xCoord = 3;
					}
					else if (widthMarginR +  (5 * rectangSide)
							+  (1  * rectangSide)-  (3 * rectangSide ) +rectangSide> x && x >widthMarginR +  (5 * rectangSide)
							+  (1  * rectangSide)-  (3 * rectangSide ) ){
						xCoord = 4;
					}
					else if (widthMarginR +  (6 * rectangSide)
							+  (1  * rectangSide)-  (3 * rectangSide ) +rectangSide> x && x >widthMarginR +  (6 * rectangSide)
							+  (1  * rectangSide)-  (3 * rectangSide ) ){
						xCoord = 5;
					}
				}
		if (xCoord == 0 || yCoord == 0) {
			return null;
		}
		return new Localisation(xCoord, yCoord);
	}

	public Localisation pxToStructure(Point p) {
		double x = p.getX();
		double y = p.getY();

		int xCoord = 0,
			yCoord = 0,
			orient = 1;

		if (boardHeight-(heightMarginR)+structSize> y && y > boardHeight-(heightMarginR)-structSize) {
			if (widthMarginR +  (3 * rectangSide)
					+  (1  * rectangSide)-  (3 * rectangSide ) -structSize< x && x <widthMarginR +  (3 * rectangSide)
					+  (1  * rectangSide)-  (3 * rectangSide ) +structSize){
				xCoord = 1;
				yCoord = 1;
				orient = 0;
			}
			else if (widthMarginR +  (4 * rectangSide)
					+  (1  * rectangSide)-  (3 * rectangSide ) -structSize< x && x <widthMarginR +  (4 * rectangSide)
					+  (1  * rectangSide)-  (3 * rectangSide ) +structSize){
				xCoord = 2;
				yCoord = 1;
				orient = 1;
			}
			else if (widthMarginR +  (5 * rectangSide)
					+  (1  * rectangSide)-  (3 * rectangSide ) -structSize< x && x <widthMarginR +  (5 * rectangSide)
					+  (1  * rectangSide)-  (3 * rectangSide ) +structSize){
				xCoord = 3;
				yCoord = 1;
				orient = 1;
			}
			else if (widthMarginR +  (6 * rectangSide)
					+  (1  * rectangSide)-  (3 * rectangSide ) -structSize< x && x <widthMarginR +  (6 * rectangSide)
					+  (1  * rectangSide)-  (3 * rectangSide ) +structSize){
				xCoord = 4;
				yCoord = 1;
				orient = 1;
			}
			else if (widthMarginR +  (7 * rectangSide)
					+  (1  * rectangSide)-  (3 * rectangSide ) -structSize< x && x <widthMarginR +  (7 * rectangSide)
					+  (1  * rectangSide)-  (3 * rectangSide ) +structSize){
				xCoord = 5;
				yCoord = 1;
				orient = 1;
			}
		}
		// sec column
				if (boardHeight-(heightMarginR+rectangSide)+structSize> y && y > boardHeight-(heightMarginR+rectangSide)-structSize) {
					if (widthMarginR +  (3 * rectangSide)
							+  (1  * rectangSide)-  (3 * rectangSide ) -structSize< x && x <widthMarginR +  (3 * rectangSide)
							+  (1  * rectangSide)-  (3 * rectangSide ) +structSize){
						xCoord = 1;
						yCoord = 2;
						orient = 1;
					}
					else if (widthMarginR +  (4 * rectangSide)
							+  (1  * rectangSide)-  (3 * rectangSide ) -structSize< x && x <widthMarginR +  (4 * rectangSide)
							+  (1  * rectangSide)-  (3 * rectangSide ) +structSize){
						xCoord = 2;
						yCoord = 2;
						orient = 1;
					}
					else if (widthMarginR +  (5 * rectangSide)
							+  (1  * rectangSide)-  (3 * rectangSide ) -structSize< x && x <widthMarginR +  (5 * rectangSide)
							+  (1  * rectangSide)-  (3 * rectangSide ) +structSize){
						xCoord = 3;
						yCoord = 2;
						orient = 1;
					}
					else if (widthMarginR +  (6 * rectangSide)
							+  (1  * rectangSide)-  (3 * rectangSide ) -structSize< x && x <widthMarginR +  (6 * rectangSide)
							+  (1  * rectangSide)-  (3 * rectangSide ) +structSize){
						xCoord = 4;
						yCoord = 2;
						orient = 1;
					}
					else if (widthMarginR +  (7 * rectangSide)
							+  (1  * rectangSide)-  (3 * rectangSide ) -structSize< x && x <widthMarginR +  (7 * rectangSide)
							+  (1  * rectangSide)-  (3 * rectangSide ) +structSize){
						xCoord = 5;
						yCoord = 2;
						orient = 1;
					}
				}// third column
				if (boardHeight-(heightMarginR+2*rectangSide)+structSize> y && y > boardHeight-(heightMarginR+2*rectangSide)-structSize) {
					if (widthMarginR +  (3 * rectangSide)
							+  (1  * rectangSide)-  (3 * rectangSide ) -structSize< x && x <widthMarginR +  (3 * rectangSide)
							+  (1  * rectangSide)-  (3 * rectangSide ) +structSize){
						xCoord = 1;
						yCoord = 3;
						orient = 1;
					}
					else if (widthMarginR +  (4 * rectangSide)
							+  (1  * rectangSide)-  (3 * rectangSide ) -structSize< x && x <widthMarginR +  (4 * rectangSide)
							+  (1  * rectangSide)-  (3 * rectangSide ) +structSize){
						xCoord = 2;
						yCoord = 3;
						orient = 1;
					}
					else if (widthMarginR +  (5 * rectangSide)
							+  (1  * rectangSide)-  (3 * rectangSide ) -structSize< x && x <widthMarginR +  (5 * rectangSide)
							+  (1  * rectangSide)-  (3 * rectangSide ) +structSize){
						xCoord = 3;
						yCoord = 3;
						orient = 1;
					}
					else if (widthMarginR +  (6 * rectangSide)
							+  (1  * rectangSide)-  (3 * rectangSide ) -structSize< x && x <widthMarginR +  (6 * rectangSide)
							+  (1  * rectangSide)-  (3 * rectangSide ) +structSize){
						xCoord = 4;
						yCoord = 3;
						orient = 1;
					}
					else if (widthMarginR +  (7 * rectangSide)
							+  (1  * rectangSide)-  (3 * rectangSide ) -structSize< x && x <widthMarginR +  (7 * rectangSide)
							+  (1  * rectangSide)-  (3 * rectangSide ) +structSize){
						xCoord = 5;
						yCoord = 3;
						orient = 1;
					}
				}// fourth column
				if (boardHeight-(heightMarginR+3*rectangSide)+structSize> y && y > boardHeight-(heightMarginR+3*rectangSide)-structSize) {
					if (widthMarginR +  (3 * rectangSide)
							+  (1  * rectangSide)-  (3 * rectangSide ) -structSize< x && x <widthMarginR +  (3 * rectangSide)
							+  (1  * rectangSide)-  (3 * rectangSide ) +structSize){
						xCoord = 1;
						yCoord = 4;
						orient = 1;
					}
					else if (widthMarginR +  (4 * rectangSide)
							+  (1  * rectangSide)-  (3 * rectangSide ) -structSize< x && x <widthMarginR +  (4 * rectangSide)
							+  (1  * rectangSide)-  (3 * rectangSide ) +structSize){
						xCoord = 2;
						yCoord = 4;
						orient = 1;
					}
					else if (widthMarginR +  (5 * rectangSide)
							+  (1  * rectangSide)-  (3 * rectangSide ) -structSize< x && x <widthMarginR +  (5 * rectangSide)
							+  (1  * rectangSide)-  (3 * rectangSide ) +structSize){
						xCoord = 3;
						yCoord = 4;
						orient = 1;
					}
					else if (widthMarginR +  (6 * rectangSide)
							+  (1  * rectangSide)-  (3 * rectangSide ) -structSize< x && x <widthMarginR +  (6 * rectangSide)
							+  (1  * rectangSide)-  (3 * rectangSide ) +structSize){
						xCoord = 4;
						yCoord = 4;
						orient = 1;
					}
					else if (widthMarginR +  (7 * rectangSide)
							+  (1  * rectangSide)-  (3 * rectangSide ) -structSize< x && x <widthMarginR +  (7 * rectangSide)
							+  (1  * rectangSide)-  (3 * rectangSide ) +structSize){
						xCoord = 5;
						yCoord = 4;
						orient = 1;
					}
				}// fifth column
				if (boardHeight-(heightMarginR+4*rectangSide)+structSize> y && y > boardHeight-(heightMarginR+4*rectangSide)-structSize) {
					if (widthMarginR +  (3 * rectangSide)
							+  (1  * rectangSide)-  (3 * rectangSide ) -structSize< x && x <widthMarginR +  (3 * rectangSide)
							+  (1  * rectangSide)-  (3 * rectangSide ) +structSize){
						xCoord = 1;
						yCoord = 5;
						orient = 1;
					}
					else if (widthMarginR +  (4 * rectangSide)
							+  (1  * rectangSide)-  (3 * rectangSide ) -structSize< x && x <widthMarginR +  (4 * rectangSide)
							+  (1  * rectangSide)-  (3 * rectangSide ) +structSize){
						xCoord = 2;
						yCoord = 5;
						orient = 1;
					}
					else if (widthMarginR +  (5 * rectangSide)
							+  (1  * rectangSide)-  (3 * rectangSide ) -structSize< x && x <widthMarginR +  (5 * rectangSide)
							+  (1  * rectangSide)-  (3 * rectangSide ) +structSize){
						xCoord = 3;
						yCoord = 5;
						orient = 1;
					}
					else if (widthMarginR +  (6 * rectangSide)
							+  (1  * rectangSide)-  (3 * rectangSide ) -structSize< x && x <widthMarginR +  (6 * rectangSide)
							+  (1  * rectangSide)-  (3 * rectangSide ) +structSize){
						xCoord = 4;
						yCoord = 5;
						orient = 1;
					}
					else if (widthMarginR +  (7 * rectangSide)
							+  (1  * rectangSide)-  (3 * rectangSide ) -structSize< x && x <widthMarginR +  (7 * rectangSide)
							+  (1  * rectangSide)-  (3 * rectangSide ) +structSize){
						xCoord = 5;
						yCoord = 5;
						orient = 1;
					}
				}
		if (xCoord == 0 && yCoord == 0 && orient == 1) {
			return null;
		}
		return new Localisation(xCoord, yCoord);
	}

	public Loca_Chemin pxToRoad(Point p) {

		double x = p.getX();
		double y = p.getY();

		int xCoord = 0,
			yCoord = 0,
			orient = 1;
        //----------------------right1
		if (widthMarginR +  (3 * rectangSide)
				+  (1  * rectangSide)-  (3 * rectangSide ) +structSize> x && x >widthMarginR +  (3 * rectangSide)
				+  (1  * rectangSide)-  (3 * rectangSide ) -structSize) {
			if (boardHeight-(heightMarginR+rectangSide)+structSize< y && y < boardHeight-(heightMarginR-rectangSide)-3*rectangSide/2) {
				xCoord = 1;
				yCoord = 2;
				orient = 1;
			}
			else if (boardHeight-(heightMarginR+2*rectangSide)+structSize< y && y < boardHeight-(heightMarginR)-3*rectangSide/2) {
				xCoord = 1;
				yCoord = 3;
				orient = 1;
			}
			else if (boardHeight-(heightMarginR+3*rectangSide)+structSize< y && y < boardHeight-(heightMarginR+1*rectangSide)-3*rectangSide/2) {
				xCoord = 1;
				yCoord = 4;
				orient = 1;
			}
			else if (boardHeight-(heightMarginR+4*rectangSide)+structSize< y && y < boardHeight-(heightMarginR+2*rectangSide)-3*rectangSide/2) {
				xCoord = 1;
				yCoord = 5;
				orient = 1;
			}
		}
		//sec
		if (widthMarginR +  (3 * rectangSide)
				+  (1  * rectangSide)-  (3 * rectangSide ) +structSize+rectangSide> x && x >widthMarginR +  (3 * rectangSide)
				+  (1  * rectangSide)-  (3 * rectangSide ) -structSize+rectangSide) {
			if (boardHeight-(heightMarginR+rectangSide)+structSize< y && y < boardHeight-(heightMarginR-rectangSide)-3*rectangSide/2) {
				xCoord = 2;
				yCoord = 2;
				orient = 1;
			}
			else if (boardHeight-(heightMarginR+2*rectangSide)+structSize< y && y < boardHeight-(heightMarginR)-3*rectangSide/2) {
				xCoord = 2;
				yCoord = 3;
				orient = 1;
			}
			else if (boardHeight-(heightMarginR+3*rectangSide)+structSize< y && y < boardHeight-(heightMarginR+1*rectangSide)-3*rectangSide/2) {
				xCoord = 2;
				yCoord = 4;
				orient = 1;
			}
			else if (boardHeight-(heightMarginR+4*rectangSide)+structSize< y && y < boardHeight-(heightMarginR+2*rectangSide)-3*rectangSide/2) {
				xCoord = 2;
				yCoord = 5;
				orient = 1;
			}
		}
		//third
		if (widthMarginR +  (3 * rectangSide)
				+  (1  * rectangSide)-  (3 * rectangSide ) +structSize+2*rectangSide> x && x >widthMarginR +  (3 * rectangSide)
				+  (1  * rectangSide)-  (3 * rectangSide ) -structSize+2*rectangSide) {
			if (boardHeight-(heightMarginR+rectangSide)+structSize< y && y < boardHeight-(heightMarginR-rectangSide)-3*rectangSide/2) {
				xCoord = 3;
				yCoord = 2;
				orient = 1;
			}
			else if (boardHeight-(heightMarginR+2*rectangSide)+structSize< y && y < boardHeight-(heightMarginR)-3*rectangSide/2) {
				xCoord = 3;
				yCoord = 3;
				orient = 1;
			}
			else if (boardHeight-(heightMarginR+3*rectangSide)+structSize< y && y < boardHeight-(heightMarginR+rectangSide)-3*rectangSide/2) {
				xCoord = 3;
				yCoord = 4;
				orient = 1;
			}
			else if (boardHeight-(heightMarginR+4*rectangSide)+structSize< y && y < boardHeight-(heightMarginR+2*rectangSide)-3*rectangSide/2) {
				xCoord = 3;
				yCoord = 5;
				orient = 1;
			}
		}
		//fourth
		if (widthMarginR +  (3 * rectangSide)
				+  (1  * rectangSide)-  (3 * rectangSide ) +structSize+3*rectangSide> x && x >widthMarginR +  (3 * rectangSide)
				+  (1  * rectangSide)-  (3 * rectangSide ) -structSize+3*rectangSide) {
			if (boardHeight-(heightMarginR+rectangSide)+structSize< y && y < boardHeight-(heightMarginR-rectangSide)-3*rectangSide/2) {
				xCoord = 4;
				yCoord = 2;
				orient = 1;
			}
			else if (boardHeight-(heightMarginR+2*rectangSide)+structSize< y && y < boardHeight-(heightMarginR)-3*rectangSide/2) {
				xCoord = 4;
				yCoord = 3;
				orient = 1;
			}
			else if (boardHeight-(heightMarginR+3*rectangSide)+structSize< y && y < boardHeight-(heightMarginR+rectangSide)-3*rectangSide/2) {
				xCoord = 4;
				yCoord = 4;
				orient = 1;
			}
			else if (boardHeight-(heightMarginR+4*rectangSide)+structSize< y && y < boardHeight-(heightMarginR+2*rectangSide)-3*rectangSide/2) {
				xCoord = 4;
				yCoord = 5;
				orient = 1;
			}
		}
		//five
		if (widthMarginR +  (3 * rectangSide)
				+  (1  * rectangSide)-  (3 * rectangSide ) +structSize+4*rectangSide> x && x >widthMarginR +  (3 * rectangSide)
				+  (1  * rectangSide)-  (3 * rectangSide ) -structSize+4*rectangSide) {
			if (boardHeight-(heightMarginR+rectangSide)+structSize< y && y < boardHeight-(heightMarginR-rectangSide)-3*rectangSide/2) {
				xCoord = 5;
				yCoord = 2;
				orient = 1;
			}
			else if (boardHeight-(heightMarginR+2*rectangSide)+structSize< y && y < boardHeight-(heightMarginR)-3*rectangSide/2) {
				xCoord = 5;
				yCoord = 3;
				orient = 1;
			}
			else if (boardHeight-(heightMarginR+3*rectangSide)+structSize< y && y < boardHeight-(heightMarginR+rectangSide)-3*rectangSide/2) {
				xCoord = 5;
				yCoord = 4;
				orient = 1;
			}
			else if (boardHeight-(heightMarginR+4*rectangSide)+structSize< y && y < boardHeight-(heightMarginR+2*rectangSide)-3*rectangSide/2) {
				xCoord = 5;
				yCoord = 5;
				orient = 1;
			}
		}
		//-------------------------------------------------  top
		if (boardHeight-(heightMarginR)+structSize> y && y > boardHeight-(heightMarginR)-structSize) {
			if (widthMarginR +  (3 * rectangSide)
					+  (1  * rectangSide)-  (3 * rectangSide ) +2*rectangSide/3> x && x >widthMarginR +  (3 * rectangSide)
					+  (1  * rectangSide)-  (3 * rectangSide ) +structSize/2){
				xCoord = 2;
				yCoord = 1;
				orient = 0;
			}
			else if (widthMarginR +  (4 * rectangSide)
					+  (1  * rectangSide)-  (3 * rectangSide ) +2*rectangSide/3> x && x >widthMarginR +  (4 * rectangSide)
					+  (1  * rectangSide)-  (3 * rectangSide ) +structSize/2){
				xCoord = 3;
				yCoord = 1;
				orient = 0;
			}
			else if (widthMarginR +  (5 * rectangSide)
					+  (1  * rectangSide)-  (3 * rectangSide ) +2*rectangSide/3> x && x >widthMarginR +  (5 * rectangSide)
					+  (1  * rectangSide)-  (3 * rectangSide ) +structSize/2){
				xCoord = 4;
				yCoord = 1;
				orient = 0;
			}
			else if (widthMarginR +  (6 * rectangSide)
					+  (1  * rectangSide)-  (3 * rectangSide ) +2*rectangSide/3> x && x >widthMarginR +  (6 * rectangSide)
					+  (1  * rectangSide)-  (3 * rectangSide ) +structSize/2){
				xCoord = 5;
				yCoord = 1;
				orient = 0;
			}
		}
		// sec column
				if (boardHeight-(heightMarginR+rectangSide)+structSize> y && y > boardHeight-(heightMarginR+rectangSide)-structSize) {
					if (widthMarginR +  (3 * rectangSide)
							+  (1  * rectangSide)-  (3 * rectangSide ) +2*rectangSide/3> x && x >widthMarginR +  (3 * rectangSide)
							+  (1  * rectangSide)-  (3 * rectangSide ) +structSize/2){
						xCoord = 2;
						yCoord = 2;
						orient = 0;
					}
					else if (widthMarginR +  (4 * rectangSide)
							+  (1  * rectangSide)-  (3 * rectangSide ) +2*rectangSide/3> x && x >widthMarginR +  (4 * rectangSide)
							+  (1  * rectangSide)-  (3 * rectangSide ) +structSize/2){
						xCoord = 3;
						yCoord = 2;
						orient = 0;
					}
					else if (widthMarginR +  (5 * rectangSide)
							+  (1  * rectangSide)-  (3 * rectangSide ) +2*rectangSide/3> x && x >widthMarginR +  (5 * rectangSide)
							+  (1  * rectangSide)-  (3 * rectangSide ) +structSize/2){
						xCoord = 4;
						yCoord = 2;
						orient = 0;
					}
					else if (widthMarginR +  (6 * rectangSide)
							+  (1  * rectangSide)-  (3 * rectangSide ) +2*rectangSide/3> x && x >widthMarginR +  (6 * rectangSide)
							+  (1  * rectangSide)-  (3 * rectangSide ) +structSize/2){
						xCoord = 5;
						yCoord = 2;
						orient = 0;
					}
					
				}// third column
				if (boardHeight-(heightMarginR+2*rectangSide)+structSize> y && y > boardHeight-(heightMarginR+2*rectangSide)-structSize) {
					 if (widthMarginR +  (3 * rectangSide)
							+  (1  * rectangSide)-  (3 * rectangSide ) +2*rectangSide/3> x && x >widthMarginR +  (3 * rectangSide)
							+  (1  * rectangSide)-  (3 * rectangSide ) +structSize/2){
						xCoord = 2;
						yCoord = 3;
						orient = 0;
					}
					else if (widthMarginR +  (4 * rectangSide)
							+  (1  * rectangSide)-  (3 * rectangSide ) +2*rectangSide/3> x && x >widthMarginR +  (4 * rectangSide)
							+  (1  * rectangSide)-  (3 * rectangSide ) +structSize/2){
						xCoord = 3;
						yCoord = 3;
						orient = 0;
					}
					else if (widthMarginR +  (5 * rectangSide)
							+  (1  * rectangSide)-  (3 * rectangSide ) +2*rectangSide/3> x && x >widthMarginR +  (5 * rectangSide)
							+  (1  * rectangSide)-  (3 * rectangSide ) +structSize/2){
						xCoord = 4;
						yCoord = 3;
						orient = 0;
					}
					else if (widthMarginR +  (6 * rectangSide)
							+  (1  * rectangSide)-  (3 * rectangSide ) +2*rectangSide/3> x && x >widthMarginR +  (6 * rectangSide)
							+  (1  * rectangSide)-  (3 * rectangSide ) +structSize/2){
						xCoord = 5;
						yCoord = 3;
						orient = 0;
					}
				}// fourth column
				if (boardHeight-(heightMarginR+3*rectangSide)+structSize> y && y > boardHeight-(heightMarginR+3*rectangSide)-structSize) {
					 if (widthMarginR +  (3 * rectangSide)
							+  (1  * rectangSide)-  (3 * rectangSide ) +2*rectangSide/3> x && x >widthMarginR +  (3 * rectangSide)
							+  (1  * rectangSide)-  (3 * rectangSide ) +structSize/2){
						xCoord = 2;
						yCoord = 4;
						orient = 0;
					}
					else if (widthMarginR +  (4 * rectangSide)
							+  (1  * rectangSide)-  (3 * rectangSide ) +2*rectangSide/3> x && x >widthMarginR +  (4 * rectangSide)
							+  (1  * rectangSide)-  (3 * rectangSide ) +structSize/2){
						xCoord = 3;
						yCoord = 4;
						orient = 0;
					}
					else if (widthMarginR +  (5 * rectangSide)
							+  (1  * rectangSide)-  (3 * rectangSide ) +2*rectangSide/3> x && x >widthMarginR +  (5 * rectangSide)
							+  (1  * rectangSide)-  (3 * rectangSide ) +structSize/2){
						xCoord = 4;
						yCoord = 4;
						orient = 0;
					}
					else if (widthMarginR +  (6 * rectangSide)
							+  (1  * rectangSide)-  (3 * rectangSide ) +2*rectangSide/3> x && x >widthMarginR +  (6 * rectangSide)
							+  (1  * rectangSide)-  (3 * rectangSide ) +structSize/2){
						xCoord = 5;
						yCoord = 4;
						orient = 0;
					}
				}// fifth column
				if (boardHeight-(heightMarginR+4*rectangSide)+structSize> y && y > boardHeight-(heightMarginR+4*rectangSide)-structSize) {
					if (widthMarginR +  (3 * rectangSide)
							+  (1  * rectangSide)-  (3 * rectangSide ) +2*rectangSide/3> x && x >widthMarginR +  (3 * rectangSide)
							+  (1  * rectangSide)-  (3 * rectangSide ) +structSize/2){
						xCoord = 2;
						yCoord = 5;
						orient = 0;
					}
					else if (widthMarginR +  (4 * rectangSide)
							+  (1  * rectangSide)-  (3 * rectangSide ) +2*rectangSide/3> x && x >widthMarginR +  (4 * rectangSide)
							+  (1  * rectangSide)-  (3 * rectangSide ) +structSize/2){
						xCoord = 3;
						yCoord = 5;
						orient = 0;
					}
					else if (widthMarginR +  (5 * rectangSide)
							+  (1  * rectangSide)-  (3 * rectangSide ) +2*rectangSide/3> x && x >widthMarginR +  (5 * rectangSide)
							+  (1  * rectangSide)-  (3 * rectangSide ) +structSize/2){
						xCoord = 4;
						yCoord = 5;
						orient = 0;
					}
					else if (widthMarginR +  (6 * rectangSide)
							+  (1  * rectangSide)-  (3 * rectangSide ) +2*rectangSide/3> x && x >widthMarginR +  (6 * rectangSide)
							+  (1  * rectangSide)-  (3 * rectangSide ) +structSize/2){
						xCoord = 5;
						yCoord = 5;
						orient = 0;
					}
				}

				if (xCoord == 0 && yCoord == 0 && orient == 1) {
					return null;
				}
				return new Loca_Chemin(xCoord, yCoord,orient);
		//return output;
	}

	class AMouseListener extends MouseAdapter{
		public void mouseClicked(MouseEvent e) {

			Point p = new Point(e.getX(), e.getY());
			if (state == 1) {
				if (p != null){
					Localisation loc = pxToTile(p);
					if (loc != null) {
						if (game.getP().moveRobber(loc)) {
							index--;
						}
						if (index == 0) {
							state = 0;
						}
					}
				}
			}
			else if (state == 2) {
				if (p != null){
					Localisation loc = pxToStructure(p);
					if (loc != null) {
						if (game.getP().placeStructure(loc, GameRunner.getCurrentPlayer())) {
							index--;
						}
						if (index == 0) {
							state = 0;
						}
					}
				}
			}
			else if (state == 3) {
				if (p != null){
					Loca_Chemin loc = pxToRoad(p);
					if (loc != null) {
						if (game.getP().placeRoad(loc, GameRunner.getCurrentPlayer())) {
							index--;
						}
						if (index == 0) {
							state = 0;
						}
					}
				}
			}
			else if (state == 4) {
				if (p != null){
					Localisation loc = pxToStructure(p);
					if (loc != null) {
						if (game.getP().placeCity(loc, GameRunner.getCurrentPlayer())) {
							index--;
						}
						if (index == 0) {
							state = 0;
						}
					}
				}
			}
			else if (state == 5) {
				if (p != null){
					Localisation loc = pxToStructure(p);

					if (loc != null) {
						if (game.getP().placeStructureNoRoad(loc, GameRunner.getCurrentPlayer())) {
							index--;
							if (capitol) {
								ArrayList<Tuile> tiles = getGame().getP().getAdjacentTilesStructure(loc);
								for (Tuile t : tiles){
									if (t != null) {
										GameRunner.getCurrentPlayer().giveResourceType(t.getType());
									}
								}
							}
						}
						if (index == 0) {
							state = 0;
						}
					}
				}
			}
			repaint();
		}
	}
	
	public Jeu getGame() {
		return game;
	}
	
	/**
	 * Puts CatanP in placing settlement state
	 * @param s how many to be placed
	 */
	public void placeSettlement(int s) {
		index = s;
		state = 2;
	}
	
	public void placeRoad(int s) {
		index = s;
		state = 3;
	}
	
	public void placeCity(int s) {
		index = s;
		state = 4;
	}
	
	public void placeRobber() {
		index = 1;
		state = 1;
	}
	
	/**
	 * Puts CatanP in placing settlement state in setup
	 * @param s how many to be placed
	 */
	public void placeSettlementNoRoad(int s) {
		index = s;
		state = 5;
	}

	/**
	 * Puts CatanP in placing settlement state in setup
	 * @param s how many to be placed
	 */
	public void placeCapitol() {
		index = 1;
		state = 5;
		capitol = true;
	}
}
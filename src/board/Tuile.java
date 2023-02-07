package board;

import java.util.ArrayList;



public class Tuile {
	
	private int number = 0;
	private Localisation location;
	private boolean hasRobber = false;
	private final String type;
		/*
		 * Possible values:
		 * DESERT, ARGILE, LAINE, BOIS, BLE, ORE
		 */
	public void afficher_tuile()
	{
		System.out.println("Type de la tuile: "+type);
		System.out.println("numero de la tuile: "+number);
		
	}
	
	
	
	public Tuile(int x, int y, int n, String str) {
		location = new Localisation(x, y);
		number = n;
		type = str;
	}
	
	
	public Tuile(String str) {
		type = str;
	}
	
	
	public Tuile(String str, boolean b) {
		type = str;
		hasRobber = b;
	}
	
	
	public Localisation getLocation() {
		return location;
	}
	
	
	public void setCoords(int col, int row) {
		location = new Localisation(col, row);
	}
	
	
	public void setNumber(int n) {
		number = n;
	}
	
	public int getNumber() {
		return number;
	}

	
	public String getType() {
		return type;
	}
	
	
	public boolean hasRobber() {
		return hasRobber;
	}
	
	
	public void setRobber(boolean b) {
		hasRobber = b;
	}
}

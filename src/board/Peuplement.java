package board;

import java.util.ArrayList;

import game.Joeur;


public abstract class Peuplement {
	
	private Joeur owner = null;
	private Localisation location;
	private int type;

	public abstract void giveResources(String resType);

	
	public void setOwner(Joeur p) {
		if (null == owner)
			owner = p;
	}

	public Joeur getOwner() {
		return owner;
	}
	
	public void setLocation(Localisation loc) {
		location = loc;
	}

	
	public Localisation getLocation() {
		return location;
	}
	
	
	public int getType() {
		return type;
	}
	
	public void setType(int t) {
		type = t;
	}
	
}
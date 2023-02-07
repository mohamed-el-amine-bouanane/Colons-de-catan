package board;

import game.Joeur;

/**
 * This class models a road from Settlers of Catan
 */
public class Chemin {

	private Joeur owner = null;
	private Loca_Chemin location;
	private boolean visited = false;
	


	
	public Chemin(int x, int y, int o) {
		location = new Loca_Chemin(x, y, o);
	}

	
	public void setOwner(Joeur p) {
		if (null == owner)
			owner = p;
		p.addRoad(this);
	}

	
	public Joeur getOwner() {
		return owner;
	}
	
	
	public Loca_Chemin getLocation() {
		return location;
	}
	
	
	public boolean isVisited() {
		return visited;
	}
	
	
	public void visit() {
		visited = true;
	}
	
	
	public void resetVisited() {
		visited = false;
	}
}

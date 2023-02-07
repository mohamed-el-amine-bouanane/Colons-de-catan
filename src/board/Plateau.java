package board;

import game.Joeur;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Stack;


public class Plateau {

	public Tuile[][] Tuile;
	private Peuplement[][] peu;
	private Chemin[][][] routes;
	private Localisation loc_vol;
		
	private Chemin endpoint = null; 
	private Localisation startside;


	
	public Plateau() {

		Tuile = new Tuile[7][7];
		peu = new Peuplement[7][7];
		routes = new Chemin[7][7][2];
		Tuile desert = new Tuile("DESERT", true);

		// Ccéer la listes des tuiles à placer dans le plateau
		ArrayList<Tuile> tileList = new ArrayList<Tuile>();
		tileList.add(new Tuile("BOIS")); tileList.add(new Tuile("LAINE")); tileList.add(new Tuile("BLE")); tileList.add(new Tuile("LAINE"));
		tileList.add(new Tuile("BLE")); tileList.add(new Tuile("ARGILE")); tileList.add(new Tuile("BOIS")); tileList.add(new Tuile("ROCHER"));
		tileList.add(new Tuile("ROCHER"));tileList.add(desert); tileList.add(new Tuile("BLE")); tileList.add(new Tuile("ARGILE"));
		tileList.add(new Tuile("ARGILE")); tileList.add(new Tuile("ROCHER")); tileList.add(new Tuile("LAINE")); tileList.add(new Tuile("BOIS"));
		

		
		Collections.shuffle(tileList);

		int count = 0;

		for (int row = 2; row < 6; row++) {
				for (int col = 2; col < 6; col++) {
					//System.out.println("Kechmegh "+ col);
					Tuile[col][row] = tileList.get(count);
					Tuile[col][row].setCoords(col, row);
					count++;
				}
			}

			loc_vol = desert.getLocation();
		

		// L ordre des numeros des tuiles à placer
		int[] ordre = {6,10,11,8,4,9,5,12,3,10,6,9,8,5,2};
		int nbTuile = 0;

		// Coordonnées corrependant au numero à placer
		int[] tileOrder = { 2,2, 3,2, 4,2, 5,2,  2,3, 3,3, 4,3, 5,3 , 2,4, 3,4 , 4,4, 5,4 , 2,5, 3,5, 4,5, 5,5};

		for (int n = 0; n < tileOrder.length - 1; n+=2) 
		{
			
			if (nbTuile == 16){
				break;
			}
			
			if (Tuile[tileOrder[n]][tileOrder[n+1]].getType().equals("DESERT")) {
			}
			else {
				Tuile[tileOrder[n]][tileOrder[n+1]].setNumber(ordre[nbTuile]);
				nbTuile++;
			}
		}

		// Place les tuiles vides
		for (int i = 0; i < Tuile.length; i++) {
			for (int j = 0; j < Tuile[0].length; j++) {
				if (Tuile[i][j] == null)
					Tuile[i][j] = new Tuile(i, j, 0, null);
			}
		}
		
		// Placer les Peuplement dans le tableau reseré 
		for (int row = 0; row < peu.length; row++) {
			for (int col = 0; col < peu[0].length; col++) {
				
					peu[col][row] = new Colonie(col, row);
				
			}
		}

		// Placer les chemins dans leur tableau dedié
		for (int row = 0; row < routes.length; row++) {
			for (int col = 0; col < routes[0].length; col++) {
				for (int ori = 0; ori < routes[0][0].length; ori++) {
					routes[col][row][ori] = new Chemin(col, row, ori);
				}
			}
		}
	}
	/*
	 * Affichage de tout les peuplement présent dans la carte
	 * 
	 */
	public void afficher_Peuplement()
	{
		for(int j =1;j<6;j++)
		{
			for(int i=1;i<6;i++)
			{
				System.out.print("Le type du peuplement "+peu[i][j].getType());
				System.out.print("   x : "+peu[i][j].getLocation().getXCoord());
				System.out.print("   y : "+peu[i][j].getLocation().getYCoord());
				if(peu[i][j].getOwner()!=null)
				System.out.print("   Le proprietaire : "+peu[i][j].getOwner().getName());
				else
				{
					System.out.println("   Le proprietaire : n existe te pas");
				}
				
				
			}
		}
	}
	public void afficher_Chemain()
	{
		for(int j =1;j<6;j++)
		{
			for(int i=1;i<6;i++)
			{
				for(int k=0;k<2;k++)
				{
				System.out.print("Le type du peuplement "+peu[i][j].getType());
				System.out.print("   x : "+routes[i][j][k].getLocation().getXCoord());
				System.out.print("   y : "+routes[i][j][k].getLocation().getYCoord());
				System.out.print("   o : "+routes[i][j][k].getLocation().getOrientation());
				if(routes[i][j][k].getOwner()!=null)
					System.out.print("   Le proprietaire : "+peu[i][j].getOwner().getName());
					else
					{
						System.out.println("   Le proprietaire : n existe te pas");
					}
				}
				
				
				
			}
		}
		
		
		
	}

	public void distributeResources(int roll) {

		ArrayList<Tuile> rollTiles = getTilesWithNumber(roll);

		for (Tuile t : rollTiles) {
			if (t.hasRobber() || t.getType().equals("DESERT")) {
				continue;
			}

			ArrayList<Peuplement> rollStructures = new ArrayList<Peuplement>();

			Localisation loc = t.getLocation();

			// Add all the six structures to the ArrayList
			rollStructures.add(peu[loc.getXCoord()][loc.getYCoord()]);
			rollStructures.add(peu[loc.getXCoord()-1][loc.getYCoord()]);
			rollStructures.add(peu[loc.getXCoord()-1][loc.getYCoord()-1]);
			rollStructures.add(peu[loc.getXCoord()][loc.getYCoord()-1]);

			for (Peuplement s : rollStructures) 
			{
				if (null != s.getOwner())
					s.giveResources(t.getType());
			}
		}
	}

	
	/*Retourne unr liste de tuiles où chaque tuile contient le nombre numb pour pouvoir affecter
	 *  les ressources */
	private ArrayList<Tuile> getTilesWithNumber(int numb) {

		ArrayList<Tuile> rollTiles = new ArrayList<Tuile>();

		for (int i = 1; i < Tuile.length; i++) {
			for (int j = 1; j < Tuile[i].length; j++) {
				if (Tuile[i][j].getNumber() == numb)
					rollTiles.add(Tuile[i][j]);
			}
		}
		return rollTiles;
	}

	
	/* Donne le peuplement correspondant au coordonnées donner en entrer */
	/*_________________________A CHANGER_______________________________*/
	public Peuplement getStructure(Localisation loc) {
		return peu[loc.getXCoord()][loc.getYCoord()];
	}

	

	public void setStructure(Localisation loc, Peuplement s) {
		peu[loc.getXCoord()][loc.getYCoord()]= s;
	}

	

	public Chemin getRoad(Loca_Chemin loc) {
		return routes[loc.getXCoord()][loc.getYCoord()][loc.getOrientation()];
	}

	
	
	public boolean placeStructureNoRoad(Localisation loc, Joeur player) {

		if (peu[loc.getXCoord()][loc.getYCoord()].getOwner() != null) { //Il y a dèjà un peuplement ici
			return false;
		}
		
		
			if (peu[loc.getXCoord()-1][loc.getYCoord()].getOwner() == null &&
				peu[loc.getXCoord()+1][loc.getYCoord()].getOwner() == null &&
				peu[loc.getXCoord()][loc.getYCoord()+1].getOwner() == null &&
				peu[loc.getXCoord()][loc.getYCoord()-1].getOwner() == null)
			{

				peu[loc.getXCoord()][loc.getYCoord()].setOwner(player);
				return true;
			}
			else {
				return false;
			}
		
	

	}

	
	public boolean placeStructure(Localisation loc, Joeur player) 
	{

		if (peu[loc.getXCoord()][loc.getYCoord()].getOwner() != null) 
		{ //Il y a dèjà un peuplement ici
			return false;
		}
		

			if ((player.equals(routes[loc.getXCoord()][loc.getYCoord()][0].getOwner()) ||
				player.equals(routes[loc.getXCoord()][loc.getYCoord()][1].getOwner()) ||
				player.equals(routes[loc.getXCoord()+1][loc.getYCoord()][0].getOwner())||
				player.equals(routes[loc.getXCoord()][loc.getYCoord()-1][1].getOwner())
				&&
				peu[loc.getXCoord()-1][loc.getYCoord()].getOwner() == null &&
				peu[loc.getXCoord()+1][loc.getYCoord()].getOwner() == null &&
				peu[loc.getXCoord()][loc.getYCoord()+1].getOwner() == null &&
				peu[loc.getXCoord()][loc.getYCoord()-1].getOwner() == null))
			{
				if (checkPort(loc) != -1)
					player.addPort(checkPort(loc));
				peu[loc.getXCoord()][loc.getYCoord()].setOwner(player);
				return true;
			}
			else {
				return false;
			}
		
	}

	
	public boolean placeRoad(Loca_Chemin loc, Joeur player) {
		
		if (routes[loc.getXCoord()][loc.getYCoord()][loc.getOrientation()].getOwner() != null) { //Il y a dèjà un chemin ici
			return false;
		}

		if (loc.getOrientation() == 0) {
			if (player.equals(peu[loc.getXCoord()-1][loc.getYCoord()].getOwner())||
					player.equals(peu[loc.getXCoord()][loc.getYCoord()].getOwner()) ||
					player.equals(routes[loc.getXCoord() - 1][loc.getYCoord()][0].getOwner()) ||//a gauche
					player.equals(routes[loc.getXCoord() - 1][loc.getYCoord()][1].getOwner()) ||//en bas a gauche
					player.equals(routes[loc.getXCoord()-1][loc.getYCoord() +1 ][1].getOwner()) ||// a gauche en haut
					player.equals(routes[loc.getXCoord()][loc.getYCoord()+1][1].getOwner())||//en haut a droite
					player.equals(routes[loc.getXCoord()][loc.getYCoord()-1][1].getOwner())||//en bas a droite
					player.equals(routes[loc.getXCoord()+1][loc.getYCoord()][0].getOwner()))//a droite
				{
					routes[loc.getXCoord()][loc.getYCoord()][loc.getOrientation()].setOwner(player);
					return true;
				}
			else {
				return false;
			}
				
			
		
		}
		else 
		{
			if (player.equals(peu[loc.getXCoord()][loc.getYCoord()].getOwner()) ||
					player.equals(peu[loc.getXCoord()][loc.getYCoord() - 1].getOwner()) ||
					player.equals(routes[loc.getXCoord()][loc.getYCoord()][0].getOwner()) ||//en haut a gauche
					player.equals(routes[loc.getXCoord()][loc.getYCoord() + 1][1].getOwner()) ||//en haut 
					player.equals(routes[loc.getXCoord()+1][loc.getYCoord()][0].getOwner()) ||//en haut a droite
					player.equals(routes[loc.getXCoord() ][loc.getYCoord()-1][0].getOwner())||//en bas a gauche
					player.equals(routes[loc.getXCoord()][loc.getYCoord()-1][1].getOwner()) ||//en bas  
					player.equals(routes[loc.getXCoord()+1][loc.getYCoord()-1][0].getOwner()) //en bas a droite
					)
				{
			
				routes[loc.getXCoord()][loc.getYCoord()][loc.getOrientation()].setOwner(player);
				return true;
			}
			else {
				return false;
			}
		}
	}

	public boolean placeCity(Localisation loc, Joeur player) {
		if (player.equals(peu[loc.getXCoord()][loc.getYCoord()].getOwner()) &&
				peu[loc.getXCoord()][loc.getYCoord()].getType() == 0) {
			peu[loc.getXCoord()][loc.getYCoord()].setType(1);
			return true;
		}
		else
			return false;
	}
	
	public Localisation getRobberLocation() {
		return loc_vol;
	}


	public void setRobberLocation(Localisation loc) {
		loc_vol = loc;
	}
	

	
	public boolean moveRobber(Localisation loc) {
		Localisation current = getRobberLocation();
		if (loc.getXCoord() == current.getXCoord() &&
				loc.getYCoord() == current.getYCoord()) {
			return false;
		}
		else{
			Tuile[current.getXCoord()][current.getYCoord()].setRobber(false);
			setRobberLocation(loc);
			Tuile[loc.getXCoord()][loc.getYCoord()].setRobber(true);
			return true;
		}
	}


	public ArrayList<Joeur> getRobberAdjacentPlayers() {
		Localisation loc = getRobberLocation();
		ArrayList<Peuplement> temp = new ArrayList<Peuplement>();
		ArrayList<Joeur> players = new ArrayList<Joeur>();
		temp.add(peu[loc.getXCoord()][loc.getYCoord()]);
		temp.add(peu[loc.getXCoord() - 1][loc.getYCoord() ]);
		temp.add(peu[loc.getXCoord()-1][loc.getYCoord() - 1]);
		temp.add(peu[loc.getXCoord()][loc.getYCoord()+1]);	
		for (Peuplement s : temp) {
			if (s.getOwner() != null) {
				if (Collections.frequency(players, s.getOwner()) < 1) {
					players.add(s.getOwner());
				}
			}
		}
		
		return players;
	}

	
	public Tuile getTile(Localisation loc) {
		return Tuile[loc.getXCoord()][loc.getYCoord()];
	}

	
	
	public ArrayList<Tuile> getAdjacentTilesStructure(Localisation loc) {
		ArrayList<Tuile> output = new ArrayList<Tuile>();
	
			Tuile a = Tuile[loc.getXCoord()][loc.getYCoord()];
			if (a.getType() != null)
				output.add(a);
			Tuile b = Tuile[loc.getXCoord()][loc.getYCoord() - 1];
			if (b.getType() != null)
				output.add(b);
			Tuile c = Tuile[loc.getXCoord() + 1][loc.getYCoord() ];
			if (c.getType() != null)
				output.add(c);
			Tuile d = Tuile[loc.getXCoord() + 1][loc.getYCoord() -1 ];
			if (d.getType() != null)
				output.add(d);

		return output;
	}

	

	private int checkPort(Localisation loc) {
		int x = loc.getXCoord();
		int y = loc.getYCoord();
		
		if ((x == 1 && y == 1 ) ||
				(x == 1 && y == 2  )||
				(x==4  && y==1)||
				(x==5  && y==1)||
				(x==5  && y==4)||
				(x==5  && y==5)) {
			return 0;//Port general
		}
		else if ((x == 3 && y == 5 ) ||
				(x == 4 && y == 5 )) {
			return 1;// Port specialisé en Argile
		}
		else if ((x == 2 && y == 1 ) ||
				(x == 3 && y == 1 )) {
			return 2;//Port specialise en Laine
		}
		else if ((x == 1 && y == 5 ) ||
				(x == 2 && y == 5)) {
			return 3; //Port specialise en PIERRE
		}
		else if ((x == 1 && y == 3 ) ||
				(x == 1 && y == 4  )) {
			return 4;//Port specialise en Blé
		}
		else if ((x == 5 && y == 2 ) ||
				(x == 5 && y == 3 )) {
			return 5; //Port specialise en Bois
		}
		else {
			return -1;
		}
	}

	
	public Tuile[][] getTiles(){
		return Tuile;
	}
	

	public Peuplement[][] getStructures(){
		return peu;
	}
	
	
	public Chemin[][][] getRoads(){
		return routes;
	}
}

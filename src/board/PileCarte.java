package board;

import java.util.ArrayList;
import java.util.Collections;

public class PileCarte {

	private ArrayList<CarteD> cards;


	public PileCarte() {
		cards = new ArrayList<CarteD>(25);
		
		for (int i = 0; i < 14; i++)
			cards.add(new CarteD("Chevalier"));
		cards.add(new CarteD("Progre", "ConstructeurChemin"));
		cards.add(new CarteD("Progre", "ConstructeurChemin"));
		cards.add(new CarteD("Progre", "AnneeAbondonce"));
		cards.add(new CarteD("Progre", "AnneeAbondonce"));
		cards.add(new CarteD("Progre", "Amoi"));
		cards.add(new CarteD("Progre", "Amoi"));
		for (int i = 0; i < 5; i++)
			cards.add(new CarteD("PointVictoire"));
		
		
		Collections.shuffle(cards);
	}
	
	
	public CarteD draw() {
		if (cards.size() > 0) {
			return cards.remove(cards.size() - 1);
		}
		else
			return null;
	}
	
	public boolean isEmpty() {
		return cards.size() == 0;
	}
}
